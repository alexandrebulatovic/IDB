package facade;

import java.util.Iterator;
import java.util.List;

import business.TableSet;
import ddl.I_AttributeModel;
import ddl.I_TableModel;
import useful.Response;
import useful.ResponseData;
import factory.MainFactory;
import manager.ddl.I_DDLManager;

public class DDLFacade 
extends AbstractDDLCRUDFacade
{
	/** Fabrique principale.*/
	private MainFactory factory;

	
	//Constructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param manager : null interdit.
	 * @param factory : null interdit.
	 */
	public DDLFacade(I_DDLManager manager, MainFactory factory,TableSet tables)
	{
		super(manager,tables);
		this.factory = factory;
	}


	//Méthodes
	/**
	 * Tente de créer $table dans la base de données.
	 * 
	 * @param table : une table à créer. L'objet peut être erroné;
	 */
	public Response createTable(I_TableModel table)
	{
		boolean addable = this.createtableBusiness(table);
		Response added;
		
		if (!addable) 
			added = new Response(false, "Cette table existe déjà.");
		else {
			added = this.createTableDBMS(table);
			if (! added.hasSuccess()) 
				this.tables.removeTable(table.getName());
		}
		return added;
	}

	
	/**
	 * @param table
	 * @return vrai ssi $table a déjà étée chargée depuis le SGBD,
	 * faux sinon.
	 */
	public boolean isLoaded(String table)
	{
		return this.tables.isLoaded(table);
	}
	
	
//	/**
//	 * Ajoute $attribute à la $table.
//	 * 
//	 * @param table : nom de la table, null interdit.
//	 * @param attribute : nom de l'attribut, null interdit.
//	 * @param type : nom du type, null interdit.
//	 * @param size : taille du type, 0 < taille.
//	 * @param notNull : vrai ssi $attribut est sous contrainte not null, faux sinon.
//	 * @param primary : vrai ssi $attribut est sous contrainte primary, faux sinon.
//	 * @return vrai ssi $attribute est ajouté avec succès à $table, faux sinon.
//	 */
//	public boolean addAttribute
//		(String table, String attribute, String type, int size, boolean notNull, boolean primary)
//	{
//		return this.tables.addAttribute(table, attribute, type, size, notNull, primary);
//	}
	
	
	/**
	 * Ajoute $attribute à la table.
	 * 
	 * @param table : nom de la table, null interdit.
	 * @param attribute : nom de l'attribut, null interdit.
	 * @return vrai ssi $attribute est ajouté avec succès à $table, faux sinon.
	 */
	public boolean addAttribute(String table, String [] att)
	{
		return this.tables.addAttribute(table, att[0], att[1], 
				Integer.parseInt(("".equals(att[2]) || null == att[2]) ? "1" : att[2]), 
				"NOTNULL".equals(att[3]), 
				"PRIMARY".equals(att[4]));
	}
	
	
	
	/**
	 * @return la liste des types de données disponibles pour le SGBD.
	 */
	public String[] getAttributeTypes()
	{
		return this.manager.getAttributeTypes();
	}
	
	/**
	 * @return vrai si et seulement si le SGBD permet de "droper" une 
	 * table avec l'option "CASCADE"
	 */
	public boolean dbmsAllowsDropCascade()
	{
		return this.manager.allowsDropCascade();
	}


	/**
	 * Supprime $table, si c'est possible.
	 * 
	 * @param table : une table à supprimer, null interdit.
	 * @param cascade : vrai si et seulement si $table peut être supprimée 
	 * alors qu'elle est référencée par d'autres tables, faux sinon.
	 * @return une réponse personnalisée décrivant si la suppression de toutes
	 * les tables a réussi ou non.
	 */
	public Response dropTable(String table, boolean cascade)
	{
		Response result = this.manager.dropTable(table, cascade);
		if (result.hasSuccess()) {
			this.tables.removeTable(table);
		}
		return result;
	}


	/**
	 * Supprime $table et toutes les tables de la bases qui utilisent la clée primaire
	 * de $table.
	 * 
	 * @param table : une table à supprimer, null interdit.
	 * @return une réponse personnalisée qui contient le nom de toutes les tables 
	 * supprimées, ou une réponse vide en cas d'erreur.
	 */
	public Response dropTableDomino(String table)
	{

		ResponseData<String> result = this.manager.dropTableDomino(table);
		if (result.hasSuccess()) {
			for (String t : result.getCollection()) {
				this.tables.removeTable(t);
			}
		}
		return result;
	}
	
	
	/**
	 * @param table : nom de la table où récupérer les attributs, null interdit.
	 * @return une réponse personnalisée.<br/>
	 * Lorsque la récupération réussi, la réponse contient dans l'ordre :<br/>
	 * -le nom d'un attribut de $table,<br/>
	 * -le nom de son type SQL,<br/>
	 * -la taille de cet attribut,<br/>
	 * -"NO" si et seulement si cet attribut est NOT NULL.<br/><br/>
	 * 
	 * Lorsque la récupération échoue, la réponse est vide et décrit l'erreur rencontrée.
	 */
	public ResponseData<String[]>getAttributesFromDBMS(String table)
	{
		return this.manager.getAttributes(table);
	}
	
	
	/**
	 * @param tableName : nom de la table, null interdit.
	 * @return tous les attributs de $tables, avec :<br/>
	 * - le nom de l'attribut,<br/>
	 * - le type de l'attribut,<br/>
	 * - la taille de l'attribut,<br/>
	 * - "NOTNULL" ssi l'attribut est sous contrainte NOT NULL,<br/>
	 * - "PRIMARY" ssi l'attribut est membre de la clée primaire.
	 */
	public List<String[]> getAttributesFromBusiness(String table)
	{
		return this.tables.getAttributes(table);
	}
	
	
	/**
	 * Retourne une réponse personnalisée qui contient les membres
	 * de la clée primaire de $table.
	 * 
	 * @param table : nom de la table, ne doit pas être null.
	 * @return CustomizedResponseWithData
	 */
	public ResponseData<String> getPrimaryKey(String table)
	{
		return this.manager.getPrimaryKey(table);
	}


	/**
	 * Ferme proprement les objets Statements.
	 */
	public void closeStatement(){this.manager.closeStatement();}


	/**
	 * @param primaryKey 
	 * @param notNull 
	 * @param parseInt 
	 * @param type 
	 * @param name 
	 * @return un model d'attribut pour les IHM de DDL.
	 */
	public I_AttributeModel getAttributeModel(String name, String type, int parseInt, boolean notNull, boolean primaryKey)
	{
		return this.factory.getAttributeModel(name,type,parseInt, notNull,primaryKey);
	}
	
	
	/**
	 * @return un modèle de table vide pour l'IHM de création des tables.
	 */
	public I_TableModel getTableModel()
	{
		return this.factory.getTableModel();
	}


	public ResponseData<String[]> getForeignFromPrimary(String string) {
		return this.manager.getForeignFromPrimary(string);
	}


	public ResponseData<String> getUniqueAttribute(String string) {
		return this.manager.getUniqueAttribute(string);
		
	}


	public ResponseData<String[]> getPrimaryFromForeign(String string) {
		return this.manager.getPrimaryFromForeign(string);
	}


	/**
	 * tente de créer $table dans les classes métiers.
	 * 
	 * @param table : une table à ajouté, faux sinon.
	 * @return vrai si et seulement si $table a pu être ajoutée aux
	 * classes métiers, faux sinon.
	 */
	private boolean createtableBusiness(I_TableModel table)
	{
		String name = table.getName();
		boolean addable = this.tables.addTable(name);
		
		if (addable) {
			for(I_AttributeModel attribute : table.getAttributes()){
				this.tables.addAttribute(
						table.getName(),
						attribute.getName(),
						attribute.getType(),
						attribute.getSize(),
						attribute.isNotNull(),
						attribute.isPrimaryKey());
			}
		}
		return addable;
	}


	/**
	 * Tente de créer $table dans le SGBD.
	 * 
	 * @param table : une table à ajouter, null interdit.
	 * @return une reponse personnalisée décrivant la tentative
	 * de création de la table dans le SGBD.
	 */
	private Response createTableDBMS(I_TableModel table)
	{
		String name = table.getName();
		List<String> sql = this.tables.getSQLTableToCreate(name);
		Iterator<String> statement = sql.iterator();
		
		String create = statement.next(); 
		Response result = this.manager.createTable(create);
		System.out.println(create); //TODO : remove
		String alter; //TODO : remove
		while (statement.hasNext() && result.hasSuccess()) {
			alter = statement.next();
			System.out.println(alter);//TODO : remove
			result = this.manager.altertable(alter);
		}
		return result;
	}

	public Response addForeignKey(String tableSourceName, String[] attributesSourcesNames, String tableDestinationName,
			String[] attributesDestinationsNames) {
		boolean addable = this.tables.addForeignKey(tableSourceName, attributesSourcesNames, tableDestinationName, attributesDestinationsNames);
		Response added;
		if(addable){
			added = new Response(false,"Cette contrainte existe déjà.");
		}else{
			added = this.addForeignKeyDBMS(tableSourceName, attributesSourcesNames, tableDestinationName, attributesDestinationsNames);
			if (! added.hasSuccess()) {
				//this.tables.removeTable(table.getName());
			}
		}
		return added;
	}


	private Response addForeignKeyDBMS(String tableSourceName, String[] attributesSourcesNames,
			String tableDestinationName, String[] attributesDestinationsNames) {
		// TODO Auto-generated method stub
		return null;
	}
}

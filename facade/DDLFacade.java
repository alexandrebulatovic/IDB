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
}

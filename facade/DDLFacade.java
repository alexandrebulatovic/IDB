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
				this.business.removeTable(table.getName());
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
		return this.business.isLoaded(table);
	}
	
	
	public Response alterTable(String oldTable, String newTable)
	{
		String sql;
		Response result;
		List<String> queries = this.business.getSQLTableToModify(oldTable, newTable);
		Iterator<String> it = queries.iterator();
		boolean stop = false;
		result = new Response(true, "Table modifiée.");
		
		while (it.hasNext() && !stop) {
			sql = it.next();
//			result = this.manager.alterTable(sql);
			stop = ! result.hasSuccess();
		}
		return result;
	}
	
	
	/**
	 * Ajoute $attribute à la table.<br/>
	 * L'attribut contient son nom, type, taille, NOT NULL et PRIMARY KEY.
	 * 
	 * @param table : nom de la table, null interdit.
	 * @param attribute : caractéristiques de l'attribut à ajouter, null interdit : <br/>
	 * nom, type, taille, NOTNULL, PRIMARY.
	 * @return vrai ssi $attribute est ajouté avec succès à $table, faux sinon.
	 */
	public boolean addAttributeToBusiness(String table, String [] att)
	{
		return this.business.addAttribute(table, att[0], att[1], 
				Integer.parseInt(("".equals(att[2]) || null == att[2]) 
						? "1" 
						: att[2]), 
				"NOTNULL".equals(att[3]), 
				"PRIMARY".equals(att[4]));
	}
	
	
	/**
	 * Ajoute tous les $attributes à $table.
	 * 
	 * @param table : nom de la table, null interdit.
	 * @param attributes : caractéristiques des attributs à ajouter, null interdit : <br/>
	 * nom, type, taille, NOTNULL, PRIMARY.
	 * @return vrai ssi $attributes est ajouté avec succès à $table, faux sinon.
	 */
	public boolean addAttributesToBusiness(String table, List<String[]> attributes)
	{
		
		boolean result = true;
		Iterator<String[]> it = attributes.iterator();
		
		while (it.hasNext() && result) {
			result = this.addAttributeToBusiness(table, it.next());
		}

		return result;
	}
	
	
	public String addUniqueBusiness(String table, String [] attributesGroup)
	{
		return this.business.addUnique(table, attributesGroup);
	}
	
	
	public String addUniqueBusiness(String uniqueName, String tableName, String[] attributesNames)
	{
		return this.business.addUnique(uniqueName, tableName, attributesNames);
	}
	
	
	//TODO : trompeur
	public Response addUniqueDBMS(String tableSourceName, String[] attributesSourcesNames, String contrainte) {
		String sql = this.business.getSQLADDConstraint(tableSourceName, attributesSourcesNames[0], contrainte);
		Response result = this.manager.addUnique(sql);
		return result;
	}

	
	/**
	 * Ajoute une clée étrangère dans les classes métiers.
	 * 
	 * @param constraintName : nom de la contrainte, null interdit.
	 * @param foreignTable : nom de la table qui contient la clée étrangère, null interdit.
	 * @param foreignAttributes : nom des attributs membres de la clée étrangère, null interdit.
	 * @param primaryTable : nom de la table contenant la clée primaire référencée.
	 * @param primaryAttributes : nom des attributs membres de la clée primaire référencées, null interdit.
	 * @return le nom de la contrainte de clée étrangère.
	 */
	public String addForeignKeyToBusiness(
			String constraintName, 
			String foreignTable, 
			String[] foreignAttributes, 
			String primaryTable, 
			String[] primaryAttributes)
	{
		return this.business.addForeignKey(
				constraintName, 
				foreignTable, 
				foreignAttributes, 
				primaryTable, 
				primaryAttributes);
	}
	
	
	public Response addUnique(String table, String[] attributesGroup) {
		String contrainte = this.business.addUnique(table, attributesGroup);
		Response added = this.addUniqueDBMS(table, attributesGroup, contrainte);
		return added;
	}


	/**
	 * @return la liste des types de données disponibles pour le SGBD.
	 */
	public String[] getDataTypes()
	{
		return this.manager.getDataTypes();
	}
	
	/**
	 * @return vrai si et seulement si le SGBD permet de "droper" une 
	 * table avec l'option "CASCADE", faux sinon.
	 */
	public boolean dbmsAllowsDropCascade()
	{
		return this.manager.allowsDropCascade();
	}

	
	/**
	 * Supprime $table du SGBD.
	 * 
	 * @param table : nom de la table à supprimer, null interdit.
	 * @param cascade : vrai ssi $table peut être supprimée malgré les références, faux sinon.
	 * @return une réponse personnalisée décrivant la tentative de suppression.
	 */
	public Response dropTableDBMS(String table, boolean cascade)
	{
		return this.manager.dropTable(table, cascade);
	}
	
	
	/**
	 * Supprime $table des classes métiers.
	 * 
	 * @param table : nom de la table à supprimer, null interdit.
	 * @return vrai ssi $table a été supprimée des classes métiers, faux sinon.
	 */
	public boolean dropTableBusiness(String table)
	{
		return this.business.removeTable(table);
	}
	
	
	/**
	 * Supprime $table et toutes les tables de la bases qui utilisent la clée primaire
	 * de $table.
	 * 
	 * @param table : une table à supprimer, null interdit.
	 * @return une réponse personnalisée qui contient le nom de toutes les tables 
	 * supprimées, ou une réponse vide en cas d'erreur.
	 */
	public ResponseData<String> dropTableDominoDBMS(String table)
	{
		return this.manager.dropTableDomino(table);
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
	public ResponseData<String[]>getAttributesDBMS(String table)
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
	public List<String[]> getAttributesBusiness(String table)
	{
		return this.business.getAttributes(table);
	}
	
	
	/**
	 * Retourne une réponse personnalisée qui contient les membres
	 * de la clée primaire de $table.
	 * 
	 * @param table : nom de la table, ne doit pas être null.
	 * @return CustomizedResponseWithData
	 */
	public ResponseData<String> getPrimaryKeyDBMS(String table)
	{
		return this.manager.getPrimaryKey(table);
	}


	public ResponseData<String[]> getForeignFromPrimary(String string) {
		return this.manager.getForeignFromPrimary(string);
	}


	/**
	 * @param table : table où chercher les attributs avec contrainte unique, null interdit.
	 * @return une réponse personnalisée contenant : <br/>
	 * - Le nom de l'index,<br/>
	 * - le nom des attributs de $table qui sont soumis à une contrainte UNIQUE.
	 */
	public ResponseData<String[]> getUniquesFromDBMS(String table) 
	{
		return this.manager.getUniques(table);
		
	}

	/**
	 * @param table : table où se trouve les clées étrangères, null interdit.
	 * @return une réponse personnalisée.<br/>
	 * 
	 * Lorsque la récupération réussi, la réponse contient dans l'ordre : <br/>
	 * - le nom d'une table $t,<br/>
	 * - le nom d'un attribut $a, clée primaire de $t,<br/>
	 * - le nom de la contrainte de clée primaire $t($a),<br/>
	 * - l'argument $table,<br/>
	 * - le nom d'un attribut $a2, clée étrangère de $table, qui référence $t($a),<br/>
	 * - le nom de la contrainte de clée étrangère de $table($a2).<br/><br/>
	 * 
	 * Pour résumer : FOREIGN KEY ($a2) REFERENCES $t($a) <br/>
	 * Lorsque la récupération échoue, la réponse est vide et décrit l'erreur rencontrée. 
	 */ 
	public ResponseData<String[]> getPrimaryFromForeign(String table) 
	{
		return this.manager.getPrimaryFromForeign(table);
	}


	public Response addForeignKey(String tableSourceName, String[] attributesSourcesNames, String tableDestinationName,
			String[] attributesDestinationsNames) {
		String contrainte = this.business.addForeignKey(tableSourceName, attributesSourcesNames, tableDestinationName, attributesDestinationsNames);
		Response added = this.addForeignKeyDBMS(tableSourceName, attributesSourcesNames, contrainte);
		return added;
	}


	public Response removeConstraint(String tableSourceName, String attribute, String constraint) {
		this.business.removeConstraint(tableSourceName, attribute, constraint);
		String sql = this.business.getSQLDropConstraint(tableSourceName,attribute, constraint);
		System.out.println(sql);
		Response result = this.manager.dropConstraint(sql);
		System.out.println(result.getMessage());
		return result;
		
	}


	/**
	 * Ferme proprement les objets Statements.
	 */
	public void closeDDL(){this.manager.closeStatement();}


	/**
	 * @return un modèle de table vide pour l'IHM de création des tables.
	 */
	public I_TableModel getTableModel()
	{
		return this.factory.getTableModel();
	}


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
	 * Tente de créer $table dans le SGBD.
	 * 
	 * @param table : une table à ajouter, null interdit.
	 * @return une reponse personnalisée décrivant la tentative
	 * de création de la table dans le SGBD.
	 */
	private Response createTableDBMS(I_TableModel table)
	{
		String name = table.getName();
		List<String> sql = this.business.getSQLTableToCreate(name);
		Iterator<String> statement = sql.iterator();
		
		String create = statement.next(); 
		Response result = this.manager.createTable(create);
		while (statement.hasNext() && result.hasSuccess()) {
			result = this.manager.alterTable(statement.next());
		}
		return result;
	}


	private Response addForeignKeyDBMS(String tableSourceName, String[] attributesSourcesNames,String ConstraintName) {
		String sql = this.business.getSQLADDConstraint(tableSourceName, attributesSourcesNames[0], ConstraintName);
		Response result = this.manager.addForeignKey(sql);
		return result;
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
		boolean addable = this.business.addTable(name);
		
		if (addable) {
			for(I_AttributeModel attribute : table.getAttributes()){
				this.business.addAttribute(
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
}

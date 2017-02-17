package facade;

import java.util.List;

import javax.swing.JTable;

import business.TableSet;
import useful.Response;
import useful.ResponseData;
import factory.MainFactory;
import gui.ddl.tools.I_AttributeModel;
import gui.ddl.tools.I_TableModel;
import manager.ddl.I_DDLManager;
import manager.sql.SQLManager;

public class DDLFacade 
extends AbstractDDLCRUDFacade
{
	/** Fabrique principale.*/
	private MainFactory factory;

	/** Gestionnaire de requêtes SQL.*/
	private SQLManager sql;
	
	//Constructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param manager : null interdit.
	 * @param factory : null interdit.
	 * @param sql : null interdit.
	 */
	public DDLFacade
	(I_DDLManager manager, MainFactory factory, TableSet tables, SQLManager sql)
	{
		super(manager,tables);
		this.factory = factory; 
		this.sql = sql;
	}


	//Méthodes
	/**
	 * @param table : nom de la table dont il faut récupérer le SQL pour la créer
	 * en base, null interdit.
	 * @return une liste de requête SQL. La première est un CREATE TABLE, les autres des
	 * ALTER TABLE.
	 */
	public List<String> getSQLToCreateTable(String table)
	{
		return this.business.getSQLTableToCreate(table);
	}

	
	/**
	 * Tente de créer une table en base à partir de la requête $sql.
	 * 
	 * @param sql : une requête SQL CREATE TABLE, null interdit.
	 * @return une réponse personnalisée décrivant si la table a pu être créée ou non.
	 */
	public Response createTableDBMS(String sql)
	{
		return this.dbms.createTable(sql);
	}

	
	/**
	 * Tente de créer une table en RAM à partir de la requête $sql.
	 * 
	 * @param table : nom d'une table à créer, null interdit.
	 * @return vrai ssi $table a pu être créée, faux sinon.
	 */
	public boolean createTableBusiness(String table)
	{
		return this.business.addTable(table);
	}
	
	
	/**
	 * Modifie une table dans la bdd
	 * @param oldTable
	 * @param newTable
	 * @param attributes
	 */
	public Response modifyTable(String oldTable, String newTable, List<Object[]> attributes) {
		List<String> sqls = this.business.getSQLTableToModify(oldTable, newTable, attributes);
		Response rep = null;
		for (String sql : sqls){
			rep = this.dbms.alterTable(sql);
		}
		return rep;//on retourne la derniere réponse
		
		
	}


	/**
	 * Tente d'altérer une table en base avec une requête $sql.
	 * @param sql : une requête SQL ALTER TABLE, null interdit.
	 * @return une réponse personnalisée décrivant si l'altération a réussi ou non.
	 */
	public Response alterTableDBMS(String sql)
	{
		return this.dbms.alterTable(sql);
	}


	/**
	 * @param table : nom de la table, null interdit.
	 * @return vrai ssi $table existe en RAM, faux sinon.
	 */
	public boolean isLoaded(String table)
	{
		return this.business.isLoaded(table);
	}
	
	
	/**
	 * Ajoute $attribute à $table en RAM, si c'est possible.
	 * 
	 * @param table : nom de la table, null interdit.
	 * @param attribute : nom de l'attribut, null interdit.
	 * @param type : type de données de l'attribut, null interdit.
	 * @param size : taille de l'attribut, 0 < size.
	 * @param notNull : vrai ssi $attribute est sous contrainte NOT NULL faux sinon.
	 * @param primaryKey : vrai ssi $attribut est membre de la clée primaire, faux sinon.
	 * @return vrai ssi $attribute a été ajouté à $table, faux sinon.
	 */
	public boolean addAttributeToBusiness
		(String table, String attribute, String type, int size, boolean notNull, boolean primaryKey)
	{
		return this.business.addAttribute(table, attribute, type, size, notNull, primaryKey);
	}
	
	
	/**
	 * Ajoute une $constraint UNIQUE sur le groupe d' $attributes de $table 
	 * en RAM, si c'est possible.
	 * 
	 * @param constraint : nom de la contrainte, null interdit.
	 * @param table : nom de la table, null interdit.
	 * @param attributes : nom des attributs, null interdit.
	 * @return vrai ssi $constraint a été ajouté, faux sinon.
	 */
	public String addUniqueBusiness(String constraint, String table, String[] attributes)
	{
		return this.business.addUnique(constraint, table, attributes);
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
	public String addForeignKeyToBusiness(String constraintName, String foreignTable, String[] foreignAttributes, String primaryTable, String[] primaryAttributes)
	{
		return this.business.addForeignKey(
				constraintName, foreignTable, foreignAttributes, primaryTable, primaryAttributes);
	}
	
	
	/**
	 * @return la liste des types de données disponibles pour le SGBD.
	 */
	public String[] getDataTypes()
	{
		return this.dbms.getDataTypes();
	}

	
	/**
	 * Supprime $table du SGBD, si c'est possible.
	 * 
	 * @param table : nom de la table à supprimer, null interdit.
	 * @param cascade : vrai ssi $table peut être supprimée malgré les références, faux sinon.
	 * @return une réponse personnalisée décrivant la tentative de suppression.
	 */
	public Response dropTableDBMS(String table, boolean cascade)
	{
		return this.dbms.dropTable(table, cascade);
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
		return this.dbms.dropTableDomino(table);
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
		return this.dbms.getAttributes(table);
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
	 * @param table : nom de la table où chercher la clée, null interdit.
	 * @return Une réponse personnalisée contenant les attributs membres
	 * de la clée primaire de $table si et seulement si la requête fonctionne,
	 * sinon une réponse personnalisée détaillant l'erreur survenue.
	 */
	public ResponseData<String> getPrimaryKeyDBMS(String table)
	{
		return this.dbms.getPrimaryKey(table);
	}

	
	/**
	 * @param table : table où se les membres de la clée primaire, null interdit.
	 * @return une réponse personnalisée.<br/>
	 * 
	 * Lorsque la récupération réussit, la réponse contient dans l'ordre :<br/>
	 * - l'argument $table, <br/>
	 * - le nom d'un attribut $a2, membre de la clée primaire de $table,<br/>
	 * - le nom de la contrainte de clée primaire de $table,<br/>
	 * - le nom d'une table $t,<br/>
	 * - le nom d'un attribut $a, clée étrangère de $t, qui référence $table($a2),<br/>
	 * - le nom de la contrainte de clée étrangère de $t($a).<br/><br/>
	 * 
	 * Pour résumer : FOREIGN KEY($a) REFERENCES $table($a2)
	 * Lorsque la récupération échoue, la réponse est vide et décrit l'erreur rencontrée.
	 */
	public ResponseData<String[]> getForeignFromPrimary(String string) 
	{
		return this.dbms.getForeignFromPrimary(string);
	}


	/**
	 * @param table : table où chercher les attributs avec contrainte unique, null interdit.
	 * @return une réponse personnalisée contenant : <br/>
	 * - Le nom de l'index,<br/>
	 * - le nom des attributs de $table qui sont soumis à une contrainte UNIQUE.
	 */
	public ResponseData<String[]> getUniquesFromDBMS(String table) 
	{
		return this.dbms.getUniques(table);
		
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
		return this.dbms.getPrimaryFromForeign(table);
	}
	
	
	/**
	 * @return un modèle de table vide pour l'IHM de création des tables.
	 */
	public I_TableModel getTableModel()
	{
		return this.factory.getTableModel();
	}


	/**
	 * Ferme proprement les objets Statements.
	 */
	public void closeDDL(){this.dbms.closeStatement();}

	
	/** @see SQLManager#sendQuery(String) */
	public boolean sendQuery(String query) 
			throws Exception{
		return this.sql.sendQuery(query);
	}
	
	
	public JTable getGeneratedJTable() {
		return this.sql.getGeneratedJTable();
	}
	
	public String getGeneratedReply() {
		return this.sql.getGeneratedReply();
	}
	
	//TODO : trompeur
	public Response addUniqueDBMS(String tableSourceName, String[] attributesSourcesNames, String contrainte) {
		String sql = this.business.getSQLADDConstraint(tableSourceName, attributesSourcesNames[0], contrainte);
		Response result = this.dbms.addUnique(sql);
		return result;
	}


	public Response addUnique(String table, String[] attributesGroup) {
		String contrainte = this.business.addUnique(table, attributesGroup);
		Response added = this.addUniqueDBMS(table, attributesGroup, contrainte);
		return added;
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


	public Response removeConstraint(String tableSourceName, String attribute, String constraint) {
		this.business.removeConstraint(tableSourceName, attribute, constraint);
		String sql = this.business.getSQLDropConstraint(tableSourceName,attribute, constraint);
		System.out.println(sql);
		Response result = this.dbms.dropConstraint(sql);
		System.out.println(result.getMessage());
		return result;
		
	}


	public Response addForeignKey(String tableSourceName, String[] attributesSourcesNames, String tableDestinationName,
			String[] attributesDestinationsNames) {
		String contrainte = this.business.addForeignKey(tableSourceName, attributesSourcesNames, tableDestinationName, attributesDestinationsNames);
		System.out.println(contrainte);
		Response added = this.addForeignKeyDBMS(tableSourceName, attributesSourcesNames, contrainte);
		return added;
	}


	private Response addForeignKeyDBMS(String tableSourceName, String[] attributesSourcesNames,String ConstraintName) {
		String sql = this.business.getSQLADDConstraint(tableSourceName, attributesSourcesNames[0], ConstraintName);
		System.out.println(sql);
		Response result = this.dbms.addForeignKey(sql);
		return result;
	}
}

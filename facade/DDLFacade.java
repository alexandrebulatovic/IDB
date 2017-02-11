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
		return this.manager.createTable(sql);
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
	 * Tente d'altérer une table en base avec une requête $sql.
	 * @param sql : une requête SQL ALTER TABLE, null interdit.
	 * @return une réponse personnalisée décrivant si l'altération a réussi ou non.
	 */
	public Response alterTableDBMS(String sql)
	{
		return this.manager.alterTable(sql);
	}
	
	
//	//TODO : useless, IHM modifier à la poubelle
//	public Response alterTable(String oldTable, String newTable)
//		{
//			String sql;
//			Response result;
//			List<String> queries = this.business.getSQLTableToModify(oldTable, newTable);
//			Iterator<String> it = queries.iterator();
//			boolean stop = false;
//			result = new Response(true, "Table modifiée.");
//			
//			while (it.hasNext() && !stop) {
//				sql = it.next();
//	//			result = this.manager.alterTable(sql);
//				stop = ! result.hasSuccess();
//			}
//			return result;
//		}


	/**
	 * @param table : nom de la table, null interdit.
	 * @return vrai ssi $table existe en RAM, faux sinon.
	 */
	public boolean isLoaded(String table)
	{
		return this.business.isLoaded(table);
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
	 * @param table : nom de la table, doit déjà exister, null interdit.
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
	
	
	public String addUniqueBusiness(String constraintName, String tableName, String[] attributesNames)
	{
		return this.business.addUnique(constraintName, tableName, attributesNames);
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
		
		String sql = this.business.getSQLDropConstraint(tableSourceName,attribute, constraint);
		this.business.removeConstraint(tableSourceName, attribute, constraint);
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


	private Response addForeignKeyDBMS(String tableSourceName, String[] attributesSourcesNames,String ConstraintName) {
		String sql = this.business.getSQLADDConstraint(tableSourceName, attributesSourcesNames[0], ConstraintName);
		Response result = this.manager.addForeignKey(sql);
		return result;
	}
}

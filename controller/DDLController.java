package controller;

import java.util.ArrayList;
import java.util.List;

import facade.DDLFacade;
import gui.ddl.CreateTableGUI;
import gui.ddl.DropTableGUI;
import gui.ddl.AlterTableGUI;
import gui.ddl.ConstraintsGUI;

import javax.swing.JFrame;

import ddl.I_AttributeModel;
import ddl.I_TableModel;
import useful.Response;
import useful.ResponseData;

/**
 * Assure le dialogue entre les IHM du langage de définition des données
 * et le gestionnaire de ce même langage.
 */
public class DDLController 
{
	//Attributs
	/** IHM pour créer une table et ses attributs.*/
	private CreateTableGUI createGUI;

	/**IHM pour modifier une table et ses attributs.*/
	private AlterTableGUI modifyGUI;

	/** IHM pour supprimer une table.*/
	private DropTableGUI dropGUI;

	/** Facade pour la définition des données.*/
	private DDLFacade facade;

	private ConstraintsGUI constraintsGUI;


	//Contructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param : null interdit.
	 */
	public DDLController(DDLFacade facade)
	{
		this.facade = facade;
		this.facade.getTables();
	}


	/**
	 * Ouvre l'IHM de création des tables si et seulement si 
	 * elle n'existe pas, sinon tente de l'afficher au premier plan.
	 */
	public void openCreateGUI()
	{
		if (this.createGUI == null) {
			this.createGUI = new CreateTableGUI(this);
		}
		else {
			showGUI(this.createGUI);
		}
	}

	/**
	 * Ouvre l'IHM de modifications des tables si et seulement si 
	 * elle n'existe pas, sinon tente de l'afficher au premier plan.
	 */
	public void openModifyGUI() {
		if (this.modifyGUI == null){
			this.modifyGUI = new AlterTableGUI(this);
		}
		else{
			showGUI(this.modifyGUI);
		}

	}


	/**
	 * Ouvre l'IHM de création des tables si et seulement si 
	 * elle n'existe pas, sinon tente de l'afficher au premier plan.
	 */
	public void openDropGUI()
	{
		if (this.dropGUI == null) {
			this.dropGUI = new DropTableGUI(this);
		}
		else{
			showGUI(this.dropGUI);
		}
	}


	public void openConstraintsGUI() {
		if (this.constraintsGUI == null) {
			this.constraintsGUI = new ConstraintsGUI(this);
		}
		else {
			showGUI(this.constraintsGUI);
		}
		
	}


	/**
	 * @return une réponse personnalisée contenant le nom des tables
	 * de la base, si et seulement si ces dernières existent et 
	 * il n'y a pas eu d'exceptions, une réponse personnalisée vide sinon.
	 */
	public ResponseData<String> getTables()
	{
		return this.facade.getTables();
	}


	/**
	 * @return la liste des types de données disponibles pour le SGBD.
	 */
	public String[] getDataTypes()
	{
		return this.facade.getDataTypes();
	}


	/**
	 * Envoie $table au DDLManager dans l'optique de la créer.
	 * 
	 * @param table : une table à créer. L'objet peut être erroné;
	 */
	public Response createTable(I_TableModel table)
	{
		return this.facade.createTable(table);
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
		Response result = this.facade.dropTableDBMS(table, cascade);
		if (result.hasSuccess()) {
			this.facade.dropTableBusiness(table);
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
		ResponseData<String> result = this.facade.dropTableDominoDBMS(table);
		if (result.hasSuccess()) {
			for (String t : result.getCollection()) {
				this.facade.dropTableBusiness(t);
			}
		}
		return result;
	}


	/**
	 * Ferme proprement les objets Statements.
	 */
	public void closeStatement(){this.facade.closeDDL();}


	/**
	 * Récupère les informations utiles pour la vue depuis le SGBD ou les classes métiers.<br/>
	 * Charge ces informations et les contraintes uniques dans les classes métiers.
	 * 
	 * @param table : nom de la table où récupérer les attributs, null interdit.
	 * @return une réponse personnalisée.<br/>
	 * Lorsque la récupération réussi, la réponse contient dans l'ordre :<br/>
	 * -le nom d'un attribut de $table,<br/>
	 * -le nom de son type SQL,<br/>
	 * -la taille de cet attribut,<br/>
	 * -"NOTNULL" si et seulement si cet attribut est NOT NULL.<br/>
	 * -"PRIMARY" si et seulement si cet attribut est membre de la clée.<br/><br/>
	 * Lorsque la récupération échoue, la réponse est vide et décrit l'erreur rencontrée.
	 */
	public ResponseData<String[]> getAttributes(String table)
	{	
		ResponseData<String[]> result;
		if (this.facade.isLoaded(table)) {
			List<String[]> latt = this.facade.getAttributesBusiness(table);
			result = new ResponseData<String[]> (true, "Attributs récupérés.", latt);
		} 
		else {
			result = this.getAttributesAndPrimaries(table);
			this.facade.addAttributesToBusiness(table, result.getCollection());
			this.loadForeigns(table); //TODO : se soucier de la reponse retournée
			this.loadUniques(table); //TODO : se soucier de la reponse retournée
		}
		return result;
	}


	/**
	 * Modifie une table existante
	 */
	public Response alterTable(String oldTable, String newTable) 
	{
		return this.facade.alterTable(oldTable, newTable);
	}

	
	/**
	 * 
	 * @param name
	 * @param type
	 * @param parseInt
	 * @param notNull
	 * @param primaryKey
	 * @return
	 */
	public I_AttributeModel getAttributeModel(String name, String type, int parseInt, boolean notNull, boolean primaryKey) {
		return this.facade.getAttributeModel(name,type,parseInt,notNull,primaryKey);
	}

	/**
	 * @return un modèle de table vide pour l'IHM de création des tables.
	 */
	public I_TableModel getTableModel()
	{
		return this.facade.getTableModel();
	}
	
	
	public ResponseData<String[]> getForeignFromPrimary(String string) {
		return this.facade.getForeignFromPrimary(string);
	}


	public ResponseData<String[]> getUniqueAttributes(String string) {
		return this.facade.getUniquesFromDBMS(string);
	}


	public ResponseData<String[]> getPrimaryFromForeign(String string) {
		return this.facade.getPrimaryFromForeign(string);
	}


	public Response addForeignKey(String tableSourceName, String[] attributesSourcesNames, String tableDestinationName,
			String[] attributesDestinationsNames) {
		return this.facade.addForeignKey(tableSourceName,attributesSourcesNames,tableDestinationName,attributesDestinationsNames);
	}


	public Response addUnique(String tableSourceName, String[] attributesSourcesNames) {
		return this.facade.addUnique(tableSourceName,attributesSourcesNames);
		
	}


	public Response removeConstraint(String tableSourceName, String attribute, String constraint) {
		return this.facade.removeConstraint(tableSourceName,attribute,constraint);
		
	}

	
	/**
	 * Interroge le SGBD pour récupérer les attributs sous contraintes UNIQUE.<br/>
	 * Enregistre ces contraintes dans les classes métiers.
	 * 
	 * @param table : nom de la table, null interdit.
	 * @return une réponse personnalisée décrivant l'interrogation du SGBD.
	 */
	private Response loadUniques(String table)
	{
		ResponseData<String[]> ins_uns = this.facade.getUniquesFromDBMS(table);
		GroupByIndex groups = new GroupByIndex(ins_uns.getCollection(), 0, 1);
		for (String index : groups.getIndexs()) {
			this.facade.addUniqueBusiness(index, table, groups.getGroup(index));
		}
		return ins_uns;
	}


	/**
	 * Interroge le SGBD pour récupérer les attributs sous contraintes FOREIGN KEY.<br/>
	 * Enregistre ces contraintes dans les classes métiers.
	 * 
	 * @param table : nom de la table, null interdit.
	 * @return une réponse personnalisée décrivant l'interrogation du SGBD.
	 */
	private Response loadForeigns(String table)
	{
		ResponseData<String[]> ddl = this.facade.getPrimaryFromForeign(table);
		GroupByIndex primaryTable = new GroupByIndex(ddl.getCollection(), 5, 0);
		GroupByIndex foreignTable = new GroupByIndex(ddl.getCollection(), 5, 3);
		GroupByIndex primariesAtt = new GroupByIndex(ddl.getCollection(), 5, 1);
		GroupByIndex foreignsAtt  = new GroupByIndex(ddl.getCollection(), 5, 4);
		for (String index : primaryTable.getIndexs()) {
			this.facade.addForeignKeyToBusiness(
					index, 
					foreignTable.getGroup(index)[0], 
					foreignsAtt.getGroup(index), 
					primaryTable.getGroup(index)[0], 
					primariesAtt.getGroup(index));
		}
		return ddl;
	}
	
	
	/**
	 * Récupère les informations depuis le SGBD.
	 * 
	 * @param table : nom de la table, null interdit.
	 * @return Une réponse personnalisée avec :<br/>
	 * - le nom des attributs,<br/>
	 * - le type des attributs,<br/>
	 * - la taile des attributs,<br/>
	 * - vrai ssi les attributs sont soumis à des contraintes NOT NULL, <br/>
	 * - vrai ssi les attributs sont membre de la clée primaire.
	 */
	private ResponseData<String[]> getAttributesAndPrimaries(String table) 
	{
		ResponseData<String[]> attributesData = 
				this.facade.getAttributesDBMS(table);
		if (! attributesData.hasSuccess()) {
			return attributesData;
		}
		
		ResponseData<String> primaries = 
				this.facade.getPrimaryKeyDBMS(table);
		if (! attributesData.hasSuccess()) {
			return new ResponseData<String[]>(false, "Clées primaires non récupérées.");
		}
		
		List<String[]> collection = new ArrayList<String[]>();
		for (String [] att : attributesData.getCollection()) {
			collection.add(convertAttribute(att, primaries.getCollection()));
		}
		return new ResponseData<String[]>(true, "Attributs récupérés.", collection);
	}


	
	private static String [] convertAttribute(String [] att, List<String> primaries)
	{
		String [] result = new String [5];
		int i = 0, size = primaries.size();
		boolean primary = false;
		
		while (i < size && !primary) {
			primary = att[0].equals(primaries.get(i));
			i++;
		}
		
		for (int j=0; j < 3; j++) {
			result[j] = att[j];
		}
		
		result[3] = !primary && "NO".equals(att[3]) ? "NOTNULL" : "NULL";
		result[4] = primary ? "PRIMARY" : "COMMON";
		return result;
	}


	//Privées
	/**
	 * Affiche $gui au premier plan.
	 * 
	 * @param gui : une IHM, null interdit.
	 */
	private static void showGUI(JFrame gui)
	{
		gui.setVisible(true);
		gui.toFront();
	}
}

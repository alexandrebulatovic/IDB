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
 * 
 * @author UGOLINI Romain
 * @author MAURY Adrian
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
	public String[] getAttributeTypes()
	{
		return this.facade.getAttributeTypes();
	}



	/**
	 * @return vrai si et seulement si le SGBD permet de "droper" une 
	 * table avec l'option "CASCADE", faux sinon.
	 */
	public boolean dbmsAllowsDropCascade()
	{
		return this.facade.dbmsAllowsDropCascade();
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
		Response result = this.facade.dropTableFromDBMS(table, cascade);
		if (result.hasSuccess()) {
			this.facade.dropTableFromBusiness(table);
		}
		return result;
	}


	/**
	 * TODO : déplacer l'intelligence de la facade ici.
	 * Supprime $table et toutes les tables de la bases qui utilisent la clée primaire
	 * de $table.
	 * 
	 * @param table : une table à supprimer, null interdit.
	 * @return une réponse personnalisée qui contient le nom de toutes les tables 
	 * supprimées, ou une réponse vide en cas d'erreur.
	 */
	public Response dropTableDomino(String table)
	{
		return this.facade.dropTableDomino(table);
	}
	
	
	/**
	 * TODO : aller chercher dans les classes métiers une fois clées chargées.
	 * Retourne une réponse personnalisée qui contient les membres
	 * de la clée primaire de $table.
	 * 
	 * @param table : nom de la table, ne doit pas être null.
	 * @return CustomizedResponseWithData
	 */
	public ResponseData<String> getPrimaryKey(String table)
	{
		return this.facade.getPrimaryKey(table);
	}


	/**
	 * Ferme proprement les objets Statements.
	 */
	public void closeStatement(){this.facade.closeStatement();}


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
		//TODO Romain tu peux charger aussi les uniques / clef Etrangères stp
		ResponseData<String[]> result;
		if (this.facade.isLoaded(table)) {
			List<String[]> latt = this.facade.getAttributesFromBusiness(table);
			result = new ResponseData<String[]> (true, "Attributs récupérés.", latt);
		} 
		else {
			result = getAttributesFromDBMS(table);
//			//---unique
//			ResponseData<String[]> uniques = this.facade.getUniqueAttributes(table);
//			for (String [] un : uniques.getCollection()) {
//				this.facade.addUnique(table, un);
//			}
//			//---fin unique
//			//--foreign
//			ResponseData<String[]> foreigns
//			//--finforeign
			List<String[]> attributes = result.getCollection();
			for (String [] att : attributes) {
				this.facade.addAttribute(table, att);
			}
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
		return this.facade.getUniqueAttributes(string);
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

	
	private String [] convertAttribute(String [] att, List<String> primaries)
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

	
	/**
	 * Récupère les informations depuis le SGBD.
	 * 
	 * @param table : nom de la table, null interdit.
	 * @return Une réponse personnalisée avec :
	 * - le nom des attributs,<br/>
	 * - le type des attributs,<br/>
	 * - la taile des attributs,<br/>
	 * - vrai ssi les attributs sont soumit à des contraintes NOT NULL, <br/>
	 * - vrai ssi les attributs sont membre de la clée primaire.
	 */
	private ResponseData<String[]> getAttributesFromDBMS(String table) 
	{
		ResponseData<String[]> attributesData; 
		ResponseData<String> primaries; 
	
		attributesData = this.facade.getAttributesFromDBMS(table);
		if (! attributesData.hasSuccess()) return attributesData;
	
		primaries = this.facade.getPrimaryKey(table);
		if (! attributesData.hasSuccess()) {
			return new ResponseData<String[]>(false, "Clées primaires non récupérées.");
		}
		
		String [] attribute;
		List<String[]> collection = new ArrayList<String[]>();
		for (String [] att : attributesData.getCollection()) {
			attribute = this.convertAttribute(att, primaries.getCollection());
			collection.add(attribute);
		}
		return new ResponseData<String[]>(true, "Attributs récupérés.", collection);
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

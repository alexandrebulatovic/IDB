package controller;

import facade.DDLFacade;
import gui.ddl.CreateTableGUI;
import gui.ddl.DropTableGUI;
import gui.ddl.ModifyTableGUI;

import javax.swing.JFrame;

import ddl.I_Attribute;

import business.Table;

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
	private ModifyTableGUI modifyGUI;
	
	/** IHM pour supprimer une table.*/
	private DropTableGUI dropGUI;
	
	/** Facade pour la définition des données.*/
	private DDLFacade facade;
		
	
	//Contructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param : null interdit.
	 */
	public DDLController(DDLFacade facade)
	{
		this.facade = facade;
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
			this.modifyGUI = new ModifyTableGUI(this);
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
	 * table avec l'option "CASCADE"
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
	 * @param chain : vrai si et seulement si toutes les tables qui référencent
	 * $table doivent être supprimées aussi.
	 * @return une réponse personnalisée décrivant si la suppression de toutes
	 * les tables a réussi ou non.
	 */
	public Response dropTable(String table, boolean cascade, boolean chain)
	{
		return this.facade.dropTable(table, cascade, chain);
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
	public Response createTable(Table table)
	{
		//TODO : à revoir complètement
		//TODO : mettre une méthode dans la facade
		return null;
	}
	
	
	/**
	 * Modifie une table existante
	 */
	public Response modifyTable(Table table,Table tableSource) {
		//TODO : à revoir complètement
		//TODO : mettre une méthode dans la facade
		return null;
		
	}


	public I_Attribute getAttributeModel(String name, String type, int parseInt, boolean notNull, boolean primaryKey) {
		return this.facade.getAttributeModel(name,type,parseInt,notNull,primaryKey);
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

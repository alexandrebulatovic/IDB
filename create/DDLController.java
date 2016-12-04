package create;

import connect.CustomizedResponse;

public class DDLController 
{
	/** Controleur en cours.*/
	private static DDLController INSTANCE;
	
	//Attributs
	/** IHM pour créer une table et ses attributs.*/
	private CreateTableView createGUI;
	
	/** Objet pour gérer la communication avec un SGBD 
	 * dans l'optique d'utiliser le LDD.*/
	private DDLManager ddlor;
	
	
	//Contructeur
	/**
	 * Constructeur commun.
	 */
	private DDLController()
	{
		INSTANCE = this;
		this.ddlor = new DDLManager();
	}
	
	
	/**
	 * Retourne le controleur actif si et seulement s'il
	 * existe déjà. Retourne un nouveau controleur sinon.
	 * 
	 * @return DDLController
	 */
	public static DDLController getInstance()
	{
		if (INSTANCE == null) new DDLController();
		return INSTANCE;
	}
	
	/**
	 * Ouvre l'IHM de création des tables.
	 */
	public void openCreateGUI()
	{
		this.createGUI = CreateTableView.getInstance();
		this.createGUI.toFront();
	}
	
	/**
	 * Envoie $table au DDLManager dans l'optique de la créer dans le SGBD.
	 * 
	 * @param table : une table à créer. L'objet peut être erroné;
	 */
	public void createTable(Table table)
	{
		CustomizedResponse response = this.ddlor.createTable(table.toSQL());
		if (response.success()) {
			this.createGUI.resetView();
			this.createGUI.talk("Table " + table.getTableName() + " créée.");
		}
		else {
			this.createGUI.talk(response.message());
		}
	}
	
	
	//Privées

}

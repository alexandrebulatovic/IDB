package main;

import connect.ConnectionManager;
import create.CreateTableView;
import create.DDLController;
import sql.SQLController;

public class MainController 
{
	//Attributes
	/** Vue du controleur.*/
	private MainView gui;
	
	/** Controleur du LDD.*/
	private DDLController ddlControl;
	
	/** Controleur du SQL.*/
	private SQLController sqlControl;
	
	
	//Constructeur
	/**
	 * Constructeur lambda.
	 * 
	 * @param connector : un objet ConnectionManager.
	 */
	public MainController()
	{
		this.gui = new MainView(this);
		this.gui.talk("Bienvenue " 
				+ ConnectionManager.getInstance().user());
	}
	
	/**
	 * Ouvre l'IHM pour créer des tables.
	 */
	public void openCreateGUI()
	{
		DDLController ddlc = DDLController.getInstance();
		ddlc.openCreateGUI();
	}
	

	public void openSqlGUI()
	{
//		if (!this.sqlMode) {
//			this.sqlMode = true;
//			new SQLController();
//		}
	}
	
	
	public void openDropGUI(){}
}

package home;

import ddl.DDLController;
import ddl.create.CreateTableView;
import sql.SQLController;
import useful.ConnectionManager;

public class HomeController 
{
	//Attributes
	/** Vue du controleur.*/
	private HomeGUI gui;
	
	/** Controleur du LDD.*/
	private DDLController ddlControl;
	
	/** Controleur du SQL.*/
	private SQLController sqlControl;
	
	
	//Constructeur
	/**
	 * Constructeur lambda.
	 */
	public HomeController()
	{
		this.gui = new HomeGUI(this);
		this.gui.talk("Bienvenue " 
				+ ConnectionManager.getInstance().user());
	}
	
	
	/**
	 * Ouvre l'IHM pour créer des tables.
	 */
	public void openCreateGUI()
	{
		this.ddlControl = DDLController.getInstance();
		this.ddlControl.openCreateGUI();
	}
	

	public void openSqlGUI()
	{

	}
	
	
	public void openDropGUI()
	{
		this.ddlControl = DDLController.getInstance();
		this.ddlControl.openDropGUI();
	}
}

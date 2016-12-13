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
	
	/**
	 * Ouvre l'IHM pour rentrer des requetes SQL.
	 */
	public void openSqlGUI()
	{
		this.sqlControl = SQLController.getInstance();
		this.sqlControl.openSQL();
		
	}
	
	
	/**
	 * Ouvre l'IHM de suppression des tables.
	 */
	public void openDropGUI()
	{
		this.ddlControl = DDLController.getInstance();
		this.ddlControl.openDropGUI();
	}
	
	
	/**
	 * Ferme proprement les objets liés à la connexion.
	 */
	public void disconnect()
	{
		if (this.ddlControl != null) this.ddlControl.closeStatement();
		ConnectionManager.getInstance().disconnect();
	}

	/**
	 * Ouvre l'IHM de modification des tables.
	 */
	public void openModifyGUI() {
		this.ddlControl = DDLController.getInstance();
		this.ddlControl.openModifyGUI();
		
	}
}

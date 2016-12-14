package home;

import manager.connection.ConnectionManager;
import ddl.DDLController;
import sql.SQLController;

/**
 * Controleur principal de l'application une fois la connexion établie.
 * Singleton.
 * 
 * @author UGOLINI Romain.
 */
public class HomeController 
{
	//Instance
	/** Instance singleton en cours.*/
	private static HomeController INSTANCE;
	
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
		INSTANCE = this;
		this.gui = HomeGUI.getInstance();
		this.gui.talk("Bienvenue " 
				+ ConnectionManager.getInstance().user());
	}
	
	
	//Méthodes
	/**
	 * Retourne une nouveau controleur principal si et seulement s'il
	 * n'en existe pas, retourne un nouveau controleur sinon.
	 * 
	 * @return HomeController
	 */
	public static HomeController getInstance()
	{
		if (INSTANCE == null) new HomeController();
		return INSTANCE;
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
	public void openSQLGUI()
	{
		this.sqlControl = SQLController.getInstance();
		//this.sqlControl.openSQL();
		
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

package home;

import connect.ConnectionStrings;
import connect.DefaultValueManager;
import manager.connection.ConnectionManager;
import manager.connection.MySQLConnectionManager;
import manager.connection.OracleConnectionManager;
import ddl.DDLController;
import sql.SQLController;
import useful.Response;

/**
 * Controleur principal de l'application.
 * 
 * @author UGOLINI Romain.
 */
public class HomeController 
{
	//Attributes
	/** Gestionnaire de connexion. */
	private ConnectionManager connector;
	
	/** Controleur du LDD.*/
	private DDLController ddlControl;
	
	/** Controleur du SQL.*/
	private SQLController sqlControl;
	
	/** Gestionnaire des valeurs de connexions par défaut.*/
	private DefaultValueManager dvm;
	
	
	//Constructeur
	/**
	 * Constructeur lambda.
	 */
	public HomeController()
	{
//		this.gui.talk("Bienvenue " 
//				+ ConnectionManager.getInstance().user());
		this.dvm = new DefaultValueManager();
	}
	
	
	//Méthodes
	/**
	 * Ouvre l'IHM pour créer des tables.
	 */
	public void openCreateGUI()
	{
		this.ddlControl = DDLController.getInstance();
		this.ddlControl.openCreateGUI();
	}
	
	/**
	 * Ouvre l'IHM de modification des tables.
	 */
	public void openModifyGUI() {
		this.ddlControl = DDLController.getInstance();
		this.ddlControl.openModifyGUI();
		
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

	
	
	//Methods de coonnexion
	/**
	 * Tente d'établir une connexion vers un SGBD
	 * en utilisant les informations de connexion de $parameters.
	 * 
	 * @param parameters : un objet ConnectionStrings, null interdit.
	 * @return CustomizedResponse
	 */
	public Response connect(ConnectionStrings parameters)
	{
		this.connector = this.chooseManager(parameters.driver);
		return this.connector.connect(parameters);

	}
	
	
	/**
	 * Retourne les informations de la dernière connexion valide.
	 * 
	 * @return ConnectionStrings
	 */
	public ConnectionStrings getDefaultValues() 
	{
		return new ConnectionStrings(
				this.dvm.getDriver(), 
				this.dvm.getUrl(), 
				this.dvm.getUser(), 
				"", 
				this.dvm.getDataBase(), 
				this.dvm.getPort());
	}
	
	
	/**
	 * Retourne les informations de la dernière connexion valide
	 * au SGBD $dbms.
	 * 
	 * @param dbms : nom du SGBD, null interdit.
	 * @return ConnectionStrings
	 */
	public ConnectionStrings getDefaultValues(String dmbs) 
	{
		this.dvm.setDriver(dmbs);
		return this.getDefaultValues();
	}


	/**
	 * Enregistre certaines informations de connexion de $parameters
	 * dans un fichier xml  situé dans le répertoire courant.
	 * Le fichier est créé s'il n'existe pas.
	 * 
	 * @param param : un objet ConnectionStrings
	 */
	public void saveDefaultValue(ConnectionStrings param)
	{
		this.dvm.setDriver(param.driver);
		this.dvm.setUrl(param.url);
		this.dvm.setUser(param.user);
		this.dvm.setPort(param.port);
		this.dvm.setDataBase(param.baseName);
		this.dvm.save();
	}


	//Privates
	/**
	 * Retourne un objet pour se connecter vers un SGBD
	 * en fonction du nom de $driver passé en paramètre.
	 * 
	 * @param driver : parmi "Oracle",
	 * @return ConnectionManager
	 */
	private ConnectionManager chooseManager(String driver)
	{
		//TODO : à mettre dans une fabrique
		switch (driver){
		case "Oracle" : return OracleConnectionManager.getConnector();
		case "MySQL" : return MySQLConnectionManager.getConnector();
		default : return null;
		}
	}
}

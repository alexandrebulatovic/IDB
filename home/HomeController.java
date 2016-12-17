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
	 * @return Une réponse personnalisée qui décrit la tentative de connexion.
	 */
	public Response connect(ConnectionStrings parameters)
	{
		this.connector = this.chooseManager(parameters.driver);
		return this.connector.connect(parameters);

	}
	
	
	/**
	 * @return Les informations de la dernière connexion valide.
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
	 * @param driver : nom du pilote de SGBD, null interdit.
	 * @return Retourne les informations de la dernière connexion valide
	 * avec le pilote $driver.
	 */
	public ConnectionStrings getDefaultValues(String driver) 
	{
		this.dvm.setDriver(driver);
		return this.getDefaultValues();
	}


	/**
	 * Enregistre certaines informations de connexion de $parameters
	 * dans un fichier xml  situé dans le répertoire courant.
	 * Le fichier est créé s'il n'existe pas.
	 * 
	 * @param param : les informations de connexions, null interdit.
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

	
	/**
	 * @return Le nom de l'utilisateur si et seulement si l'application
	 * est connectée à un SGBD, null sinon.
	 */
	public String getUser(){return this.connector.user();}
	
	
	//Privates
	/**
	 * @param driver : parmi "Oracle", "MySQL", null interdit.
	 * @return Un objet pour se connecter vers un SGBD
	 * en fonction du nom de $driver passé en paramètre.
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

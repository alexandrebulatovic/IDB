package home;

import manager.ConnectionManager;
import manager.DefaultValueManager;
import manager.connection.MySQLConnectionManager;
import manager.connection.OracleConnectionManager;
import ddl.DDLController;
import sql.SQLController;
import useful.ConnectionStrings;
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
		this.dvm = new DefaultValueManager();
	}


	/**
	 * @return Les informations de la dernière connexion valide.
	 */
	public ConnectionStrings getDefaultValues() 
	{
		if (this.dvm == null) { 
			return new ConnectionStrings();
		}
		else {
			return new ConnectionStrings(
					this.dvm.getDriver(), 
					this.dvm.getUrl(), 
					this.dvm.getUser(), 
					"", 
					this.dvm.getDataBase(), 
					this.dvm.getPort());
		}

	}


	/**
	 * Enregistre $driver comme étant le dernier SGBD connecté.
	 * 
	 * @param driver : nom du pilote de SGBD, null interdit.
	 * @return Les informations de la dernière connexion valide
	 * avec le pilote $driver.
	 */
	public ConnectionStrings getDefaultValues(String driver) 
	{
		if (this.dvm != null) this.dvm.setDriver(driver);
		return this.getDefaultValues();
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
	 * Enregistre certaines informations de connexion de $parameters
	 * dans un fichier xml  situé dans le répertoire courant.
	 * Le fichier est créé s'il n'existe pas.<br/>
	 * 
	 * Après appel de cette méthode, il n'est plus possible d'utiliser
	 * le gestionnaire de valeur par défaut jusqu'à la fermeture de l'application.
	 * 
	 * @param param : les informations de connexions, null interdit.
	 */
	public void saveDefaultValue(ConnectionStrings param)
	{
		if (this.dvm != null) {
			this.dvm.setDriver(param.driver);
			this.dvm.setUrl(param.url);
			this.dvm.setUser(param.user);
			this.dvm.setPort(param.port);
			this.dvm.setDataBase(param.baseName);
			this.dvm.save();
			this.dvm = null;
		}
	}


	/**
	 * @return Le nom de l'utilisateur si et seulement si l'application
	 * est connectée à un SGBD, null sinon.
	 */
	public String getUser(){return this.connector.getUser();}


	/**
	 * Ouvre l'IHM pour rentrer des requetes SQL.
	 */
	public void openSQLGUI()
	{
		this.createOrNotSQLController();
		this.sqlControl.openSQL();
	}


	//Méthodes
	/**
	 * Ouvre l'IHM pour créer des tables.
	 */
	public void openCreateGUI()
	{
		this.createOrNotDDLControl();
		this.ddlControl.openCreateGUI();
	}
	
	public void openModifyGui() {
		this.createOrNotDDLControl();
		this.ddlControl.openModifyGUI();
		
	}



	/**
	 * Ouvre l'IHM de suppression des tables.
	 */
	public void openDropGUI()
	{
		this.createOrNotDDLControl();
		this.ddlControl.openDropGUI();
	}


	/**
	 * Ferme proprement les objets liés à la connexion.
	 */
	public void disconnect()
	{
		if (this.ddlControl != null) this.ddlControl.closeStatement();
		if (this.connector != null)  this.connector.disconnect();
	}


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
		case "Oracle" : return new OracleConnectionManager();
		case "MySQL" : return new  MySQLConnectionManager();
		default : return null;
		}
	}


	/**
	 * Définit le controleur de LDD pour $this si besoin.
	 */
	private void createOrNotDDLControl()
	{
		if (this.ddlControl == null) {
			this.ddlControl = new DDLController(this.connector.getConnection());
		}
	}

	/**
	 * Définit le controleur de SQL pour $this si besoin.
	 */
	private void createOrNotSQLController()
	{
		if (this.sqlControl == null) {
			this.sqlControl = new SQLController(this.connector.getConnection());
		}
	}



}

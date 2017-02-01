package home;

import manager.DefaultValueManager;
import manager.Facade;
import manager.I_ConnectionManager;
import crud.CRUDController;
import ddl.DDLController;
import factory.MainFactory;
import sql.SQLController;
import useful.ConnectionStrings;
import useful.Response;

/**
 * Controleur principal de l'application.
 */
public class HomeController 
{
	//Attributes
	/** Facade pour utiliser les différents gestionnaires.*/
	private Facade facade;
	
	/** Gestionnaire de connexion. */
	private I_ConnectionManager connector;

	/** Controleur du LDD.*/
	private DDLController ddlControl;

	/** Controleur du SQL.*/
	private SQLController sqlControl;
	
	/** Controleur du CRUD.*/
	private CRUDController crudControl;

	/** Gestionnaire des valeurs de connexions par défaut.*/
	private DefaultValueManager dvm;

	/** Fabrique principale et unique de l'application.*/
	private MainFactory factory;
	
	
	//Constructeur
	/**
	 * Constructeur lambda.
	 */
	public HomeController()
	{
		this.dvm = new DefaultValueManager();
		this.factory = new MainFactory();
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
					"", //mot de passe
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


	//Methodes de connexion
	/**
	 * Tente d'établir une connexion vers un SGBD
	 * en utilisant les informations de connexion de $parameters.
	 * 
	 * @param parameters : un objet ConnectionStrings, null interdit.
	 * @return Une réponse personnalisée qui décrit la tentative de connexion.
	 */
	public Response connect(ConnectionStrings parameters)
	{
		this.connector = this.createConnectionManager(parameters.driver);
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
	 * @return la liste des SGBD disponibles.
	 */
	public String [] getAvailableDBMS() {return this.factory.getAvailableDBMS();}
	
	
	/**
	 * Ouvre l'IHM pour rentrer des requetes SQL.
	 */
	public void openSQLGUI()
	{
		this.createOrNotSQLController();
		this.sqlControl.openSQL();
	}


	/**	 * Pré-requis : utilisation de la méthode connect(), avec pour retour
	 * une réponse personnalisée décrivant le succès de la connexion.
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
	 *  Ouvre l'IHM pour les opérations {@code Create - Read - Update - Delete. }
	 */
	public void openCRUDGUI() 
	{
		this.createOrNotDDLControl();
		this.createOrNotSQLController();
		this.createOrNotCRUDControl();
		this.crudControl.openCRUDGUI();
		
	}


	/**
	 * Ferme proprement les objets liés à la connexion.
	 */
	public void disconnect()
	{
		if (this.ddlControl != null) this.ddlControl.closeStatement();
		if (this.connector != null)  this.connector.disconnect();
	}


	/**
	 * @param dbms : parmi ce qu'il se trouve dans getAvailableDBMS(), null interdit.
	 * @return Un objet pour se connecter vers un SGBD
	 * en fonction du $dbms passé en paramètre.
	 */
	public I_ConnectionManager createConnectionManager(String dbms)
	{
		this.setFactory(dbms);
		return this.factory.getConnectionManager();
	}
	

	//Privates
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
	 * Définit le controleur du CRUD pour $this si besoin.
	 */
	private void createOrNotCRUDControl()
	{
		if (this.crudControl == null) {
			this.crudControl = new CRUDController(this.connector.getConnection());
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


	/**
	 * Configure la fabrique pour retourner des objets conçus pour $dbms.
	 * 
	 * @param dbms : parmi les variables statiques de MainFactory, null interdit.
	 */
	private void setFactory(String dbms)
	{
		this.factory.setDBMS(dbms);
	}
}

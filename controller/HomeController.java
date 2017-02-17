package controller;

import facade.HomeFacade;
import useful.ConnectionStrings;
import useful.Response;

/**
 * Controleur principal de l'application.
 */
public class HomeController 
{
	//Attributes
	/** Facade pour utiliser les différents gestionnaires.*/
	private HomeFacade facade;

	/** Controleur du LDD.*/
	private DDLController ddlControl;

	/** Controleur du SQL.*/
	private SQLController sqlControl;

	/** Controleur du CRUD.*/
	private CRUDController crudControl;


	//Constructeur
	/**
	 * Constructeur lambda.
	 * 
	 * @param facade : null interdit.
	 */
	public HomeController(HomeFacade facade)
	{
		this.facade = facade;
	}


	/**
	 * Force le gestionnaire à relire le fichier XML.
	 */
	public void reloadDefaultValue()
	{
		this.facade.reloadDefaultValue();
	}


	/**
	 * @return Les informations de la dernière connexion valide.
	 */
	public ConnectionStrings getDefaultValues() 
	{
		return this.facade.getDefaultValues();
	}


	/**
	 * Enregistre $driver comme étant le dernier SGBD connecté.
	 * 
	 * @param dbms : nom du pilote de SGBD, null interdit.
	 * @return Les informations de la dernière connexion valide
	 * avec le SGBD $dbms.
	 */
	public ConnectionStrings getDefaultValues(String dbms) 
	{
		return this.facade.getDefaultValues(dbms);
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
		return this.facade.connect(parameters);
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
		this.facade.saveDefaultValue(param);
	}


	/**
	 * @return Le nom de l'utilisateur si et seulement si l'application
	 * est connectée à un SGBD, null sinon.
	 */
	public String getUser(){return this.facade.getUser();}


	/**
	 * @return la liste des SGBD disponibles.
	 */
	public String [] getAvailableDBMS() 
	{
		return this.facade.getAvailableDBMS();
	}


	/**
	 * Ferme proprement les objets liés à la connexion.
	 */
	public void disconnect()
	{
		if (this.ddlControl != null) this.ddlControl.closeStatement();
		this.facade.disconnect();
	}


	/**
	 * Ouvre l'IHM pour rentrer des requetes SQL.
	 */
	public void openSQLGUI()
	{
		this.createOrNotSQLController();
		this.sqlControl.openSQLGUI();
	}


	/**	 * Pré-requis : utilisation de la méthode connect(), avec pour retour
	 * une réponse personnalisée décrivant le succès de la connexion.
	 * Ouvre l'IHM pour créer des tables.
	 */
	public void openCreateGUI()
	{
		this.createOrNotDDLController();
		this.ddlControl.openCreateGUI();
	}

	public void openModifyGui() {
		this.createOrNotDDLController();
		this.ddlControl.openModifyGUI();

	}


	/**
	 * Ouvre l'IHM de suppression des tables.
	 */
	public void openDropGUI()
	{
		this.createOrNotDDLController();
		this.ddlControl.openDropGUI();
	}

	public void openConstraintsGUI() {
		this.createOrNotDDLController();
		this.ddlControl.openConstraintsGUI();

	}


	public void openQbeGUI() {
		this.createOrNotDDLController();
		this.ddlControl.openQbeGUI();

	}

	/**
	 *  Ouvre l'IHM pour les opérations {@code Create - Read - Update - Delete. }
	 */
	public void openCRUDGUI() 
	{
		this.createOrNotDDLController();
		this.createOrNotSQLController();
		this.createOrNotCRUDController();
		this.crudControl.openCRUDGUI();
	}


	//Privates
	/**
	 * Définit le controleur de LDD pour $this si besoin.
	 */
	private void createOrNotDDLController()
	{
		if (this.ddlControl == null) 
		{
			this.ddlControl = new DDLController(this.facade.getDDLFacade());
		}
	}


	/**
	 * Définit le controleur du CRUD pour $this si besoin.
	 */
	private void createOrNotCRUDController()
	{
		if (this.crudControl == null)
		{
			this.crudControl = new CRUDController(this.facade.getCRUDFacade());
		}
	}


	/**
	 * Définit le controleur de SQL pour $this si besoin.
	 */
	private void createOrNotSQLController()
	{
		if (this.sqlControl == null) 
		{
			this.sqlControl = new SQLController(this.facade.getSQLFacade());
		}
	}
}
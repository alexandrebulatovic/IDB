package connect;

import manager.connection.ConnectionManager;
import manager.connection.MySQLConnectionManager;
import manager.connection.OracleConnectionManager;
import home.HomeController;
import useful.CustomizedResponse;

/**
 * Gère le dialogue entre l'IHM et le connecteur au SGBD.
 * 
 * @author UGOLINI Romain
 */
public class ConnectionController 
{
	//Attributes
	/** IHM pour se connecter à un SGBD.*/
	private ConnectionGUI gui;
	
	/** Objet pour se connecter à un SGBD.*/
	private ConnectionManager connector;
	
	/** Gestionnaire des valeurs de connexions par défaut.*/
	private DefaultValueManager dvm;
	
	//Constructeurs
	/**
	 * Constructeur commun.
	 */
	public ConnectionController()
	{
		this.dvm = new DefaultValueManager();
		this.gui = new ConnectionGUI(this);
	}
	
	
	//Methods
	/**
	 * Tente d'établir une connexion vers un SGBD
	 * en utilisant les informations de connexion de $parameters.
	 * 
	 * @param parameters : un objet ConnectionStrings
	 */
	public void connect(ConnectionStrings parameters)
	{
		this.connector = this.chooseManager(parameters.driver);
		CustomizedResponse response = this.connector.connect(parameters);
		this.gui.talk(response.toString());
		
		if (response.hasSuccess()) {
			this.saveDefaultValue(parameters);
			this.gui.dispose();
			HomeController.getInstance();
		}
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
		switch (driver){
		case "Oracle" : return OracleConnectionManager.getConnector();
		case "MySQL" : return MySQLConnectionManager.getConnector();
		default : return null;
		}
	}
	
	
	/**
	 * Enregistre certaines informations de connexion de $parameters
	 * dans un fichier xml  situé dans le répertoire courant.
	 * Le fichier est créé s'il n'existe pas.
	 * 
	 * @param param : un objet ConnectionStrings
	 */
	private void saveDefaultValue(ConnectionStrings param)
	{
		DefaultValueManager dvm = new DefaultValueManager();
		dvm.setDriver(param.driver);
		dvm.setUrl(param.url);
		dvm.setUser(param.user);
		dvm.setPort(param.port);
		dvm.setDataBase(param.baseName);
		dvm.save();
	}
}


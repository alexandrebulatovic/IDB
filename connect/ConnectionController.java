package connect;

import main.MainController;

/**
 * Gère le dialogue entre l'IHM et le connecteur au SGBD.
 */
public class ConnectionController 
{
	//Attributes
	/**
	 * IHM pour se connecter à un SGBD.
	 */
	private ConnectionView ihm;
	
	/**
	 * Objet pour se connecter à un SGBD.
	 */
	private ConnectionManager connector;
	
	
	//Constructeurs
	/**
	 * Constructeur commun.
	 */
	public ConnectionController ()
	{
		this.ihm = new ConnectionView(this);
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
		this.talk("Tentative de connexion...");
		ConnectionResponse response = this.connector.connect(parameters);
		this.talk(response.toString());
		if (response.success()) {
			this.saveDefaultValue(parameters);
			this.ihm.dispose();
			new MainController(this.connector);
		}
	}
	 
	
	/**
	 * Communique avec l'utilisateur en lui affichant $msg.
	 * 
	 * @param msg : un message à transmettre à l'utilisateur.
	 */
	public void talk(String msg)
	{
		this.ihm.talk(msg);
	}
	
	
	//Privates
	/**
	 * Retourne un objet pour se connecter vers un SGBD
	 * en fonction du nom de $driver passé en paramètre.
	 * 
	 * @param driver : le nom grossier du pilote, parmi
	 * "Oracle"
	 * @return ConnectionManager
	 */
	private ConnectionManager chooseManager(String driver)
	{
		switch (driver){
		case "Oracle" : return new OracleConnectionManager();
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
		dvm.setUrl(param.url);
		dvm.setUser(param.user);
		dvm.setPort(param.port);
		dvm.setDataBase(param.baseName);
		dvm.save();
	}
}


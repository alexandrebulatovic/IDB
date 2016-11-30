package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gère la connection à un SGBD.
 */
public abstract class ConnectionManager 
{
	//Attributes
	/**
	 * Nom du driver java pour utiliser la base de données.
	 */
	protected final String driverName;
	
	
	/**
	 * Driver java utiliser pour la base de données.
	 */
	protected Class<?> driver;
	
	/**
	 * Adresse du SGBD connecté avec succès.
	 */
	protected String url;
	
	/**
	 * Nom d'utilisateur connecté au SGBD avec succès.
	 */
	protected String user;
	
	/**
	 * Mot de passe d'utilisateur de la Base de Données.
	 */
	protected String pswd;
	
	/**
	 * indentificateur
	 */
	protected String baseName;
	
	/**
	 * Le port nécessaire à la connexion
	 */
	protected String port;
	
	/**
	 * Un objet qui représente la connexion à un SGBD.
	 */
	protected Connection dbms;


	/**
	 * Contient tous les paramètres d'une connexion réussie.
	 */
	protected ConnectionStrings parameters;
	
	
	//Constructor
	/**
	 * Constructeur commun
	 * 
	 * @param driverName : Nom du driver java pour utiliser la base de données.
	 */
	protected ConnectionManager(String driverName)
	{
		this.driverName = driverName;
	}
	
	
	//Methods
	/**
	 * Tente d'établir une connexion vers un SGBD en fonction
	 * des informations de connexions de $parameters.
	 * Retourne un objet qui décrit la tentative de connexion.
	 * 
	 * @param parameters : un objet ConnectionStrings
	 * @return ConnectionResponse
	 */
	public CustomizedResponse connect(ConnectionStrings param)
	{
		CustomizedResponse result;
		if (! this.loadDriver()) {
			result = new CustomizedResponse(false, "problème de pilote.");
		}		
		else{
			result = this.reachConnection(param);
		}
		return result;
	}
	
	
	/**
	 * Tente de re-établir une connexion vers le SGBD précédement
	 * atteint avec succès, et dont les paramètres de connexions 
	 * sont toujours enregistrés.
	 * Retourne un objet qui décrit la tentative de connexion.
	 * 
	 * Ne doit être utilisé que si $this s'est déjà connecté avec succès
	 * au cours de l'exécution de l'application.
	 * 
	 * @return CustomizedResponse
	 */
	public CustomizedResponse connect()
	{
		return this.connect(this.parameters);
	}
	
	/**
	 * Retourne le nom de l'utilisateur connecté.
	 * 
	 * @return String
	 */
	public String user(){return this.user;}


	/**
	 * Retourne le SGBD avec lequel est connecté $this.
	 * 
	 * @return Connection
	 */
	public Connection dbms() {return this.dbms;}
	
	
	/**
	 * Retourne vrai si et seulement si $this est connecté à un SGBD,
	 * faux sinon.
	 * 
	 * @return boolean
	 */
	public boolean isConnected() {return this.dbms != null;}


	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		StringBuilder result = new StringBuilder ();
		result.append(this.isConnected() 
				? "Connecté à l'adresse : " 
				: "Non connecté.\n");
		if (this.isConnected()) {
			result.append("Pilote : " + this.driver.toString());
			result.append(this.url + "\n");
			result.append("Nom d'utilisateur : " + this.user + "\n");
			result.append("Port : " + this.port);
		}
		return result.toString();
	}
	
	
	//Protected
	/**
	 * Enregistre les informations de connexions en fonction
	 * de $dbms et $param.
	 * Cette méthode ne doit être appelée que si la connexion
	 * avec un SGBD est effective.
	 * 
	 * @param dbms : un objet Connection
	 * @param param : un objet ConnectionStrings
	 */
	protected void set(Connection dbms, ConnectionStrings param)
	{
		this.url = param.url;
		this.user = param.user;
		this.pswd = param.password;
		this.baseName = param.baseName;
		this.port = param.port;
		this.dbms = dbms;
		this.parameters = param;
	}
	
	
	/**
	 * Charge le pilôte pour se connecter au SGBD correspondant.
	 * Retourne vrai si et seulement si le pilôte est correctement chargé,
	 * faux sinon.
	 * 
	 * @return boolean
	 */
	protected boolean loadDriver()
	{
		boolean result;
		try{
			this.driver = Class.forName(this.driverName);
			result = true;
		}
		catch(Exception e1){
			result = false;
		}
		return result;
	}
	
	
	/**
	 * Tente d'établir une connexion vers un SGBD en fonction
	 * des informations de connexions de $param.
	 * Retourne un objet qui décrit la tentative de connexion.
	 * 
	 * @return ConnectionResponse
	 */
	protected CustomizedResponse reachConnection(ConnectionStrings param)
	{
		Connection dbms;
		CustomizedResponse result;
		String entireUrl =this.entireUrl(param);
		try {
			dbms = DriverManager.getConnection(
					entireUrl, 
					param.user, 
					param.password);
			this.set(dbms, param);
			result = new CustomizedResponse(true,  "Connexion réussie.");
		}
		catch(SQLException e){
			result = new CustomizedResponse(false, this.errorMessage(e));
		}
		catch(Exception e){
			result =  new CustomizedResponse(false, "inconnue.");
		}
		return result;
	}
	
	
	/**
	 * Retourne un message d'erreur plus lisible 
	 * que celui retourné par le SGBD après une tentative de connexion.
	 * 
	 * @return String
	 */
	protected abstract String errorMessage(SQLException e);
	
	
	/**
	 * Retourne l'adresse complète pour se connecter à un SGBD,
	 * en fonction des informations de $param.
	 * 
	 * @param param : un objet ConnectionStrings
	 * @return String
	 */
	protected abstract String entireUrl(ConnectionStrings param);
}

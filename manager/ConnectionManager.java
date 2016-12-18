package manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import useful.ConnectionStrings;
import useful.Response;



/**
 * Gère la connexion et la déconnexion à un SGBD.
 * 
 * @author UGOLINI Romain
 */
public abstract class ConnectionManager 
{
	//Attributes
	/** Nom du driver java pour utiliser la base de données.*/
	protected final String driverName;
	
	/** Driver java utiliser pour la base de données.*/
	protected Class<?> driver;
	
	/** Adresse du SGBD connecté avec succès.*/
	protected String url;
	
	/** Nom d'utilisateur connecté au SGBD avec succès.*/
	protected String user;
	
	/** Mot de passe d'utilisateur de la Base de Données.*/
	protected String pswd;
	
	/** Nom de la base de données.*/
	protected String baseName;
	
	/** Le port nécessaire à la connexion.*/
	protected String port;
	
	/** Un objet qui représente la connexion à un SGBD.*/
	protected Connection dbms;


	/** Contient tous les paramètres d'une connexion réussie.*/
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
	
	
	//Accesseurs
	/**
	 * @return Le nom de l'utilisateur connecté.
	 */
	public String getUser(){return this.user;}


	/**
	 * @return Le SGBD avec lequel est connecté $this, null si n'est pas connecté.
	 */
	public Connection getConnection() {return this.dbms;}
	
	
	/**
	 * @return Vrai si et seulement si $this est connecté à un SGBD,
	 * faux sinon.
	 */
	public boolean isConnected() {return this.dbms != null;}


	//Methods
	/**
	 * Tente d'établir une connexion vers un SGBD en fonction
	 * des informations de connexions de $param.
	 * 
	 * @param param : un objet ConnectionStrings
	 * @return un objet qui décrit la tentative de connexion.
	 */
	public Response connect(ConnectionStrings param)
	{
		Response result;
		if (! this.loadDriver()) {
			result = new Response(false, "problème de pilote.");
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
	 * 
	 * Ne doit être utilisé que si $this s'est déjà connecté avec succès
	 * au cours de l'exécution de l'application.
	 * 
	 * @return Un objet qui décrit la tentative de connexion.
	 */
	public Response reconnect()
	{
		return this.connect(this.parameters);
	}
	
	
	/**
	 * Ferme proprement la connexion.
	 */
	public void disconnect()
	{
		try {this.dbms.close();} 
		catch (SQLException e) {}
	}
	
	
	@Override
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
	 * @param dbms : null interdit.
	 * @param param : null interdit.
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
	 * Charge le pilote pour se connecter au SGBD correspondant.
	 * 
	 * @return Vrai si et seulement si le pilôte est correctement chargé,
	 * faux sinon.
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
	 * 
	 * @param param : null interdit.
	 * @return Un objet qui décrit la tentative de connexion.
	 */
	protected Response reachConnection(ConnectionStrings param)
	{
		Connection dbms;
		Response result;
		String entireUrl =this.entireUrl(param);
		try {
			dbms = DriverManager.getConnection(
					entireUrl, 
					param.user, 
					param.password);
			this.set(dbms, param);
			result = new Response(true,  "Connexion réussie.");
		}
		catch(SQLException e){
			result = new Response(false, this.errorMessage(e));

		}
		catch(Exception e){
			result =  new Response(false, "inconnue.");
		}
		return result;
	}
	
	
	/**
	 * @return Un message d'erreur plus lisible 
	 * que celui retourné par le SGBD après une tentative de connexion.
	 */
	protected abstract String errorMessage(SQLException e);
	
	
	/**
	 * @param param : null interdit.
	 * @return L'adresse complète pour se connecter à un SGBD,
	 * en fonction des informations de $param.
	 */
	protected abstract String entireUrl(ConnectionStrings param);
}

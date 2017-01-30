package manager.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import manager.I_ConnectionManager;

import useful.ConnectionStrings;
import useful.Response;



/**
 * Gère la connexion et la déconnexion à un SGBD.
 * 
 * @author UGOLINI Romain
 */
public abstract class AbstractConnectionManager 
implements I_ConnectionManager 
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
	protected AbstractConnectionManager(String driverName)
	{
		this.driverName = driverName;
	}
	
	
	//Accesseurs
	@Override
	public String getUser(){return this.user;}

	
	@Override
	public Connection getConnection() {return this.dbms;}
	
	
	@Override
	public boolean isConnected() {return this.dbms != null;}


	//Methods
	@Override
	public Response connect(ConnectionStrings param)
	{
		Response result;
		try {
			this.loadDriver();
			this.reachConnection(param);
			result = new Response(true, "Connexion réussie.");
		}
		catch(SQLException e){
			result = new Response(false, this.errorMessage(e));
		}
		catch(Exception e){
			result = new Response(false, "problème de pilote.");
		}
		return result;
	}
	
	
	@Override
	public Response reconnect()
	{
		return this.connect(this.parameters);
	}
	
	
	@Override
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
	 * Enregistre les informations de connexion en fonction
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
	 * @throws Exception
	 */
	protected void loadDriver() throws Exception
	{
		this.driver = Class.forName(this.driverName);
	}
	
	
	/**
	 * Tente d'établir une connexion vers un SGBD en fonction
	 * des informations de connexion de $param.
	 * Enregistre ces informations si et seulement si la connexion
	 * réussie, sinon ne els enregistre pas.
	 * 
	 * @param param : null interdit.
	 * @throws SQLException
	 */
	protected void reachConnection(ConnectionStrings param)
	throws SQLException
	{
		Connection dbms;
		String entireUrl = this.entireUrl(param);
		dbms = DriverManager.getConnection(
				entireUrl, 
				param.user, 
				param.password);
		this.set(dbms, param);
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

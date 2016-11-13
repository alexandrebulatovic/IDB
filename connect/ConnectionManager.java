package connect;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Gère la connection à un SGBD.
 */
public class ConnectionManager 
{
	//Attributes
	/**
	 * Un objet qui représente la connexion à un SGBD.
	 */
	private Connection connection;
	
	/**
	 * Adresse du SGBD connté avec succès.
	 */
	private String url;
	
	/**
	 * Nom d'utilisateur connecté au SGBD avec succès.
	 */
	private String user;
	
	
	//Constructeur
	/**
	 * Constructeur commun.
	 */
	public ConnectionManager()
	{
		this.connection = null;
		this.url = null;
		this.user = null;
	}
	
	
	//Methods
	/**
	 * Etablit une connexion vers le SGBD $url, pour l'utilisateur $user,
	 * avec son mot de passe $pswd.
	 * Retourne un objet qui décrit grossièrement la tentative de connexion.
	 * 
	 * @param url : url du SGBD.
	 * @param user : nom d'utilisateur souhaitant se connecter.
	 * @param pswd : mot de passe de l'utilisateur.
	 * @return ConnectionResponse
	 */
	public ConnectionResponse connect(String url, String user, String pswd)
	{
		Connection conn;
		try{
			Class.forName("oracle.jdbc.OracleDriver");
		}
		catch(Exception e1){
			return new ConnectionResponse(false, "problème de pilote Oracle.");
		}
		
		try {
			conn = DriverManager.getConnection(url, user, pswd);
		}
		catch(Exception e2){
			return new ConnectionResponse(false, "impossible de se connecter au SGBD.");
		}
		
		this.connection = conn;
		return new ConnectionResponse(true, "Connexion réussie.");
	}
	
	
	/**
	 * Retourne vrai si et seulement si $this est connecté à un SGBD,
	 * faux sinon.
	 * 
	 * @return boolean
	 */
	public boolean isConnected() {return this.connection != null;}
	
	
	/**
	 * Retourne le SGBD avec lequel est connecté $this.
	 * 
	 * @return Connection
	 */
	public Connection sgbd() {return this.connection;}
	
	
	/**
	 * Retourne une chaîne de caractères qui décrit $this.
	 */
	public String toString()
	{
		StringBuilder result = new StringBuilder ();
		result.append(this.isConnected() 
				? "Connecté à l'adresse : " 
				: "Non connecté.\n");
		if (this.isConnected()) {
			result.append(this.url + "\n");
			result.append("Nom d'utilisateur : " + this.user + "\n");
		}
		return result.toString();
	}
}

package connect;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager 
{
	//Attributes
	/**
	 * Un objet qui représente la connexion à un SGBD.
	 */
	private Connection connection;
	
	
	//Constructeur
	public ConnectionManager()
	{
		this.connection = null;
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
			return new ConnectionResponse(false, "problème de pilôte Oracle.");
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
	 * Retourne vrai si et seulement si $this est connecté à un SGBD.
	 * 
	 * @return boolean
	 */
	public boolean isConnected() {return this.connection != null;}
}

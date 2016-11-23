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
	 * Mot de passe d'utilisateur de la Base de Données.
	 */
	private String pswd;
	
	/**
	 * driver utilisé pour la base de données
	 */
	Class<?> driver;

	
	/**
	 * indentificateur
	 */
	private String baseName;
	
	/**
	 * Le port nécessaire à la connexion
	 */
	private int port;
	
	
	/**
	 * Un objet qui représente la connexion à un SGBD.
	 */
	private Connection connection;
	
	/**
	 * Adresse du SGBD connecté avec succès.
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
	public ConnectionResponse connect(String driver, String url, String user, String pswd, String baseName, int port)
	{
		Connection conn;
		try{
			if (driver.equals("Oracle")){
				this.driver = Class.forName("oracle.jdbc.OracleDriver");
			}
			
		}
		catch(Exception e1){
			return new ConnectionResponse(false, "problème de pilote.");
		}
		
		
		 String entireUrl = this.getEntireUrl(url,port,baseName);
		try {
			conn = DriverManager.getConnection(entireUrl, user, pswd);
		}
		catch(Exception e2){
			System.out.println("impossible de se connecter au SGBD.");
			System.out.println("Connexion : " + entireUrl);
			return new ConnectionResponse(false, "impossible de se connecter au SGBD.");
		}
		
		this.connection = conn;
		this.port=port;
		this.baseName=baseName;
		this.url=url;
		this.user=user;
		return new ConnectionResponse(true, "Connexion réussie.");
	}
	
	
	/**
	 * retourne une chaine de charactère permettant de se connecter à jdbc d'après
	 * toutes les informations disponibles dans les attributs de la classe
	 * @param baseName 
	 * @param port 
	 * @param url 
	 * @return String
	 */
	private String getEntireUrl(String url, int port, String baseName){	
		String stringReturn = "jdbc:";
		stringReturn +=":thin:@" + url + ":" + String.valueOf(port) +":" + baseName;
		return stringReturn;
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
	 *
	 *@return String
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

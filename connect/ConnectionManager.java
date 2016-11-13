package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.MissingResourceException;

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
	
	/**
	 * Mot de passe d'utilisateur de la Base de Données.
	 */
	private String password;
	
	/**
	 * driver utilisé pour la base de données
	 */
	Class<?> driver;

	
	/**
	 * indentificateur
	 */
	private String identifier;
	
	/**
	 * Le port nécessaire à la connexion
	 */
	private String port;
	
	
	
	//Constructeur
	/**
	 * Constructeur commun.
	 */
	public ConnectionManager()
	{
		this.connection = null;
		this.url = null;
		this.user = null;
		
		this.port = null;
		this.identifier = "IUT";
		this.password = null;
	}
	public void set(String url,String port,String db,String user){
		this.set(url, port, db, user,"","IUT");
	}
	
	public void set(String url,String port,String db,String user,String password){
		this.set(url, port, db, user,password,"IUT");
	}
	
	public void set(String url,String port,String db,String user,String identifier,String password){
		this.url = url;
		this.user = db;
		
		this.port = port;
		this.identifier = identifier;
		this.password = password;
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
	
	public ConnectionResponse connect(String url, String user, String pswd){
		this.url = url;
		this.user = user;
		this.password = pswd;
		return this.connect();
	}
	
	/**
	 * Se connecte et instancie une connection qu'il retourne
	 * @return ConnectionReponse
	 */
	public ConnectionResponse connect()
	{

		
		Connection conn;
		try{
			//ici le driver sera choisi à l'extérieur de cette classe car elle dépendra de la vue
			driver = Class.forName("oracle.jdbc.OracleDriver");
			
			if (this.port == null)
				this.port = "1521";

		}
		catch(Exception e1){
			return new ConnectionResponse(false, "problème de pilote Oracle.");
		}
		
		
		try {
			conn = DriverManager.getConnection(this.getEntireUrl(), this.user, this.password);
		}
		catch(Exception e2){
			
			System.out.println(this.getEntireUrl());
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
	
	/**
	 * retourne une chaine de charactère permettant de se connecter à jdbc d'après
	 * toutes les informations disponibles dans les attributs de la classe
	 * @return String
	 */
	private String getEntireUrl(){
		
		String stringReturn = "jdbc:";
		if (this.driver.getSimpleName().equals("OracleDriver") == true){
			stringReturn += "oracle";
		}
		else{
			throw new MissingResourceException("Erreur, le driver n'est pas connu", driver.getName(),stringReturn);
		}
		stringReturn +=":thin:@" + this.url + ":" + this.port +":" + this.identifier;
		System.out.println(stringReturn);
		return stringReturn;
	}
	
}

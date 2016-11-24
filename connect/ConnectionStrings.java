package connect;

/**
 * Classe pour enrouler les différentes chaînes de
 * caractères indispensable à la connexion vers un
 * SGBD.
 */
public class ConnectionStrings 
{
	//Attributs
	/**
	 * Nom grossier du pilote.
	 */
	public String driver;
	
	/**
	 * Adresse du SGBD.
	 */
	public String url;
	
	/**
	 * Nom d'utilisateur.
	 */
	public String user;
	
	/**
	 * Mot de passe.
	 */
	public String password;
	
	/**
	 * Nom de la base de données.
	 */
	public String baseName;
	
	/**
	 * Port nécessaire à la connexion.
	 */
	public String port;
	
	
	//Constructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param driver : Nom grossier du pilote.
	 * @param url : Adresse du SGBD.
	 * @param user : Nom d'utilisateur.
	 * @param password : Mot de passe.
	 * @param baseName : Nom de la base de données.
	 * @param port : Port nécessaire à la connexion.
	 */
	public ConnectionStrings
	(String driver, String url, String user, String password, String baseName, String port)
	{
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
		this.baseName = baseName;
		this.port = port;
	}
}

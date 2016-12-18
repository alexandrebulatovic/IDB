package useful;

/**
 * Classe pour enrouler les différentes chaînes de
 * caractères indispensable à la connexion vers un
 * SGBD.
 * 
 * @author UGOLINI Romain
 */
public class ConnectionStrings 
{
	//Attributs
	/** Nom grossier du pilote.*/
	public String driver;
	
	/** Adresse du SGBD.*/
	public String url;
	
	/** Nom d'utilisateur.*/
	public String user;
	
	/** Mot de passe.*/
	public String password;
	
	/** Nom de la base de données.*/
	public String baseName;
	
	/** Port nécessaire à la connexion.*/
	public String port;
	
	
	//Constructeur
	/**
	 * Constructeur vide.
	 * Passe des chaînes vides à chaque paramètre de connexion.
	 */
	public ConnectionStrings()
	{
		this.driver = "";
		this.url = "";
		this.user = "";
		this.password = "";
		this.baseName = "";
		this.port = "";
	}
	
	
	/**
	 * Constructeur commun.
	 * 
	 * @param driver : Nom grossier du pilote, null interdit.
	 * @param url : Adresse du SGBD, null interdit.
	 * @param user : Nom d'utilisateur, null interdit.
	 * @param password : Mot de passe, null interdit.
	 * @param baseName : Nom de la base de données, null interdit.
	 * @param port : Port nécessaire à la connexion, null interdit.
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

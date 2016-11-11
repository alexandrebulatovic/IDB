package connect;

public class ConnectionController 
{
	//Attributes
	/**
	 * IHM pour se connecter à un SGBD.
	 */
	private ConnectionView view;
	
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
		this.view = new ConnectionView(this);
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
		this.connector = new ConnectionManager();
		return connector.connect(url, user, pswd);
	}
}

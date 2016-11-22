package connect;

import main.MainController;

/**
 * Gère le dialogue entre l'IHM et le connecteur au SGBD.
 */
public class ConnectionController 
{
	//Attributes
	/**
	 * IHM pour se connecter à un SGBD.
	 */
	private ConnectionView ihm;
	
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
		this.ihm = new ConnectionView(this);
		this.connector = new ConnectionManager();
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
	public void connect(String url, String user, String pswd)
	{
		ConnectionResponse response;
		this.talk("Tentative de connexion...");
		response = this.connector.connect(url, user, pswd);
		this.talk(response.toString());
		if (response.success()) {
			this.saveDefaultValue(url, user);
			this.ihm.dispose();
			new MainController(this.connector);
		}
	}
	
	
	/**
	 * Communique avec l'utilisateur en lui affichant $msg.
	 * 
	 * @param msg : un message à transmettre à l'utilisateur.
	 */
	public void talk(String msg)
	{
		this.ihm.talk(msg);
	}
	
	
	//Privates
	private void saveDefaultValue(String url, String user)
	{
		DefaultValueManager dvm = new DefaultValueManager();
		dvm.setUrl(url);
		dvm.setUser(user);
		System.out.println(dvm);
		dvm.save();
	}
}


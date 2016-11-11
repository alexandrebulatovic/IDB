package connect;

public class ConnectionResponse 
{
	//Attributes
	/**
	 * Vrai si la connexion a eu lieu, faux sinon.
	 */
	private boolean response;
	
	/**
	 * Message associé à la connexion
	 */
	private String msg;
	
	
	//Constructeurs
	/**
	 * Constructeur commun.
	 * 
	 * @param response : vrai si la connexion a eu lieu, faux sinon.
	 * @param msg : message associé à la connexion.
	 */
	public ConnectionResponse(boolean response, String msg)
	{
		this.response = response;
		this.msg = msg;
	}
	
	
	//Methods
	/**
	 * Retourne vrai si et seulement si la tentative de connexion 
	 * a réussie, faux sinon.
	 * 
	 * @return boolean
	 */
	public boolean success(){return this.response;}
	
	
	/**
	 * Retourne un message décrivant grossièrement le succès ou
	 * l'échec de la tentative de connexion.
	 * 
	 * @return String
	 */
	public String message(){return this.msg;}
}

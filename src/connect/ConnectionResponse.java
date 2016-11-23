package connect;

/**
 * Fournit plusieurs informations sur une tentative de connection.
 */
public class ConnectionResponse 
{
	//Attributes
	/**
	 * Vrai si la connexion a eu lieu, faux sinon.
	 */
	private boolean response;
	
	/**
	 * Message associé à la tentative de connexion.
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
	
	
	/**
	 * Retourne une chaîne de caractères qui décrit $this.
	 */
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append(this.success() 
				? ""
				: "Erreur : ");
		result.append(this.message());
		return result.toString();
	}
}

package useful;

/**
 * Fournit plusieurs informations sur une tentative de connexion
 * ou de requête sur le SGBD.
 */
public class CustomizedResponse 
{
	//Attributes
	/**
	 * Vrai si la tentative a réussie, faux sinon.
	 */
	private boolean response;
	
	/**
	 * Message associé à la tentative.
	 */
	private String msg;
	
	
	//Constructors
	/**
	 * Constructeur commun.
	 * 
	 * @param response : vrai si la tentative a réussie, faux sinon.
	 * @param msg : message associé à la tentative.
	 */
	public CustomizedResponse(boolean response, String msg)
	{
		this.response = response;
		this.msg = msg;
	}
	
	
	//Methods
	/**
	 * Retourne vrai si et seulement si la tentative
	 * a réussie, faux sinon.
	 * 
	 * @return boolean
	 */
	public boolean success(){return this.response;}
	
	
	/**
	 * Retourne un message décrivant grossièrement le succès ou
	 * l'échec de la tentative.
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

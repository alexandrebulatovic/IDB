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
	protected boolean response;
	
	/**
	 * Message associé à la tentative.
	 */
	protected String msg;
	
	
	//Constructors
	/**
	 * Constructeur commun.
	 * 
	 * @param response : vrai ssi la tentative a réussie, faux sinon.
	 * @param msg : message associé à la tentative.
	 */
	public CustomizedResponse(boolean response, String msg)
	{
		this.response = response;
		this.msg = msg;
	}
	
	/**
	 * Contructeur par recopie.
	 * 
	 * @param copy : un objet CustomizedResponse à recopier.
	 */
	public CustomizedResponse(CustomizedResponse copy)
	{
		this.response = copy.response;
		this.msg = copy.msg;
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

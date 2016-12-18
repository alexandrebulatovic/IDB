package useful;

/**
 * Propose une solution pour savoir si une quelconque requête envoyée au SGBD
 * a réussie ou échouée.<br/><br/>
 * 
 * La totalité des méthodes proposées par Java pour agir sur le SGBD peuvent lever une
 * SQLException. Même si les différents gestionnaires de l'application travaillent avec ces 
 * Exceptions,
 * ce n'est pas le cas des IHM et des controleurs, et cela doit rester ainsi.<br/><br/>
 * 
 * Pour maintenir cette séparation, cette classe met à disposition un premier attribut de type
 * boolean, qui est vrai si et seulement si la requête a réussie, faux sinon. Le deuxième 
 * attribut est une chaîne de caractères qui dans l'idéal contient le message d'erreur obtenu
 * lorsque la requête échoue (disponible via Exception.getMessage()), ou bien un message de 
 * réussite à la charge du développeur lorsque la requête réussit.<br/>
 * 
 * @author UGOLINI Romain
 */
public class Response 
{
	//Attributes
	/** Vrai si la tentative a réussie, faux sinon.*/
	protected boolean response;
	
	/** Message associé à la tentative.*/
	protected String msg;
	
	
	//Constructors
	/**
	 * Constructeur sans message.
	 * 
	 * @param response : vrai ssi la tentative a réussie, faux sinon.
	 */
	public Response(boolean response)
	{
		this.response = response;
		this.msg = "";
	}
	
	
	/**
	 * Constructeur commun.
	 * 
	 * @param response : vrai ssi la tentative a réussie, faux sinon.
	 * @param msg : message associé à la tentative, null interdit.
	 */
	public Response(boolean response, String msg)
	{
		this.response = response;
		this.msg = msg;
	}
	
	
	/**
	 * Contructeur en cas d'erreur.
	 * Définit la requête comme échouée, et stocke le message d'erreur.
	 * 
	 * @param e : une exception levée, null interdit.
	 */
	public Response (Exception e)
	{
		this.response = false;
		this.msg = e.getMessage();
	}
	
	
	/**
	 * Contructeur par recopie.
	 * 
	 * @param copy : null interdit.
	 */
	public Response(Response copy)
	{
		this.response = copy.response;
		this.msg = copy.msg;
	}
	
	
	//Mutateurs
	/**
	 * Définit le message de $this comme étant $msg.
	 * 
	 * @param msg : nouveau message de $this, null interdit.
	 */
	public void setMessage(String msg){this.msg = msg;}
	
	
	//Methodes
	/**
	 * @return Vrai si et seulement si la tentative
	 * a réussie, faux sinon.
	 */
	public boolean hasSuccess(){return this.response;}
	
	
	/**
	 * @return Un message décrivant grossièrement le succès ou
	 * l'échec de la tentative.
	 */
	public String getMessage(){return this.msg;}
	
	
	/**
	 * Retourne une chaîne de caractères qui décrit $this.
	 */
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append(this.hasSuccess() 
				? ""
				: "Erreur : ");
		result.append(this.getMessage());
		return result.toString();
	}
}

package useful;

import java.util.ArrayList;
import java.util.List;

/**
 * Spécialise sa classe mère en mettant à disposition une collection, pour récupérer
 * le résultat d'une requête lorsqu'elle réussie.<br/><br/>
 * 
 * @author romain
 *
 * @param <T> : le type des données à retourner.
 * @see Response
 */
public class ResponseData <T>
extends Response
{
	//Attributes
	/** Contient des données à récupérer.*/
	protected List<T> data;
	
	
	//Constructeur
	/**
	 * Constructeur partiel.
	 * Retourne une réponse sans données.
	 * 
	 * @param response : vrai ssi la tentative a réussie, faux sinon. 
	 * @param msg message associé à la tentative, null interdit.
	 */
	public ResponseData(boolean response, String msg)
	{	
		super(response, msg);
		this.data = new ArrayList<T>();
	}
	
	/**
	 * Constructeur sans message.
	 * 
	 * @param response : vrai ssi la tentative a réussie, faux sinon.
	 */
	public ResponseData(boolean response)
	{
		super(response);
		this.data = new ArrayList<T>();
	}
	
	/**
	 * Constructeur complet 
	 * Retourne une réponse dans un état fini.
	 * 
	 * @param response :  vrai ssi la tentative a réussie, faux sinon.
	 * @param msg : message associé à la tentative, null interdit.
	 * @param data : liste des données à retourner, null interdit.
	 */
	public ResponseData(boolean response, String msg, List<T> data)
	{
		super(response, msg);
		this.data = data;
	}
	
	
	/**
	 * Constructeur sur levée d'exceptions.
	 * Retourne une réponse qui décrit une erreur.
	 * @param e : null interdit.
	 */
	public ResponseData(Exception e)
	{
		super(e);
		this.data = new ArrayList<T>();
	}
	
	
	/**
	 * Constructeur à partir d'une réponse personnalisée contenant
	 * une liste de tableau de données. Les éléments sont sorti du tableau
	 * et ajoutés à la suite dans la liste. Utile pour les tableaux d'une case.
	 * 
	 * @param rd : null interdit.
	 */
	public ResponseData(ResponseData<T []> rd)
	{
		this(rd.response, rd.msg);
		
		for (T[] t : rd.data) {
			for (T e : t) {
				this.data.add(e);
			}
		}
	}


	//Accesseurs
	/**
	 * @return L'ensemble des données de $this.
	 */
	public List<T> getCollection(){return this.data;}
	
	
	//Méthodes
	/**
	 * Ajoute $element à $this.
	 * 
	 * @param element : null autorisé.
	 */
	public void add(T element)
	{
		if (! this.data.contains(element)) {
			this.data.add(element);
		}
	}
	
	
	/**
	 * Ajoute tous les éléments de $elements à la reponse.
	 * 
	 * @param elements : null interdit.
	 */
	public void add(List<T> elements)
	{
		for (T e : elements) {
			this.add(e);
		}
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder(super.toString());
		if (data != null) result.append('\n' + data.toString());
		return result.toString();
	}
}

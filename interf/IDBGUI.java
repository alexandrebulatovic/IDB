package interf;

import useful.Response;

/**
 * Cette interface propose des méthodes qui reviennent régulièrement dans les IHM.
 * 
 * @author UGOLINI Romain
 */
public interface IDBGUI 
{	
	/**
	 * Retourne vrai si et seulement si tous les composants nécessaires
	 * de $this sont remplis, faux sinon.
	 * 
	 * @return boolean
	 */
	public boolean isComplete();
	
	
	/**
	 * Communique avec l'utilisateur en affichant $msg.
	 * 
	 * @param msg : un message à transmettre à l'utilisateur, null interdit.
	 */
	public void talk(String msg);
	
	
	/**
	 * Communique avec l'utilisateur en affichant $reponse.
	 * 
	 * @param response : une reponse suite à une tentative, null interdit.
	 */
	public void talk(Response response);
}

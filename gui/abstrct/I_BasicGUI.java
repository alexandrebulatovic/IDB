package gui.abstrct;

import useful.Response;

/**
 * Cette interface propose des méthodes qui reviennent régulièrement dans les IHM.
 * 
 * @author UGOLINI Romain
 */
public interface I_BasicGUI 
{	
	/**
	 * @return Vrai si et seulement si tous les composants nécessaires
	 * de $this sont remplis, faux sinon.
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
	 * La couleur du message est noire si et seulement si $response 
	 * est positive, rouge sinon.
	 * 
	 * @param response : une reponse suite à une tentative, null interdit.
	 */
	public void talk(Response response);
}

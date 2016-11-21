package launch;

import connect.*;


public class Launcher {

	/**
	 * Lance l'application
	 * @param args : les arguments de la ligne de commande.
	 */
	public static void main(String[] args) 
	{
		ConnectionController home = new ConnectionController ();
		System.out.println("FIN du programme.");
	}

}

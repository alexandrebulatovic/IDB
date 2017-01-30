
package main;

import home.HomeController;
import connection.ConnectionGUI;

/**
 * Initialise l'application.
 */
public class Launcher {

	/**
	 * @param args : aucun argument.
	 */
	public static void main(String[] args) 
	{
		launchApplication();
	}

	
	private static void launchApplication()
	{
		HomeController control = new HomeController();
		new ConnectionGUI(control);
	}
}


package main;

import manager.DefaultValueManager;
import manager.HomeFacade;
import factory.MainFactory;
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

	
	/**
	 * Initialise l'application.
	 */
	private static void launchApplication()
	{
		DefaultValueManager dvm = new DefaultValueManager();
		MainFactory factory = new MainFactory();
		HomeFacade facade = new HomeFacade(dvm, factory);
		HomeController control = new HomeController(facade);
		new ConnectionGUI(control);
	}
}

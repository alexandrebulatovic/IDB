
package main;

import manager.DefaultValueManager;
import manager.Facade;
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
		Facade facade = new Facade(dvm, factory);
		HomeController control = new HomeController(facade);
		new ConnectionGUI(control);
	}
}

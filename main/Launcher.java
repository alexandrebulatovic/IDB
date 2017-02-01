
package main;

import useful.ConnectionStrings;
import manager.DefaultValueManager;
import manager.HomeFacade;
import manager.I_DDLManager;
import factory.MainFactory;
import home.HomeController;
import connection.ConnectionGUI;
import ddl.DDLController;

/**
 * Initialise l'application.
 */
public class Launcher {

	/**
	 * @param args : aucun argument.
	 */
	public static void main(String[] args) 
	{
		HomeController control = initApplication();
		launchApplication(control);
	}

	
	/**
	 * Lance l'application.
	 */
	private static void launchApplication(HomeController control)
	{
//		ConnectionStrings parameters = control.getDefaultValues();
//		parameters.password = "";
//		control.connect(parameters);
		
//		I_DDLManager ddlmanager = facade.getDDLManager();
//		System.out.println("lol");
		new ConnectionGUI(control);
	}
	
	/**
	 * Initialise le backend de l'application.
	 */
	private static HomeController initApplication()
	{
		DefaultValueManager dvm = new DefaultValueManager();
		MainFactory factory = new MainFactory();
		HomeFacade facade = new HomeFacade(dvm, factory);
		return new HomeController(facade);
	}
}

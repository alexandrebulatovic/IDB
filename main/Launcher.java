
package main;

import useful.ConnectionStrings;
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
		launchApplication();
//		testKey();
	}

	
	private static void launchApplication()
	{
		HomeController control = new HomeController();
		new ConnectionGUI(control);
	}
	
	
//	private static void testKey()
//	{
//		HomeController control = new HomeController();
//		ConnectionStrings cs = new ConnectionStrings("Oracle",
//				"162.38.222.142", "ugolinir", "2302017000S", "IUT", "1521");
//		control.connect(cs);
//		DDLController ddl = new DDLController(control.connector.getConnection());
//		ddl.getPrimaryKey("un");
//		control.disconnect();
//	}
}

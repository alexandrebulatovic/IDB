
package main;

import useful.ConnectionStrings;
import useful.ResponseData;
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

	private DefaultValueManager dvm;
	private MainFactory factory;
	private HomeController hc;
	private HomeFacade facade;
	
	
	protected Launcher()
	{
		this.initApplication();
	}
	
	
	/**
	 * @param args : aucun argument.
	 */
	public static void main(String[] args) 
	{
		Launcher main = new Launcher();
		main.launchApplication();
	}

	
	/**
	 * Lance l' IHM de l'application.
	 */
	private void launchApplication()
	{
		new ConnectionGUI(this.hc);
	}
	
	
	/**
	 * Initialise le backend de l'application.
	 */
	private void initApplication()
	{
		this.dvm = new DefaultValueManager();
		this.factory = new MainFactory();
		this.facade = new HomeFacade(dvm, factory);
		this.hc = new HomeController(facade);
	}
	
	
	private void testKey()
	{
		ConnectionStrings cs = this.facade.getDefaultValues();
//		cs.password = "";
		this.hc.connect(cs);
		I_DDLManager ddlManager = this.facade.getDDLManager();
		ResponseData<String []> imported = ddlManager.getImportedKey("TROIS");
		
		for (String [] ligne: imported.getCollection()) {
			for (String colonne : ligne) {
				System.out.print(colonne + ",\t");
			}
			System.out.println();
		}
		
		System.out.println();
		ResponseData<String []> exported = ddlManager.getExportedKey("TROIS");
		for (String [] ligne: exported.getCollection()) {
			for (String colonne : ligne) {
				System.out.print(colonne + ",\t");
			}
			System.out.println();
		}
		this.facade.disconnect();
		int a = 5;
	}
}

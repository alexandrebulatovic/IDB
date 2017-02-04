
package main;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

import useful.ConnectionStrings;
import useful.ResponseData;
import manager.DefaultValueManager;
import manager.HomeFacade;
import manager.ddl.I_DDLManager;
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
//		main.connect();
//		try {
//			Statement st = main.facade.getConnection().createStatement();
//			String sql = "select search_condition from all_constraints " +
//					"where owner = 'UGOLINIR'" +
//					" and table_name = 'TEST'";
//			ResultSet rs = st.executeQuery(sql);
//			while (rs.next()) {
//				System.out.println(rs.getString(1));
//			}
//		}
//		catch(Exception e) {
//
//		}
	}

	
	private void testNotNull(Launcher main)
	{
		main.connect();
		I_DDLManager ddlmanager = main.factory.getDDLManager(main.facade.getConnection());
		ResponseData<String []> r = ddlmanager.getAttributes("eleves");
		System.out.println(r.getCollection());
		for (String [] st : r.getCollection()) {
			for (String s : st) {
				System.out.print(s +", ");
			}
			System.out.println();
		}
		main.hc.disconnect();
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
	
	/**
	 * Se connecte au dernier SGBD atteint (fichier XML).
	 */
	private void connect()
	{
		ConnectionStrings cs = this.facade.getDefaultValues();
		cs.password = "";
		this.hc.connect(cs);
	}
	
	
	private void testKey()
	{
		ConnectionStrings cs = this.facade.getDefaultValues();
//		cs.password = "";
		this.hc.connect(cs);
		I_DDLManager ddlManager = this.facade.getDDLManager();
		ResponseData<String []> imported = ddlManager.getPrimaryFromForeign("TROIS");
		
		for (String [] ligne: imported.getCollection()) {
			for (String colonne : ligne) {
				System.out.print(colonne + ",\t");
			}
			System.out.println();
		}
		
		System.out.println();
		ResponseData<String []> exported = ddlManager.getForeignFromPrimary("TROIS");
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

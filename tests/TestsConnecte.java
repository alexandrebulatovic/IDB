package tests;

import static org.junit.Assert.*;

import java.sql.Connection;

import manager.connection.MySQLConnectionManager;
import manager.connection.OracleConnectionManager;
import manager.ddl.MySQLDDLManager;
import manager.ddl.OracleDDLManager;
import manager.xml.DefaultValueManager;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import useful.ConnectionStrings;

import controller.DDLController;
import controller.HomeController;
import ddl.MySQLAttribute;
import ddl.OracleAttribute;

import facade.DDLFacade;
import facade.HomeFacade;
import factory.MainFactory;

/**
 * Réaliser ces tests en ayant sur sa base le script 
 * disponible sur Google Drive, et uniquement cela.
 * 
 * Se faire une classe Perso, contenant deux variable static final String
 * LOGIN et MDP dans le package test, et les ajouter au .gitignore.
 */
public class TestsConnecte 
{
	private static DefaultValueManager dvm;
	private static MainFactory factory;
	private static HomeController homeControl;
	private static Connection connection;
	private static HomeFacade homeFacade;
	private static DDLFacade ddlFacade;
	private static DDLController ddlControl;
	private static String dbms;
	
	
	@BeforeClass
	public static void before()
	{
		dvm = new DefaultValueManager();
		factory = new MainFactory(MainFactory.NULL);
		homeFacade = new HomeFacade(dvm, factory);
		homeControl = new HomeController(homeFacade);
		ConnectionStrings parameters = homeControl.getDefaultValues();
		parameters.password = Perso.MDP;
		dbms = parameters.driver;
		homeControl.connect(parameters);
		connection = homeFacade.getConnection();
		ddlFacade = homeFacade.getDDLFacade();
		ddlControl = new DDLController(ddlFacade);
	}
	
	
	@AfterClass
	public static void after()
	{
		homeControl.disconnect();
	}
	
	
	@Test
	public void checkDefaultValueManager()
	{
		dvm.setDriver("Oracle");
		assertEquals("Oracle",dvm.getDriver());
		assertEquals("162.38.222.149",dvm.getUrl());
		assertEquals("IUT", dvm.getDataBase());
		assertEquals("1521", dvm.getPort());
		assertEquals(Perso.LOGIN, dvm.getUser());
		
		dvm.setDriver("MySQL");
		assertEquals("MySQL",dvm.getDriver());
		assertEquals("162.38.222.142",dvm.getUrl());
		assertEquals(Perso.LOGIN, dvm.getDataBase());
		assertEquals("3306", dvm.getPort());
		assertEquals(Perso.LOGIN, dvm.getUser());
	}
	
	
	@Test
	public void checkMainFactory()
	{
		String name = "test", type = "CHAR";
		int size = 1;
		boolean nn = false, pk = false;
		
		assertEquals(MainFactory.ORACLE, factory.getAvailableDBMS()[0]);
		assertEquals(MainFactory.MYSQL, factory.getAvailableDBMS()[1]);
		
		factory.setDBMS(MainFactory.NULL);
		assertEquals("Fabrique inactive.", factory.toString());
		assertEquals(null, factory.getConnectionManager());
		assertEquals(null, factory.getDDLManager(connection));
		assertEquals(null, factory.getAttributeModel
				(name, type, size, nn, pk));
		
		factory.setDBMS(MainFactory.ORACLE);
		assertEquals("Fabrique pour Oracle.", factory.toString());
		assertEquals(true, factory.getConnectionManager() instanceof OracleConnectionManager);
		assertEquals(true, factory.getDDLManager(connection) instanceof OracleDDLManager);
		assertEquals(true, factory.getAttributeModel
				(name, type, size, nn, pk) instanceof OracleAttribute);
		
		factory.setDBMS(MainFactory.MYSQL);
		assertEquals("Fabrique pour MySQL.", factory.toString());
		assertEquals(true, factory.getConnectionManager() instanceof MySQLConnectionManager);
		assertEquals(true, factory.getDDLManager(connection) instanceof MySQLDDLManager);
		assertEquals(true, factory.getAttributeModel
				(name, type, size, nn, pk) instanceof MySQLAttribute);
	}
}

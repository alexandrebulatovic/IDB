package tests;

import static org.junit.Assert.*;

import manager.xml.DefaultValueManager;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import useful.ConnectionStrings;

import controller.DDLController;
import controller.HomeController;

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
public class TestsRomain 
{
	private static DefaultValueManager dvm;
	private static MainFactory factory;
	private static HomeController homeControl;
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
		factory.setDBMS(MainFactory.NULL);
		assertEquals(null, factory.getConnectionManager());
		
	}
}

package tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import manager.connection.MockConnectionManager;
import manager.connection.MySQLConnectionManager;
import manager.connection.OracleConnectionManager;
import manager.ddl.I_DDLManager;
import manager.ddl.MockDDLManager;
import manager.ddl.MySQLDDLManager;
import manager.ddl.OracleDDLManager;
import manager.xml.DefaultValueManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import business.TableSet;
import useful.ConnectionStrings;
import useful.Response;
import useful.ResponseData;
import controller.DDLController;
import controller.HomeController;
import ddl.MockAttributeModel;
import ddl.MockTableModel;
import ddl.MySQLAttributeModel;
import ddl.MySQLTableModel;
import ddl.OracleAttributeModel;
import ddl.OracleTableModel;
import facade.DDLFacade;
import facade.HomeFacade;
import factory.MainFactory;

/**
 * Réaliser ces tests en ayant sur sa base le script 
 * disponible sur Google Drive, et uniquement cela.
 * 
 * Se faire une classe Perso, contenant deux variable static final String
 * LOGIN et MDP dans le package test, et les ajouter au .gitignore.
 * 
 * 
 */
public class TestsConnecte 
{
	private static DefaultValueManager dvm;
	private static MainFactory factory;
	private static HomeController homeControl;
	private static Connection connection;
	private static HomeFacade homeFacade;
	private static DDLFacade ddlFacade;
	private static TableSet business;
	private static DDLController ddlControl;
	private static String dbms;
	
	
	@BeforeClass
	public static void before()
	{
		dvm = new DefaultValueManager();
		factory = new MainFactory(MainFactory.MOCK);
		business = new TableSet();
		homeFacade = new HomeFacade(dvm, factory, business);
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
		
		factory.setDBMS(MainFactory.MOCK);
		assertEquals("Fabrique inactive.", factory.toString());
		assertEquals(true, factory.getConnectionManager() instanceof MockConnectionManager);
		assertEquals(true, factory.getDDLManager(connection) instanceof MockDDLManager);
		assertEquals(true, factory.getAttributeModel
				(name, type, size, nn, pk) instanceof MockAttributeModel);
		assertEquals(true, factory.getTableModel() instanceof MockTableModel);
		
		factory.setDBMS(MainFactory.ORACLE);
		assertEquals("Fabrique pour Oracle.", factory.toString());
		assertEquals(true, factory.getConnectionManager() instanceof OracleConnectionManager);
		assertEquals(true, factory.getDDLManager(connection) instanceof OracleDDLManager);
		assertEquals(true, factory.getAttributeModel
				(name, type, size, nn, pk) instanceof OracleAttributeModel);
		assertEquals(true, factory.getTableModel() instanceof OracleTableModel);
		
		factory.setDBMS(MainFactory.MYSQL);
		assertEquals("Fabrique pour MySQL.", factory.toString());
		assertEquals(true, factory.getConnectionManager() instanceof MySQLConnectionManager);
		assertEquals(true, factory.getDDLManager(connection) instanceof MySQLDDLManager);
		assertEquals(true, factory.getAttributeModel
				(name, type, size, nn, pk) instanceof MySQLAttributeModel);
		assertEquals(true, factory.getTableModel() instanceof MySQLTableModel);

	}
	
	
	@Test
	public void checkConnectionStrings()
	{
		ConnectionStrings cs1 = new ConnectionStrings();
		assertEquals("", cs1.driver);
		assertEquals("", cs1.url);
		assertEquals("", cs1.user);
		assertEquals("", cs1.password);
		assertEquals("", cs1.baseName);
		assertEquals("", cs1.port);
		
		ConnectionStrings cs2 = new ConnectionStrings("driver", "url", "user", "password", "base", "0000");
		assertEquals("driver", cs2.driver);
		assertEquals("url", cs2.url);
		assertEquals("user", cs2.user);
		assertEquals("password", cs2.password);
		assertEquals("base", cs2.baseName);
		assertEquals("0000", cs2.port);
	}
	
	
	@Test
	public void checkResponse()
	{
		Response r = new Response(true);
		assertEquals(true, r.hasSuccess());
		assertEquals("", r.getMessage());
		assertEquals("", r.toString());
		r.setMessage("hello");
		assertEquals("hello", r.getMessage());
		assertEquals("hello", r.toString());
		
		r = new Response(false);
		assertEquals(false, r.hasSuccess());
		assertEquals("", r.getMessage());
		assertEquals("Erreur : ", r.toString());
		r.setMessage("hello");
		assertEquals("hello", r.getMessage());
		assertEquals("Erreur : hello", r.toString());
		
		r = new Response(true, "Message reçu.");
		assertEquals(true, r.hasSuccess());
		assertEquals("Message reçu.", r.toString());
		assertEquals("Message reçu.", r.getMessage());
		r.setMessage("hello");
		assertEquals("hello", r.getMessage());
		assertEquals("hello", r.toString());
		
		r = new Response(false, "Message non reçu.");
		assertEquals(false, r.hasSuccess());
		assertEquals("Erreur : Message non reçu.", r.toString());
		assertEquals("Message non reçu.", r.getMessage());
		r.setMessage("hello");
		assertEquals("hello", r.getMessage());
		assertEquals("Erreur : hello", r.toString());

		
		Response copy = new Response(r);
		assertEquals(false, copy.hasSuccess());
		assertEquals("Erreur : hello", r.toString());
		assertEquals("hello", copy.getMessage());
		copy.setMessage("");
		assertEquals("", copy.getMessage());
		assertEquals("Erreur : ", copy.toString());
	}
	
	
	@Test
	public void checkResponseData()
	{
		ResponseData<String> r = new ResponseData<String>(true, "Message récupéré.");
		assertEquals(0, r.getCollection().size());
		assertEquals("Message récupéré.\n[]", r.toString());
		
		r.add("Bonjour");
		assertEquals(1, r.getCollection().size());
		assertEquals("Message récupéré.\n[Bonjour]", r.toString());
		
		r.add("Bonjour");
		assertEquals(1, r.getCollection().size());
		assertEquals("Message récupéré.\n[Bonjour]", r.toString());
		
		List<String> lst = new ArrayList<String>();
		lst.add("Hello"); lst.add("Bonjour"); lst.add("Au revoir");
		r.add(lst);
		assertEquals(3, r.getCollection().size());
		assertEquals("Message récupéré.\n[Bonjour, Hello, Au revoir]", r.toString());
		
		assertEquals("Bonjour", r.getCollection().get(0));
		assertEquals("Hello", r.getCollection().get(1));
		assertEquals("Au revoir", r.getCollection().get(2));
		
		ResponseData<String[]> rr = new ResponseData<String[]>(false, "Compte à rebours non récupéré.");
		String [] ss;
		List<String []> lstc = new ArrayList<String[]>();
		ss = new String [3];
		ss[0] = "1";
		ss[1] = "2";
		ss[2] = "3";
		lstc.add(ss);
		
		ss = new String [2];
		ss[0] = "4";
		ss[1] = "5";
		lstc.add(ss);
		
		ss = new String [4];
		ss[0] = "6";
		ss[1] = "7";
		ss[2] = "8";
		ss[3] = "9";
		lstc.add(ss);
		rr.add(lstc);
		
		r = new ResponseData<String>(rr);
		assertEquals(9, r.getCollection().size());
		assertEquals(rr.hasSuccess(), r.hasSuccess());
		assertEquals(rr.getMessage(), r.getMessage());
		assertEquals("Erreur : Compte à rebours non récupéré.\n[1, 2, 3, 4, 5, 6, 7, 8, 9]", r.toString());
	
		Exception e = new Exception("Ceci est une erreur.");
		r = new ResponseData<String>(e);
		assertEquals(false, r.hasSuccess());
		assertEquals(e.getMessage(), r.getMessage());
		assertEquals("Erreur : " + e.getMessage() + "\n[]", r.toString());
	}
	
	
	@Test
	public void checkDDLManager()
	{
		factory.setDBMS(dbms);
		I_DDLManager ddl = factory.getDDLManager(connection);
		String [] dataTypes = ddl.getDataTypes();
		String charr = dataTypes[3], number = dataTypes[1], varchar = dataTypes[0], date = dataTypes[2];
		String table1 = "existepas", table2 = "referenceexistepas", table3 = "referenceexistepasbis";

		String createOne = "CREATE TABLE "+ table1 + "\n(\n" +
						"idexistepas " + charr + "(1),\n" +
						"txtexistepas " + varchar + "(30),\n" +
						"nombreexistepas " + number +" NOT NULL,\n" +
						"dateexistepas " + date + ",\n" +
						"symbolexistepas " + charr + "(1),\n" +
						"CONSTRAINT pk_existepas PRIMARY KEY (idexistepas),\n" +
						"CONSTRAINT un_existepas_date_symbole UNIQUE (dateexistepas, symbolexistepas)\n)";
		
		
		String createTwo = "CREATE TABLE " + table2 + "\n(\n" + 
						"idreference " + number + ",\n" +
						"extreference " + charr + "(1),\n" +
						"CONSTRAINT pk_referenceexistepas PRIMARY KEY(idreference),\n" +
						"CONSTRAINT fk_referenceexistepas_extref FOREIGN KEY (extreference)\n" +
						"\t REFERENCES existepas(idexistepas)\n)";
		
		String createThree = "CREATE TABLE " + table3 + "\n(\n" + 
						"idreference " + number + ",\n" +
						"extreference " + charr + "(1),\n" +
						"CONSTRAINT pk_referenceexistepasbis PRIMARY KEY(idreference),\n" +
						"CONSTRAINT fk_referenceexistepasbis_extrf FOREIGN KEY (extreference)\n" +
						"\t REFERENCES existepas(idexistepas)\n)";
		
		ddl.dropTable(table1, true);
		ddl.dropTable(table2, true);
		ddl.dropTable(table3, true);
		
		//Créer sans doublons
		assertEquals(true, ddl.createTable(createOne).hasSuccess());
		assertEquals(false, ddl.createTable(createOne).hasSuccess());
		
		assertEquals(true, ddl.createTable(createTwo).hasSuccess());
		assertEquals(false, ddl.createTable(createTwo).hasSuccess());
		
		assertEquals(true, ddl.createTable(createThree).hasSuccess());
		assertEquals(false, ddl.createTable(createThree).hasSuccess());
		
		//Supprimer sans cascade
		assertEquals(false, ddl.dropTable(table1, false).hasSuccess());
		assertEquals(true, ddl.dropTable(table2, false).hasSuccess());
		assertEquals(true, ddl.dropTable(table3, false).hasSuccess());
		assertEquals(true, ddl.createTable(createTwo).hasSuccess());
		assertEquals(true, ddl.createTable(createThree).hasSuccess());
		
		//Supprimer avec cascade
		assertEquals(true, ddl.dropTable(table1, true).hasSuccess());
		assertEquals(false, ddl.dropTable(table1, true).hasSuccess());
		assertEquals(true, ddl.dropTable(table2, true).hasSuccess());
		assertEquals(false, ddl.dropTable(table2, true).hasSuccess());
		assertEquals(true, ddl.dropTable(table3, true).hasSuccess());
		assertEquals(false, ddl.dropTable(table3, true).hasSuccess());
		
		//Recréer les deux tables
		assertEquals(false, ddl.createTable(createTwo).hasSuccess());
		assertEquals(false, ddl.createTable(createThree).hasSuccess());
		this.createTables(ddl, createOne, createTwo, createThree);
		
		//Supprimer les tables avec effet domino
		ResponseData<String> r = ddl.dropTableDomino(table1);
		assertEquals(true, r.hasSuccess());
		assertEquals(3, r.getCollection().size());
		this.createTables(ddl, createOne, createTwo, createThree);
		
		r = ddl.dropTableDomino(table2);
		assertEquals(true, r.hasSuccess());
		assertEquals(1, r.getCollection().size());
		r = ddl.dropTableDomino(table3);
		assertEquals(true, r.hasSuccess());
		assertEquals(1, r.getCollection().size());
		r = ddl.dropTableDomino(table1);
		assertEquals(true, r.hasSuccess());
		assertEquals(1, r.getCollection().size());
		
//		assertEquals(false, ddl.dropTable(table1, false).hasSuccess());
		assertEquals(false, ddl.dropTableDomino(table1).hasSuccess());
		assertEquals(false, ddl.dropTableDomino(table2).hasSuccess());
		assertEquals(false, ddl.dropTableDomino(table3).hasSuccess());
		this.createTables(ddl, createOne, createTwo, createThree);
	}
	
	
	private void createTables(I_DDLManager ddl, String createOne, String createTwo, String createThree)
	{
		assertEquals(true, ddl.createTable(createOne).hasSuccess());
		assertEquals(true, ddl.createTable(createTwo).hasSuccess());
		assertEquals(true, ddl.createTable(createThree).hasSuccess());
	}
}

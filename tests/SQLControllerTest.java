package tests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import javax.swing.JTable;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.Matchers;
import org.mockito.Mockito;

import business.TableSet;
import controller.SQLController;
import facade.SQLFacade;
import manager.connection.I_ConnectionManager;
import manager.connection.MockConnectionManager;
import manager.ddl.I_DDLManager;
import manager.ddl.MockDDLManager;
import manager.sql.SQLManager;

public class SQLControllerTest {

	private static I_DDLManager ddlManager;
	private static I_ConnectionManager connectionManager;
	private static TableSet tableSet;
	private static SQLManager sqlManager;
	private static SQLFacade sqlFacade;
	private static SQLController sqlController;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		ddlManager 			= new MockDDLManager();
		connectionManager 	= new MockConnectionManager();
		tableSet 			= new TableSet();
		sqlManager 			= Mockito.mock(SQLManager.class);

		Mockito.when(sqlManager.sendQuery(Mockito.contains("SELECT"))).thenReturn(true);
		Mockito.when(sqlManager.sendQuery(AdditionalMatchers.not(Matchers.contains("SELECT")))).thenReturn(false);
		Mockito.when(sqlManager.sendQuery("")).thenThrow(IllegalArgumentException.class);
		Mockito.when(sqlManager.getGeneratedJTable()).thenReturn(new JTable());
		Mockito.when(sqlManager.getGeneratedReply()).thenReturn(Mockito.anyString());

		sqlFacade 			= new SQLFacade(ddlManager, connectionManager, tableSet, sqlManager);
		sqlController 		= new SQLController(sqlFacade);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEnvoiRequeteVideSQLManager() throws IllegalArgumentException, NullPointerException, SQLException {
		sqlManager.sendQuery("");
	}
	
	@Test
	public void testEnvoiRequeteDepuisSQLManager() throws IllegalArgumentException, NullPointerException, SQLException {
		assertTrue(sqlManager.sendQuery("SELECT * FROM table"));
	}
	
	@Test
	public void testReceptionJTable()  {
		assertTrue(sqlManager.getGeneratedJTable() instanceof JTable);
	}
	
	@Test
	public void testReceptionString()  {
		assertTrue(sqlManager.getGeneratedReply() instanceof String);
	}

	@Test
	public void testEnvoiRequeteSelectDepuisSQLController() throws IllegalArgumentException, NullPointerException, SQLException {
		sqlController.sendQuery("SELECT * FROM table");
		Mockito.verify(sqlManager, Mockito.times(1)).sendQuery("SELECT * FROM table");
	}
	
	@Test
	public void testEnvoiRequeteDeleteDepuisSQLController() throws IllegalArgumentException, NullPointerException, SQLException {
		sqlController.sendQuery("DELETE FROM table");
		Mockito.verify(sqlManager, Mockito.times(1)).sendQuery("DELETE FROM table");
	}
}

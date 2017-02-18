package tests;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JTable;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import business.TableSet;
import controller.CRUDController;
import facade.CRUDFacade;
import manager.connection.I_ConnectionManager;
import manager.connection.MockConnectionManager;
import manager.ddl.I_DDLManager;
import manager.ddl.MockDDLManager;
import manager.sql.SQLManager;
import useful.ResponseData;

public class CRUDControllerTest {
	
	
	private static I_DDLManager ddlManager;
	private static I_ConnectionManager connectionManager;
	private static TableSet tableSet;
	private static SQLManager sqlManager;
	private static CRUDFacade crudFacade;
	private static CRUDController crudController;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		ddlManager 			= new MockDDLManager();
		connectionManager 	= new MockConnectionManager();
		tableSet 			= new TableSet();
		
		sqlManager 			= Mockito.mock(SQLManager.class);
		Mockito.when(sqlManager.getJTableFromTableName(Mockito.anyString())).thenReturn(new JTable());
		
		crudFacade 			= Mockito.spy(new CRUDFacade(ddlManager, connectionManager, tableSet, sqlManager));
		Mockito.doNothing().when(crudFacade).setStatementType(Mockito.anyInt());
		Mockito.when(crudFacade.getTables()).thenReturn(new ResponseData<String>(true));
		
		crudController 		= new CRUDController(crudFacade);

	}

	@Test
	public void testRecuperationJTableAPartirNomTable() {
		assertTrue(crudController.getJTableFromTableName("table") instanceof ResponseData<?>);
	}
	
	@Test
	public void testAppelAddTupleCRUDFacade() throws UnsupportedOperationException, IllegalArgumentException, SQLException {
		Vector vector = new Vector<>();
		vector.addElement("5");
		
		crudFacade.addTuple(vector);
		Mockito.verify(sqlManager, Mockito.times(1)).addTuple(vector);
	}
	
	@Test
	public void testAppelDeleteTupleCRUDFacade() throws UnsupportedOperationException, IllegalArgumentException, SQLException {
		crudFacade.deleteTuple(5);
		Mockito.verify(sqlManager, Mockito.times(1)).deleteTuple(5);
	}
	
	@Test
	public void testAppelUpdateTupleCRUDFacade() throws UnsupportedOperationException, IllegalArgumentException, SQLException {
		crudFacade.updateTuple(5, 2, "test");
		Mockito.verify(sqlManager, Mockito.times(1)).updateTuple(5, 2, "test");
	}
	
	@Test
	public void testAppelGetTablesDepuisCRUDController() {
		crudController.getTables();
		Mockito.verify(crudFacade, Mockito.times(1)).getTables();
	}


}

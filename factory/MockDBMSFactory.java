package factory;

import java.sql.Connection;

import ddl.I_AttributeModel;
import ddl.I_TableModel;
import ddl.MockTableModel;

import manager.connection.I_ConnectionManager;
import manager.connection.MockConnectionManager;
import manager.ddl.I_DDLManager;
import manager.ddl.MockDDLManager;
import ddl.MockAttributeModel;

/**
 * Fabrique à utilisé si aucun SGBD n'est encore sélectionné.
 */
public class MockDBMSFactory 
implements I_DBMSFactory 
{

	@Override
	public I_ConnectionManager getConnectionManager() 
	{
		return new MockConnectionManager();
	}

	@Override
	public I_DDLManager getDDLManager(Connection connection) 
	{
		return new MockDDLManager();
	}

	
	@Override
	public I_AttributeModel getAttributeModel
	(String name, String type, int parseInt, boolean notNull, boolean primaryKey) 
	{
		return new MockAttributeModel
				(name, type, parseInt, notNull, primaryKey);
	}
	
	
	@Override
	public I_TableModel getTableModel()
	{
		return new MockTableModel();
	}
	
	
	@Override
	public String toString()
	{
		return "Fabrique inactive.";
	}
}

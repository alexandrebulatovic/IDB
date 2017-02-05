package factory;

import java.sql.Connection;

import ddl.I_Attribute;

import manager.connection.I_ConnectionManager;
import manager.connection.MockConnectionManager;
import manager.ddl.I_DDLManager;
import manager.ddl.MockDDLManager;
import ddl.MockAttribute;

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
	public I_Attribute getAttributeModel
	(String name, String type, int parseInt, boolean notNull, boolean primaryKey) 
	{
		return new MockAttribute
				(name, type, parseInt, notNull, primaryKey);
	}
	
	
	@Override
	public String toString()
	{
		return "Fabrique inactive.";
	}
}

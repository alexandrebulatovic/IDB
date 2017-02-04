package factory;

import java.sql.Connection;

import manager.connection.I_ConnectionManager;
import manager.ddl.I_DDLManager;

public class VoidDBMSFactory 
implements I_DBMSFactory {

	@Override
	public I_ConnectionManager getConnectionManager() 
	{
		return null;
	}

	@Override
	public I_DDLManager getDDLManager(Connection connection) 
	{
		return null;
	}

	
	@Override
	public String toString()
	{
		return "Fabrique inactive.";
	}
}

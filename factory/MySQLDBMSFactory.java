package factory;

import java.sql.Connection;

import manager.I_ConnectionManager;
import manager.I_DDLManager;
import manager.connection.MySQLConnectionManager;
import manager.ddl.MySQLDDLManager;

public class MySQLDBMSFactory 
implements I_DBMSFactory {

	@Override
	public I_ConnectionManager getConnectionManager() 
	{
		return new MySQLConnectionManager();
	}

	@Override
	public I_DDLManager getDDLManager(Connection connection) 
	{
		return new MySQLDDLManager(connection);
	}

	
	@Override
	public String toString()
	{
		return "Fabrique pour MySQL.";
	}
}

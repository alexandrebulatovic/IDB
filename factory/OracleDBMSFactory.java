package factory;

import java.sql.Connection;

import manager.I_ConnectionManager;
import manager.I_DDLManager;
import manager.connection.OracleConnectionManager;
import manager.ddl.OracleDDLManager;

/**
 * Fabrique à utiliser si le SGBD choisit est Oracle.
 */
public class OracleDBMSFactory 
implements I_DBMSFactory 
{
	//Méthodes
	@Override
	public I_ConnectionManager getConnectionManager() 
	{
		return new OracleConnectionManager();
	}

	
	@Override
	public I_DDLManager getDDLManager(Connection connection) 
	{
		return new OracleDDLManager(connection);
	}
	
	
	@Override
	public String toString()
	{
		return "Fabrique pour Oracle.";
	}
}
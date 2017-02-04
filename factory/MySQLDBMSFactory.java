package factory;

import java.sql.Connection;

import ddl.I_Attribute;

import manager.connection.I_ConnectionManager;
import manager.connection.MySQLConnectionManager;
import manager.ddl.I_DDLManager;
import manager.ddl.MySQLDDLManager;

/**
 * Fabrique à utiliser si le SGBD choisit est Oracle.
 */
public class MySQLDBMSFactory 
implements I_DBMSFactory 
{

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
	public I_Attribute getAttributeModel() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public String toString()
	{
		return "Fabrique pour MySQL.";
	}
}

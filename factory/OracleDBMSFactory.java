package factory;

import java.sql.Connection;

import ddl.I_Attribute;
import ddl.MySQLAttribute;
import ddl.OracleAttribute;
import manager.connection.I_ConnectionManager;

import manager.connection.OracleConnectionManager;
import manager.ddl.I_DDLManager;
import manager.ddl.OracleDDLManager;


/**
 * Fabrique à utiliser si le SGBD choisit est Oracle.
 */
public class OracleDBMSFactory 
implements I_DBMSFactory 
{

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
	public I_Attribute getAttributeModel(String name, String type, int parseInt, boolean notNull, boolean primaryKey) 
	{
		return new OracleAttribute(name,type,parseInt, notNull,primaryKey);
	}
	
	
	@Override
	public String toString()
	{
		return "Fabrique pour Oracle.";
	}
}

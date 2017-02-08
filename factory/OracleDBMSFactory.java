package factory;

import java.sql.Connection;

import ddl.I_AttributeModel;
import ddl.I_TableModel;
import ddl.OracleAttributeModel;
import ddl.OracleTableModel;
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
	public I_AttributeModel getAttributeModel(String name, String type, int parseInt, boolean notNull, boolean primaryKey) 
	{
		return new OracleAttributeModel(name,type,parseInt, notNull,primaryKey);
	}
	
	
	@Override
	public I_TableModel getTableModel()
	{
		return new OracleTableModel();
	}
	
	
	@Override
	public String toString()
	{
		return "Fabrique pour Oracle.";
	}
}

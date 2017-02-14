package factory;

import gui.ddl.tools.I_AttributeModel;
import gui.ddl.tools.I_TableModel;
import gui.ddl.tools.MySQLAttributeModel;
import gui.ddl.tools.MySQLTableModel;

import java.sql.Connection;

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
	public I_AttributeModel getAttributeModel(String name, String type, int parseInt, boolean notNull, boolean primaryKey) 
	{
		return new MySQLAttributeModel(name,type,parseInt, notNull,primaryKey);
	}
	
	
	@Override
	public I_TableModel getTableModel()
	{
		return new MySQLTableModel();
	}
	
	
	@Override
	public String toString()
	{
		return "Fabrique pour MySQL.";
	}
}

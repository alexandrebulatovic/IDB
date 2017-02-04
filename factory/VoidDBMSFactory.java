package factory;

import java.sql.Connection;

import ddl.I_Attribute;
import manager.I_ConnectionManager;
import manager.I_DDLManager;

/**
 * Fabrique à utilisé si aucun SGBD n'est encore sélectionné.
 */
public class VoidDBMSFactory 
implements I_DBMSFactory 
{

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
	public I_Attribute getAttributeModel() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public String toString()
	{
		return "Fabrique inactive.";
	}
}

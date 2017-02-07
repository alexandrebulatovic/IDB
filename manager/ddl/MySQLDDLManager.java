package manager.ddl;

import java.sql.Connection;
import java.util.ArrayList;

import useful.Response;

public class MySQLDDLManager 
extends AbstractDLLManager 
{
	//Constructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param connection : une connexion active, null interdit.
	 */
	public MySQLDDLManager(Connection connection) 
	{
		super(connection);
	}

	
	//Méthodes
	@Override
	public String[] getAttributeTypes() 
	{
		String [] result = {"VARCHAR", "NUMERIC", "DATE", "CHAR"};
		return result;
	}

	
	@Override
	public boolean allowsDropCascade() {return false;}
	
	
	@Override
	public Response createTable(String sql) 
	{
		return this.executeUpdate(sql+"\nENGINE=InnoDB", CREATE_TABLE);
	}

	
	@Override
	public ArrayList<Response> modifyTable(ArrayList<String> sqls) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public Response dropTable(String table, boolean cascade) 
	{
		String sql = "DROP TABLE " + table + (cascade ? " CASCADE" : "") ;
		return this.executeUpdate(sql, DROP_TABLE);
	}
}

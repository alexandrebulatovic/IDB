package manager.ddl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import useful.Response;
import business.Attribute;

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
		String [] result = {"char", "varchar", "date", "numeric"};
		return result;
	}

	
	@Override
	public Response createTable(String sql) 
	{
		return this.executeUpdate(sql+"ENGINE=InnoDB", "Table créée");
	}

	
	@Override
	public ArrayList<Response> modifyTable(ArrayList<String> sqls) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Response dropTable(String table, boolean cascade) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public List<Attribute> getAttributes(String table) {
		// TODO Auto-generated method stub
		return null;
	}
}

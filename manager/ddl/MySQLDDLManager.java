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
	public boolean allowsDropCascade() {return false;}
	
	
	@Override
	public Response createTable(String sql) 
	{
		return this.executeUpdate(sql+"\nENGINE=InnoDB", "Table créée");
	}

	
	@Override
	public ArrayList<Response> modifyTable(ArrayList<String> sqls) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Response dropTable(String table, boolean cascade) 
	{
		String sql = "DROP TABLE " + table + (cascade ? " CASCADE" : "");
		return this.executeUpdate(sql, "Table supprimée.");
	}

	
	@Override
	public List<Attribute> getAttributes(String table) {
		// TODO Auto-generated method stub
		return null;
	}
}

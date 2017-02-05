package manager.ddl;

import java.util.ArrayList;

import useful.Response;
import useful.ResponseData;

public class MockDDLManager 
implements I_DDLManager 
{

	@Override
	public String[] getAttributeTypes() 
	{	
		String [] result = {};
		return result;
	}

	
	@Override
	public boolean allowsDropCascade() {return false;}

	
	@Override
	public Response createTable(String sql) 
	{
		return new Response(true, "Table créée.");
	}

	@Override
	public ArrayList<Response> modifyTable(ArrayList<String> sqls) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response dropTable(String table, boolean cascade, boolean chain) 
	{
		return new Response(true, "Table supprimée.");
	}

	@Override
	public ResponseData<String> getTables() 
	{
		return new ResponseData<String>(true, "Tables récupérées.");
	}

	@Override
	public ResponseData<String> getPrimaryKey(String table) 
	{
		return new ResponseData<String>(true, "Clées primaires récupérées.");
	}

	@Override
	public ResponseData<String[]> getPrimaryFromForeign(String table) 
	{
		return new ResponseData<String[]>(true, "Clée étrangères récupérées.");
	}

	@Override
	public ResponseData<String[]> getForeignFromPrimary(String table) 
	{
		return new ResponseData<String[]>(true, "Clée primaires récupérées.");
	}

	@Override
	public ResponseData<String> getUniqueAttribute(String table) 
	{
		return new ResponseData<String>(true, "Attributs uniques récupérés.");
	}

	@Override
	public ResponseData<String[]> getAttributes(String table) 
	{
		return new ResponseData<String[]>(true, "Attributs récupérés.");
	}

	@Override
	public void closeStatement() {}

}

package manager.ddl;

import useful.Response;
import useful.ResponseData;

public class MockDDLManager 
extends AbstractSuccesDDLManager
{

	@Override
	public String[] getDataTypes() 
	{	
		String [] result = {};
		return result;
	}

	
	@Override
	public Response createTable(String sql) 
	{
		return new Response(true, GET_TABLES);
	}
	

	@Override
	public Response dropTable(String table, boolean cascade) 
	{
		return new Response(true, DROP_TABLE);
	}

	
	@Override
	public ResponseData<String> getTables() 
	{
		return new ResponseData<String>(true, GET_TABLES);
	}

	
	@Override
	public ResponseData<String> getPrimaryKey(String table) 
	{
		return new ResponseData<String>(true, GET_PRIMARY);
	}

	
	@Override
	public ResponseData<String[]> getPrimaryFromForeign(String table) 
	{
		return new ResponseData<String[]>(true, PRIMARIES_FROM_FOREIGN);
	}

	
	@Override
	public ResponseData<String[]> getForeignFromPrimary(String table) 
	{
		return new ResponseData<String[]>(true, FOREIGNS_FROM_PRIMARY);
	}
	

	@Override
	public ResponseData<String[]> getUniques(String table) 
	{
		return new ResponseData<String[]>(true, GET_UNIQUE);
	}

	
	@Override
	public ResponseData<String[]> getAttributes(String table) 
	{
		return new ResponseData<String[]>(true, GET_COLUMNS);
	}

	@Override
	public void closeStatement() {}


	@Override
	public ResponseData<String> dropTableDomino(String table) 
	{
		return new ResponseData<String>(true, DOMINO);
	}


	@Override
	public Response alterTable(String sql) 
	{
		return new Response(true, CREATE_TABLE);
	}


	@Override
	public Response addForeignKey(String sql) {
		return new Response (true, "Clée étrangère ajoutée.");
	}


	@Override
	public Response addUnique(String sql) {
		return new Response(true, "Contrainte unique ajoutée.");
	}


	@Override
	public Response dropConstraint(String sql) {
		// TODO Généraliser les alter si possible
		// TODO céer des statiques avec les messages
		return new Response(true, "Contrainte supprimée.");
	}


	@Override
	public Response dropForeignKey(String table, String fkName) 
	{
		return new Response(true, DROP_FK);
	}
}

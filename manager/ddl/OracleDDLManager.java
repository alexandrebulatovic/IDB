package manager.ddl;

import java.sql.Connection;
import java.util.ArrayList;

import useful.Response;



public class OracleDDLManager 
extends AbstractDLLManager
{
	//Constructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param connection : une connexion active, null interdit.
	 */
	public OracleDDLManager(Connection connection)
	{
		super(connection);
	}


	//Méthodes
	@Override
	public String[] getAttributeTypes() 
	{
		String [] result = {"VARCHAR2", "NUMBER", "DATE", "CHAR"};
		return result;
	}


	@Override
	public boolean allowsDropCascade() {return true;}
	
	
	@Override
	public Response createTable(String sql)
	{	
		return this.executeUpdate(sql, CREATE_TABLE);
	}
	
	
	@Override
	public ArrayList<Response> modifyTable(ArrayList<String> sqls) {
		
		ArrayList<Response> rep = new ArrayList<Response>();
		for (String sql : sqls){
			System.out.println("=== Requete === : \n"+sql+"\n===");
			rep.add(this.executeUpdate(sql, "Table Modifiée"));
		}
		return rep;
	}

	
	@Override
	public Response dropTable(String table, boolean cascade) 
	{
		String sql = "DROP TABLE " + table + (cascade ? " CASCADE CONSTRAINT" : "");
		return this.executeUpdate(sql, DROP_TABLE);
	}


	@Override
	public Response addForeignKey(String sql) {
		return this.executeUpdate(sql, "Contrainte fk créée");
	}


	@Override
	public Response addUnique(String sql) {
		return this.executeUpdate(sql, "Contrainte unique créée");
	}


	@Override
	public Response dropConstraint(String sql) {
		return this.executeUpdate(sql, "Contrainte supprimée");
	}


	@Override
	public Response dropForeignKey(String table, String fkName)
	{
		String sql = "ALTER TABLE " + table + " DROP CONSTRAINT " + fkName; 
		return this.executeUpdate(sql, DROP_FK);
	}
}

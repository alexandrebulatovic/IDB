package manager.ddl;

import java.sql.Connection;

import useful.Response;
import useful.ResponseData;

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
	public String[] getDataTypes() 
	{
		String [] result = {"VARCHAR", "NUMERIC", "DATE", "CHAR"};
		return result;
	}
	
	
	@Override
	public Response createTable(String sql) 
	{
		return this.executeUpdate(sql+"\nENGINE=InnoDB", CREATE_TABLE);
	}
	

	@Override
	public Response dropTable(String table, boolean cascade) 
	{
		Response result = null; //Compilateur chiale.
		if (cascade) {
			ResponseData<String[]> r = this.getForeignFromPrimary(table);
			for (String [] ss : r.getCollection()) {
				this.dropForeignKey(ss[3], ss[5]);
			}
			result = r;
		}
		
		if (!cascade || result.hasSuccess()) {
			String sql = "DROP TABLE " + table;
			result =  this.executeUpdate(sql, DROP_TABLE);
		} 
		return result;
	}


	@Override
	public Response addForeignKey(String sql) {
		return this.executeUpdate(sql, "Contrainte créée");
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
		String sql = "ALTER TABLE " + table + " DROP FOREIGN KEY " + fkName;
		return this.executeUpdate(sql, DROP_FK);
	}
}

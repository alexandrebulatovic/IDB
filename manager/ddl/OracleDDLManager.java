package manager.ddl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import business.Attribute;
import useful.Response;
import useful.ResponseData;



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
	public Response createTable(String sql)
	{	
		System.out.println(sql);
		return this.executeUpdate(sql, "Table créée.");
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
		return this.executeUpdate(sql, "Table supprimée.");
	}
	

	@Override
	public List<Attribute> getAttributes(String table) {
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		List<String> pks = this.getPrimaryKey(table).getCollection();
		List<String[]> fks = this.getImportedKey(table).getCollection();
		
		ResponseData<String> uniqueAttributes = this.getUniqueAttribute(table);
		

			ResultSet rsColumns;
			ResultSet rsIndex;
			try {
				rsColumns = this.metadata.getColumns(null, null, table, null);
				while(rsColumns.next()){
					String nameAttribute = 	rsColumns.getString("COLUMN_NAME");
					String type = 			rsColumns.getString("TYPE_NAME");
					int size = 				rsColumns.getInt("COLUMN_SIZE");
					boolean notNull = 		(rsColumns.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls) || (rsColumns.getString("IS_NULLABLE").equals("NO"));
					//TODO ne fonctionne pas
					boolean unique = this.isUnique(nameAttribute,uniqueAttributes.getCollection());
					boolean pk = this.isPk(nameAttribute,pks);
					if (pk){
						unique = false;
						notNull = false;
					}
					
					boolean isFk = false;
					String fkTable = "";
					String fkAttribute ="";
					
					
					for (String[] fk : fks){
						if (fk[2].equals(nameAttribute)){
							isFk = true;
							fkTable = fk[0];
							fkAttribute = fk[1];
						}
					}
					Attribute attribute = new Attribute(
							nameAttribute, 
							type, 
							size, 
							notNull,
							unique, 
							pk, 
							isFk, 
							fkTable, 
							fkAttribute
							);
					attribute.setTableName(table);
					attributes.add(attribute);
					
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			return attributes;
		}


	//Privées



	/**
	 * Exécute une requête SQL qui ne retourne rien.
	 * 
	 * @param sql : une requête sql qui ne retourne rien, null interdit.
	 * @param success : message en cas de succès, null interdit.
	 * @return une réponse personnalisée avec un message de succès $success
	 * si et seulement si la requête aboutie, un message détaillant l'erreur sinon.
	 */
	private Response executeUpdate(String sql, String success)
	{
		Response result;
		try{
			this.statement.executeUpdate(sql);
			result = new Response(true, success);
		}
		catch(SQLException e){
			result = new Response(e);
		}
		return result;
	}


	private boolean isPk(String nameAttribute,List<String> pks) {
		//TODO : cette méthode n'a rien à faire dans cette classe-ci.
		for (String pk : pks){
			if (pk.equals(nameAttribute)){
				return true;
			}
		}
		return false;
	}


	private boolean isUnique(String nameAttribute, List<String> uniqueAttributes) {
		//TODO : cette méthode n'a rien à faire dans cette classe.
		for (String unique : uniqueAttributes){
			if (nameAttribute.equals(unique)){
				return true;
			}
		}
		return false;
	}
}

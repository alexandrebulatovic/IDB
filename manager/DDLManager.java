package manager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import business.Attribute;
import business.Table;

import useful.Response;
import useful.ResponseData;
import useful.ForeinKey;


public class DDLManager 
{
	//Statiques
	/** Constante pour récupérer le nom des tables de données.*/
	private final static int TABLES = 0;

	/** Constante pour récupérer les membres de la clée primaire d'une table donnée.*/
	private final static int PRIMARY_KEY = 1;

	/** Constante pour les clées étrangères DANS une table.*/
	private final static int IN_FOREIGN_KEY = 2;

	/** Constante pour les attributs utilisés comme référence par une autre table.*/
	private final static int OUT_FOREIGN_KEY = 3;

	//Attributs
	/** Pour créer des requètes SQL.*/
	private Statement statement;

	/** Pour obtenir des méta-données sur le SGBD.*/
	private DatabaseMetaData metadata;

	/** Stocke les résultats d'une requête sur les meta-données.*/
	private ResultSet metaDataResult;

	private Connection connection;


	//Constructeur
	/**
	 * Constructeur commun.
	 */
	public DDLManager(Connection connection)
	{
		this.connection = connection;
		this.createStatementAndMetaData(connection);
	}


	//Méthodes
	/**
	 * Tente de créer une table dans la base de données.
	 * 
	 * @param sql : une requête SQL pour créer une table, null interdit.
	 * @return Une réponse personnalisée avec un message de succès si et seulement si
	 * la table est créée, un message détaillant l'erreur sinon.
	 */
	public Response createTable(String sql)
	{	
		System.out.println(sql);
		return this.executeUpdate(sql, "Table créée.");
	}
	
	public ArrayList<Response> modifyTable(ArrayList<String> sqls) {
		
		ArrayList<Response> rep = new ArrayList<Response>();
		for (String sql : sqls){
			System.out.println("=== Requete === : \n"+sql+"\n===");
			rep.add(this.executeUpdate(sql, "Table Modifiée"));
			
		}
		return rep;
		
	}


	/**
	 * Tente de supprimer une table dans la base de données.
	 * 
	 * @param table : une requête SQL pour supprimer une table, null interdit.
	 * @return Une réponse personnalisée avec un message de succès si et seulement si
	 * la table est supprimée, un message détaillant l'erreur sinon.
	 */
	public Response dropTable(String table, boolean cascade)
	{
		String sql = "DROP TABLE " + table + (cascade ? " CASCADE CONSTRAINT" : "");
		return this.executeUpdate(sql, "Table supprimée.");

	}


	/**
	 * @param table : nom de la table où chercher la clée, null interdit.
	 * @return Une réponse personnalisée contenant les attributs membres
	 * de la clée primaire de $table si et seulement si la requête fonctionne,
	 * sinon une réponse personnalisée détaillant l'erreur survenue.
	 */
	public ResponseData<String> getPrimaryKey(String table)
	{
		int [] columns = {4};
		ResponseData<String []> r = this.procedureToGetMetadata
				(PRIMARY_KEY, table, columns, "Clée primaire récupérée.");
		return new ResponseData<String> (r) ;
		
	}


	/**
	 * @return Une réponse personnalisée contenant le nom des tables de données
	 * de la base si et seulement si la requête fonctionne, sinon une réponse 
	 * personnalisée détaillant l'erreur survenue.
	 */
	public ResponseData<String> getTables()
	{
		int [] columns = {3};
		ResponseData<String []> r = this.procedureToGetMetadata
				(TABLES, null, columns, "Tables récupérées");
		return new ResponseData<String>(r);
	}
	
	public List<Attribute> getAttributes(String table) {
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		List<String> pks = this.getPrimaryKey(table).getCollection();
		List<String[]> fks = this.getImportedKey(table).getCollection();
		
		List<String> uniqueAttributes = this.getUnique(table);
		

			ResultSet rsColumns;
			ResultSet rsIndex;
			try {
				rsColumns = this.metadata.getColumns(null, null, table, null);
				while(rsColumns.next()){
					String nameAttribute = 	rsColumns.getString("COLUMN_NAME");
					String type = 			rsColumns.getString("TYPE_NAME");
					int size = 				rsColumns.getInt("COLUMN_SIZE");
					boolean notNull = 		rsColumns.getString("IS_NULLABLE").equals("NO");
					boolean unique = this.isUnique(nameAttribute,uniqueAttributes);
					boolean pk = this.isPk(nameAttribute,pks);
					
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
					attributes.add(new Attribute(
							nameAttribute, 
							type, 
							size, 
							notNull,
							unique, 
							pk, 
							isFk, 
							fkTable, 
							fkAttribute)
							);
					
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			return attributes;
		}


	private boolean isUnique(String nameAttribute, List<String> uniqueAttributes) {
		for (String unique : uniqueAttributes){
			if (nameAttribute.equals(unique)){
				return true;
			}
		}
		return false;
	}


	private List<String> getUnique(String table) {
		List<String> unique = new ArrayList<String>();
		ResultSet rsIndex;
		try {
			rsIndex = this.metadata.getIndexInfo(null, null, table, true, false);
			rsIndex.next();
			while (rsIndex.next()){
				unique.add(rsIndex.getString("COLUMN_NAME"));		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}


		return unique;
	}


	private boolean isPk(String nameAttribute,List<String> pks) {
		for (String pk : pks){
			if (pk.equals(nameAttribute)){
				return true;
			}
		}
		return false;
	}


	/**
	 * @param table : table où chercher les clées étrangères.
	 * @return Une réponse personnalisée qui contient les clées étrangères
	 * de $table et leurs références si et seulement si la requête fonctionne,
	 * sinon une réponse personnalisée détaillant l'erreur survenue.
	 */
	public ResponseData<String []> getImportedKey(String table)
	{
		int [] columns = {3,4,8};
		return this.procedureToGetMetadata
				(IN_FOREIGN_KEY, table, columns, "Clées étrangères récupérées.");
	}
	

	

	/**
	 * Ferme proprement les objets statements.
	 * Ne fait rien en cas d'erreur et n'avertit pas l'utilisateur.
	 */
	public void closeStatement()
	{
		try{this.statement.close();}
		catch(SQLException e){}
	}


	//Privées
	/**
	 * Fabrique des objets Statement et DataBaseMetaData  
	 * et en fait des attributs pour $this.
	 * Ne fait rien en cas d'erreur.
	 * 
	 * @param connection : une connexion active, null interdit.
	 */
	private void createStatementAndMetaData(Connection connection ) 
	{
		try{
			this.statement = connection.createStatement();
			this.metadata = connection.getMetaData();
		}catch(SQLException e){}
	}


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


	/**
	 * Interroge le SGBD à propos de ses métadonnées $what, qui peuvent se trouver dans $table.
	 * Lit les résultats obtenus dans la $column-ième colonne en cas de succès.
	 * 
	 * @param what : les métadonnées voulues, parmi les variables statiques de la classe.
	 * @param table : nom de la table qui contient $what, null autorisé si et seulement si 
	 * les métadonnées $what ne se trouvent pas dans une table.
	 * @param column : numéros des colonnes où trouver les métadonnées, null interdit.
	 * @param success : message en cas de réussite, null interdit.
	 * @return Une réponse personnalisée contenant les métadonnées voulues avec 
	 * un message de réussite $success si et seulement si
	 * la requête a aboutie, sinon une réponse personnalisée détaillant l'erreur rencontrée
	 * et aucune donnée. 
	 */
	private ResponseData<String []> procedureToGetMetadata(
			int what, String table, int [] column, String success)
	{
		ResponseData<String []> result;
		try {
			this.chooseMetaData(what, table);
			result = new ResponseData<String []>
			(true, success, this.readMetaData(column));
		}
		catch(SQLException e){
			result = new ResponseData<String []>(e);
		}
		return result;
	}


	/**
	 * Exécute une requête pour récupérer les métadonnées $what qui se trouvent
	 * éventuellement dans $table.
	 * 
	 * @param what : les métadonnées voulues, parmi les variables statiques de la classe.
	 * @param table : nom de la table qui contient $what, null autorisé si et seulement si 
	 * les métadonnées $what ne se trouvent pas dans une table.
	 * @throws SQLException
	 */
	private void chooseMetaData(int what, String table) 
			throws SQLException
	{
		String user = this.metadata.getUserName();
		
		switch (what){
		case TABLES : 
			String [] tableType = {"TABLE"};
			this.metaDataResult = this.metadata.getTables(
					null, user, "%", tableType);
			break;

		case PRIMARY_KEY :
			this.metaDataResult = this.metadata.getPrimaryKeys(
					null, user, table);
			break;
		
		case IN_FOREIGN_KEY :
			this.metaDataResult = this.metadata.getImportedKeys(null, user, table);
			break;
		}	
	}


	/**
	 * Lit les dernières métadonnées obtenues.
	 * 
	 * @param columns : numéros des colonnes où trouver les métadonnées, null interdit.
	 * @return Une liste contenant toutes les métadonnées lues.
	 * @throws SQLException
	 */
	private List<String []> readMetaData(int [] columns)
			throws SQLException
	{
		List<String []> result = new ArrayList<String []>();
		String [] row;
		int column;
		
		while (this.metaDataResult.next()) {
			row = new String [columns.length];
			for (int j=0; j < columns.length; j++) {
				column = columns[j];
				row[j] = this.metaDataResult.getString(column);
			}
			result.add(row);
		}
		this.metaDataResult.close();
		return result;
	}






	

}

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
	/** Constante pour récupérer les tables de données.*/
	private final static int TABLES = 0;
	
	/** Constante pour récupérer les clées primaires de la base.*/
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
	
	
	//Constructeur
	/**
	 * Constructeur commun.
	 */
	public DDLManager(Connection connection)
	{
		this.createStatementAndMetaData(connection);
	}
	
	
	//Méthodes
	/**
	 * @return Le nombre de tables existantes dans la base de données
	 * @throws SQLException
	 */
	public int getNbTables() throws SQLException{
		ResultSet rs = this.statement.executeQuery("SELECT COUNT(*) FROM user_tables");
		rs.next();
		return rs.getInt(1);
	}
	
	/**
	 *
	 * @return la liste des tables présentes dans la bdd
	 */
	public List<String> getTablesString(){
//		CustomizedResponseWithData<String>
		//TODO trouver un comprimis avec la méthode getTables.
		List<String> valeurs = new ArrayList<String>();
		try {
			ResultSet rs = this.statement.executeQuery("SELECT TABLE_NAME FROM user_tables");
			while (rs.next()){
				valeurs.add(rs.getString(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return valeurs;

	}
	
	/**
	 * 
	 * @param table
	 * @return une liste de nom de clés primaires ou
	 * null s'il n'en existe pas
	 */
	private List<String> getPrimaryKeys(String table){
		List<String>pks = new ArrayList<String>();
		try {
			ResultSet rsKeys = this.metadata.getPrimaryKeys(null, null, table);
			
			pks = new ArrayList<String>();
			while (rsKeys.next()){
				pks.add(rsKeys.getString("COLUMN_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (pks.isEmpty())
			return null;
		else
			return pks;
	}
	
	private boolean isPk(String table, String attributeName){
		List<String>pks = this.getPrimaryKeys(table);
		if (pks != null){ //s'il existe des clés primaires
			for (String pkString : pks){
				if (attributeName.equals(pkString)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Pour des raisons techniques, il est nécessaire de récupérer
	 * TOUTES les clés étrangères pour les appliqués à un attribut
	 * @param table : String
	 * @return List<ForeinKey>
	 */
	private List<ForeinKey> getAllForeinKeys(){
		List<String> tables = this.getTablesString();
		List<ForeinKey> fks = new ArrayList<ForeinKey>();
		try {
			ResultSet rsFk;
//			String name,String table,String fk, String ref,String user, String userTarget
			
			for (String t : tables){
				rsFk = this.metadata.getExportedKeys(null,null,t);
				while (rsFk.next()){
					fks.add(//TODO mettre des id plus lisibles
					new ForeinKey(	rsFk.getString("FK_NAME"),
									rsFk.getString("PKTABLE_SCHEM"), 
									rsFk.getString("PKTABLE_NAME"),
									rsFk.getString("PKCOLUMN_NAME"),
									
									rsFk.getString("FKTABLE_SCHEM"), 
									rsFk.getString("FKTABLE_NAME"), 
									rsFk.getString("FKCOLUMN_NAME")) 
					);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (fks.isEmpty())
			return null;
		else
			return fks;
	}
	
	/**
	 * Retourne un type ForeinKey représentant une clé étrangère
	 * selon le nom d'un attribut dans une table
	 *  
	 * @param table
	 * @param attributeName
	 * @return ForeinKey ou 
	 * null si l'attribut n'est pas clé étrangère
	 */
	private ForeinKey getForeinKey(String table,String attributeName){
		List<ForeinKey>fks = this.getAllForeinKeys();
		if (fks != null){ //s'il existe des clés étrangères
			for (ForeinKey fk : fks){
				if (fk.getFkColumnName().equals(attributeName) && fk.getFkTableName().equals(table)){
					return fk;
				}
			}
		}
		return null;
	}

	
	public List<Attribute> getAttributes(String table) throws SQLException{
		
//
//		for (ForeinKey k : this.getAllForeinKeys()){
//			System.out.println(k.toString());
//		}
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		ResultSet rs = this.statement.executeQuery("SELECT a.* FROM "+table+" a");
	
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int nbColumns = rsmd.getColumnCount();
		for (int i=1 ; i<=nbColumns ; i++){
			
			String nameAttribute = rsmd.getColumnName(i);
			
			String type = rsmd.getColumnTypeName(i);
			int size = rsmd.getPrecision(i);
			
			boolean nullable = !(rsmd.isNullable(i)==ResultSetMetaData.columnNullable);
			boolean unique = false; //TODO trouver un moyen de détecter si la colonne est unique
			boolean pk = this.isPk(table, nameAttribute);//TODO le problème, c'est que la meme requette est envoyé à chaque attribut !
			ForeinKey fk = this.getForeinKey(table, nameAttribute);//TODO meme problème (solution ? Créer une classe InfoTables).
			String fkTable;
			String fkAttribute;
			if (fk==null){
				fkTable=null;
				fkAttribute=null;
			}
			else{
				fkTable = fk.getPkTableName();
				fkAttribute = fk.getPkColumnName();
			}
			
			attributes.add(new Attribute(
					nameAttribute, 
					type, 
					size, 
					nullable, 
					unique, 
					pk, 
					!(fk==null), 
					fkTable, 
					fkAttribute)
					);
		}
		return attributes;
	}

	
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
	 * de la clée primaire de $table si et seulement si la requête réussit,
	 * sinon détaillant l'érreur survenue.
	 */
	public ResponseData<String> getPrimaryKey(String table)
	{
		return this.procedureToGetMetadata(
				PRIMARY_KEY, table, 4, "Clée primaire récupérée.");
	}
	

	
	/**
	 * @return Une réponse personnalisée contenant le nom des tables de données
	 * de la base si et seulement si la requête fonctionne, sinon détaillant
	 * l'erreur survenue.
	 */
	public ResponseData<String> getTables()
	{
		return this.procedureToGetMetadata(
				TABLES, null, 3, "Tables récupérées");
	}
	
	
	/**
	 * Ferme proprement les objets statements.
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
	 * @param column : numéro de colonne où trouver les métadonnées.
	 * @param success : message en cas de réussite, null interdit.
	 * @return Une réponse personnalisée contenant les métadonnées voulues avec 
	 * un message de réussite $success si et seulement si
	 * la requête a aboutie, sinon une réponse personnalisée détaillant l'erreur rencontrée
	 * et aucune donnée. 
	 */
	private ResponseData<String> procedureToGetMetadata(
			int what, String table, int column, String success)
	{
		ResponseData<String> result;
		try {
			this.chooseMetaData(what, table);
			result = new ResponseData<String>
				(true, success, this.readMetaData(column));
		}
		catch(SQLException e){
			result = new ResponseData<String>(e);
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
		switch (what){
		case TABLES : 
			String [] tab = {"TABLE"};
			this.metaDataResult = this.metadata.getTables(
					null, this.metadata.getUserName(), "%", tab);
			break;
			
		case PRIMARY_KEY :
			this.metaDataResult = this.metadata.getPrimaryKeys(
					null, this.metadata.getUserName(), table);
			break;
		}
	}


	/**
	 * Lit les dernières métadonnées obtenues.
	 * 
	 * @param column : numéro de colonne où trouver les métadonnées.
	 * @return Une liste contenant toutes les métadonnées lues.
	 * @throws SQLException
	 */
	private List<String> readMetaData(int column)
	throws SQLException
	{
		List<String> result = new ArrayList<String>();
		while (this.metaDataResult.next()) {
			result.add(this.metaDataResult.getString(column));
		}
		this.metaDataResult.close();
		return result;
	}
}

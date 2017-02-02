package manager.ddl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import useful.Response;
import useful.ResponseData;

import manager.I_DDLManager;

public abstract class AbstractDLLManager 
implements I_DDLManager 
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

	/** Constante pour récupérer les champs de contrainte unique.*/
	private final static int UNIQUE = 4;
	
	
	//Attributs
	/** Pour créer des requètes SQL.*/
	protected Statement statement;

	/** Pour obtenir des méta-données sur le SGBD.*/
	protected DatabaseMetaData metadata;

	/** Stocke les résultats d'une requête sur les meta-données.*/
	protected ResultSet metaDataResult;
	
	
	//Constructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param connection : une connexion active, null interdit.
	 */
	protected AbstractDLLManager(Connection connection)
	{
		this.createStatementAndMetaData(connection);
	}
	
	
	//Méthodes
	@Override
	public ResponseData<String> getTables()
	{
		int [] columns = {3};
		ResponseData<String []> r = this.procedureToGetMetadata
				(TABLES, null, columns, "Tables récupérées");
		return new ResponseData<String>(r);
	}
	
	
	@Override
	public ResponseData<String> getPrimaryKey(String table)
	{
		int [] columns = {4};
		ResponseData<String []> r = this.procedureToGetMetadata
				(PRIMARY_KEY, table, columns, "Clée primaire récupérée.");
		return new ResponseData<String> (r) ;
		
	}
	

	@Override
	public ResponseData<String []> getImportedKey(String table)
	{
		int [] columns = {3, 4, 13, 7, 8, 12};
		return this.procedureToGetMetadata
				(IN_FOREIGN_KEY, table, columns, "Clées étrangères récupérées.");
	}
	
	
	@Override
	public ResponseData<String> getUniqueAttribute(String table) {
		int [] columns = {9};
		 ResponseData<String[]> r = 
				 this.procedureToGetMetadata(UNIQUE, table, columns, "Attributs uniques récupérés.");
		 return new ResponseData<String>(r);
	}
	
	
	@Override
	public ResponseData<String []> getExportedKey(String table)
	{
		int columns [] = {3, 4, 13, 7, 8, 12};
		return this.procedureToGetMetadata
				(OUT_FOREIGN_KEY, table, columns, "Clées étrangères récupérées.");
	}
	
	@Override
	public Response dropTable(String table, boolean cascade, boolean chain)
	{
		Response domino = null;
		if (chain) {
			domino = this.dropTableRecursive(table);
		}
		if (domino != null && ! domino.hasSuccess())
			return domino;
		else 
			return this.dbmsDropTable(table, cascade);
	}
	
	
	@Override
	public void closeStatement()
	{
		try{this.statement.close();}
		catch(SQLException e){}
	}
	
	
	//Protected
	/**
	 * Exécute une requête SQL qui ne retourne rien.
	 * 
	 * @param sql : une requête sql qui ne retourne rien, null interdit.
	 * @param success : message en cas de succès, null interdit.
	 * @return une réponse personnalisée avec un message de succès $success
	 * si et seulement si la requête aboutie, un message détaillant l'erreur sinon.
	 */
	protected Response executeUpdate(String sql, String success)
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
	 * Supprime récursivement toutes les tables qui référencent $table.<br/>
	 * $table n'est pas supprimée.
	 * 
	 * @param table : la table référencées par d'autres tables, null interdit.
	 * @return une réponse personnalisée décrivant la suppression des tables
	 * qui référencent $table.
	 */
	protected Response dropTableRecursive(String table) {
		Response result = null; //Compilateur chiale
		ResponseData<String []> exported = this.getExportedKey(table);
		
		if (! exported.hasSuccess())
			result = exported;
		else {
			String [] tables = extractTableExported(exported.getCollection());
			
			for (String t : tables) {
				result = this.dropTable(t, false, true);
				if (! result.hasSuccess()) return result;
			}
			if (result == null) 
				result = new Response(true, "Pas de table qui référence.");
		}
		return result;
	}
	
	
	protected abstract Response dbmsDropTable(String table, boolean cascade);
	
	
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
			
		case UNIQUE :
			this.metaDataResult = this.metadata.getIndexInfo(null, null, table, true, false);
			break;
		
		case OUT_FOREIGN_KEY :
			this.metaDataResult = this.metadata.getExportedKeys(null, user, table);
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
	
	
	/**
	 * @param list : collection obtenu sur l'appel de getExportedKey().
	 * @return un ensemble de tables.
	 */
	private static String [] extractTableExported(List<String []> list)
	{
		List<String> sort = new ArrayList<String>();
		
		for (String [] tab : list) {
			if (! sort.contains(tab[3]))
				sort.add(tab[3]);
		}
		String [] result = new String [sort.size()];
		int i = 0;
		for (String table : sort) {
			result[i] = table;
			i++;
		}
		return result;
	}
}

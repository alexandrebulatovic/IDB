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
	 * @param column : numéro des colonnes où trouver les métadonnées, null interdit.
	 * @param success : message en cas de réussite, null interdit.
	 * @return Une réponse personnalisée contenant les métadonnées voulues avec 
	 * un message de réussite $success si et seulement si
	 * la requête a aboutie, sinon une réponse personnalisée détaillant l'erreur rencontrée
	 * et aucune donnée. 
	 */
	private ResponseData<String> procedureToGetMetadata(
			int what, String table, int [] column, String success)
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
	 * @param columns : numéro des colonnes où trouver les métadonnées, null interdit.
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

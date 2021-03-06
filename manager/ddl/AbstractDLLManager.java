package manager.ddl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import useful.Response;
import useful.ResponseData;


public abstract class AbstractDLLManager 
extends AbstractSuccesDDLManager
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

	/** Constante pour récupérer les attributs d'une table.*/
	private final static int COLUMNS = 5;


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
				(TABLES, null, columns, GET_TABLES);
		return new ResponseData<String>(r);
	}


	@Override
	public ResponseData<String> getPrimaryKey(String table)
	{
		int [] columns = {4};
		ResponseData<String []> r = this.procedureToGetMetadata
				(PRIMARY_KEY, table, columns, GET_PRIMARY);
		return new ResponseData<String> (r) ;

	}


	@Override
	public ResponseData<String []> getPrimaryFromForeign(String table)
	{
		int [] columns = {3, 4, 13, 7, 8, 12};
		return this.procedureToGetMetadata
				(IN_FOREIGN_KEY, table, columns, PRIMARIES_FROM_FOREIGN);
	}


	@Override
	public ResponseData<String[]> getUniques(String table) {
		int [] columns = {6, 9};
		return this.procedureToGetMetadata
				(UNIQUE, table, columns, GET_UNIQUE);
	}


	@Override
	public ResponseData<String []> getForeignFromPrimary(String table)
	{
		int columns [] = {3, 4, 13, 7, 8, 12};
		return this.procedureToGetMetadata
				(OUT_FOREIGN_KEY, table, columns, FOREIGNS_FROM_PRIMARY);
	}


	@Override
	public ResponseData<String[]> getAttributes(String table)
	{
		int columns [] = {4, 6, 7, 18};
		return this.procedureToGetMetadata
				(COLUMNS, table, columns, GET_COLUMNS);
	}


	@Override
	public Response alterTable(String sql) 
	{
		System.out.println(sql);
		return this.executeUpdate(sql, AbstractSuccesDDLManager.ALTER_TABLE);
	}


	@Override
	public ResponseData<String> dropTableDomino(String table)
	{
		int i = 0;
		List<String> toDrop = new ArrayList<String>();
		ResponseData<String[]> primaries;
		Response dropFk;
		String currentTable;

		toDrop.add(table);
		do{
			currentTable = toDrop.get(i);
			primaries = this.getForeignFromPrimary(currentTable);
			dropFk = this.fillDropableTableAndDropConstraint
					(toDrop, primaries.getCollection());
			i++;
		}while (primaries.hasSuccess() 
				&& dropFk.hasSuccess()
				&& i < toDrop.size());

		if ( ! primaries.hasSuccess()) 
			return new ResponseData<String>(false, primaries.getMessage());
		else if (! dropFk.hasSuccess())
			return new ResponseData<String>(false, dropFk.getMessage());
		else
			return this.dropTables(toDrop);
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


	//Privées
	/**
	 * Ajoute à $toDrop les tables contenues dans $primaries
	 * qui n'existent pas dans $toDrop.<br/>
	 * Supprime les contraintes de clées étrangères des tables ajoutées.
	 * 
	 * @param toDrop : une liste de table à supprimer, null interdit.
	 * @param primaries : résultat d'une méthode getForeignFromPrimary().
	 * @return une réponse personnalisée décrivant si les contraintes
	 * ont étées supprimées avec succès.
	 */
	private  Response fillDropableTableAndDropConstraint
	(List<String> toDrop, List<String[]> primaries)
	{
		Iterator<String[]> it = primaries.iterator();
		Response result = new Response (true, DROP_FK);
		String [] bloc;
		String table, fkName;

		while(it.hasNext() && result.hasSuccess()) {
			bloc = it.next();
			table = bloc[3];
			fkName = bloc[5];
			if (! toDrop.contains(table)){
				toDrop.add(table);
				result = this.dropForeignKey(table, fkName);
			}
		}
		return result;
	}


	/**
	 * Supprime les $tables, dans l'ordre.
	 * 
	 * @param tables : nom des tables, null interdit.
	 * @return le nom des tables qui se sont fait supprimer avec succès.
	 */
	private ResponseData<String> dropTables(List<String> tables)
	{
		Iterator<String> it = tables.iterator();
		ResponseData<String> result = new ResponseData<String>(true, DROP_TABLE);
		Response drop = new Response(true, DROP_TABLE);
		String table;

		while (it.hasNext() && drop.hasSuccess()) {
			table = it.next();
			drop = this.dropTable(table, true);
			if (drop.hasSuccess()) {
				result.add(table);
			}
		}
		
		if (! drop.hasSuccess()) {
			result = new ResponseData<String>(false, drop.getMessage(), result.getCollection());
		}
		return result;
	}


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
			this.metaDataResult = this.metadata.getPrimaryKeys(null, user, table);
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

		case COLUMNS :
			this.metaDataResult = this.metadata.getColumns(null, null, table, null);
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

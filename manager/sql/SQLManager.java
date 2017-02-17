package manager.sql;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Un objet utilisé pour communiquer avec le serveur de base de données.
 * <P>
 * L'envoi de requêtes SQL se fait via la méthode {@code sendQuery}. La réponse du serveur
 * est transformée en une {@code JTable} ou un message qu'il est possible de récupérer 
 * grâce aux méthodes {@code getGeneratedJTable} ou {@code getGeneratedReply}.
 */
public class SQLManager {

	/* CONSTANTES */

	/** Indique une création du {@code Statement} avec les paramètres par défaut de JDBC.  */
	public static final int TYPE_PLAIN_RESULTSET = 0;

	/** Indique une création du {@code Statement} permettant un {@code ResultSet} modifiable et
	 * au positionnement absolue. 
	 * <P>
	 * <strong>Note:</strong> ce choix désactive l'optimisation du {@code Statement}. */
	public static final int TYPE_UPDATABLE_RESULTSET = 1;

	/*-----------------------------------------------------------------*/

	/* ATTRIBUTS */

	private static SQLManager INSTANCE;

	/**  Objet pour exécuter les requêtes SQL. */
	private Statement stat;

	/** Stocke les données et méta-données de la dernière requête executée. */
	private ResultSet rs;

	/** Stocke les méta-données de la dernière requête executée. */
	private ResultSetMetaData rsmd;

	/** Représente la connexion à une base de données. */
	private Connection conn;

	/** Type de {@code Statement} utilisé actuellement. */
	private int statementType;

	/** {@code JTable} créée à partir de l'envoi d'une requête SELECT [..]. */
	private JTable generatedJTable;

	/** {@code String} créé à partir de l'envoi d'une requête autre que SELECT [..]. */
	private String generatedReply;

	/*-----------------------------------------------------------------*/

	/* CONSTRUCTEUR */

	/**
	 * Fabrique un {@code SQLManager} prêt à envoyer des requêtes SQL.
	 * @param conn : objet {@code Connection} représentant la connexion à une base de données.
	 * @param requiredStatementType : constante parmi {@code TYPE_UPDATABLE_RESULTSET} pour avoir un {@code ResultSet} 
	 * dynamique ou {@code TYPE_PLAIN_RESULTSET} pour avoir un {@code ResultSet} fixe mais optimisé.
	 * @throws SQLException si l'accès à la base de données échoue; si le driver utilisé 
	 * ne supporte pas le type de {@code Statement} demandé ou si l'objet {@code Connection} a été fermé.
	 * @throws IllegalArgumentException si le type de {@code Statement} renseigné n'existe pas.
	 * @throws NullPointerException si aucune {@code Connection} n'est renseignée.
	 */
	public SQLManager(Connection conn, int requiredStatementType) throws SQLException, IllegalArgumentException, NullPointerException
	{
		if (conn == null)
			throw new NullPointerException("Aucun objet Connection n'a été fourni");

		else 
		{
			this.conn = conn;
			this.initStatement(requiredStatementType);
		}
	}

	/*-----------------------------------------------------------------*/

	/* METHODES*/
	public SQLManager getSQLManager(Connection conn, int requiredStatementType) 
			throws IllegalArgumentException, NullPointerException, SQLException 
	{
		if (INSTANCE == null)
			INSTANCE = new SQLManager(conn, requiredStatementType);

		else
			this.setStatementType(requiredStatementType);

		return INSTANCE;
	}

	public JTable getGeneratedJTable() {
		return generatedJTable;
	}

	public String getGeneratedReply() {
		return generatedReply;
	}

	/**
	 * Change le type de {@code Statement} utilisé pour envoyer les requêtes SQL.
	 * @param newStatementType : constante parmi {@code TYPE_UPDATABLE_RESULTSET} pour avoir un {@code ResultSet} 
	 * dynamique ou {@code TYPE_PLAIN_RESULTSET} pour avoir un {@code ResultSet} fixe mais optimisé.
	 * @throws SQLException si l'accès à la base de données échoue ou si le driver utilisé ne supporte 
	 * pas le type de {@code Statement} demandé.
	 * @throws IllegalArgumentException si le type de {@code Statement} renseigné n'existe pas.
	 */
	public void setStatementType(int newStatementType ) throws SQLException, IllegalArgumentException 
	{
		if (newStatementType != TYPE_PLAIN_RESULTSET && newStatementType != TYPE_UPDATABLE_RESULTSET)
			throw new IllegalArgumentException("Le type de Statement choisi n'est pas correct");
		else
		{
			if (newStatementType == TYPE_PLAIN_RESULTSET && this.statementType != TYPE_PLAIN_RESULTSET ) 
			{
				this.stat.close();
				this.initStatement(TYPE_PLAIN_RESULTSET);
			}
			else if (newStatementType == TYPE_UPDATABLE_RESULTSET && this.statementType != TYPE_UPDATABLE_RESULTSET) 
			{
				this.stat.close();
				this.initStatement(TYPE_UPDATABLE_RESULTSET);
			}
		}
	}

	/** @return le type de {@code Statement} utilisé parmi {@code TYPE_UPDATABLE_RESULTSET} 
	 * ou {@code TYPE_PLAIN_RESULTSET}. */
	public int getStatementType() {
		return this.statementType;
	}

	/**
	 * Envoie une requête au serveur de base de données.
	 * <P>
	 * Cette méthode indique la forme du résultat ({@code JTable} ou {@code String}). Il faut utiliser ensuite
	 * la méthode {@code getGeneratedJTable} ou {@code getGeneratedReply} pour récupérer le résultat.
	 * @param query : requête SQL à envoyer.
	 * @return Vrai si la réponse est une {@code JTable} (SELECT [...]), faux si c'est un {@code String}.
	 * @throws SQLException si l'accès à la base de données échoue; si la récupération du résultat de l'opération 
	 * effectuée échoue; si la récupération des attributs de la table ou d'une de ses valeurs échoue.
	 * @throws IllegalArgumentException si la requête fournie est vide.
	 * @throws NullPointerException si aucune requête n'est fournie.
	 */
	public boolean sendQuery(String query) throws SQLException, IllegalArgumentException, NullPointerException
	{
		if (query == null)
			throw new NullPointerException();
		else if (query.equals(""))
			throw new IllegalArgumentException("La requête à envoyer au serveur n'est pas valide.");

		String formatedQuery = formatQuery(query);

		boolean isResultSet = stat.execute(formatedQuery);

		if (isResultSet) 
		{ // SELECT
			this.rs = this.stat.getResultSet();
			this.generatedJTable = this.buildJTable();

			return true;
		}
		else 
		{ //  INSERT, UPDATE, DELETE ou LDD
			this.generatedReply = this.generateReply(formatedQuery);

			return false;
		}
	}

	/**
	 * Permet de récupérer un objet {@code JTable} à partir du nom d'une table stockée dans la base de données.
	 * @param tableName : nom de la table à récupérer.
	 * @return un objet {@code JTable} représentant la table demandée et dont les cellules sont non modifiable.
	 * @throws SQLException si l'accès à la base de données échoue; si la récupération des attributs de la table 
	 * ou d'une de ses valeurs échoue.
	 */
	public JTable getJTableFromTableName(String tableName) throws SQLException
	{
		String query = "SELECT T.* FROM "+ tableName +" T";

		this.rs = this.stat.executeQuery(query);

		JTable jTable = this.buildJTable();
		return jTable;
	}

	/**
	 * Insère un nouveau tuple dans la table affichée actuellement.
	 * @param vector : un objet {@code Vector} représentant les données (pouvant être NULL) à insérer dans le bon ordre.
	 * @throws IllegalArgumentException si une valeur n'a pas pu être converti dans le type de l'attribut.
	 * @throws UnsupportedOperationException si le type de données d'un attribut n'est pas géré.
	 * @throws SQLException si l'accès à la base de données échoue; si le {@code ResultSet} n'est pas modifiable; 
	 * si le driver utilisé ne supporte pas l'ajout de tuple ou si le tuple ne respecte pas les contraintes de la table.
	 */
	public void addTuple(Vector<String> vector) throws UnsupportedOperationException, IllegalArgumentException, SQLException
	{
		rs.moveToInsertRow();

		for (int i = 0; i < vector.size() ; i++) 
			this.performUpdate(vector.get(i), i+1);

		rs.insertRow();
		rs.moveToCurrentRow();
		conn.commit(); // on sauvegarde l'ajout dans la base de données
	}



	/** Supprime un tuple de la table affichée actuellement.
	 * @param index : index du tuple à supprimer tel que affiché dans la {@code JTable} (commence à 0).
	 * @throws SQLException si l'accès à la base de données échoue; si le {@code ResultSet} n'est pas modifiable
	 * ou si le driver ne supporte pas la suppression d'un tuple.
	 */
	public void deleteTuple(int index) throws SQLException
	{
		//		conversion entre l'index de la JTable (index commence à 0) et 
		//		la methode ResultSet.absolute(int) où l'index commence à 1.  
		index++;

		rs.absolute(index);
		rs.deleteRow();
		conn.commit();
	}

	/** Met à jour un champ d'un tuple dans la table affichée.
	 * @param index : numéro d'index du tuple (commence à 0).
	 * @param column : numéro d'index de la colonne du champ à mettre à jour (commence à 0).
	 * @param value : nouvelle valeur du champ.
	 * @throws SQLException si l'accès à la base de données échoue; si le numéro de colonne n'est pas valide; 
	 * si le ResultSet n'est pas modifiable; si le driver ne supporte pas la mise à jour d'un tuple; 
	 * si la récupération du type de l'attribut échoue; si la valeur ne respecte pas les contraintes de la table.
	 * @throws IllegalArgumentException si la valeur n'a pas pu être converti dans le type de l'attribut.
	 * @throws UnsupportedOperationException si le type de données de l'attribut n'est pas géré.
	 */
	public void updateTuple(int index, int column, String value) throws UnsupportedOperationException, IllegalArgumentException, SQLException
	{
		//		conversion de l'index et de la colonne dans la JTable (index commence à 0) 
		//		et la methode ResultSet.absolute(int) et SQLManager.performUpdate(String, int)
		//		où l'index commence à 1.  
		column++; 
		index++;

		rs.absolute(index);

		this.performUpdate(value, column);

		rs.updateRow();
		conn.commit();
	}

	/** Formate la requête SQL pour pouvoir fonctionner avec JDBC (= enlève le point virgule). 
	 * @param query : requête SQL à formater.
	 * @return un {@code String} de la requête formatée. */
	private String formatQuery(String query) 
	{
		return query.replace(";","");
	}

	/** 
	 * Créé une représentation d'une table avec ses attributs et ses tuples.
	 * @return une {@code JTable} dont les cellules sont non modifiable et qui représente 
	 * la table du dernier {@code ResultSet} produit.
	 * @throws SQLException si la récupération des attributs de la table ou d'une de ses valeurs échoue.
	 */
	@SuppressWarnings("serial")
	private JTable buildJTable() throws SQLException
	{
		Vector<Vector<Object>> data = new Vector<Vector<Object>>(); // pour stocker les données de la table
		Vector<String> columnNames = new Vector<String>(); // pour stocker les attributs de la table

		this.rsmd = this.rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		for (int i = 1; i <= columnCount; i++) 
			columnNames.add(rsmd.getColumnName(i)); // on récupère les noms des attributs

		while (rs.next()) // on récupère les données
		{
			Vector<Object> tuple = new Vector<Object>();

			for (int i = 1; i <= columnCount; i++) 
			{ // gérer les problèmes d'affichage éventuels ici
				Object retrievedObject = rs.getObject(i);

				if (retrievedObject instanceof  java.sql.Clob ) // gestion du type CLOB
				{
					java.sql.Clob clob = (java.sql.Clob) retrievedObject;
					int length; 
					if (clob.length() < Integer.MAX_VALUE)
						length = (int)clob.length();
					else
						length = Integer.MAX_VALUE;

					tuple.add(clob.getSubString(1, length)); // on ajoute le contenu du type CLOB (tronqué si nécessaire)
				} else
					tuple.add(retrievedObject);

				if (rs.wasNull()) // gestion des NULLS
					tuple.set(i-1, "<null>");

			}
			data.add(tuple); // on rajoute le tuple à nos données
		}

		JTable retrievedTable = new JTable(new DefaultTableModel(data, columnNames))
		{
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		return retrievedTable;
	}

	/**
	 * Insère une valeur dans un champ du tuple courant (= le curseur du {@code ResultSet} est dessus).
	 * <P>
	 * Effectue un {@code cast} si nécessaire afin de respecter le type de données de l'attribut.
	 * @param value : valeur à insérer.
	 * @param column : numéro d'index de la colonne où insérer la valeur (commence à 1).
	 * @throws SQLException si l'accès à la base de données échoue; si le numéro de colonne n'est pas valide; 
	 * si le {@code ResultSet} n'est pas modifiable; si le driver ne supporte pas la mise à jour d'un tuple; 
	 * si la récupération du type de l'attribut échoue; si la valeur ne respecte pas les contraintes de la table.
	 * @throws UnsupportedOperationException si le type de données d'un attribut n'est pas géré.
	 * @throws IllegalArgumentException si une valeur n'a pas pu être converti dans le type de l'attribut.
	 */
	private void performUpdate(String value, int column) 
			throws SQLException, UnsupportedOperationException, IllegalArgumentException
	{
		if ( value == null || value.equals("<null>") || value.equals("NULL") || value.equals("")) 
			rs.updateNull(column); // si l'utilisateur n'a rien entré ou s'il indique explicitement NULL, on met la valeur à NULL

		else
		{
			switch (this.rsmd.getColumnClassName(column)) // sinon on récupère le mapping (classe Java utilisée) de la colonne
			{
			case "java.lang.String": // CHAR, VARCHAR, VARCHAR2, TINYTEXT, TEXT, MEDIUMTEXT, LONGTEXT, CHARACTER, LONG, STRING
				rs.updateString(column, value);
				break;
			case "java.lang.Integer": // TINYINT, SMALLINT, MEDIUMINT, INT, INTEGER, BINARY_INTEGER, NATURAL, NATURALN, PLS_INTEGER, POSITIVE, POSITIVEN, SIGNTYPE
				rs.updateInt(column, Integer.parseInt(value));
				break;
			case "java.lang.Boolean": // BIT, TINYINT, BOOLEAN
				rs.updateBoolean(column, Boolean.parseBoolean(value));
				break;
			case "java.lang.Long": // BIGINT, INT,INTEGER
				rs.updateLong(column, Long.parseLong(value));
				break;
			case "java.lang.Float": // FLOAT, REAL
				rs.updateFloat(column,  Float.valueOf(value));
				break;
			case "java.lang.Double": // DOUBLE, DOUBLE PRECISION, FLOAT
				rs.updateDouble(column,  Double.valueOf(value));
				break;
			case "java.math.BigDecimal": // DECIMAL, DEC, NUMBER, NUMERIC
				rs.updateBigDecimal(column, new BigDecimal(value));
				break;
			case "[Bjava.lang.Byte": // BIT, BINARY, VARBINARY, TINYBLOB, BLOB, MEDIUMBLOB, LONGBLOB, RAW, LONG RAW
				rs.updateBytes(column, value.getBytes());
				break;
			case "java.sql.Date": // DATE
				rs.updateDate(column, java.sql.Date.valueOf(value));
				break;
			case "java.sql.Time": // TIME
				rs.updateTime(column, java.sql.Time.valueOf(value));
				break;
			case "java.sql.Timestamp": // DATETIME, TIMESTAMP, DATE
				rs.updateTimestamp(column, java.sql.Timestamp.valueOf(value));
				break;
			case "java.sql.Clob": // CLOB
			case "oracle.sql.CLOB": 
			case "oracle.jdbc.OracleClob":
				java.sql.Clob clob = this.conn.createClob();
				clob.setString(1, value);
				rs.updateClob(column,  clob);
				break;
			default:
				throw new UnsupportedOperationException("Type de données de l'attribut non pris en charge par ce programme.");
			} 
		}
	}

	/**
	 * Génère la réponse de la requête à partir de sa syntaxe et du résultat de la base de données.
	 * @param query : la requête envoyée au serveur.
	 * @return un {@code String} de la réponse à afficher.
	 * @throws SQLException si la récupération du résultat de l'opération sur la base de données échoue.
	 */
	private String generateReply(String query) throws SQLException
	{
		String generatedReply;

		if (query.contains("INSERT")){
			generatedReply = stat.getUpdateCount()+ " ligne ajoutée.";
		} else if (query.contains("DELETE")){
			generatedReply = stat.getUpdateCount()+" ligne supprimée.";
		} else if (query.contains("UPDATE")){
			generatedReply = stat.getUpdateCount()+" ligne mis à jour.";
		} else if (query.contains("CREATE")) {
			generatedReply = "Table créée.";
		} else if (query.contains("DROP")) {
			generatedReply = "Table supprimée.";
		} else {
			generatedReply = "Aucune ligne retournée.";
		}

		return generatedReply;
	}

	/** 
	 * Initialise le type de {@link Statement} à utiliser pour envoyer les requêtes SQL au serveur de base de données. 
	 * @param requiredStatementType : constante parmi {@code TYPE_UPDATABLE_RESULTSET} pour avoir un {@code ResultSet} 
	 * dynamique ou {@code TYPE_PLAIN_RESULTSET} pour avoir un {@code ResultSet} fixe mais optimisé.
	 * @throws SQLException si l'accès à la base de données échoue ou si le driver utilisé ne supporte 
	 * pas le type de {@code Statement} demandé.
	 * @throws IllegalArgumentException si le type de {@code Statement} renseigné n'existe pas.
	 */
	private void initStatement(int requiredStatementType) throws SQLException, IllegalArgumentException
	{
		conn.setAutoCommit(false);

		if (requiredStatementType == TYPE_UPDATABLE_RESULTSET)
		{
			this.stat = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			this.statementType = TYPE_UPDATABLE_RESULTSET;
		}

		else if (requiredStatementType == TYPE_PLAIN_RESULTSET)
		{
			this.stat = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			this.stat.setFetchSize(100);
			this.statementType = TYPE_PLAIN_RESULTSET;
		}
		else
			throw new IllegalArgumentException("Le type de Statement choisi n'est pas correct");
	}
}
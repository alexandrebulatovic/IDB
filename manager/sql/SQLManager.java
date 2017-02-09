package manager.sql;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import useful.Response;

/**
 * Gère la communication avec le serveur à partir de la requête entrée dans la {@code SQLView}.
 * <P>
 * Elle génére une {@code JTable} ou un message à partir de la réponse du serveur et l'envoie à 
 * la classe appelante.
 */

public class SQLManager {

	/* CONSTANTES */

	/** Indique une création du {@code Statement} avec les paramètres par défaut.  */
	public static final int TYPE_PLAIN_RESULTSET = 0;

	/** Indique une création du {@code Statement} permettant un {@code ResultSet} modifiable et
	 * au positionnement absolue. 
	 * <P>
	 * <strong>Note:</strong> ce choix désactive l'optimisation du {@code Statement}. */
	public static final int TYPE_UPDATABLE_RESULTSET = 1;

	/* ATTRIBUT */

	/**  Objet pour executer les requetes sql. */
	private Statement stat;

	/** Stocke les données et méta-données de la dernière requête executée. */
	private ResultSet rs;

	/** Stocke les méta-données de la dernière requête executée. */
	private ResultSetMetaData rsmd;

	/** Connexion à la base de données. */
	private Connection conn;

	/** Dernière exception levée. */
	private Exception exception;

	/** Type de statement utilisé actuellement. */
	private int statementType;


	/* CONSTRUCTEUR */

	/** Fabrique un {@code SQLManager}. 
	 * @param conn : {@code Connection} à la base de données. */
	public SQLManager(Connection conn)
	{
		this.conn = conn;
	}

	/* METHODES*/

	/**
	 * Permet d'obtenir la dernière exception levée par le SGBD.
	 * @return un objet {@code Exception}.
	 */
	public Exception getLastException() {
		return exception;
	}


	/**
	 * Change le type d'objet {@code Statement} utilisé pour envoyer les requêtes SQL.
	 * @param newStatementType : constantes parmi {@code TYPE_UPDATABLE_RESULTSET} pour avoir un {@code ResultSet} dynamique, 
	 * {@code TYPE_PLAIN_RESULTSET} pour avoir un {@code ResultSet} fixe et optimisé.
	 */
	public void setStatementType(int newStatementType ) {

		if (newStatementType == TYPE_PLAIN_RESULTSET && this.statementType != TYPE_PLAIN_RESULTSET ) {

			this.initStatement( TYPE_PLAIN_RESULTSET);
			this.statementType= TYPE_PLAIN_RESULTSET;

		}
		else if (newStatementType == TYPE_UPDATABLE_RESULTSET && this.statementType != TYPE_UPDATABLE_RESULTSET) {

			this.initStatement(TYPE_UPDATABLE_RESULTSET);
			this.statementType = TYPE_UPDATABLE_RESULTSET;

		}

	}

	/** @return le type de {@code Statement} utilisé  parmi {@code TYPE_UPDATABLE_RESULTSET} 
	 * ou {@code TYPE_PLAIN_RESULTSET}. */
	public int getStatementType() {
		return this.statementType;
	}

	/** Initialise l'attribut {@code Statement} nécessaire pour envoyer les requêtes SQL. 
	 * @param statementType : constantes parmi {@code TYPE_UPDATABLE_RESULTSET} pour avoir un {@code ResultSet} dynamique, 
	 * {@code TYPE_PLAIN_RESULTSET} pour avoir un {@code ResultSet} fixe et optimisé.
	 * @return Vrai en cas de réussite, faux sinon et l'attribut {@code this.exception} est mis à jour. */
	public boolean initStatement(int statementType)
	{
		try {

			if (statementType==TYPE_UPDATABLE_RESULTSET)
			{
				this.stat = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			}
			else if (statementType==TYPE_PLAIN_RESULTSET)
			{
				conn.setAutoCommit(false);
				this.stat = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				this.stat.setFetchSize(100);
			}

			return true;


		} catch (SQLException exception) {	
			this.exception = exception;
			return false;
		}
	}

	/** Envoie la requête au serveur de base de données.
	 * @param qry : requête à envoyer. 
	 * @return un {@code JTable} si c'est un SELECT, sinon un {@code String} de la réponse du serveur. */
	public Object sendSQL(String qry)
	{
		String qry_formated = formatSQL(qry);

		try {
			boolean is_resultset = stat.execute(qry_formated);

			if (is_resultset) { // SELECT

				this.rs = stat.getResultSet();
				this.rsmd = this.rs.getMetaData();

				return buildJTable(this.rs, this.rsmd);

			} else { //  INSERT, UPDATE, DELETE ou LDD

				return buildReply(qry_formated);
			}

		} catch (SQLException exception) {
			this.exception = exception;
			return new Response(false);
		}
	}

	/** Formate la requête SQL pour pouvoir fonctionner avec JDBC. 
	 * @param qry : requête SQL à formater.
	 * @return un {@code String} de la requête formatée. */
	public String formatSQL(String qry) 
	{
		return qry.replace(";","");
	}

	/** Créé la réponse de la requête à partir de sa syntaxe et du résultat de la base de données.
	 * @param qry : la requête envoyée au serveur. 
	 * @return un {@code String} de la réponse à afficher qui peut éventuellement être une erreur. */
	private Response buildReply(String qry)
	{
		String str = null;

		try {
			if (qry.contains("INSERT")){
				str = stat.getUpdateCount()+ " ligne ajoutée.";
			} else if (qry.contains("DELETE")){
				str = stat.getUpdateCount()+" ligne supprimée.";
			} else if (qry.contains("UPDATE")){
				str = stat.getUpdateCount()+" ligne mis à jour.";
			} else if (qry.contains("CREATE")) {
				str = "Table créée.";
			} else if (qry.contains("DROP")) {
				str = "Table supprimée.";
			} else {
				str = "Aucune ligne retournée.";
			}

		} catch (SQLException exception) {	
			this.exception = exception;
		} catch (NullPointerException exception) {
			System.out.println("il n'y a pas de requête à parser.");
		}

		return new Response(true, str);
	}

	/** Crée un message d'erreur à partir d'une {@code SQLException}.
	 * @param exception : objet {@code SQLException} caractérisant l'erreur rencontrée. 
	 * @return un {@code String} expliquant l'erreur. */
	public static String errorHandler(SQLException exception) 
	{
		String str;

		switch (exception.getErrorCode()) {
		case 942: str = "Cette table ou cette vue n'existe pas.";
		case 900: str = "Veuillez saisir la requête correctement !";
		case 955: str =  "Ce nom est déjà pris.";
		default: str = "ECHEC ! Erreur: "+ exception.getErrorCode() + " " + exception.getMessage();
		}

		return str;
	}

	/** Créé une représentation d'une table avec ses attributs et ses tuples.
	 * @param rs : {@code ResultSet} à partir duquel générer la représentation.
	 * @return un {@code JTable} qui représente la table.
	 */
	@SuppressWarnings("serial")
	public static JTable buildJTable(ResultSet rs, ResultSetMetaData rsmd) 
	{

		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		Vector<String> columnNames = new Vector<String>();

		try {


			int size = rsmd.getColumnCount();

			for (int i = 1; i <= size; i++) {
				columnNames.add(rsmd.getColumnName(i));
			}

			while (rs.next()) {
				Vector<Object> vector = new Vector<Object>();

				for (int i = 1; i <= size; i++) {
					vector.add(rs.getObject(i));

					if (rs.wasNull())
						vector.set(i-1, "<null>");
				}
				data.add(vector);
			}

		} catch (SQLException exception) {
			System.out.println(errorHandler(exception));
		}

		return new JTable(new DefaultTableModel(data, columnNames)){

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};
	}

	public DefaultTableModel requestTableModel(String tableName) 
	{
		String qry = "SELECT T.* FROM "+ tableName+" T";
		JTable table = (JTable)this.sendSQL(qry);

		return (DefaultTableModel) table.getModel();
	}

	/** Insère un nouveau tuple dans la base de données.
	 * @param vector : {@code Vector} représentant les données à insérer dans l'ordre.
	 * @return Vrai si l'insertion réussie, sinon faux et l'attribut {@code this.exception}
	 * est mis à jour.
	 */
	public boolean addTuple(Vector<Object> vector)
	{

		try {
			rs.moveToInsertRow();

			int columnPosition = 1;

			for (int i = 0; i < vector.size() ; i++, columnPosition++) 
			{ 
				if ( vector.get(i) == null || ((String)vector.get(i)).equals("<null>") 
						|| ((String)vector.get(i)).equals("NULL") || ((String)vector.get(i)).equals("")){
					rs.updateNull(columnPosition);
				} else {

					switch (this.rsmd.getColumnClassName(columnPosition)) // on récupère la classe de l'objet utilisé pour l'affichage
					{
					case "java.lang.String": // type sql : CHAR, VARCHAR, LONGVARCHAR
						rs.updateString(columnPosition, (String) vector.get(i));
						break;
					case "java.lang.Integer": // INTEGER
						rs.updateInt(columnPosition, (int) vector.get(i));
						break;
					case "java.sql.Date": // DATE
						rs.updateDate(columnPosition, (Date) vector.get(i));
						break;
						/*case "java.math.BigDecimal": // NUMERIC, DECIMAL
						rs.updateBigDecimal(columnPosition, (BigDecimal) vector.get(i));
						break;*/
					case "java.lang.Boolean": // BIT
						rs.updateBoolean(columnPosition, (boolean) vector.get(i));
						break;
					case "java.lang.Long": // BIGINT
						rs.updateLong(columnPosition, (long) vector.get(i));
						break;
					case "java.lang.Float": // REAL
						rs.updateFloat(columnPosition,  (float) vector.get(i));
						break;
					case "java.lang.Double": // DOUBLE, FLOAT
						rs.updateDouble(columnPosition,  (double) vector.get(i));
						break;
					}

				}

			}

			rs.insertRow();
			rs.moveToCurrentRow();
			conn.commit();
			return true;   

		} catch (Exception exception) {
			this.exception = exception;
			return false;
		}
	}

	/** Supprime un tuple de la base de données.
	 * @param index : index du tuple à supprimer affiché dans la {@code JTable} (commence à 0).
	 * @return Vrai si l'insertion réussie, sinon faux et l'attribut {@code this.exception}
	 * est mis à jour.
	 */
	public boolean removeTuple(int index) 
	{
		index++; // conversion entre index du TableModel et la methode absolute() où l'index commence à 1
		try 
		{
			rs.absolute(index);
			rs.deleteRow();
			conn.commit();
			return true;

		} catch (Exception exception) {
			this.exception = exception;
			return false;
		}
	}

	/** Met à jour un attribut d'un tuple de la table courante.
	 * @param index : numéro d'index du tuple de la valeur à mettre à jour, commence à 0.
	 * @param column : numéro de colonne de la valeur à mettre à jour, commence à 0.
	 * @param value : nouvelle valeur de l'attribut.
	 * @return Vrai si l'insertion réussie, sinon faux et l'attribut {@code this.exception}
	 * est mis à jour.
	 */
	public boolean updateTuple(int index, int column, Object value)
	{
		//correspondance entre la TableModel (index commence à 0) et le ResultSet (index commence à 1..)
		column++; 
		index++;

		try {
			rs.absolute(index);
			// TODO utiliser updateObject
			if ( value == null || ((String)value).equals("<null>") || 
					((String)value).equals("NULL") || ((String)value).equals(""))
				rs.updateNull(column);
			else if (value instanceof Integer)
				rs.updateInt(column, (int) value);
			else if (value instanceof String)
				rs.updateString(column, (String) value);
			else if (value instanceof Date)
				rs.updateDate(column, (Date) value);
			else if (value instanceof Float)
				rs.updateFloat(column, (float) value);
			else if (value instanceof Double)
				rs.updateDouble(column, (double) value);
			else if (value instanceof Boolean)
				rs.updateBoolean(column, (boolean) value);
			else if (value instanceof Long)
				rs.updateLong(column,  (long) value);
			else if (value instanceof BigDecimal)
				rs.updateBigDecimal(column, (BigDecimal) value);
			// updateNull ?
			rs.updateRow();
			conn.commit();
			return true;

		} catch (Exception exception) {
			this.exception = exception;
			return false;
		}
	}
}
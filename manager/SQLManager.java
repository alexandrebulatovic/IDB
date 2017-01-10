package manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Gère la communication avec le serveur à partir de la requête reçue par la {@code SQLView}.
 * <P>
 * Elle génére une {@code JTable} ou un message à partir de la réponse du serveur et lui renvoie.
 */

public class SQLManager {

	/* CONSTANTES */

	/** Indique une création du {@code Statement} avec les paramètres par défaut.  */
	public static final int TYPE_PLAIN_RESULTSET = 0;

	/** Indique une création du {@code Statement} permettant un {@code ResultSet} modifiable et
	 * à positionnement absolue. 
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


	/* CONSTRUCTEUR */

	/** Fabrique un {@code SQLManager}.
	 * @param conn : {@code Connection} à partir duquel initialiser le {@code SQLManager}. */
	public SQLManager(Connection conn, int statementType)
	{
		this.initStatement(conn, statementType);
	}

	/* METHODES*/

	/** Initialise l'attribut {@code Statement} nécessaire pour envoyer les requêtes SQL. 
	 * @param conn : {@code Connection} à partir duquel initialiser le {@code Statement}. */
	private void initStatement(Connection conn, int statementType)
	{
		try {

			if (statementType==TYPE_UPDATABLE_RESULTSET)
				this.stat = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			else if (statementType==TYPE_UPDATABLE_RESULTSET)
			{
				conn.setAutoCommit(false);
				this.stat = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				this.stat.setFetchSize(100);
			}


		} catch (SQLException exception) {	
			System.out.println(errorHandling(exception));
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

				return buildJTable();

			} else { //  INSERT, UPDATE, DELETE ou LDD

				return buildReply(qry_formated);

			}

		}catch (SQLException exception) {	
			return errorHandling(exception);
		}
		catch (Exception exception) {
			exception.printStackTrace();	
		}
		return null;
	}

	/** Formate la requête SQL pour pouvoir fonctionner avec JDBC. 
	 * @param qry : requête SQL à formater.
	 * @return un {@code String} de la requête formatée. */
	private String formatSQL(String qry) 
	{
		return qry.replace(";","");
	}

	/** Créé la réponse de la requête à partir de sa syntaxe et du résultat de la base de données.
	 * @param qry : la requête envoyée au serveur. 
	 * @return un {@code String} de la réponse à afficher qui peut éventuellement être une erreur. */
	private String buildReply(String qry)
	{
		String str = "Aucune ligne retournée.";

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
			}

		} catch (SQLException exception) {	
			return errorHandling(exception);
		} catch (Exception exception) {
			exception.printStackTrace();	
		}

		return str;
	}

	/** Créé un message d'erreur à partir d'une {@code SQLException}.
	 * @param exception : objet {@code SQLException} caractérisant l'erreur rencontrée. 
	 * @return un {@code String} expliquant l'erreur. */
	private String errorHandling(SQLException exception) 
	{
		String str;

		switch (exception.getErrorCode()) {
		case 942: str = "Cette table ou cette vue n'existe pas.";
		case 900: str = "Veuillez saisir la requête correctement !";
		case 955: str =  "Ce nom est déjà pris.";
		default: str = "ECHEC! erreur: "+ exception.getErrorCode() + " " + exception.getMessage();
		}

		return str;
	}

	/** Créé une représentation d'une table avec ses attributs et ses tuples.
	 * @param rs : {@code ResultSet} à partir duquel générer la représentation.
	 * @return un {@code JTable} qui représente la table.
	 */
	private JTable buildJTable() 
	{

		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		Vector<String> columnNames = new Vector<String>();

		try {
			this.rs = stat.getResultSet();
			this.rsmd = rs.getMetaData();

			int size = rsmd.getColumnCount();

			for (int i = 1; i <= size; i++) {
				columnNames.add(rsmd.getColumnName(i));
			}

			while (rs.next()) {
				Vector<Object> vector = new Vector<Object>();

				for (int i = 1; i <= size; i++) {
					vector.add(rs.getObject(i));
				}
				data.add(vector);
			}

		} catch (SQLException exception) {
			System.out.println(errorHandling(exception));
		}

		return new JTable(new DefaultTableModel(data, columnNames));
	}

	public JTable requestJTable(String tableName) 
	{
		String qry = "SELECT * FROM "+ tableName;
		JTable table = (JTable)this.sendSQL(qry);

		return table;
	}

	/** Insère un nouveau tuple dans la base de données.
	 * @param vector : {@code Vector} représentant les données à insérer dans l'ordre.
	 * @return "OK" si l'insertion réussie, sinon un message d'erreur.
	 */
	public String addTuple(Vector<?> vector)
	{

		try {
			rs.moveToInsertRow();

			for (int i=0; i < vector.size() ; i++) 
			{ 
				// TODO : implémenter d'autres types
				switch (this.rsmd.getColumnClassName(i+1))
				{
				case "java.lang.String":
					rs.updateString(i+1, (String) vector.get(i));
					break;
				case "java.lang.Integer":
					rs.updateInt(i+1, (int) vector.get(i));
					break;
				case "java.sql.Date":
					rs.updateDate(i+1, (Date) vector.get(i));
					break;
				}
			}

			rs.insertRow();
			rs.moveToCurrentRow();

			return "OK";   

		} catch (SQLException exception) {
			return errorHandling(exception);
		}
	}

	/** Supprime un tuple de la base de données.
	 * @param index : index du tuple à supprimer affiché dans la {@code JTable}.
	 * @return "OK" si la suppression réussie, sinon un message d'erreur.
	 */
	public String removeTuple(int index) 
	{
		try 
		{
			rs.absolute(index+1);
			rs.deleteRow();
			return "OK";

		} catch (SQLException exception) {
			return this.errorHandling(exception);
		}
	}

	/** Met à jour un tuple de la base de données.
	 * @param index : index du tuple de la valeur à mettre à jour.
	 * @param column : colonne de la valeur à mettre à jour.
	 * @param value : nouvelle valeur.
	 * @return : "OK" si la modification réussie, sinon un message d'erreur.
	 */
	public String updateTuple(int index, int column, Object value)
	{
		column++; //correspondance entre la TableModel (index commence à 0) et le ResultSet (index commence à 1..)
		index++;

		try {
			rs.absolute(index);
			// TODO : implémenter d'autres types
			if (value instanceof Integer)
				rs.updateInt(column, (int) value);
			else if (value instanceof String)
				rs.updateString(column, (String) value);
			else if (value instanceof Date)
				rs.updateDate(column, (Date) value);

			rs.updateRow();

			return "OK";

		} catch (SQLException exception) {
			return this.errorHandling(exception);
		}
	}
}
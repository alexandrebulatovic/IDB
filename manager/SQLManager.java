package manager;

import java.sql.*;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SQLManager {

	/* ATTRIBUT */

	/**  Objet pour executer les requetes sql. */
	private Statement stat;


	/* CONSTRUCTEUR */

	/** Fabrique un {@code SQLManager}.
	 * @param conn : {@code Connection} à partir duquel initialiser le {@code SQLManager}. */
	public SQLManager(Connection conn)
	{
		this.initStatement(conn);
	}

	/* METHODES*/

	/** Initialise l'attribut {@code Statement} nécessaire pour envoyer les requêtes SQL. 
	 * @param conn : {@code Connection} à partir duquel initialiser le {@code Statement}. */
	private void initStatement(Connection conn)
	{
		try {
			conn.setAutoCommit(false);
			this.stat = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			this.stat.setFetchSize(100);
		} catch (SQLException exception) {	
			errorHandling(exception);
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
		default: str = "ECHEC!"+ exception.getErrorCode() + " " + exception.getMessage();
		}

		return str;
	}

	/** Créé une représentation d'une table avec ses attributs et ses tuples.
	 * @param rs : {@code ResultSet} à partir duquel générer la représentation.
	 * @return un {@code JTable} qui représente la table.
	 */
	private JTable buildJTable() 
	{

		ResultSetMetaData rsmd;
		ResultSet rs;

		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		Vector<String> columnNames = new Vector<String>();

		try {
			rs = stat.getResultSet();
			rsmd = rs.getMetaData();

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
			errorHandling(exception);
		}

		return new JTable(new DefaultTableModel(data, columnNames));
	}
}

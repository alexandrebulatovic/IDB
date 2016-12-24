package sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import manager.SQLManager;

public class SQLController {

	/** IHM pour taper des requetes SQL. */
	private SQLView sql;

	/** Objet pour envoyer des requetes au SGBD. */
	private SQLManager creator;

	/**  Objet pour appeler les methodes execute sql */
	private Statement stat;

	/**  Objet qui représente la connexion à la BD. */
	private Connection conn;

	/** Constructeur commun
	 * @param cm : objet ConnectionManager obtenu lors de la connexion. */
	public SQLController(Connection connection){
		this.creator = new SQLManager(connection);
		this.conn = connection;

		try {
			conn.setAutoCommit(false);
			this.stat = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			this.stat.setFetchSize(100);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	/** Ouvre l'IHM pour taper du code SQL si et seulement si 
	 * elle n'existe pas, sinon tente de l'afficher au premier plan. */
	public void openSQL()
	{
		if (this.sql == null) {
			this.sql = new SQLView(this);
		} else {
			this.sql.setVisible(true);
			this.sql.toFront();
		}
	}

	/** Pop-up pour afficher un message.
	 * @param res : message à afficher. */
	public void showResult(String res){
		JOptionPane.showMessageDialog(null, res, "Résultat", JOptionPane.INFORMATION_MESSAGE);
	}

	/** Pop-up pour afficher une table.
	 * @param res : JTable à afficher. */
	public void showResult(JTable res){
		JOptionPane.showMessageDialog(null, new JScrollPane(res), "Résultat", JOptionPane.INFORMATION_MESSAGE);
	}

	/** Methode pour envoyer la requête au serveur.
	 * @param rq : requête sous forme de chaîne de caractères à envoyer. */
	public void sendSQL(String rq){

		try {
			boolean res = stat.execute(rq);

			if (res) { // SELECT

				ResultSet rs = stat.getResultSet() ;
				JTable table = buildJTable(rs);
				showResult(table);

			} else { //  INSERT, UPDATE, DELETE ou LDD

				parseQuery(rq);

			}

		}catch (SQLException exception) {	
			errorHandling(exception);
		}
		catch (Exception ex) {
			ex.printStackTrace();	
		}
	}

	/** Méthode pour gérer les différents cas d'erreurs rencontrées lors de l'exécution d'une requête.
	 * @param exception : objet SQLException représentant l'erreur rencontrée. */
	public void errorHandling(SQLException exception) {
		switch (exception.getErrorCode()) {
		case 942:showResult("Cette table ou cette vue n'existe pas.");
		break;
		case 900:showResult("Veuillez saisir la requête correctement !");
		break;
		case 955:showResult("Ce nom est déjà pris.");
		break;
		default: showResult("ECHEC!"+ exception.getErrorCode() + " " + exception.getMessage());
		break;
		}
	}

	/** Analyse la syntaxe de la requête pour afficher le résultat sous la bonne forme
	 * @param rq : requête à analyser. */
	public void parseQuery(String rq){
		try {

			if (rq.contains("INSERT")){
				showResult(stat.getUpdateCount()+ " ligne ajoutée.");
			} else if (rq.contains("DELETE")){
				showResult(stat.getUpdateCount()+" ligne supprimée.");
			} else if (rq.contains("UPDATE")){
				showResult(stat.getUpdateCount()+" ligne mis à jour.");
			} else if (rq.contains("CREATE")) {
				showResult("Table créée.");
			} else if (rq.contains("DROP")) {
				showResult("Table supprimée.");
			}else {
				showResult("Aucune ligne retournée.");
			}
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
	}

	/**
	 * Méthode pour créer un objet représentant une table de la base de données avec ses attributs et ses données.
	 * @param rs : ResultSet à partir duquel générer l'objet.
	 * @return un objet JTable représentant notre table dans la base de données.
	 */
	public JTable buildJTable(ResultSet rs) {

		ResultSetMetaData rsmd;

		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		Vector<String> columnNames = new Vector<String>();

		try {
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

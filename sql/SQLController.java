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
		}
		this.sql.setVisible(true);
		this.sql.toFront();
	}

	/** Affiche un pop-up avec un message.
	 * @param res : message à afficher. */
	public void showResult(String res){
		JOptionPane.showMessageDialog(null, res, "Résultat", JOptionPane.INFORMATION_MESSAGE);
	}

	/** Methode pour envoyer la requête au serveur.
	 * @param rq : requête sous forme de chaîne de caractères à envoyer. */
	public void sendSQL(String rq){

		try {
			boolean res = stat.execute(rq);

			if (res) { // Si le résultat est un ResultSet

				ResultSet rs = stat.getResultSet() ;
				JTable table = new JTable(buildJTable(rs));
				JOptionPane.showMessageDialog(null, new JScrollPane(table),"Résultat", JOptionPane.INFORMATION_MESSAGE);


			} else { //  INSERT, UPDATE, or DELETE ou LDD
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

		}catch (SQLException ex) {
			switch (ex.getErrorCode()) {
			case 942:showResult("Cette table ou cette vue n'existe pas.");
			break;
			case 900:showResult("Veuillez saisir la requête correctement !");
			break;
			case 955:showResult("Ce nom est déjà pris.");
			break;
			default: showResult("ECHEC!"+ ex.getMessage());
			break;
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();	
		}


	}

	/** Créé un modèle pour remplir une jTable.
	 * @param rs : ResultSet à partir duquel créer le modèle.
	 * @return un nouveau modèle avec les attributs et les données de la table.
	 */	
	public static DefaultTableModel buildJTable(ResultSet rs) {

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

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new DefaultTableModel(data, columnNames);
	}

}

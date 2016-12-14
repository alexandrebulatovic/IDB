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

import manager.connection.ConnectionManager;
import manager.ddl.DDLManager;

import ddl.DDLController;
import ddl.create.CreateTableGUI;

public class SQLController {

	/** Controleur en cours.*/
	private static SQLController INSTANCE;

	/** IHM pour taper des requetes SQL. */
	private SQLView sql;

	/** Objet pour envoyer des requetes au SGBD. */
	private SQLModel creator;

	/**  Objet pour appeler les methodes execute sql */
	private Statement stat;

	/** Constructeur commun
	 * @param cm : objet ConnectionManager obtenu lors de la connexion
	 */
	public SQLController(){
		INSTANCE = this;
		this.sql = new SQLView();
		this.creator = new SQLModel(ConnectionManager.getInstance());
		Connection conn = this.creator.getConnector();

		try {
			this.stat = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** Retourne le controleur actif si et seulement s'il
	 * existe déjà. Retourne un nouveau controleur sinon.
	 * @return SQLController */
	public static SQLController getInstance()
	{
		if (INSTANCE == null) new SQLController();
		return INSTANCE;
	}


	/** Ouvre l'IHM pour taper du code SQL si et seulement si 
	 * elle n'existe pas, sinon tente de l'afficher au premier plan. */
	public void openSQL()
	{
		this.sql = SQLView.getInstance();
	}

	public void showResult(String res){
		JOptionPane.showMessageDialog(null, res, "Résultat", JOptionPane.INFORMATION_MESSAGE);
	}

	/** Methode pour envoyer la requête au serveur.
	 * @param rq : requête sous forme de chaîne de caractères à envoyer. */
	public void sendSQL(String rq){

		try {
			StringBuilder stringBuilder = new StringBuilder();
			boolean res = stat.execute(rq);

			if (res) { // Si le résultat est un ResultSet

				ResultSet rs = stat.getResultSet() ;
				JTable table = new JTable(buildTableModel(rs));
				JOptionPane.showMessageDialog(null, new JScrollPane(table));

			} else { //  INSERT, UPDATE, or DELETE ou LDD donc on affiche, soit rien, soit le nombre de lignes affectés.
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

	/** Créée un modèle pour la jTable.
	 * @param rs : ResultSet à partir duquel créer le modèle. 
	 */	
	public static DefaultTableModel buildTableModel(ResultSet rs) {

		ResultSetMetaData metaData;


		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		Vector<String> columnNames = new Vector<String>();


		try {
			metaData = rs.getMetaData();
			// recupere le nom des colonnes
			int columnCount = metaData.getColumnCount();
			for (int column = 1; column <= columnCount; column++) {
				columnNames.add(metaData.getColumnName(column));
			}

			// recupere les informations
			while (rs.next()) {
				Vector<Object> vector = new Vector<Object>();
				for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
					vector.add(rs.getObject(columnIndex));
				}
				data.add(vector);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new DefaultTableModel(data, columnNames);

	}

}

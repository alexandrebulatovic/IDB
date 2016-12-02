package sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import connect.ConnectionManager;
import create.CreateTableManager;
import create.CreateTableView;

public class SQLController {

	/** IHM pour taper des requetes SQL. */
	private SQLView sql;

	/** Objet pour envoyer des requetes au SGBD. */
	private SQLModel creator;

	/**  Objet pour appeler les methodes execute sql */
	private Statement stat;

	/** Constructeur commun
	 * @param cm : objet ConnectionManager obtenu lors de la connexion
	 */
	public SQLController(ConnectionManager cm){
		this.sql = new SQLView(this);
		this.creator = new SQLModel(cm);
		Connection conn = this.creator.getConnector();
		try {
			this.stat = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void showResult(String res){
		JOptionPane.showMessageDialog(null, res, "Résultat", JOptionPane.INFORMATION_MESSAGE);
	}

	public void sendSQL(String rq){

		try {
			StringBuilder stringBuilder = new StringBuilder(); // pour les requetes SELECT
			// j'execute la requete
			boolean res = stat.execute(rq);

			if (res) { // SELECT donc on affiche le resultat

				ResultSet rs = stat.getResultSet() ;
				ResultSetMetaData rsmd = rs.getMetaData(); // permet d'avoir des infos sur les colonnes retournees

				for (int i = 1; i <= rsmd.getColumnCount() ; i++) {
					if (i > 1) stringBuilder.append(",  ");
					stringBuilder.append(rsmd.getColumnName(i));
				}
				stringBuilder.append("\n");

				while (rs.next()) {
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						if (i > 1) stringBuilder.append(",  ");
						stringBuilder.append(rs.getString(i));
					}
					stringBuilder.append("\n");  
				}

				showResult(stringBuilder.toString());
			} else { //  INSERT, UPDATE, or DELETE ou LDD donc on affiche, soit rien, soit le nombre de lignes affectés.
				if (rq.contains("INSERT")){
					showResult(stat.getUpdateCount()+ " ligne ajoutée.");
				} else if (rq.contains("DELETE")){
					showResult(stat.getUpdateCount()+" ligne supprimée.");
				} else if (rq.contains("UPDATE")){
					showResult(stat.getUpdateCount()+" ligne mis à jour.");
				}else {
					showResult("Réussite!");
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
			default: showResult("ECHEC!");
			break;
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}


}

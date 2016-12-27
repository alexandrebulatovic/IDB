package sql;

import java.sql.Connection;
import javax.swing.JTable;
import manager.SQLManager;

public class SQLController {

	/* ATTRIBUTS */

	/** IHM pour taper des requetes SQL. */
	private SQLView sql_view;

	/** Objet pour envoyer des requetes au SGBD. */
	private SQLManager sql_manager;

	/* CONSTRUCTEUR */

	/** Initialise le {@code SQLManager}.
	 * @param conn : l'objet Connection résultant de la connexion à la base de données. */
	public SQLController(Connection conn)
	{
		this.sql_manager = new SQLManager(conn);
	}

	/* METHODES*/

	/** Affiche l'IHM pour taper du code SQL en la créant si nécessaire. */
	public void openSQL()
	{
		if (this.sql_view == null) {
			this.sql_view = new SQLView(this);
		} else {
			this.sql_view.setVisible(true);
			this.sql_view.toFront();
		}
	}

	/** Envoie la requête au manager.
	 * @param qry : requête sous forme de chaîne de caractères à envoyer. */
	public void transmitSQL(String qry)
	{
		Object reply = this.sql_manager.sendSQL(qry);
		transmitReply(reply);
	}

	/** Envoie la réponse retournée par la requête SQL à la view.
	 * @param reply : un {@code Object} de la réponse à envoyer.	 */
	private void transmitReply(Object reply) 
	{
		if (reply instanceof String){

			this.sql_view.showReply((String) reply);

		} else if (reply instanceof JTable)	{

			this.sql_view.showTable((JTable) reply);
		}
	}

}

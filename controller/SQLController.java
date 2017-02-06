package controller;

import gui.SQLGUI;

import java.sql.Connection;

import javax.swing.JTable;

import useful.Response;
import manager.sql.SQLManager;

/** Effectue la communication entre la {@code SQLView} et le {@code SQLController}.
 * 
 * @see SQLManager
 * @see SQLGUI
 */
public class SQLController {

	/* ATTRIBUTS */

	/** IHM pour taper des requetes SQL. */
	private SQLGUI sql_view;

	/** Objet pour envoyer des requetes au SGBD. */
	private SQLManager sql_manager;

	/* CONSTRUCTEUR */

	/** Instancie le {@code SQLManager}.
	 * @param conn : l'objet Connection résultant de la connexion à la base de données. */
	public SQLController(Connection conn)
	{
		this.sql_manager = new SQLManager(conn, SQLManager.TYPE_PLAIN_RESULTSET);
	}

	/* METHODES*/

	/** Affiche au premier plan l'IHM pour taper du code SQL,
	 *  la créée si elle n'existe pas. */
	public void openSQL()
	{
		if (this.sql_view == null) {
			this.sql_view = new SQLGUI(this);
		} else {
			this.sql_view.setVisible(true);
			this.sql_view.toFront();
		}
	}

	/** Demande au {@code SQLManager} d'exécuter la requête SQL.
	 * @param qry : requête sous forme de {@code String} à transmettre au SGBD. */
	public void transmitQuery(String qry)
	{
		Object reply = this.sql_manager.sendSQL(qry);
		transmitReply(reply);
	}

	/** Demande à {@code SQLView} d'afficher un message d'information ou 
	 * une {@code JTable} en fonction de la réponse du {@code SQLManager}.
	 * @param reply : la réponse du serveur, un {@code String} ou un {@code JTable}. */
	private void transmitReply(Object reply) 
	{
		if (reply instanceof Response)
			this.sql_view.showReply(((Response) reply).getMessage());

		else if (reply instanceof JTable)
			this.sql_view.showTable((JTable) reply);
	}

}

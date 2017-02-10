package controller;

import facade.CRUD_SQL_Facade;
import gui.SQLGUI;

import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JTable;

import useful.Response;
import manager.sql.SQLManager;

/** Effectue la communication entre la vue et le manager du SGBD.
 * 
 * @see SQLManager
 * @see SQLGUI
 */
public class SQLController {

	/* ATTRIBUTS */

	/** IHM pour taper des requetes SQL. */
	private SQLGUI sql_view;

	private CRUD_SQL_Facade crud_facade;

	/* CONSTRUCTEUR */

	/** Instancie le {@code SQLManager}.
	 * @param conn : l'objet Connection résultant de la connexion à la base de données. 
	 * @param facade : null interdit.
	 * */
	public SQLController(Connection conn, CRUD_SQL_Facade facade)
	{
		this.crud_facade = facade;
		this.crud_facade.optimizeStatement(SQLManager.TYPE_PLAIN_RESULTSET);
	}


	/* METHODES*/
	/** Affiche au premier plan l'IHM pour taper du code SQL,
	 *  la créée si elle n'existe pas. */
	public void openSQL()
	{
		if (this.sql_view == null) {
			this.sql_view = new SQLGUI(this);
		} else {
			showGUI(this.sql_view);
		}
	}

	/** Demande au {@code SQLManager} d'exécuter la requête SQL.
	 * @param qry : requête sous forme de {@code String} à transmettre au SGBD. */
	public void transmitQuery(String qry)
	{

		Object reply = this.crud_facade.transmitQuery(qry);
		transmitReply(reply);
	}

	/** Demande à {@code SQLView} d'afficher un message d'information ou 
	 * une {@code JTable} en fonction de la réponse du serveur.
	 * @param reply : la réponse du serveur, un {@code String} ou un {@code JTable}. */
	private void transmitReply(Object reply) 
	{
		Object replyParsed = this.crud_facade.transmitReply(reply);

		if (replyParsed instanceof JTable)
			this.sql_view.showTable((JTable) reply); // on affiche la JTable retournée

		else if (replyParsed instanceof Response) 
		{
			if (!((Response) replyParsed).hasSuccess())  // si c'est une erreur
				this.sql_view.showError(((Response) replyParsed).getMessage()); // on affiche le message d'erreur
			else
				this.sql_view.showReply(((Response) replyParsed).getMessage()); // on affiche une notification à l'utilisateur
		}
	}


	/**
	 * Affiche une fenêtre au premier plan.
	 * 
	 * @param gui : une IHM, null interdit.
	 */
	private static void showGUI(JFrame gui)
	{
		gui.setVisible(true);
		gui.toFront();
	}
}
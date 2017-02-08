package controller;

import facade.CRUDFacade;
import gui.SQLGUI;

import java.sql.Connection;
import java.sql.SQLException;

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

	/** Objet pour envoyer des requetes au SGBD. */
	private SQLManager sql_manager;

	private CRUDFacade crud_facade;

	/* CONSTRUCTEUR */

	/** Instancie le {@code SQLManager}.
	 * @param conn : l'objet Connection résultant de la connexion à la base de données. 
	 * @param facade : null interdit.
	 * */
	public SQLController(Connection conn, CRUDFacade facade)
	{
		this.crud_facade = facade;
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
		if (reply instanceof Response) {

			if (!((Response) reply).hasSuccess()) { // si c'est une erreur

				Exception exception = this.sql_manager.getLastException(); // on récupère l'exception
				String errorMsg = this.crud_facade.generateErrorMsg(exception); // on crée un message d'erreur

				this.sql_view.showError(errorMsg); // on affiche le message d'erreur

			} else {

				String msg = ((Response) reply).getMessage();

				this.sql_view.showReply(msg); // on affiche une notification à l'utilisateur

			}
		}

		else if (reply instanceof JTable)
			this.sql_view.showTable((JTable) reply); // on affiche la JTable retournée
	}

}

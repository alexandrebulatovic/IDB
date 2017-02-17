package controller;

import java.sql.SQLException;

import javax.swing.JFrame;

import facade.SQLFacade;
import gui.SQLGUI;
import manager.sql.SQLManager;
import useful.DialogBox;

/**
 * Un objet qui effectue la communication avec le {@link SQLManager}.
 * <P>
 * L'envoi d'une requête s'effectue via la méthode {@code sendQuery}. Le résultat est affiché
 * automatiquement sous la forme d'une boîte de dialogue.
 */
public class SQLController {

	/* ATTRIBUTS */

	private SQLGUI sqlView;

	private SQLFacade sqlFacade;

	/* ------------------------------------------------------------------------ */

	/* CONSTRUCTEUR */

	/**
	 * Construit un objet {@code SQLController} avec un {@code ResultSet} fixe (= son curseur est 
	 * seulement séquentiel) mais optimisé par défaut pour exécuter les requêtes SQL.
	 * @param facade : un objet {@code SQLFacade} permettant d'utiliser le {@link SQLManager}.
	 */
	public SQLController(SQLFacade facade)
	{
		this.sqlFacade = facade;

		try 
		{
			this.sqlFacade.setStatementType(SQLManager.TYPE_PLAIN_RESULTSET);
		} 
		catch (IllegalArgumentException exception) 
		{
			System.err.println(exception.getMessage());
			exception.printStackTrace();
		}
		catch (SQLException exception)
		{
			String msgException = this.sqlFacade.generateErrorMessage(exception);
			DialogBox.showError(msgException);
		}
	}

	/* ------------------------------------------------------------------------ */

	/* METHODES*/

	/**
	 * Affiche à l'utilisateur l'objet {@code SQLGUI}.
	 * @see SQLGUI
	 */
	public void openSQLGUI()
	{
		if (this.sqlView == null)
			this.sqlView = new SQLGUI(this);

		else 
			showGUI(this.sqlView);
	}

	/**
	 * Exécute une requête SQL et affiche le résultat 
	 * de cette requête dans une boîte de dialogue.
	 * @param query : une requête SQL à exécuter.
	 */
	public void sendQuery(String query)
	{
		boolean isJTable;
		try 
		{
			isJTable = this.sqlFacade.sendQuery(query);

			if (isJTable)
				DialogBox.showTable(this.sqlFacade.getGeneratedJTable(), "Résultat");
			else
				DialogBox.showMessage(this.sqlFacade.getGeneratedReply(), "Résultat");
		}
		catch (IllegalArgumentException | NullPointerException exception)
		{
			System.err.println(exception.getMessage());
			exception.printStackTrace();
		}
		catch (SQLException exception)
		{
			String msgException = this.sqlFacade.generateErrorMessage(exception);
			DialogBox.showError(msgException);
		}
	}

	/**
	 * Affiche une fenêtre au premier plan.
	 * @param gui : une IHM, {@code null} interdit.
	 */
	private static void showGUI(JFrame gui)
	{
		gui.setVisible(true);
		gui.toFront();
	}
}
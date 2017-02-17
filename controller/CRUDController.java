package controller;

import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTable;

import facade.AbstractDDLCRUDFacade;
import facade.CRUDFacade;
import gui.CRUDGUI;
import manager.sql.SQLManager;
import useful.DialogBox;
import useful.Response;
import useful.ResponseData;

/**
 * Un objet qui effectue la communication avec le {@link SQLManager} à partir d'opérations
 * effectuées sur un objet IHM {@code CRUDGUI}.
 */
public class CRUDController {

	/* ATTRIBUTS */

	private CRUDGUI crudView;

	private CRUDFacade crudFacade;


	/* ------------------------------------------------------------------------ */

	/* CONSTRUCTEUR */

	/**
	 * Construit un objet {@code CRUDController} avec un {@code ResultSet} dynamique.
	 * @param facade : un objet {@code CRUDFacade} permettant d'utiliser le {@link SQLManager}.
	 */
	public CRUDController(CRUDFacade facade)
	{
		this.crudFacade = facade;

		try 
		{
			this.crudFacade.setStatementType(SQLManager.TYPE_UPDATABLE_RESULTSET);
		} 
		catch (IllegalArgumentException exception) 
		{
			System.err.println(exception.getMessage());
			exception.printStackTrace();
		}
		catch (SQLException exception)
		{
			String msgException = this.crudFacade.generateErrorMessage(exception);
			DialogBox.showError(msgException);
		}
	}

	/* ---------------------------------------------------------------------------- */

	/* METHODES */

	/**
	 * Affiche à l'utilisateur l'objet {@code CRUDGUI}.
	 * @see CRUDGUI
	 */
	public void openCRUDGUI()
	{
		if (this.crudView == null) 
			this.crudView = new CRUDGUI(this);

		else
			showGUI(this.crudView);
	}

	/** @see AbstractDDLCRUDFacade#getTables() */
	public ResponseData<String> getTables()
	{
		return this.crudFacade.getTables();
	}

	/**
	 * Permet de récupérer un objet {@code JTable} à partir du nom d'une table stockée dans la base de données.
	 * @param tableName : nom de la table à récupérer.
	 * @return un objet {@code ResponseData} contenant soit une {@code JTable}, soit un message d'erreur en cas d'échec.
	 * @see ResponseData
	 */
	public ResponseData<JTable> getJTableFromTableName(String tableName)
	{
		JTable jTable;
		ResponseData<JTable> responseData;

		try 
		{
			jTable = this.crudFacade.getJTableFromTableName(tableName);
		} 
		catch (SQLException exception) 
		{
			String msgException = this.crudFacade.generateErrorMessage(exception);

			responseData = new ResponseData<JTable>(false, msgException);
			return responseData;
		}

		responseData = new ResponseData<JTable>(true);
		responseData.add(jTable);

		return responseData;
	}

	/**
	 * Supprime un tuple de la table affichée actuellement.
	 * @param index : index du tuple à supprimer tel que affiché dans la {@code JTable} (commence à 0).
	 * @return un objet {@code Response} positif ou négatif.
	 * @see Response
	 */
	public Response deleteTuple(int index) 
	{
		Response response;

		try 
		{
			this.crudFacade.deleteTuple(index);

			response = new Response(true);
		} 
		catch (SQLException exception) 
		{
			response = this.generateErrorResponse(exception);
		}

		return response;
	}

	/**
	 * Insère dans la base de données le tuple situé à la {@code index}-ième ligne dans la {@code JTable}.
	 * @param index : position du tuple à ajouter. 
	 * @param tableName : nom de la table concernée.
	 * @return un objet {@code Response} positif ou négatif.
	 * @see Response
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Response addTuple(int index, String tableName) 
	{
		// on récupère le modèle de données de la JTable
		Vector<Vector> dataVector = this.crudView.getTableModel().getDataVector(); 

		// puis on récupere la ligne concernée
		Vector<String> newRow =  (Vector<String>) dataVector.elementAt(index);

		Response response;

		try 
		{
			this.crudFacade.addTuple(newRow);
			response = new Response(true);
		} 
		catch (UnsupportedOperationException | IllegalArgumentException | SQLException exception) 
		{
			response = this.generateErrorResponse(exception);
		}

		return response;
	}

	/** Met à jour un champ d'un tuple dans la base de données.
	 * @param index : numéro d'index du tuple (commence à 0).
	 * @param column : numéro d'index de la colonne du champ à mettre à jour (commence à 0).
	 * @param value : nouvelle valeur du champ.
	 * @return un objet {@code Response} positif ou négatif.
	 * @see Response
	 */
	public Response updateTuple(int index, int column, String value)
	{
		Response response;

		try 
		{
			this.crudFacade.updateTuple(index, column, value);
			response = new Response(true);
		} 
		catch (UnsupportedOperationException | IllegalArgumentException | SQLException exception) 
		{
			response = this.generateErrorResponse(exception);
		}

		return response;
	}

	/**
	 *  Analyse une {@code Exception} pour en faire un message d'erreur.
	 * @param exception : un objet {@code Exception} à analyser. 
	 * @return un message explicite de l'erreur.
	 */
	private String generateErrorMessage(Exception exception) 
	{
		if (exception instanceof SQLException)
			return this.crudFacade.generateErrorMessage((SQLException)exception);

		else if (exception instanceof NumberFormatException)
			return "La valeur entrée est incompatible avec le type de l'attribut.";

		else
			return exception.getMessage();
	}

	/**
	 * Analyse une {@code Exception} pour en faire un objet {@code Response} indiquant une erreur.
	 * @param exception : un objet {@code Exception} à analyser.
	 * @return un objet {@code Response} négatif avec un message d'erreur.
	 */
	private Response generateErrorResponse(Exception exception) 
	{
		String msgException = this.generateErrorMessage(exception);

		return new Response(false, msgException);
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
package controller;

import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTable;

import facade.AbstractDDLCRUDFacade;
import facade.SQLFacade;
import gui.CRUDGUI;
import manager.sql.SQLManager;
import useful.DialogBox;
import useful.Response;
import useful.ResponseData;

/**
 * S'occupe de toutes les tâches demandées par la fenêtre qui
 * permet les opérations CRUD.
 */
public class CRUDController 
{
	/* ATTRIBUTS */

	/** IHM du CRUD.*/
	private CRUDGUI crudView;

	/** Facade permettant l'accès au manager.*/
	private SQLFacade crudFacade;

	/**
	 * Constructeur associant une facade au {@code CRUDController}.
	 * @param facade : null interdit.
	 */
	public CRUDController(SQLFacade facade)
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
	 * Ouvre l'IHM de CRUD si et seulement si elle n'existe pas, 
	 * sinon tente de l'afficher au premier plan.
	 */
	public void openCRUDGUI() {
		if (this.crudView == null) {
			this.crudView = new CRUDGUI(this);
		}
		else{
			showGUI(this.crudView);
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
	 * Insère dans la base de données le tuple situé à {@code index} dans la {@code JTable}.
	 * @param index : position du tuple à ajouter. 
	 * @param tableName : nom de la table concernée.
	 * @return un objet {@code Response}.
	 * @see Response
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Response addTuple(int index, String tableName) 
	{
		Vector<Vector> dataVector = this.crudView.getTableModel().getDataVector();

		Vector<String> row_to_add =  (Vector<String>) dataVector.elementAt(index); // on récupere la ligne concernée

		Response response;

		try 
		{
			this.crudFacade.addTuple(row_to_add);

			response = new Response(true);
			return response;
		} 
		catch (UnsupportedOperationException | IllegalArgumentException exception) 
		{
			System.err.println(exception.getMessage());
			exception.printStackTrace();
			response = new Response(false);
		}
		catch (SQLException exception)
		{
			response = this.generateErrorResponse(exception);
		}

		return response;
	}

	/** Met à jour une valeur d'un tuple dans la base de données.
	 * @param index : position du tuple.
	 * @param column : colonne de la valeur.
	 * @param updateBuffer : nouvelle valeur.
	 * @return un objet {@code Response}.
	 * @see Response
	 */
	public Response updateTuple(int index, int column, String updateBuffer)
	{
		Response response;

		try 
		{
			this.crudFacade.updateTuple(index, column, updateBuffer);

			response = new Response(true);
			return response;
		} 
		catch (UnsupportedOperationException | IllegalArgumentException exception) 
		{
			System.err.println(exception.getMessage());
			exception.printStackTrace();
			response = new Response(false);
		}
		catch (SQLException exception)
		{
			response = this.generateErrorResponse(exception);
		}

		return response;
	}
	
	/**
	 *  Analyse une {@code Exception} pour en faire un message d'erreur.
	 * @param exception : un objet {@code Exception} à parser. 
	 * @return un message explicite de l'erreur.
	 */
	public String generateErrorMessage(Exception exception) 
	{
		if (exception instanceof SQLException)
			return this.crudFacade.generateErrorMessage((SQLException)exception);

		else if (exception instanceof NumberFormatException)
			return "La valeur entrée est incompatible avec le type de l'attribut.";

		else
			return exception.getMessage();
	}
	
	/**
	 * Parse une {@code Exception} pour en faire un objet {@code Response} indiquant une erreur.
	 * @param exception : un objet {@code Exception} à parser.
	 * @return un objet {@code Response} négatif.
	 */
	public Response generateErrorResponse(Exception exception) 
	{
		String msgException = this.generateErrorMessage(exception);

		return new Response(false, msgException);
	}
}
package controller;

import facade.CRUD_SQL_Facade;
import gui.CRUDGUI;
import manager.sql.SQLManager;

import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
	private CRUD_SQL_Facade crudFacade;

	/**
	 * Constructeur associant une facade au {@code CRUDController}.
	 * @param facade : null interdit.
	 */
	public CRUDController(CRUD_SQL_Facade facade)
	{
		this.crudFacade = facade;
		this.crudFacade.setStatement(SQLManager.TYPE_UPDATABLE_RESULTSET);
	}


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

	/**
	 * @return la liste des tables disponibles.
	 */
	public ResponseData<String> getTables()
	{
		return this.crudFacade.getTables();
	}

	/**
	 * Permet de récupérer un objet {@code JTable} à partir du nom d'une table stockée dans la base de données.
	 * @param tableName : nom de la table à récupérer.
	 * @return un objet {@code ResponseData} contenant une {@code JTable} ou un message d'erreur.
	 * @see ResponseData
	 */
	public ResponseData<JTable> getJTableFromTableName(String tableName)
	{
		return this.crudFacade.getJTableFromTableName(tableName);
	}

	/**
	 * Supprime le tuple situé à {@code index} de la base de données.
	 * @param index : position du tuple à supprimer.
	 * @return un objet {@code Response}.
	 * @see Response
	 */
	public Response deleteRow(int index) {
		return this.crudFacade.deleteRow(index);
	}

	/**
	 * Insère dans la base de données le tuple situé à {@code index} dans la {@code JTable}.
	 * @param index : position du tuple à ajouter. 
	 * @param tableName : nom de la table concernée.
	 * @return un objet {@code Response}.
	 * @see Response
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Response addRow(int index, String tableName) 
	{
		Vector<Vector> dataVector = this.crudView.getTableModel().getDataVector();

		Vector<String> row_to_add =  (Vector<String>) dataVector.elementAt(index); // on récupere la ligne concernée

		return this.crudFacade.addTuple(row_to_add);
	}

	/** Met à jour une valeur d'un tuple dans la base de données.
	 * @param index : position du tuple.
	 * @param column : colonne de la valeur.
	 * @param updateBuffer : nouvelle valeur.
	 * @return un objet {@code Response}.
	 * @see Response
	 */
	public Response updateRow(int index, int column, String updateBuffer)
	{
		return this.crudFacade.updateRow(index, column, updateBuffer);
	}
}
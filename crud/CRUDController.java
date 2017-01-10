package crud;

import java.sql.Connection;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JTable;
import manager.DDLManager;
import manager.SQLManager;
import useful.ResponseData;

public class CRUDController 
{
	/* ATTRIBUTS */

	/** IHM du CRUD.*/
	private CRUDView crud_view;

	private DDLManager ddl_manager;

	private SQLManager sql_manager;



	/* METHODES */

	public CRUDController(Connection connection) 
	{
		this.ddl_manager = new DDLManager(connection);
		this.sql_manager = new SQLManager(connection, SQLManager.TYPE_UPDATABLE_RESULTSET);
	}

	/**
	 * Retourne une réponse personnalisée contenant le nom des tables
	 * de la base, si et seulement si (ces dernières existent et 
	 * il n'y a pas eu d'exceptions).
	 * 
	 * @return CustomizedResponseWithData
	 */
	public ResponseData<String> getTables()
	{
		return this.ddl_manager.getTables();
	}

	/**
	 * Ouvre l'IHM de CRUD si et seulement si 
	 * elle n'existe pas, sinon tente de l'afficher au premier plan.
	 */
	public void openCRUDGUI() {
		if (this.crud_view == null) {
			this.crud_view = new CRUDView(this);
		}
		else{
			showGUI(this.crud_view);
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
	 * Demande au {@code SQLManager} la {@code JTable} correspondant au nom de la table demandée.
	 * @param tableName : nom de la table demandée.
	 * @return la {@code JTable} correspondante.
	 */
	public JTable requestTable(String tableName) 
	{
		return this.sql_manager.requestJTable(tableName);
	}

	/**
	 * Supprime le tuple situé à {@code index} de la base de données.
	 * @param index : position du tuple à supprimer.
	 */
	public void deleteRow(int index) {
		System.out.println(this.sql_manager.removeTuple(index));
	}

	/**
	 * Insère dans la base de données le tuple situé à {@code index} dans la {@code JTable}.
	 * @param index : position du tuple à ajouter. 
	 * @param tableName : nom de la table concernée.
	 */
	public void addRow(int index, String tableName) 
	{
		Vector dataVector = this.crud_view.getTableModel().getDataVector();
		Vector<Object> row_to_add =  (Vector<Object>) dataVector.get(index);

		this.sql_manager.addTuple(row_to_add);
	}

	/** Met à jour une valeur d'un tuple.
	 * @param index : position du tuple.
	 * @param column : colonne de la valeur.
	 * @param updateBuffer : nouvelle valeur.
	 */
	public void updateRow(int index, int column, Object updateBuffer)
	{
		System.out.println(this.sql_manager.updateTuple(index, column, updateBuffer));
	}
}
package facade;

import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import useful.Response;
import useful.ResponseData;
import business.TableSet;
import manager.connection.I_ConnectionManager;
import manager.connection.MySQLConnectionManager;
import manager.ddl.AbstractDLLManager;
import manager.ddl.I_DDLManager;
import manager.sql.SQLManager;

public class CRUDFacade 
extends AbstractDDLCRUDFacade
{

	/** Structure représentant une connexion à un SGBD. */
	private I_ConnectionManager connector;

	private SQLManager sql_manager;

	public CRUDFacade(I_DDLManager manager, I_ConnectionManager connector, TableSet tables) {
		super(manager,tables);
		this.connector = connector;
		this.sql_manager = new SQLManager(this.connector.getConnection(), SQLManager.TYPE_UPDATABLE_RESULTSET);
	}

	/**
	 * Demande au {@code SQLManager} un modèle de données de {@code JTable}.
	 * @param tableName : nom de la table demandée.
	 * @return un objet {@code DefaultTableModel} contenant les données de la table.
	 */
	public DefaultTableModel requestTable(String tableName) 
	{
		return this.sql_manager.requestTableModel(tableName);
	}

	/**
	 * Supprime le tuple situé à {@code index} de la base de données.
	 * @param index : position du tuple à supprimer.
	 * @return un objet {@code Response}.
	 * @see Response
	 */
	public Response deleteRow(int index) {
		if (!this.sql_manager.removeTuple(index)) {
			SQLException sqlException = this.sql_manager.getSqlException();
			String msgException = generateErrorMsg(sqlException);
			return new Response(false, msgException);
		} else
			return new Response(true);
	}


	/** Met à jour une valeur d'un tuple dans la base de données.
	 * @param index : position du tuple.
	 * @param column : colonne de la valeur.
	 * @param updateBuffer : nouvelle valeur.
	 * @return un objet {@code Response}.
	 * @see Response
	 */
	public Response updateRow(int index, int column, Object updateBuffer)
	{
		if (!this.sql_manager.updateTuple(index, column, updateBuffer)) {
			SQLException sqlException = this.sql_manager.getSqlException();
			String msgException = generateErrorMsg(sqlException);
			return new Response(false, msgException);
		} else
			return new Response(true);
	}

	/** Insère un nouveau tuple dans la base de données.
	 * @param row_to_add : objet {@code Vector} représentant les données à insérer dans l'ordre.
	 * @return un objet {@code Response}.
	 * @see Response
	 */
	public Response addTuple(Vector<Object> row_to_add) {
		if (!this.sql_manager.addTuple(row_to_add)) {
			SQLException sqlException = this.sql_manager.getSqlException();
			String msgException = generateErrorMsg(sqlException);
			return new Response(false, msgException);
		} else
			return new Response(true);
	}

	/** {@link I_ConnectionManager#errorMessage(SQLException)} */
	public String generateErrorMsg(SQLException sqlException) {
		return connector.errorMessage(sqlException);
	}


}
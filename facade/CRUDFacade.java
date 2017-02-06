package facade;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import useful.ResponseData;
import business.TableSet;
import manager.connection.I_ConnectionManager;
import manager.ddl.I_DDLManager;
import manager.sql.SQLManager;

public class CRUDFacade extends AbstractBusinessDDLFacade{

	private I_ConnectionManager connector;

	private SQLManager sql_manager;

	public CRUDFacade(I_DDLManager manager, I_ConnectionManager connector, TableSet tables) {
		super(manager,tables);
		this.connector = connector;
		this.sql_manager = new SQLManager(this.connector.getConnection(), SQLManager.TYPE_UPDATABLE_RESULTSET);
	}

	/**
	 * Demande au {@code SQLManager} un modèle de {@code JTable} avec les données correspondant 
	 * au nom de la table demandée.
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
	 * @return "OK" si la suppression a réussie, un message d'erreur sinon.
	 */
	public String deleteRow(int index) {
		return (this.sql_manager.removeTuple(index));
	}


	/** Met à jour une valeur d'un tuple dans la base de données.
	 * @param index : position du tuple.
	 * @param column : colonne de la valeur.
	 * @param updateBuffer : nouvelle valeur.
	 * @return "OK" si la modification a réussie, un message d'erreur sinon.
	 */
	public String updateRow(int index, int column, Object updateBuffer)
	{
		return this.sql_manager.updateTuple(index, column, updateBuffer);
	}

	/** Insère un nouveau tuple dans la base de données.
	 * @param row_to_add : {@code Vector} représentant les données à insérer dans l'ordre.
	 * @return "OK" si l'insertion réussie, sinon un message d'erreur.
	 */
	public String addTuple(Vector<Object> row_to_add) {
		return (this.sql_manager.addTuple(row_to_add));
	}
}

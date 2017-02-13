package facade;

import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import useful.Response;
import business.TableSet;
import manager.connection.I_ConnectionManager;

import manager.ddl.I_DDLManager;
import manager.sql.SQLManager;

public class CRUD_SQL_Facade 
extends AbstractDDLCRUDFacade
{

	/** Structure représentant une connexion à un SGBD. */
	private I_ConnectionManager connector;

	private SQLManager sql_manager;

	public CRUD_SQL_Facade(I_DDLManager manager, I_ConnectionManager connector, TableSet tables) {
		super(manager,tables);
		this.connector = connector;
		this.sql_manager = new SQLManager(this.connector.getConnection());


		if (!this.sql_manager.initStatement(SQLManager.TYPE_PLAIN_RESULTSET))
		{
			Exception exception = this.sql_manager.getLastException();
			String msgException = generateErrorMsg(exception);
			System.out.println(msgException);
		}
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
			Exception exception = this.sql_manager.getLastException();
			String msgException = generateErrorMsg(exception);
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
			Exception exception = this.sql_manager.getLastException();
			String msgException = generateErrorMsg(exception);
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

		if (!this.sql_manager.addTuple(row_to_add)) 
		{
			Exception exception = this.sql_manager.getLastException();
			String msgException = generateErrorMsg(exception);
			
			return new Response(false, msgException);
		} 
		else
			return new Response(true);
	}

	/**
	 *  Parse l'objet {@code Exception} levée par le système. 
	 * @param exception {@code Exception} à parser. 
	 * @return un message explicite de l'erreur.
	 */
	private String generateErrorMsg(Exception exception) {
		if (exception instanceof SQLException)
			return connector.errorMessage((SQLException)exception);
		else
			return exception.getMessage();
	}



	/**
	 * Optimise le {@code SQLManager} selon le type d'opération à effectuer.
	 * @param statementTypeRequired : constantes parmi {@code SQLManager.TYPE_UPDATABLE_RESULTSET} pour avoir un {@code ResultSet} dynamique, 
	 * {@code SQLManager.TYPE_PLAIN_RESULTSET} pour avoir un {@code ResultSet} fixe et optimisé.
	 */
	public void setStatement(int statementTypeRequired) {
		int statementType = this.sql_manager.getStatementType();

		if (statementType != statementTypeRequired)
			this.sql_manager.setStatementType(statementTypeRequired);
	}


	/** Demande au {@code SQLManager} d'exécuter la requête SQL.
	 * @param qry : requête sous forme de {@code String} à transmettre au SGBD. 
	 * @return un {@code JTable} si c'est un SELECT, sinon un {@code String} de la réponse du serveur. */
	public Object transmitQuery(String qry) {
		return this.sql_manager.sendSQL(qry);
	}

	/** Demande à {@code SQLView} d'afficher un message d'information ou 
	 * une {@code JTable} en fonction de la réponse du {@code SQLManager}.
	 * @param reply : la réponse du serveur, un {@code String} ou un {@code JTable}. */
	public Object transmitReply(Object reply) 
	{
		if (reply instanceof Response) {

			if (!((Response) reply).hasSuccess()) { // si c'est une erreur

				Exception lastException = this.sql_manager.getLastException();
				String errorMsg = this.generateErrorMsg(lastException);
				return new Response(false, errorMsg); // on affiche le message d'erreur

			} else {

				String msg = ((Response) reply).getMessage();
				return new Response(true, msg); // on affiche une notification à l'utilisateur
			}
		}
		else if (reply instanceof JTable)
			return ((JTable) reply); // on affiche la JTable retournée

		return reply;
	}
}
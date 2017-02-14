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
	 * Génère un objet {@code Response} à l'aide du {@code SQLManager} en fonction 
	 * du résultat d'une opération. 
	 * @param reply : résultat d'une opération sur un SDBD.
	 * @return un objet {@code Response} complété.
	 */
	private Response generateResponse(boolean reply) {

		if (!reply)
		{
			Exception exception = this.sql_manager.getLastException();
			String msgException = generateErrorMsg(exception);

			return new Response(false, msgException);
		} 
		else
			return new Response(true);
	}

	/**
	 * Supprime le tuple situé à la {@code index}-ième ligne de la table courante.
	 * @param index : position du tuple à supprimer.
	 * @return un objet {@code Response}.
	 * @see Response
	 */
	public Response deleteRow(int index) {
		return generateResponse(this.sql_manager.removeTuple(index));
	}

	/** Met à jour une valeur d'un tuple dans la base de données.
	 * @param index : position du tuple à mettre à jour.
	 * @param column : colonne à mettre à jour.
	 * @param updateBuffer : nouvelle valeur.
	 * @return un objet {@code Response}.
	 * @see Response
	 */
	public Response updateRow(int index, int column, String updateBuffer) {
		return generateResponse(this.sql_manager.updateTuple(index, column, updateBuffer));
	}

	/** Insère un nouveau tuple dans la table courante.
	 * @param newRow : un objet {@code Vector} représentant les données (pouvant être NULL) à insérer dans le bon ordre.
	 * @return un objet {@code Response}.
	 * @see Response
	 */
	public Response addTuple(Vector<String> newRow) {
		return generateResponse(this.sql_manager.addTuple(newRow));
	}

	/**
	 *  Parse l'objet {@code Exception} levée par le système. 
	 * @param exception {@code Exception} à parser. 
	 * @return un message explicite de l'erreur.
	 */
	private String generateErrorMsg(Exception exception) 
	{
		if (exception instanceof SQLException)
			return connector.errorMessage((SQLException)exception);

		else if (exception instanceof NumberFormatException)
			return "La valeur entrée est incompatible avec le type de l'attribut.";

		else
			return exception.getMessage();
	}

	/**
	 * Optimise le {@code SQLManager} selon le type d'opération à effectuer.
	 * @param statementTypeRequired : constantes parmi {@code SQLManager.TYPE_UPDATABLE_RESULTSET} pour avoir un {@code ResultSet} dynamique, 
	 * {@code SQLManager.TYPE_PLAIN_RESULTSET} pour avoir un {@code ResultSet} fixe et optimisé.
	 */
	public void setStatement(int statementTypeRequired) {
		try {
			this.sql_manager.setStatementType(statementTypeRequired);
		} catch (SQLException e) {
			System.out.println(generateErrorMsg(e));
		}
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
package facade;

import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JTable;

import business.TableSet;
import manager.connection.I_ConnectionManager;
import manager.ddl.I_DDLManager;
import manager.sql.SQLManager;
import useful.DialogBox;

/**
 * Un objet permettant d'accéder aux fonctionnalités du {@link SQLManager} sans
 * le manipuler directement. 
 */
public class SQLFacade extends AbstractDDLCRUDFacade
{

	/** Structure représentant une connexion à un SGBD. */
	private I_ConnectionManager connector;

	private SQLManager sql_manager;

	public SQLFacade(I_DDLManager manager, I_ConnectionManager connector, TableSet tables) 
	{
		super(manager, tables);
		this.connector = connector;

		try 
		{
			this.sql_manager = new SQLManager(this.connector.getConnection(), SQLManager.TYPE_PLAIN_RESULTSET);
		} 
		catch (IllegalArgumentException | NullPointerException exception) 
		{
			System.err.println(exception.getMessage());
			exception.printStackTrace();
		}
		catch (SQLException exception)
		{
			String msgException = generateErrorMessage(exception);
			DialogBox.showError(msgException);
		}
	}

	/** @see SQLManager#getJTableFromTableName(String) */
	public JTable getJTableFromTableName(String tableName) throws SQLException {
		return this.sql_manager.getJTableFromTableName(tableName);
	}

	/** @see SQLManager#deleteTuple(int) */
	public void deleteTuple(int index) throws SQLException {
		this.sql_manager.deleteTuple(index);
	}

	/** @see SQLManager#updateTuple(int, int, String) */
	public void updateTuple(int index, int column, String value) 
			throws UnsupportedOperationException, IllegalArgumentException, SQLException {
		this.sql_manager.updateTuple(index, column, value);
	}

	/** @see SQLManager#addTuple(Vector) */
	public void addTuple(Vector<String> newRow) 
			throws UnsupportedOperationException, IllegalArgumentException, SQLException {
		this.sql_manager.addTuple(newRow);
	}

	/** @see SQLManager#setStatementType(int) */
	public void setStatementType(int statementTypeRequired) 
			throws IllegalArgumentException, SQLException {
		this.sql_manager.setStatementType(statementTypeRequired);
	}

	/** @see SQLManager#sendQuery(String) */
	public boolean sendQuery(String query) 
			throws IllegalArgumentException, NullPointerException, SQLException {
		return this.sql_manager.sendQuery(query);
	}

	public JTable getGeneratedJTable() {
		return this.sql_manager.getGeneratedJTable();
	}

	public String getGeneratedReply() {
		return this.sql_manager.getGeneratedReply();
	}
	
	/** @see I_ConnectionManager#generateErrorMessage(SQLException) */
	public String generateErrorMessage(SQLException exception) {
		return this.connector.generateErrorMessage(exception);
	}
}
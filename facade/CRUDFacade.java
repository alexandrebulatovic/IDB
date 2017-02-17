package facade;

import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JTable;

import business.TableSet;
import manager.connection.I_ConnectionManager;
import manager.ddl.I_DDLManager;
import manager.sql.SQLManager;

/**
 * Un objet permettant d'accéder aux fonctionnalités du {@link SQLManager} sans
 * le manipuler directement.
 */
public class CRUDFacade 
extends AbstractDDLCRUDFacade
{

	/* ATTRIBUTS */

	/** Structure représentant une connexion à un SGBD. */
	private I_ConnectionManager connector;

	private SQLManager sqlManager;


	/* ----------------------------------------------------------------- */

	/* CONSTRUCTEUR */

	public CRUDFacade
	(I_DDLManager manager, 
			I_ConnectionManager connector, 
			TableSet tables, 
			SQLManager sqlManager) 
	{
		super(manager, tables);
		this.connector = connector;
		this.sqlManager = sqlManager;
	}

	/* ----------------------------------------------------------------- */

	/* METHODES */

	/** @see SQLManager#getJTableFromTableName(String) */
	public JTable getJTableFromTableName(String tableName) throws SQLException {
		return this.sqlManager.getJTableFromTableName(tableName);
	}

	/** @see SQLManager#deleteTuple(int) */
	public void deleteTuple(int index) throws SQLException {
		this.sqlManager.deleteTuple(index);
	}

	/** @see SQLManager#updateTuple(int, int, String) */
	public void updateTuple(int index, int column, String value) 
			throws UnsupportedOperationException, IllegalArgumentException, SQLException {
		this.sqlManager.updateTuple(index, column, value);
	}

	/** @see SQLManager#addTuple(Vector) */
	public void addTuple(Vector<String> newRow) 
			throws UnsupportedOperationException, IllegalArgumentException, SQLException {
		this.sqlManager.addTuple(newRow);
	}

	/** @see SQLManager#setStatementType(int) */
	public void setStatementType(int statementTypeRequired) 
			throws IllegalArgumentException, SQLException {
		SQLManager.setStatementType(statementTypeRequired);
	}

	/** @see I_ConnectionManager#generateErrorMessage(SQLException) */
	public String generateErrorMessage(SQLException exception) {
		return this.connector.generateErrorMessage(exception);
	}
}
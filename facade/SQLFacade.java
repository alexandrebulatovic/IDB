package facade;

import java.sql.SQLException;

import javax.swing.JTable;

import business.TableSet;
import manager.connection.I_ConnectionManager;
import manager.ddl.I_DDLManager;
import manager.sql.SQLManager;


/**
 * Un objet permettant d'accéder aux fonctionnalités du {@link SQLManager} sans
 * le manipuler directement. 
 */
public class SQLFacade 
extends AbstractDDLCRUDFacade
{

	/* ATTRIBUTS */

	/** Structure représentant une connexion à un SGBD. */
	private I_ConnectionManager connector;

	private SQLManager sqlManager;

	/* ----------------------------------------------------------------- */

	/* CONSTRUCTEUR */

	public SQLFacade
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

	/** @see SQLManager#setStatementType(int) */
	public void setStatementType(int statementTypeRequired) 
			throws IllegalArgumentException, SQLException {
		SQLManager.setStatementType(statementTypeRequired);
	}

	/** @see SQLManager#sendQuery(String) */
	public boolean sendQuery(String query) 
			throws IllegalArgumentException, NullPointerException, SQLException {
		return this.sqlManager.sendQuery(query);
	}

	public JTable getGeneratedJTable() {
		return this.sqlManager.getGeneratedJTable();
	}

	public String getGeneratedReply() {
		return this.sqlManager.getGeneratedReply();
	}

	/** @see I_ConnectionManager#generateErrorMessage(SQLException) */
	public String generateErrorMessage(SQLException exception) {
		return this.connector.generateErrorMessage(exception);
	}
}
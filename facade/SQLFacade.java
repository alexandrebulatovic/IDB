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

	/* ----------------------------------------------------------------- */

	/* CONSTRUCTEUR */

	public SQLFacade
			(I_DDLManager manager, 
			I_ConnectionManager connector, 
			TableSet tables, 
			SQLManager sqlManager) 
	{
		super(manager, tables, sqlManager);
		this.connector = connector;
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
		return this.sql.sendQuery(query);
	}

	public JTable getGeneratedJTable() {
		return this.sql.getGeneratedJTable();
	}

	public String getGeneratedReply() {
		return this.sql.getGeneratedReply();
	}

	/** @see I_ConnectionManager#generateErrorMessage(SQLException) */
	public String generateErrorMessage(SQLException exception) {
		return this.connector.generateErrorMessage(exception);
	}
}
package sql;

import java.sql.*;

import javax.swing.JOptionPane;

import connect.*;

public class SQLModel {


	/**Gestionnaire de connexion connecté à un SGBD. */
	private ConnectionManager connector;

	public SQLModel(ConnectionManager cm){
		this.connector = cm;
	}


	public Connection getConnector() {
		return connector.dbms();
	}


}

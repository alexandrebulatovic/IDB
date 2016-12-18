package manager;

import java.sql.*;

public class SQLManager {


	/**Gestionnaire de connexion connecté à un SGBD. */
	
	private Connection connector;

	public SQLManager(Connection cm){
		this.connector = cm;
	}

	public Connection getConnector() {
		return this.connector;
	}


}

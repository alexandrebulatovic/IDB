package manager.connection;

import java.sql.SQLException;

import connect.ConnectionStrings;

public class MySQLConnectionManager 
extends ConnectionManager
{
	//Constructor
	/**
	 * Constructeur commun.
	 */
	private MySQLConnectionManager()
	{
		super("com.mysql.jdbc.Driver");
		ConnectionManager.INSTANCE = this;
	}
	
	
	//Methodes
	/**
	 * Retourne le gestionnaire de connection en cours si et seulement
	 * s'il existe déjà. 
	 * Retourne un nouveau gestionnaire sinon.
	 * 
	 * @return ConnectionManager
	 */
	public static ConnectionManager getConnector()
	{
		if (ConnectionManager.INSTANCE == null) new MySQLConnectionManager();
		return ConnectionManager.INSTANCE;
	}
	
	
	//Protected
	@Override
	protected String errorMessage(SQLException e)
	{
		switch (e.getErrorCode()) {
		case 0 		: return "adresse IP ou port incorrect.";
		case 1044 	: return "nom de base de données inaccessible.";
		case 1045 	: return "nom d'utilisateur ou mot de passe incorrect.";
		default : return "inconnue.";
		}
	}
	
	
	//Privates
	@Override
	protected String entireUrl(ConnectionStrings param)
	{	
		return "jdbc:" 
				+ "mysql://"
				+ param.url + ":" 
				+ param.port + "/" 
				+ param.baseName;
	}
}

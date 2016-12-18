package manager.connection;

import java.sql.SQLException;

import useful.ConnectionStrings;

import manager.ConnectionManager;


/**
 * Gère la connexion et la déconnexion vers MySQL.
 * 
 * @author UGOLINI Romain
 */
public class MySQLConnectionManager 
extends ConnectionManager
{
	//Constructor
	/**
	 * Constructeur commun.
	 */
	public MySQLConnectionManager(){super("com.mysql.jdbc.Driver");}
	

	//Protected
	@Override
	protected String errorMessage(SQLException e)
	{
		switch (e.getErrorCode()) {
		case 0 		: return "adresse IP ou port incorrect.";
		case 1044 	: return "nom de base de données inaccessible.";
		case 1045 	: return "nom d'utilisateur ou mot de passe incorrect.";
		default 	: return "inconnue.";
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

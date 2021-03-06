package manager.connection;

import java.sql.SQLException;

import useful.ConnectionStrings;



/**
 * Gère la connexion et la déconnexion vers MySQL.
 * 
 * @author UGOLINI Romain
 */
public class MySQLConnectionManager 
extends AbstractConnectionManager
{
	//Constructor
	/**
	 * Constructeur commun.
	 */
	public MySQLConnectionManager(){super("com.mysql.jdbc.Driver");}


	//Protected
	@Override
	public String generateErrorMessage(SQLException e)
	{
		switch (e.getErrorCode()) {
		case 0 		: 
			if (e.getSQLState().startsWith("08"))
				return "adresse IP ou port incorrect.";
			else
				return e.getMessage().split("\\.")[0]; // retourne la premiere phrase de l'erreur (= le plus significatif sur MySQL)
		case 1044 	: return "nom de base de données inaccessible.";
		case 1045 	: return "nom d'utilisateur ou mot de passe incorrect.";
		default 	: return e.getErrorCode() + " : " + e.getMessage();
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

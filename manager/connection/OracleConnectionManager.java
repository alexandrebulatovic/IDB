package manager.connection;

import java.sql.SQLException;

import useful.ConnectionStrings;

import manager.ConnectionManager;



/**
 * Gère la connexion et la déconnexion vers Oracle.
 * 
 * @author UGOLINI Romain
 */
public class OracleConnectionManager 
extends ConnectionManager
{	
	//Constructor
	/**
	 * Constructeur commun.
	 */
	public OracleConnectionManager(){super("oracle.jdbc.OracleDriver");}
	
	
	//Protected
	@Override
	protected String errorMessage(SQLException e)
	{
		switch (e.getErrorCode()) {
		case 0 		: return "nom de base de données incorrect.";
		case 1017 	: return "utilisateur ou mot de passe incorrect.";
		case 17002	: return "adresse IP ou port incorrect.";
		case 17443 	: return "mot de passe nécessaire.";
		default 	: return "inconnue.";
		}
	}
	
	
	//Privates
	@Override
	protected String entireUrl(ConnectionStrings param)
	{	
		return "jdbc:" 
				+ "oracle:"
				+ "thin:@" 
				+ param.url + ":" 
				+ param.port + ":" 
				+ param.baseName;
	}
}

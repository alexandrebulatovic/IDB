package connect;

import java.sql.SQLException;

public class OracleConnectionManager 
extends ConnectionManager
{	
	//Constructor
	public OracleConnectionManager()
	{
		super("oracle.jdbc.OracleDriver");
	}
	
	
	//Protected
	/**
	 * {@inheritDoc}
	 */
	protected String errorMessage(SQLException e)
	{
		switch (e.getErrorCode()) {
		case 0 		: return "nom de base de données incorrect.";
		case 1017 	: return "utilisateur ou mot de passe incorrect.";
		case 17002	: return "adresse IP ou port incorrect.";
		case 17443 	: return "utilisateur ou mot de passe incorrect.";
		default : return "2 inconnue.";
		}
	}
	
	
	//Privates
	/**
	 * {@inheritDoc}
	 */
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

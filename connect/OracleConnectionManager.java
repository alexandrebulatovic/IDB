package connect;

import java.sql.SQLException;

public class OracleConnectionManager 
extends ConnectionManager
{	
	//Constructor
	/**
	 * Constructeur commun.
	 */
	private OracleConnectionManager()
	{
		super("oracle.jdbc.OracleDriver");
		INSTANCE = this;
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
		if (INSTANCE == null) new OracleConnectionManager();
		return INSTANCE;
	}
	
	
	//Protected
	@Override
	protected String errorMessage(SQLException e)
	{
		switch (e.getErrorCode()) {
		case 0 		: return "nom de base de données incorrect.";
		case 1017 	: return "utilisateur ou mot de passe incorrect.";
		case 17002	: return "adresse IP ou port incorrect.";
		case 17443 	: return "mot de passe nécessaire.";
		default : return "inconnue.";
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

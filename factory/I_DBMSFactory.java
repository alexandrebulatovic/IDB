package factory;

import java.sql.Connection;

import manager.I_ConnectionManager;
import manager.I_DDLManager;

/**
 * Interface pour les fabriques abstraites dédiées à un et un seul SGBD.
 */
public interface I_DBMSFactory 
{
	/**
	 * @return un manager de connexion vers un SGBD.
	 */
	public I_ConnectionManager getConnectionManager();
	
	
	/**
	 * @return un gestionnaire de définition des données.
	 */
	public I_DDLManager getDDLManager(Connection connection);
}
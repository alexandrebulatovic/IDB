package factory;

import java.sql.Connection;

import ddl.I_Attribute;
import manager.connection.I_ConnectionManager;
import manager.ddl.I_DDLManager;


/**
 * Interface pour les fabriques abstraites dédiées à un et un seul SGBD.
 */
public interface I_DBMSFactory 
{
	/**
	 * @return un gestionnaire de connexion vers un SGBD.
	 */
	public I_ConnectionManager getConnectionManager();
	
	
	/**
	 * @return un gestionnaire de définition des données.
	 */
	public I_DDLManager getDDLManager(Connection connection);
	
	
	/**
	 * @param primaryKey 
	 * @param notNull 
	 * @param parseInt 
	 * @param type 
	 * @param name 
	 * @return un model d'attribut pour les IHM de DDL.
	 */
	public I_Attribute getAttributeModel(String name, String type, int parseInt, boolean notNull, boolean primaryKey);
}

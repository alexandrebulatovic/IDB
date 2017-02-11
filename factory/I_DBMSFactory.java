package factory;

import gui.ddl.tools.I_AttributeModel;
import gui.ddl.tools.I_TableModel;

import java.sql.Connection;

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
	public I_AttributeModel getAttributeModel(String name, String type, int parseInt, boolean notNull, boolean primaryKey);

	/**
	 * @return un modèle de table vide pour l'IHM de création des tables.
	 */
	public I_TableModel getTableModel();
}

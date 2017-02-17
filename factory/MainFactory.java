package factory;

import gui.ddl.tools.I_AttributeModel;
import gui.ddl.tools.I_TableModel;

import java.sql.Connection;

import manager.connection.I_ConnectionManager;
import manager.ddl.I_DDLManager;
import manager.sql.SQLManager;

/**
 * Fabrique concrète de fabriques de SGBD.<br/>
 * Utilise le pattern stratégie pour chacune de ses méthodes.
 */
public class MainFactory 
{
	//Statiques
	/** Mot clé pour passer sur la fabrique d'Oracle.*/
	public static final String ORACLE = "Oracle";
	
	/** Mot clé pour passer sur la fabrique de MySQL.*/
	public static final String MYSQL = "MySQL";
	
	/** Mot-clé pour une fabrique inactive.*/
	public static final String MOCK = "Mock";
	
	
	//Attributs
	/** Fabrique choisie pour l'exécution de l'application.*/
	private I_DBMSFactory factory;
	
	
	//Constructeur
	/**
	 * Constructeur vide.
	 */
	public MainFactory(String dbms)
	{
		this.setDBMS(dbms);
	}
	
	
	//Méthodes
	/**
	 * @return la liste des SGBD disponibles.
	 */
	public String [] getAvailableDBMS() 
	{
		String [] result = {ORACLE, MYSQL};
		return result; 
	}


	/**
	 * @return null si et seulement si cette fabrique n'est pas liée
	 * à une autre fabrique de SGBD, un gestionnaire de connexion sinon.
	 */
	public I_ConnectionManager getConnectionManager()
	{
		return this.factory.getConnectionManager();
	}
	
	
	/**
	 * @param connection : null interdit, ne doit pas être fermée.
	 * @return null si et seulement si cette fabrique n'est pas liée
	 * à une autre fabrique de SGBD, un gestionnaire de définition des données sinon.
	 */
	public I_DDLManager getDDLManager(Connection connection)
	{
		return this.factory.getDDLManager(connection);
	}
	
	
	/**
	 * @param primaryKey 
	 * @param notNull 
	 * @param parseInt 
	 * @param type 
	 * @param name 
	 * @return un model d'attribut pour les IHM de DDL.
	 */
	public I_AttributeModel getAttributeModel(String name, String type, int parseInt, boolean notNull, boolean primaryKey)
	{
		return this.factory.getAttributeModel(name,type,parseInt, notNull,primaryKey);
	}
	
	
	/**
	 * @return un modèle de table vide pour l'IHM de création des tables.
	 */
	public I_TableModel getTableModel()
	{
		return this.factory.getTableModel();
	}
	
	
	/**
	 * @param connection : une connexion active vers un SGBD, null interdit.
	 * @return un gestionnaire de requête SQL.
	 */
	public SQLManager getSQLManager(Connection connection)
	{
		return new SQLManager(connection, SQLManager.TYPE_PLAIN_RESULTSET);
	}
	
	
	/**
	 * Définit pour quel $dbms fabriquer les objets.
	 * 
	 * @param dbms : parmi les variables statiques, null interdit.
	 */
	public void setDBMS(String dbms)
	{
		switch(dbms) {
		case ORACLE : this.factory = new OracleDBMSFactory();
		break;
		
		case MYSQL  : this.factory = new MySQLDBMSFactory();
		break;
		
		default 	: this.factory = new MockDBMSFactory();
		}
	}
	
	
	@Override
	public String toString() 
	{
		return this.factory.toString();
	}
}

package factory;

import java.sql.Connection;

import manager.I_ConnectionManager;
import manager.I_DDLManager;

/**
 * Fabrique concrète de fabriques de SGBD.
 */
public class MainFactory 
{
	//Statiques
	/** Mot clé pour passer sur la fabrique d'Oracle.*/
	public static final String ORACLE = "Oracle";
	
	/** Mot clé pour passer sur la fabrique de MySQL.*/
	public static final String MYSQL = "MySQL";
	
	//Attributs
	/** Fabrique choisie pour l'exécution de l'application.*/
	private I_DBMSFactory factory;
	
	
	//Constructeur
	/**
	 * Constructeur vide.
	 */
	public MainFactory()
	{
		this.factory = null;
	}
	
	
	//Méthodes
	/**
	 * @return null si et seulement si cette fabrique n'est pas liée
	 * à une autre fabrique de SGBD, un gestionnaire de connexion sinon.
	 */
	public I_ConnectionManager getConnectionManager()
	{
		if (this.isLinked()) {
			return this.factory.getConnectionManager();
		}
		else {
			return null;
		}
	}
	
	
	/**
	 * @param connection : null interdit, ne doit pas être fermée.
	 * @return null si et seulement si cette fabrique n'est pas liée
	 * à une autre fabrique de SGBD, un gestionnaire de définition des données sinon.
	 */
	public I_DDLManager getDDLManager(Connection connection)
	{
		if (this.isLinked()) {
			return this.factory.getDDLManager(connection);
		}
		else {
			return null;
		}
	}
	
	
	/**
	 * @return la liste des SGBD disponibles.
	 */
	public String [] getAvailableDBMS() 
	{
		String [] result = {ORACLE, MYSQL};
		return result; 
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
		
		case MYSQL : this.factory = new MySQLDBMSFactory();
		break;
		}
	}
	
	
	@Override
	public String toString() {return "Fabrique principale.";}
	
	
	//Privées
	/**
	 * A supprimer si volonté d'un design pattern "ETAT", mais 
	 * ça me semble un peu lourd pour deux méthodes et deux états.
	 * 
	 * @return vrai si et seulement si la fabrique est associée à une
	 * fabrique de SGBD, faux sinon.
	 */
	private boolean isLinked() {return this.factory != null;}
}

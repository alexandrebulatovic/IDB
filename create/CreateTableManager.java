package create;

import connect.ConnectionManager;
import connect.CustomizedResponse;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

public class CreateTableManager 
{
	//Attributs
	/**
	 * Gestionnaire de connexion connecté à un SGBD.
	 */
	private ConnectionManager connector;
	
	/**
	 * Pour créer des requètes SQL.
	 */
	private Statement sql;
	
	
	//Constructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param cm :  Gestionnaire de connexion vers un SGBD.
	 */
	public CreateTableManager(ConnectionManager cm)
	{
		this.connector = cm;
	}
	
	
	//Méthodes
	/**
	 * Tente de créer une table dans la base de données
	 * connecté à $this, à partir de la requête $sqlQuery.
	 * Retourne une réponse personnalisée qui décrit la tentative.
	 * 
	 * @param sqlQuery : une requête SQL de LDD pour créer une table.
	 * @return CustomizedResponse
	 */
	public CustomizedResponse createTable(String sqlQuery)
	{	
		//TODO : améliorer les messages d'erreurs.
		CustomizedResponse result = this.createStatement();
		if (result.success()) {
			try{
				this.sql.executeUpdate(sqlQuery);
			}
			catch(SQLException e){
				result = new CustomizedResponse(false, e.getMessage());
			}
		}
		return result;
	}
	
	
	/**
	 * Retourne un message personnalisé décrivant la
	 * tentative de création d'un objet Statement.
	 * En cas d'erreur, tente de se reconnecter trois fois au SGBD.
	 * 
	 * @return CustomizedResponse
	 */
	private CustomizedResponse createStatement()
	{
		Statement statement;
		CustomizedResponse result;
		boolean error;
		int countConnection = 0;
		
		do{
			try{
				statement = this.connector.dbms().createStatement();
				result = new CustomizedResponse(true, "OK");
				error = false;
			}
			catch(SQLException e){
				//TODO : ajouter le message de $e au $result.
				statement = null;
				result = this.connector.connect();
				countConnection++;
				error = true;
			}
		}while(countConnection < 3 && error);
		
		if (!error) this.sql = statement;
		return result;
	}
}

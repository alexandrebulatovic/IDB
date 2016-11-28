package create;

import connect.ConnectionManager;
import connect.CustomizedResponse;

public class CreateTableController 
{
	//Attributs
	/**
	 * IHM pour créer une table et ses attributs.
	 */
	private CreateTableView mhi;
	
	/**
	 * Objet pour gérer la communication avec un SGBD
	 * dans l'optique de créer des tables.
	 */
	private CreateTableManager creator;
	
	
	//Contructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param cm : objet ConnectionManager obtenu lors de la connexion.
	 */
	public CreateTableController(ConnectionManager cm)
	{
		this.mhi = new CreateTableView(this);
		this.creator = new CreateTableManager(cm);
	}
	
	
	/**
	 * Envoie la requête SQL $sqlQuery au SGBD.
	 * 
	 * @param sqlQuery : une requête SQL de LDD pour créer une table.
	 */
	public void createTable(String sqlQuery)
	{
		//TODO : implémenter une méthode talk().
		this.creator.createTable(sqlQuery);
	}
}

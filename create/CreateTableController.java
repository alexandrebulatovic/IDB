package create;

import ddl.create.CreateTableView;
import useful.ConnectionManager;
import useful.CustomizedResponse;

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
	 * @param table : une table à créer. L'objet peut être erroné;
	 */
	public void createTable(Table table)
	{
		CustomizedResponse response = this.creator.createTable(table.toSQL());
		if (response.success()) {
			this.mhi.resetView();
			this.talk("Table " + table.getTableName() + " créée.");
		}
		else {
			this.talk(response.message());
		}
	}
	
	
	public void setView(Attribute[] attributes,String tableName){
		for (Attribute a : attributes){
			this.mhi.addAttributeToTable(a);
		}
		this.mhi.setTableName(tableName);
	}
	
	
	//Privées
	/**
	 * Communique avec l'utilisateur en affichant $msg.
	 * 
	 * @param msg : un message à transmettre à l'utilisateur.
	 */
	public void talk(String msg)
	{
		this.mhi.talk(msg);
	}
}

package ddl;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;

import business.Attribute;
import business.Table;

import manager.DDLManager;
import useful.Response;
import useful.ResponseData;

/**
 * Assure le dialogue entre les IHM du langage de définition des données
 * et le gestionnaire de ce même langage.
 * 
 * @author UGOLINI Romain
 * @author MAURY Adrian
 */
public class DDLController 
{
	//Attributs
	/** IHM pour créer une table et ses attributs.*/
	private CreateTableGUI createGUI;
	
	/** IHM pour supprimer une table.*/
	private DropTableGUI dropGUI;
	
	/** Gère la communication avec un SGBD dans l'optique d'utiliser le LDD.*/
	private DDLManager manager;
		
	
	//Contructeur
	/**
	 * Constructeur commun.
	 */
	public DDLController(Connection connection)
	{
		this.manager = new DDLManager(connection);
	}
	
	
	/**
	 * Ouvre l'IHM de création des tables si et seulement si 
	 * elle n'existe pas, sinon tente de l'afficher au premier plan.
	 */
	public void openCreateGUI()
	{
		if (this.createGUI == null) {
			this.createGUI = new CreateTableGUI(this);
		}
		else {
			showGUI(this.createGUI);
		}
	}

	
	/**
	 * Ouvre l'IHM de création des tables si et seulement si 
	 * elle n'existe pas, sinon tente de l'afficher au premier plan.
	 */
	public void openDropGUI()
	{
		if (this.dropGUI == null) {
			this.dropGUI = new DropTableGUI(this);
		}
		else{
			showGUI(this.dropGUI);
		}
	}


	
	/**
	 * Envoie $table au DDLManager dans l'optique de la créer.
	 * 
	 * @param table : une table à créer. L'objet peut être erroné;
	 */
	public void createTable(Table table)
	{
		Response response = this.manager.createTable(table.toCreate());
		if (response.hasSuccess()) {
			this.createGUI.resetView();
		}
		this.createGUI.talk(response.toString());
	}
	
	
	/**
	 * Envoie $table au DDLManager dans l'optique de la supprimer.
	 * 
	 * @param table : une table à supprimé. L'objet peut être erroné.
	 * @return CustomizedResponse
	 */
	public Response dropTable(String table, boolean cascade)
	{
		return this.manager.dropTable(table, cascade);
	}
	
	
	/**
	 * Retourne une réponse personnalisée contenant le nom des tables
	 * de la base, si et seulement si (ces dernières existent et 
	 * il n'y a pas eu d'exceptions).
	 * 
	 * @return CustomizedResponseWithData
	 */
	public ResponseData<String> getTables()
	{
		return this.manager.getTables();
	}


	/**
	 * Retourne une réponse personnalisée qui contient les membres
	 * de la clée primaire de $table.
	 * 
	 * @param table : nom de la table, ne doit pas être null.
	 * @return CustomizedResponseWithData
	 */
	public ResponseData<String> getPrimaryKey(String table)
	{
		return this.manager.getPrimaryKey(table);
	}
	

	/**
	 * Ferme proprement les objets Statements.
	 */
	public void closeStatement(){this.manager.closeStatement();}

	
	//Privées
	/**
	 * Affiche $gui au premier plan.
	 * 
	 * @param gui : une IHM, null interdit.
	 */
	private static void showGUI(JFrame gui)
	{
		gui.setVisible(true);
		gui.toFront();
	}
}

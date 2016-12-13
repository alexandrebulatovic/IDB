package ddl;

import manager.DDLManager;
import ddl.create.CreateTableGUI;
import ddl.drop.DropTableGUI;
import ddl.modify.ModifyTableView;
import useful.CustomizedResponse;
import useful.CustomizedResponseWithData;

/**
 * Assure le dialogue entre les IHM du langage de définition des données
 * et le gestionnaire de ce même langage.
 * Singleton.
 * 
 * @author UGOLINI Romain
 * @author MAURY Adrian
 */
public class DDLController 
{
	/** Controleur en cours.*/
	private static DDLController INSTANCE;
	
	//Attributs
	/** IHM pour créer une table et ses attributs.*/
	private CreateTableGUI createGUI;
	
	/** IHM pour supprimer une table.*/
	private DropTableGUI dropGUI;
	
	/** Gère la communication avec un SGBD dans l'optique d'utiliser le LDD.*/
	private DDLManager manager;
	
	
	private ModifyTableView modifyGUI;
	
	
	//Contructeur
	/**
	 * Constructeur commun.
	 */
	private DDLController()
	{
		INSTANCE = this;
		this.manager = DDLManager.getInstance();
	}
	
	
	/**
	 * Retourne le controleur actif si et seulement s'il
	 * existe déjà. Retourne un nouveau controleur sinon.
	 * 
	 * @return DDLController
	 */
	public static DDLController getInstance()
	{
		if (INSTANCE == null) new DDLController();
		return INSTANCE;
	}
	
	/**
	 * Ouvre l'IHM de création des tables si et seulement si 
	 * elle n'existe pas, sinon tente de l'afficher au premier plan.
	 */
	public void openCreateGUI()
	{
		this.createGUI = CreateTableGUI.getInstance();
		this.createGUI.toFront();
	}
	
	
	/**
	 * Ouvre l'IHM de création des tables si et seulement si 
	 * elle n'existe pas, sinon tente de l'afficher au premier plan.
	 */
	public void openDropGUI()
	{
		this.dropGUI = DropTableGUI.getInstance();
		this.dropGUI.toFront();
	}


	public void openModifyGUI() {
		this.modifyGUI = ModifyTableView.getInstance();
	}
	
	
	/**
	 * Envoie $table au DDLManager dans l'optique de la créer.
	 * 
	 * @param table : une table à créer. L'objet peut être erroné;
	 */
	public void createTable(Table table)
	{
		CustomizedResponse response = this.manager.createTable(table);
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
	public CustomizedResponse dropTable(Table table)
	{
		return this.manager.dropTable(table);
	}
	
	
	/**
	 * Retourne une réponse personnalisée contenant le nom des tables
	 * de la base, si et seulement si (ces dernières existent et 
	 * il n'y a pas eu d'exceptions).
	 * 
	 * @return CustomizedResponseWithData
	 */
	public CustomizedResponseWithData<String> getTables()
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
	public CustomizedResponseWithData<String> getPrimaryKey(String table)
	{
		return this.manager.getPrimaryKey(table);
	}


	public void modifier(String tableName) {
		System.out.println("Je vais modifier la table"+tableName);
		CreateTableGUI create = CreateTableGUI.getInstance();
		Attribute att = new Attribute(tableName, tableName, 0, false, false, false, false, tableName, tableName);
		create.setView(new Attribute[]{att}, tableName);
		
	}
	

	/**
	 * Ferme proprement les objets Statements.
	 */
	public void closeStatement(){this.manager.closeStatement();}

	
	
	//Privées

}

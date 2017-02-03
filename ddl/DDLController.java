package ddl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import business.Attribute;
import business.Table;

import manager.I_DDLManager;
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
	
	/**IHM pour modifier une table et ses attributs.*/
	private ModifyTableGUI modifyGUI;
	
	/** IHM pour supprimer une table.*/
	private DropTableGUI dropGUI;
	
	/** Gère la communication avec un SGBD dans l'optique d'utiliser le LDD.*/
	private I_DDLManager manager;
		
	
	//Contructeur
	/**
	 * Constructeur commun.
	 */
	public DDLController(I_DDLManager manager)
	{
		this.manager = manager;
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
	
	public void openModifyGUI() {
		if (this.modifyGUI == null){
			this.modifyGUI = new ModifyTableGUI(this);
		}
		else{
			showGUI(this.modifyGUI);
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
		//TODO : déplacer le talk vers l'IHM
		Response response = this.manager.createTable(table.toCreate());
		if (response.hasSuccess()) {
			this.createGUI.resetView();
		}
		this.createGUI.talk(response);
	}
	
	/**
	 * Modifie une table existante
	 */
	public void modifyTable(Table table,Table tableSource) {
		//TODO : déplacer le talk vers l'IHM
		ArrayList<Response> responses = this.manager.modifyTable(table.toModify(tableSource));
		boolean error = false;
		for (Response response : responses){
			if (!response.hasSuccess()){
				error = true;
				this.modifyGUI.talk(response);
			}
		}
		if (!error) {
			this.modifyGUI.resetView();
		}
		
	}
	
	
	/**
	 * Supprime $table, si c'est possible.
	 * 
	 * @param table : une table à supprimer, null interdit.
	 * @param cascade : vrai si et seulement si $table peut être supprimée 
	 * alors qu'elle est référencée par d'autres tables, faux sinon.
	 * @param chain : vrai si et seulement si toutes les tables qui référencent
	 * $table doivent être supprimées aussi.
	 * @return une réponse personnalisée décrivant si la suppression de toutes
	 * les tables a réussi ou non.
	 */
	public Response dropTable(String table, boolean cascade, boolean chain)
	{
		return this.manager.dropTable(table, cascade, chain);
	}
	
	
	/**
	 * @return vrai si et seulement si le SGBD permet de "droper" une 
	 * table avec l'option "CASCADE"
	 */
	public boolean dbmsAllowsDropCascade()
	{
		return this.manager.allowsDropCascade();
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

	
	public List<Attribute> getAttributes(String table) {
		return manager.getAttributess(table);
	}


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

	public String[] getAttributeTypes(){
		return manager.getAttributeTypes();
	}





}

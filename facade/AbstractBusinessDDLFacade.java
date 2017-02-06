package facade;

import useful.ResponseData;
import manager.ddl.I_DDLManager;
import business.TableSet;

public class AbstractBusinessDDLFacade 
{
	//Attributs
	/** Tables disponibles.*/
	private TableSet tables;

	/** Gestionnaire de définition des données.*/
	private I_DDLManager manager;


	//Constructeur
	/**
	 * Constructeur communb.
	 * 
	 * @param tables : null interdit.
	 * @param ddlmanager : null interdit.
	 */
	protected AbstractBusinessDDLFacade(TableSet tables, I_DDLManager ddlmanager)
	{
		this.tables = tables;
		this.manager = ddlmanager;
	}


	//Méthodes
	/**
	 * @return la liste des tables disponibles.
	 */
	public ResponseData<String> getTables()
	{
		ResponseData<String> response;
		if (tables.isLoaded()) {
			response = new ResponseData<String>
			(true, "Tables récupérées.", this.tables.getTablesNames());
		} 
		else {
			response = this.manager.getTables();

			for(String table : response.getCollection()){
				this.tables.addTable(table);
			}
		}
		return response;
	}
}

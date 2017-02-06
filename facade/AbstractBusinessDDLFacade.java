package facade;

import useful.ResponseData;
import manager.ddl.I_DDLManager;
import business.TableSet;

public class AbstractBusinessDDLFacade 
{
	//Attributs
	/** Tables disponibles.*/
	protected TableSet tables;

	/** Gestionnaire de définition des données.*/
	protected I_DDLManager manager;


	//Constructeur
	/**
	 * Constructeur communb.
	 * @param ddlmanager : null interdit.
	 * @param tables : null interdit.
	 */
	protected AbstractBusinessDDLFacade(I_DDLManager ddlmanager, TableSet tables)
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

package facade;

import useful.ResponseData;
import manager.ddl.I_DDLManager;
import manager.sql.SQLManager;
import business.TableSet;

public class AbstractDDLCRUDFacade 
{
	//Attributs
	/** Tables disponibles.*/
	protected TableSet business;

	/** Gestionnaire de définition des données.*/
	protected I_DDLManager dbms;

	/** Gestionnaire de requêtes SQL.*/
	protected SQLManager sql;
	
	
	//Constructeur
	/**
	 * Constructeur communb.
	 * @param ddlmanager : null interdit.
	 * @param tables : null interdit.
	 * @param sql : null interdit.
	 */
	protected AbstractDDLCRUDFacade(I_DDLManager ddlmanager, TableSet tables, SQLManager sql)
	{
		this.business = tables;
		this.dbms = ddlmanager;
	}


	//Méthodes
	/**
	 * @return la liste des tables disponibles.
	 */
	public ResponseData<String> getTables()
	{
		ResponseData<String> response;
		if (business.isLoaded()) {
			response = new ResponseData<String>
			(true, "Tables récupérées.", this.business.getTablesNames());
		} 
		else {
			response = this.dbms.getTables();
			business.loadTables(response.getCollection());
		}
		return response;
	}
}

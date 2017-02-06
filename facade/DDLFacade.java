package facade;

import business.TableSet;
import ddl.I_Attribute;
import ddl.I_Table;
import useful.Response;
import useful.ResponseData;
import factory.MainFactory;
import manager.ddl.I_DDLManager;

public class DDLFacade 
extends AbstractDDLCRUDFacade
{
	/** Fabrique principale.*/
	private MainFactory factory;

	
	//Constructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param manager : null interdit.
	 * @param factory : null interdit.
	 */
	public DDLFacade(I_DDLManager manager, MainFactory factory,TableSet tables)
	{
		super(manager,tables);
		this.factory = factory;
	}


	//Méthodes
	/**
	 * Tente de créer $table dans la base de données.
	 * 
	 * @param table : une table à créer. L'objet peut être erroné;
	 */
	public Response createTable(I_Table table)
	{
		if(this.tables.addTable(table.getName())){
			for(I_Attribute attribute : table.getAttributes()){
				this.tables.addAttributeToTable(
						table.getName(),
						attribute.getName(),
						attribute.getType(),
						attribute.getSize(),
						attribute.isNotNull(),
						attribute.isPrimaryKey());
			}
			//TODO : ligne en dessous
			return this.manager.createTable("");
		}else{
			return new Response(false,"Cette table existe déjà.");
		}
		//return this.manager.createTable(table);
	}


	/**
	 * @return la liste des types de données disponibles pour le SGBD.
	 */
	public String[] getAttributeTypes()
	{
		return this.manager.getAttributeTypes();
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


	/**
	 * @param primaryKey 
	 * @param notNull 
	 * @param parseInt 
	 * @param type 
	 * @param name 
	 * @return un model d'attribut pour les IHM de DDL.
	 */
	public I_Attribute getAttributeModel(String name, String type, int parseInt, boolean notNull, boolean primaryKey)
	{
		return this.factory.getAttributeModel(name,type,parseInt, notNull,primaryKey);
	}
}

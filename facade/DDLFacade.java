package facade;

import java.util.Iterator;
import java.util.List;

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
		boolean addable = this.createtableBusiness(table);
		if (!addable) 
			return new Response(false, "Cette table existe déjà.");
		else 
			return this.createTableDBMS(table);
	}

	
	/**
	 * 
	 * @param table
	 * @return
	 */
	private boolean createtableBusiness(I_Table table)
	{
		String name = table.getName();
		boolean addable = this.tables.isAddable(name);
		
		if (addable) {
			for(I_Attribute attribute : table.getAttributes()){
				this.tables.addAttributeToTable(
						table.getName(),
						attribute.getName(),
						attribute.getType(),
						attribute.getSize(),
						attribute.isNotNull(),
						attribute.isPrimaryKey());
			}
		}
		return addable;
	}
	
	
	private Response createTableDBMS(I_Table table)
	{
		String name = table.getName();
		List<String> sql = this.tables.getSQLTableToCreate(name);
		Iterator<String> statement = sql.iterator();
		
		String create = statement.next(); 
		Response result = this.manager.createTable(create);
		
		while (statement.hasNext() && result.hasSuccess()) {
			result = this.manager.altertable(statement.next());
		}
		return result;
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
	 * @return une réponse personnalisée décrivant si la suppression de toutes
	 * les tables a réussi ou non.
	 */
	public Response dropTable(String table, boolean cascade)
	{
		Response result = this.manager.dropTable(table, cascade);
		if (result.hasSuccess()) {
			this.tables.removeTable(table);
		}
		return result;
	}


	/**
	 * Supprime $table et toutes les tables de la bases qui utilisent la clée primaire
	 * de $table.
	 * 
	 * @param table : une table à supprimer, null interdit.
	 * @return une réponse personnalisée qui contient le nom de toutes les tables 
	 * supprimées, ou une réponse vide en cas d'erreur.
	 */
	public Response dropTableDomino(String table)
	{

		ResponseData<String> result = this.manager.dropTableDomino(table);
		if (result.hasSuccess()) {
			for (String t : result.getCollection()) {
				this.tables.removeTable(t);
			}
		}
		return result;
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

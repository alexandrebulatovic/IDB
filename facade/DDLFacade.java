package facade;

import ddl.I_Attribute;
import useful.Response;
import useful.ResponseData;

import factory.MainFactory;

import manager.ddl.I_DDLManager;

public class DDLFacade 
{
	/** Gestionnaire de définition des données.*/
	private I_DDLManager manager;
	
	/** Fabrique principale.*/
	private MainFactory factory;
	
	//TODO : attribut pour la classe business
	
	//Constructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param manager : null interdit.
	 * @param factory : null interdit.
	 */
	public DDLFacade(I_DDLManager manager, MainFactory factory)
	{
		this.manager = manager;
		this.factory = factory;
	}
	
	
	//Méthodes
	//TODO : setBusiness()
	
	/**
	 * Tente de créer $table dans la base de données.
	 * 
	 * @param table : une table à créer. L'objet peut être erroné;
	 */
	public Response createTable(String sql)
	{
		return this.manager.createTable(sql);
	}
	
	
	/**
	 * @return la liste des types de données disponibles pour le SGBD.
	 */
	public String[] getAttributeTypes()
	{
		return this.manager.getAttributeTypes();
	}
	
	
	/**
	 * @return une réponse personnalisée contenant le nom des tables
	 * de la base, si et seulement si ces dernières existent et 
	 * il n'y a pas eu d'exceptions, une réponse personnalisée vide sinon.
	 */
	public ResponseData<String> getTables()
	{
		return this.manager.getTables();
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
	 * @return un model d'attribut pour les IHM de DDL.
	 */
	public I_Attribute getAttributeModel()
	{
		return this.factory.getAttributeModel();
	}
}

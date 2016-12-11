package ddl;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;

import ddl.create.Table;

import useful.ConnectionManager;
import useful.CustomizedResponse;
import useful.CustomizedResponseWithData;

public class DDLManager 
{
	//Instance
	/** Singleton en cours ou non.*/
	private static DDLManager INSTANCE;
	
	//Attributs
	/** Gestionnaire de connexion connecté à un SGBD.*/
	private ConnectionManager connector;
	
	/** Pour créer des requètes SQL.*/
	private Statement statement;
	
	/** Pour obtenir le nom des tables.*/
	private DatabaseMetaData metadata;
	
	/** Stocke les tables de la base de données.*/
	private ResultSet tables;
	
	/** Stocke les clés primaires de la base de données.*/
	private ResultSet pkAttributes;
	
	//Constructeur
	/**
	 * Constructeur commun.
	 */
	private DDLManager()
	{
		INSTANCE = this;
		this.connector = ConnectionManager.getInstance();
		this.createStatementAndMetaData();
	}
	
	
	//Méthodes
	/**
	 * Retourne l'instance en cours si et seulement si elle existe déjà,
	 * retourne une nouvelle instance sinon.
	 * 
	 * @return DDLManager
	 */
	public static DDLManager getInstance()
	{
		if (INSTANCE == null) new DDLManager();
		return INSTANCE;
	}
	
	
	/**
	 * Retourne vrai si et seulement si le singleton de $this est instancié, 
	 * faux sinon.
	 * 
	 * @return boolean
	 */
	public static boolean hasInstance(){return INSTANCE != null;}


	/**
	 * Tente de créer une $table.
	 * Retourne une réponse personnalisée qui décrit la tentative.
	 * 
	 * @param table : une table à créer.
	 * @return CustomizedResponse
	 */
	public CustomizedResponse createTable(Table table)
	{	
		System.out.println(table);
		CustomizedResponse result = this.executeUpdate(table.toCreate());
		if (result.hasSuccess()) {
			result.setMessage("Table " + table.getName() + " créée.");
		}
		return result;
	}
	
	/**
	 * Tente de supprimer $table.
	 * Retourne une réponse personnalisée qui décrit la tentative.
	 * 
	 * @param table : une table à supprimer.
	 * @return CustomizedResponse
	 */
	public CustomizedResponse dropTable(Table table)
	{
		CustomizedResponse result = this.executeUpdate(table.toDrop());
		if (result.hasSuccess()) {
			result.setMessage("Table " + table.getName() + " supprimée.");
		}
		return result;
	}
	
	public CustomizedResponseWithData<String> getPrimaryKey(String table){
		boolean ok;
		CustomizedResponseWithData<String> result = null;

		try{
			this.pkAttributes = this.metadata.getPrimaryKeys(
					null, 
					this.metadata.getUserName(),
					table);
			ok = true;
		}
		catch(SQLException e){
			ok = false;
			result = new CustomizedResponseWithData<String>
			(false, e.getMessage(), null);
		}
		
		if (ok) result = this.readPkAttributes();
		return result;
	}
	
	/**
	 * Retourne une réponse personnalisée vis à vis d'une tentative 
	 * pour récupérer toutes les tables de données.
	 * 
	 * @return CustomizedResponseWithData
	 */
	public CustomizedResponseWithData<String> getTables()
	{
		boolean ok;
		CustomizedResponseWithData<String> result = null;
		
		try{
			String [] tab = {"TABLE"};
			this.tables = this.metadata.getTables(
					null, 
					this.metadata.getUserName(),
					"%",  //Symbole en SQL et PL/SQL pour dire "aucun ou n'importe quels caractères"
					tab);
			ok = true;
		}
		catch(SQLException e){
			ok = false;
			result = new CustomizedResponseWithData<String>
			(false, e.getMessage(), null);
		}
		
		if (ok) result = this.readTables();
		return result;
	}
	
	
	/**
	 * Ferme proprement les statements.
	 */
	public void closeStatement()
	{
		try{this.statement.close();}
		catch(SQLException e){}
	}
	
	
	//Privées
	/**
	 * Fabrique des objets Statement et DataBaseMetaData  
	 * et en fait des attributs pour $this.
	 */
	private void createStatementAndMetaData() 
	{
		//TODO : voir si on peut empécher la catastrophe ferroviaire
		try{
			this.statement = this.connector.dbms().createStatement();
			this.metadata = this.connector.dbms().getMetaData();
		}catch(SQLException e){}
	}
		
	
	/**
	 * Retourne une réponse personnalisée qui décrit une tentative
	 * pour lire un objet ResulSet contenant le nom des tables de la base.
	 * La réponse contient le nom des tables si et seulement si elles ont toutes
	 * été lues, sinon ne les contient pas.
	 * 
	 * @return CustomizedResponseWithData
	 */
	private CustomizedResponseWithData<String> readTables()
	{
		ArrayList<String> result = new ArrayList<String>();
		try{
			while (this.tables.next()) {
				result.add(this.tables.getString(3));
			}
			this.tables.close();
		}
		catch(SQLException e){
			return new CustomizedResponseWithData<String>
			(false, e.getMessage(), null);
		}
		return new CustomizedResponseWithData<String>
			(true, "Tables récupérées", result);
	}
	
	private CustomizedResponseWithData<String> readPkAttributes()
	{
		ArrayList<String> result = new ArrayList<String>();
		try{
			while (this.pkAttributes.next()) {
				result.add(this.pkAttributes.getString(4));
			}
			this.pkAttributes.close();
		}
		catch(SQLException e){
			return new CustomizedResponseWithData<String>
			(false, e.getMessage(), null);
		}
		return new CustomizedResponseWithData<String>
			(true, "Clés Primaires récupérées", result);
	}
	

	/**
	 * Retourne une réponse personnalisée après l'envoi
	 * d'une requête sans qui ne retourne rien.
	 * 
	 * @param sql : une requête SQL qui ne retourne rien.
	 * @return CustomizedResponse
	 */
	private CustomizedResponse executeUpdate(String sql)
	{
		System.out.println(sql);
		CustomizedResponse result;
		try{
			this.statement.executeUpdate(sql);
			result = new CustomizedResponse(true);
		}
		catch(SQLException e){
			result = new CustomizedResponse(false, e.getMessage());
		}
		return result;
	}
}

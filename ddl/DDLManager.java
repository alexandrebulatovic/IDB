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
	
	/** Stocke les résultats d'une requête sur les meta-données.*/
	private ResultSet metaDataResult;
	
	
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
	 * @return Le nombre de tables existantes dans la base de données
	 * @throws SQLException
	 */
	public int getNbTables() throws SQLException{
		this.createStatementAndMetaData();
		ResultSet rs = this.statement.executeQuery("SELECT COUNT(*) FROM user_tables");
		rs.next();
		return rs.getInt(1);
	}
	
	/**
	 *
	 * @return la liste des tables présentes dans la bdd
	 */
	public String[] getTablesString(){
//		CustomizedResponseWithData<String>
		String[] valeurs = null;
		
		
		
		
		this.createStatementAndMetaData();
		try {
			valeurs = new String[this.getNbTables()];
			ResultSet rs = this.statement.executeQuery("SELECT TABLE_NAME FROM user_tables");
			int i=0;
			while (rs.next()){
				valeurs[i] = rs.getString(1);				
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valeurs;

	}

	
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
	
	
	/**
	 * Retourne une réponse personnalisée qui contient les noms des différentes
	 * tables de données disponibles pour l'utilisateur si et seulement si aucune
	 * erreur ne survient lors de l'interrogation de la base.
	 * Retourne une réponse personnalisée qui ne contient aucun nom de table 
	 * en cas d'erreur. La réponse décrit l'erreur rencontrée. 
	 * 
	 * @param table : nom de la table où chercher les clées primaires, ne doit pas être null.
	 * @return CustomizedResponseWithData
	 */
	public CustomizedResponseWithData<String> getPrimaryKey(String table)
	{
		CustomizedResponse cr = this.getMetaData("PRIMARY KEYS", table);
		if (cr == null) return this.readMetaData("Clées primaires récupérées", 4);
		else 			return new CustomizedResponseWithData<String>(cr, null);
	}
	

	
	/**
	 * Retourne une réponse personnalisée qui contient les noms des différentes
	 * tables de données disponibles pour l'utilisateur si et seulement si aucune
	 * erreur ne survient lors de l'interrogation de la base.
	 * Retourne une réponse personnalisée qui ne contient aucun nom de table 
	 * en cas d'erreur. La réponse décrit l'erreur rencontrée. 
	 * 
	 * @return CustomizedResponseWithData
	 */
	public CustomizedResponseWithData<String> getTables()
	{
		CustomizedResponse cr = this.getMetaData("TABLES", null);
		if (cr == null) return this.readMetaData("Clées primaires récupérées", 3);
		else 			return new CustomizedResponseWithData<String>(cr, null);
	}
	
	
	/**
	 * Ferme proprement les objets statements.
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
		try{
			this.statement = this.connector.dbms().createStatement();
			this.metadata = this.connector.dbms().getMetaData();
		}catch(SQLException e){}
	}
		
	
	/**
	 * Retourne une réponse personnalisée qui décrit les effets 
	 * d'une requête SQL qui ne retourne rien.
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


	/**
	 * Retourne null si et seulement si une requête concernant les méta-données
	 * a été exécutée avec succès et remplit l'attribut de $this avec le résultat de cette requête.
	 * Retourne une réponse personnnalisée qui détaille pourquoi la requête a échouée
	 * dans les autres cas.
	 * 
	 * @param wanted : méta-donnée voule, parmi "TABLES", "PRIMARY KEYS".
	 * @param tableName : nom de la table où chercher les méta-données, 
	 * null ssi $wanted == "TABLES".
	 * @return CustomizedResponseWithData
	 */
	private CustomizedResponse getMetaData(String wanted, String tableName)
	{
		CustomizedResponse result;
		try{
			this.retrieveMetaData(wanted, tableName);
			result = null;
		}
		catch(SQLException e){
			result = new CustomizedResponse(false, e.getMessage());
		}
		return result;
	}


	/**
	 * Interroge le SGBD à propos de ses méta-données $wanted.
	 * Stocke le resultat de la requête dans un attribut de $this.
	 * Lève une exception en cas d'erreur lors de l'interrogation.
	 * 
	 * @param wanted : parmi "TABLES", "PRIMARY KEYS".	 
	 * @param tableName : nom de la table où chercher les clées primaires. 
	 * null ssi $wanted == "TABLES".
	 * @throws SQLException
	 */
	private void retrieveMetaData(String wanted, String tableName) throws SQLException
	{
		switch (wanted){
		case "TABLES" : 
			String [] tab = {"TABLE"};
			this.metaDataResult = 
					this.metadata.getTables(null, this.metadata.getUserName(), "%", tab);
			break;
			
		case "PRIMARY KEYS" :
			this.metaDataResult = 
				this.metadata.getPrimaryKeys(null, this.metadata.getUserName(), tableName);
			break;
		}
	}


	/**
	 * Retourne une réponse personnalisée qui décrit une tentative
	 * pour lire un objet ResulSet contenant les dernières méta-données recherchées.
	 * La réponse contient une collection de données si la lecture ne lève aucune Exception,
	 * sinon la collection est null.
	 * 
	 * @param succesMessage : Message à afficher si réussite, ne doit pas être null.
	 * @param column : numero de la colonne où chercher les méta-données.
	 * @return CustomizedResponseWithData
	 */
	private CustomizedResponseWithData<String> readMetaData(String succesMessage, int column)
	{
		ArrayList<String> data = new ArrayList<String>();
		try{
			while (this.metaDataResult.next()) {
				data.add(this.metaDataResult.getString(column));
			}
			this.metaDataResult.close();
			return new CustomizedResponseWithData<String> (true, succesMessage, data);
		}
		catch(SQLException e){
			return new CustomizedResponseWithData<String> (false, e.getMessage(), null);
		}
		
	}
}

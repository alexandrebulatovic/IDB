package ddl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ddl.create.Table;
import useful.ConnectionManager;
import useful.CustomizedResponse;
import useful.CustomizedResponseWithData;
import useful.ForeinKey;
import ddl.create.Attribute;

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
			e.printStackTrace();
		}
		return valeurs;

	}
	
	private List<String> getPrimaryKeys(String table){
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection co = manager.dbms();
		DatabaseMetaData meta;
		List<String>pks = null;
		try {
			meta = co.getMetaData();
			ResultSet rsKeys = meta.getPrimaryKeys(null, null, table);
			
			pks = new ArrayList<String>();
			while (rsKeys.next()){
				pks.add(rsKeys.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pks;
	}
	
	private boolean isPk(String table, String attributeName){
		List<String>pks = this.getPrimaryKeys(table);
		for (String pkString : pks){
			if (attributeName.equals(pkString)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retourne un type ForeinKey représentant une clé étrangère
	 * selon le nom d'un attribut dans une table
	 *  
	 * @param table
	 * @param attributeName
	 * @return ForeinKey ou 
	 * null si l'attribut n'est pas clé étrangère
	 */
	private ForeinKey getForeinKey(String table, String attributeName){
		
	}
	
	public List<Attribute> getAttributes(String table) throws SQLException{
		List<Attribute> attributes = new ArrayList<Attribute>();
		

		this.createStatementAndMetaData();
		ResultSet rs = this.statement.executeQuery("SELECT a.* FROM "+table+" a");
		
		ConnectionManager manager = ConnectionManager.getInstance();
		Connection co = manager.dbms();
		DatabaseMetaData meta = co.getMetaData();
		
		
		
		ResultSet rsFk = meta.getExportedKeys(null,null,table);

		while (rsFk.next()){
			System.out.println(rsFk.getString(1));//null
			System.out.println(rsFk.getString(2));//user
			System.out.println(rsFk.getString(3));//table
			System.out.println(rsFk.getString(4));//pk REFERENCE
			System.out.println(rsFk.getString(5));//null
			System.out.println(rsFk.getString(6));//user
			System.out.println(rsFk.getString(8));//FK
			System.out.println(rsFk.getString(9));//1 ??
			System.out.println(rsFk.getString(10));//null
			System.out.println(rsFk.getString(12));//nom fk
			
			System.out.println("\n\n");
			new Attribute(table, table, 0, false, false, false, false, table, table)
			
		}
		
		
		
//		ResultSetMetaData rsmd = rs.getMetaData();
////		rs.getFetchSize();
//		int nbColumns = rsmd.getColumnCount();
//		for (int i=1 ; i<=nbColumns ; i++){
//			
//			
//			rsFk.next();
//			String nameAttribute = rsmd.getColumnName(i);
//			System.out.println(nameAttribute);
//			
//			String type = rsmd.getColumnTypeName(i);
//			int size = rsmd.getPrecision(i);
//			
//			boolean nullable = (rsmd.isNullable(i)==ResultSetMetaData.columnNullable);
//			boolean unique = false; //TODO trouver un moyen de détecter si la colonne est unique
//			boolean pk = this.isPk(table, nameAttribute);
//			
//			
//			//String fkTable = rsFk.getString("FKTABLE_NAME");
//			
//			//boolean fk
//			
//			attributes.add(new Attribute(nameAttribute, type, size, nullable, unique, pk, false, null, null));
//		}
		return attributes;
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
		if (cr == null) return this.readMetaData("Tables récupérées", 3);
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

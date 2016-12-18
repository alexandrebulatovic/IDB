package manager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import business.Attribute;
import business.Table;

import useful.Response;
import useful.ResponseData;
import useful.ForeinKey;


public class DDLManager 
{
	//Attributs
	/** Pour créer des requètes SQL.*/
	private Statement statement;
	
	/** Pour obtenir des méta-données sur le SGBD.*/
	private DatabaseMetaData metadata;
	
	/** Stocke les résultats d'une requête sur les meta-données.*/
	private ResultSet metaDataResult;
	
	
	//Constructeur
	/**
	 * Constructeur commun.
	 */
	public DDLManager(Connection connection)
	{
		this.createStatementAndMetaData(connection);
	}
	
	
	//Méthodes
	/**
	 * @return Le nombre de tables existantes dans la base de données
	 * @throws SQLException
	 */
	public int getNbTables() throws SQLException{
		ResultSet rs = this.statement.executeQuery("SELECT COUNT(*) FROM user_tables");
		rs.next();
		return rs.getInt(1);
	}
	
	/**
	 *
	 * @return la liste des tables présentes dans la bdd
	 */
	public List<String> getTablesString(){
//		CustomizedResponseWithData<String>
		//TODO trouver un comprimis avec la méthode getTables.
		List<String> valeurs = new ArrayList<String>();
		try {
			ResultSet rs = this.statement.executeQuery("SELECT TABLE_NAME FROM user_tables");
			while (rs.next()){
				valeurs.add(rs.getString(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return valeurs;

	}
	
	/**
	 * 
	 * @param table
	 * @return une liste de nom de clés primaires ou
	 * null s'il n'en existe pas
	 */
	private List<String> getPrimaryKeys(String table){
		List<String>pks = new ArrayList<String>();
		try {
			ResultSet rsKeys = this.metadata.getPrimaryKeys(null, null, table);
			
			pks = new ArrayList<String>();
			while (rsKeys.next()){
				pks.add(rsKeys.getString("COLUMN_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (pks.isEmpty())
			return null;
		else
			return pks;
	}
	
	private boolean isPk(String table, String attributeName){
		List<String>pks = this.getPrimaryKeys(table);
		if (pks != null){ //s'il existe des clés primaires
			for (String pkString : pks){
				if (attributeName.equals(pkString)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Pour des raisons techniques, il est nécessaire de récupérer
	 * TOUTES les clés étrangères pour les appliqués à un attribut
	 * @param table : String
	 * @return List<ForeinKey>
	 */
	private List<ForeinKey> getAllForeinKeys(){
		List<String> tables = this.getTablesString();
		List<ForeinKey> fks = new ArrayList<ForeinKey>();
		try {
			ResultSet rsFk;
//			String name,String table,String fk, String ref,String user, String userTarget
			
			for (String t : tables){
				rsFk = this.metadata.getExportedKeys(null,null,t);
				while (rsFk.next()){
					fks.add(//TODO mettre des id plus lisibles
					new ForeinKey(	rsFk.getString("FK_NAME"),
									rsFk.getString("PKTABLE_SCHEM"), 
									rsFk.getString("PKTABLE_NAME"),
									rsFk.getString("PKCOLUMN_NAME"),
									
									rsFk.getString("FKTABLE_SCHEM"), 
									rsFk.getString("FKTABLE_NAME"), 
									rsFk.getString("FKCOLUMN_NAME")) 
					);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (fks.isEmpty())
			return null;
		else
			return fks;
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
	private ForeinKey getForeinKey(String table,String attributeName){
		List<ForeinKey>fks = this.getAllForeinKeys();
		if (fks != null){ //s'il existe des clés étrangères
			for (ForeinKey fk : fks){
				if (fk.getFkColumnName().equals(attributeName) && fk.getFkTableName().equals(table)){
					return fk;
				}
			}
		}
		return null;
	}

	
	public List<Attribute> getAttributes(String table) throws SQLException{
		
//
//		for (ForeinKey k : this.getAllForeinKeys()){
//			System.out.println(k.toString());
//		}
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		ResultSet rs = this.statement.executeQuery("SELECT a.* FROM "+table+" a");
	
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int nbColumns = rsmd.getColumnCount();
		for (int i=1 ; i<=nbColumns ; i++){
			
			String nameAttribute = rsmd.getColumnName(i);
			
			String type = rsmd.getColumnTypeName(i);
			int size = rsmd.getPrecision(i);
			
			boolean nullable = !(rsmd.isNullable(i)==ResultSetMetaData.columnNullable);
			boolean unique = false; //TODO trouver un moyen de détecter si la colonne est unique
			boolean pk = this.isPk(table, nameAttribute);//TODO le problème, c'est que la meme requette est envoyé à chaque attribut !
			ForeinKey fk = this.getForeinKey(table, nameAttribute);//TODO meme problème (solution ? Créer une classe InfoTables).
			String fkTable;
			String fkAttribute;
			if (fk==null){
				fkTable=null;
				fkAttribute=null;
			}
			else{
				fkTable = fk.getPkTableName();
				fkAttribute = fk.getPkColumnName();
			}
			
			attributes.add(new Attribute(
					nameAttribute, 
					type, 
					size, 
					nullable, 
					unique, 
					pk, 
					!(fk==null), 
					fkTable, 
					fkAttribute)
					);
		}
		return attributes;
	}

	
	/**
	 * Tente de créer une $table.
	 * Retourne une réponse personnalisée qui décrit la tentative.
	 * 
	 * @param table : une table à créer.
	 * @return CustomizedResponse
	 */
	public Response createTable(Table table)
	{	
		System.out.println(table);
		Response result = this.executeUpdate(table.toCreate());
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
	public Response dropTable(Table table)
	{
		Response result = this.executeUpdate(table.toDrop());
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
	public ResponseData<String> getPrimaryKey(String table)
	{
		Response cr = this.getMetaData("PRIMARY KEYS", table);
		if (cr == null) return this.readMetaData("Clées primaires récupérées", 4);
		else 			return new ResponseData<String>(cr, null);
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
	public ResponseData<String> getTables()
	{
		Response cr = this.getMetaData("TABLES", null);
		if (cr == null) return this.readMetaData("Tables récupérées", 3);
		else 			return new ResponseData<String>(cr, null);
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
	private void createStatementAndMetaData(Connection connection ) 
	{
		try{
			this.statement = connection.createStatement();
			this.metadata = connection.getMetaData();
		}catch(SQLException e){}
	}
		
	
	/**
	 * Retourne une réponse personnalisée qui décrit les effets 
	 * d'une requête SQL qui ne retourne rien.
	 * 
	 * @param sql : une requête SQL qui ne retourne rien.
	 * @return CustomizedResponse
	 */
	private Response executeUpdate(String sql)
	{
		System.out.println(sql);
		Response result;
		try{
			this.statement.executeUpdate(sql);
			result = new Response(true);
		}
		catch(SQLException e){
			result = new Response(false, e.getMessage());
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
	private Response getMetaData(String wanted, String tableName)
	{
		Response result;
		try{
			this.retrieveMetaData(wanted, tableName);
			result = null;
		}
		catch(SQLException e){
			result = new Response(false, e.getMessage());
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
	private ResponseData<String> readMetaData(String succesMessage, int column)
	{
		ArrayList<String> data = new ArrayList<String>();
		try{
			while (this.metaDataResult.next()) {
				data.add(this.metaDataResult.getString(column));
			}
			this.metaDataResult.close();
			return new ResponseData<String> (true, succesMessage, data);
		}
		catch(SQLException e){
			return new ResponseData<String> (false, e.getMessage(), null);
		}
		
	}
}

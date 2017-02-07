package business;

import java.util.ArrayList;
import java.util.List;

/** Conteneur de tables. */

public class TableSet {
	
	private List<Table> tables;
	
	private boolean tablesLoaded = false;
	
	
	public TableSet(){
		tables = new ArrayList<Table>();
	}
	
	public boolean isLoaded(){
		return this.tablesLoaded;
	}
	
	public List<String> getTablesNames(){
		List<String> retour = new ArrayList<String>();
		for (Table table : tables){
			retour.add(table.getName());
		}
		return retour;
	}
	
	
	/**
	 * Retourne true s'il y a eu des erreurs
	 * @param tablesNames
	 * @return
	 */
	public void loadTables(List<String>tablesNames){
		this.tablesLoaded = true;
		for (String tableName : tablesNames){
			this.tables.add(new Table(tableName));
		}
	}
	
	/**
	 * ajoute une table si c'est possible (si aucune autres table n'existe),
	 * sinon garde l'ancienne.<br>
	 * il est nécessaire d'utiliser {@link business.TableSet#isAddable(String)} avant d'ajouter une table
	 * (de préférence)
	 * @param tableName
	 * @param cascadeConstraint
	 * @see business.TableSet#isAddable(String)
	 */
	public void addTable(String tableName){
		if (this.isAddable(tableName)){
			this.tables.add(new Table(tableName));
		}
	}
	
	
	public boolean isAddable(String tableName){
		if (this.getTableWithName(tableName)==null){
			return true;
		}
		return false;
	}
	
	
	/**
	 * Ajoute un attribut dans la table spécifié
	 * vérifie les contraintes et si la table existe
	 * ajoute si nécessaire la clé primaire sur l'attribut
	 * Cette méthode part du principe qu'il ne peu y avoir qu'une clé primaire
	 * par table (gère automatiquement les groupes de clé primaires pour
	 * un attribut donné)
	 * @param tableName
	 * @param attributeName
	 * @param type
	 * @param size
	 * @param notNull
	 * @param primaryKey
	 * @return boolean
	 */
	public boolean addAttributeToTable(String tableName,String attributeName, String type, int size, boolean notNull,boolean primaryKey){
		for (Table table:tables){
			if (table.getName().equals(tableName) && !table.containsAttributeName(attributeName)){
				Attribute newAttribute = new Attribute(attributeName, type, size, null, tableName, notNull);
				
				if (primaryKey){
					ajouterPrimaryKey(table, newAttribute);
				}
				
				
				return table.addAttribute(newAttribute);
			}
		}
		return false;
	}

	private void ajouterPrimaryKey(Table table, Attribute a) {
		boolean havePk = false;
		for (Attribute attributeActual : table.getAttributes()){
			if (attributeActual.isPk()){
				havePk = true;
				PrimaryKeyConstraint pk = attributeActual.getPk();
				pk.addAttribute(a);
			}
		}
		if (!havePk){
			PrimaryKeyConstraint pk = new PrimaryKeyConstraint();
			pk.addAttribute(a);
			pk.setTable(table);
			a.addConstraint(pk);
			table.addConstraint(pk);
		}
	}
	

	/**
	 * retourne une liste de requettes permettant de modifier la table
	 * passé en paramètres
	 * @exemple [CREATE TABLE table(...),ALTER TABLE table... , ALTER T...,...]
	 * @see business.Table#toCreate(Table)
	 * @param tableName
	 * @return ArrayList<String> sqlString
	 */
	public List<String> getSQLTableToCreate(String tableName){
		for (Table table:tables){
			if (table.getName().equals(tableName)){
				return table.toCreate();
			}
		}
		return null;
	}
	
	/**
	 * retourne une liste de requettes permettant de modifier la table
	 * passé en paramètres
	 * @see business.Table#toModify(Table)
	 * @param tableName
	 * @return ArrayList<String> sqlString
	 */
	public ArrayList<String> getSQLTableToModify(String tableName){
		return null;//TODO
	}
	
	
	/**
	 * Supprime une table s'après son nom
	 * et s'occupe de supprimer les sous attributs
	 * et les contraintes associés 
	 * @param table
	 * @return true en cas de succès<br>
	 * false en cas d'erreur
	 */
	public boolean removeTable(String table)
	{
		Table laTable = this.getTableWithName(table);
		laTable.cleanAll();
		
		return this.tables.remove(laTable);
	}
	
	
	/**
	 * @param tableName
	 * @return Retourne une table d'après son nom<br>
	 * Retourne null en cas d'échec ou si la table n'existe pas
	 */
	private Table getTableWithName(String tableName){		
		for (Table table : tables){
			if (table.getName().equals(tableName)){
				return table;
			}
		}
		return null;
	}
}

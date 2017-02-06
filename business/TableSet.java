package business;

import java.util.ArrayList;
import java.util.List;

/** Conteneur de tables. */

public class TableSet {
	
	public List<Table> tables;
	
	boolean tablesLoaded = false;
	
	
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
	 * retourne false en cas d'échec
	 * @param tableName
	 * @param cascadeConstraint
	 * @return
	 */
	public boolean addTable(String tableName){
		for (Table table:tables){
			if (table.getName().equals(tableName)){
				return false;
			}
		}
		return this.tables.add(new Table(tableName));
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
	

	
	public ArrayList<String> getSQLTableToCreate(String tableName){
		for (Table table:tables){
			if (table.getName().equals(tableName)){
				return table.toCreate();
			}
		}
		return null;
	}
	
	
}

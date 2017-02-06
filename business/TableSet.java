package business;

import java.util.ArrayList;
import java.util.List;

public class TableSet {
	private List<Table> tables;
	
	
	public TableSet(){
		tables = new ArrayList<Table>();
	}
	
	
	public List<String> getTablesNames(){
		List<String> retour = new ArrayList<String>();
		for (Table table : tables){
			retour.add(table.getName());
		}
		return retour;
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
	
	
	public boolean addAttributeToTable(String tableName,String AttributeName, String type, int size, boolean notNull,boolean primaryKey){
		for (Table table:tables){
			if (table.getName().equals(tableName)){
				Attribute a = new Attribute(AttributeName, type, size, null, tableName, notNull);
				return table.addAttribute(a);
			}
		}
		return false;
	}
	
	public boolean addPrimaryKey(String tableName, List<String> AttributeName){
		
		
		return true;
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

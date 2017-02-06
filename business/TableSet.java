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
		return this.tables.add(new Table(tableName,false));
	}
	
	
	public boolean addAttributeToTable(String tableName,String AttributeName, String type, int size, boolean notNull){
		for (Table table:tables){
			if (table.getName().equals(tableName)){
				Attribute a = new Attribute(AttributeName, type, size, null, tableName, notNull);
				return table.addAttribute(a);
			}
		}
		return false;
	}
	
//	public boolean addConstraintToAttributeToTable(String tableName, String AttributeName){
//		
//		
//		return false;
//	}
	
	
}

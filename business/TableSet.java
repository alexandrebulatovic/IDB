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
	
	
	public boolean addTable(String tableName,boolean cascadeConstraint){
		//TODO verifier que c'est possible et l'ajoute
		return false;
	}
	
	public boolean addAttributeToTable(String tableName,String AttributeName, String type, int size, boolean notNull){
		//TODO ++ vérifications
		return false;
	}
	
	public boolean addConstraintToAttributeToTable(String tableName, String AttributeName){
		
		
		return false;
	}
	
	
}

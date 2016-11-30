package create;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class AttributesAbstractTableModel 
extends AbstractTableModel {

	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();

	private final String[] header = {"Nom Attribut", "Type", "Taille", "NOT NULL", "UNIQUE","PRIMARY KEY","FOREIGN KEY","Table","Attribut"};

	public AttributesAbstractTableModel() {
		super();
	}

	public int getRowCount() {
		return attributes.size();
	}

	public int getColumnCount() {
		return header.length;
	}

	public String getColumnName(int columnIndex) {
		return header[columnIndex];
	}
	public Attribute getAttributeAt(int rowIndex){
		Attribute a = this.attributes.get(rowIndex);
		return a;
	}
			
			
			
			
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
		case 0:
			return attributes.get(rowIndex).name;
		case 1:
			return attributes.get(rowIndex).type;
		case 2:
			return attributes.get(rowIndex).size;
		case 3:
			return attributes.get(rowIndex).notNull;
		case 4:
			return attributes.get(rowIndex).unique;
		case 5:
			return attributes.get(rowIndex).primaryKey;
		case 6:
			return attributes.get(rowIndex).foreignKey;
		case 7:
			return attributes.get(rowIndex).fkTable;
		case 8:
			return attributes.get(rowIndex).fkAttribute;
		default:
			return null; 
		}
	}
	public boolean isDuplicateAttributeName(Attribute a){
		boolean res = false;
		for(Attribute object: attributes){
			if(object.equals(a)){
				res=true;
			}	
		}
		return res;
	}

	public int addAttribute(Attribute atrribute) {
		if(!(isDuplicateAttributeName(atrribute))){
		attributes.add(atrribute);
		fireTableRowsInserted(attributes.size()-1, attributes.size()-1);
			return 1;
		}else{
			return 0;
		}
	}
	
	public void removeAll(){
		 for(int i = this.attributes.size() - 1; i >= 0; i--){
			 removeAttributes(i);
         }
	}
	

	public void removeAttributes(int rowIndex) {
		attributes.remove(rowIndex);
		fireTableRowsDeleted(rowIndex, rowIndex);
	}
	
	public ArrayList<Attribute> getAttributes(){
		return this.attributes;
	}
	
	public boolean isEmpty(){
		if(attributes.size() == 0){
			return true;
		}else{
			return false;
		}
		
	}
}

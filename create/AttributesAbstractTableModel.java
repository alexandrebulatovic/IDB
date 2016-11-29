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

	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex){
		case 0:
			return attributes.get(rowIndex).getName();
		case 1:
			return attributes.get(rowIndex).getType();
		case 2:
			return attributes.get(rowIndex).getSize();
		case 3:
			return attributes.get(rowIndex).isNotNull();
		case 4:
			return attributes.get(rowIndex).isUnique();
		case 5:
			return attributes.get(rowIndex).isPrimaryKey();
		case 6:
			return attributes.get(rowIndex).isForeignKey();
		case 7:
			return attributes.get(rowIndex).getFkTable();
		case 8:
			return attributes.get(rowIndex).getFkAttribute();
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

	public void addAttribute(Attribute atrribute) {
		if(!(isDuplicateAttributeName(atrribute))){
		attributes.add(atrribute);
		fireTableRowsInserted(attributes.size()-1, attributes.size()-1);
		}
	}
	
	

	public void removeAttributes(int rowIndex) {
		attributes.remove(rowIndex);

		fireTableRowsDeleted(rowIndex, rowIndex);
	}
	
	public ArrayList<Attribute> getAttributes(){
		return this.attributes;
	}
}

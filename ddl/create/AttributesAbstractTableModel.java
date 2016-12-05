package ddl.create;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;


public class AttributesAbstractTableModel 
extends AbstractTableModel {

	/**
	 * Liste des attributs présent dans le tableau
	 */
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();

	/**
	 * Initialise l'en-tête
	 */
	private final String[] header = {"Nom Attribut", "Type", "Taille", "NOT NULL", "UNIQUE","PRIMARY KEY","FOREIGN KEY","Table","Attribut"};

	/**
	 * Constructeur 
	 */
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
	/**
	 * @param rowIndex
	 * @return Attribute
	 */
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
	/**
	 * @param a
	 * @return boolean
	 */
	public boolean isDuplicateAttributeName(Attribute a){
		boolean res = false;
		for(Attribute object: attributes){
			if(object.equals(a)){
				res=true;
			}	
		}
		return res;
	}

	/**
	 * Ajoute un attribut dans la Table et dans l'ArrayList
	 * @param atrribute
	 * @return int
	 */
	public int addAttribute(Attribute atrribute) {
		if(!(isDuplicateAttributeName(atrribute))){
		attributes.add(atrribute);
		fireTableRowsInserted(attributes.size()-1, attributes.size()-1);
			return 1;
		}else{
			return 0;
		}
	}
	
	/**
	 * Supprime tout les attribut de la Table et de l'ArrayList
	 */
	public void removeAll(){
		 for(int i = this.attributes.size() - 1; i >= 0; i--){
			 removeAttributes(i);
         }
	}
	

	/**
	 * Supprime un attribut dans la Table et dans l'ArrayList
	 * @param rowIndex
	 */
	public void removeAttributes(int rowIndex) {
		attributes.remove(rowIndex);
		fireTableRowsDeleted(rowIndex, rowIndex);
	}
	
	/**
	 * @return ArrayList<Attribute>
	 */
	public ArrayList<Attribute> getAttributes(){
		return this.attributes;
	}
	
	/**
	 * @return boolean
	 */
	public boolean isEmpty(){
		if(attributes.size() == 0){
			return true;
		}else{
			return false;
		}
		
	}
}

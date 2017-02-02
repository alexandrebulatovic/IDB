package ddl;

import java.util.LinkedHashSet;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import business.Attribute;




public class AttributesAbstractTableModel 
extends AbstractTableModel {

	/**
	 * Liste des attributs présent dans le tableau
	 */
	private LinkedHashSet<Attribute> attributes = new LinkedHashSet<Attribute>();

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
		int i = 0;
		Iterator <Attribute> iterator = this.attributes.iterator();
		Attribute result = null; //compilateur chiale
		
		while (iterator.hasNext() && i <= rowIndex) {
			result = iterator.next();
			i++;
		}
		return result;
	}
			
			
			
			
	public Object getValueAt(int rowIndex, int columnIndex) {
		Attribute a = this.getAttributeAt(rowIndex);
		switch(columnIndex){
		case 0:
			return a.name;
		case 1:
			return a.type;
		case 2:
			return a.size;
		case 3:
			return a.notNull;
		case 4:
			return a.unique;
		case 5:
			return a.primaryKey;
		case 6:
			return a.foreignKey;
		case 7:
			return a.fkTable;
		case 8:
			return a.fkAttribute;
		default:
			return null; 
		}
	}
	
	public void setAttributeValueAt(int rowIndex, String name, String type, int size, boolean notNull, 
			boolean unique, boolean pk, boolean fk, 
			String fkTable, String fkAttribute){
		Attribute select = this.getAttributeAt(rowIndex);
		select.name=name;
		select.type=type;
		select.size=size;
		select.notNull=notNull;
		select.unique=unique;
		select.primaryKey=pk;
		select.foreignKey=fk;
		select.fkTable=fkTable;
		select.fkAttribute=fkAttribute;
		
	}
	
	public void setAttributeValueAt(int rowIndex, Attribute a){
		Attribute select = this.getAttributeAt(rowIndex);
		select.name=a.name;
		select.type=a.type;
		select.size=a.size;
		select.notNull=a.notNull;
		select.unique=a.unique;
		select.primaryKey=a.primaryKey;
		select.foreignKey=a.foreignKey;
		select.fkTable=a.fkTable;
		select.fkAttribute=a.fkAttribute;
	}
	/**
	 * @param a
	 * @return booleans
	 */
	public boolean isDuplicateAttributeName(Attribute a){
		boolean res = false;
		for(Attribute object: attributes){
			if(object.name.equals(a.name)){
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
	
	public void changeAttributePosition(String direction, int rowIndex){
		Attribute select;
		Attribute overSelect;
		switch (direction){
		case "UP" : 
			changePosition(rowIndex,-1);
			break;
			
		case "DOWN" :
			changePosition(rowIndex,+1);
			break;
		}
	}

	private void changePosition(int rowIndex,int direction ) {
		Attribute select;
		Attribute overSelect;
		select = new Attribute(this.getAttributeAt(rowIndex));
		overSelect= new Attribute(this.getAttributeAt(rowIndex+direction));
		this.setAttributeValueAt(rowIndex+direction, select.name, select.type, select.size, select.notNull, select.unique, select.primaryKey, select.foreignKey, select.fkTable, select.fkAttribute);
		this.setAttributeValueAt(rowIndex, overSelect.name, overSelect.type, overSelect.size, overSelect.notNull, overSelect.unique, overSelect.primaryKey, overSelect.foreignKey, overSelect.fkTable, overSelect.fkAttribute);
		this.fireTableDataChanged();
	}

	/**
	 * Supprime un attribut dans la Table et dans l'ArrayList
	 * @param rowIndex
	 */
	public void removeAttributes(int rowIndex) {
		attributes.remove(this.getAttributeAt(rowIndex));
		fireTableRowsDeleted(rowIndex, rowIndex);
	}
	
	/**
	 * @return ArrayList<Attribute>
	 */
	public LinkedHashSet<Attribute> getAttributes(){
		return this.attributes;
	}
	
	public Attribute createAttribute(String name, String type, String size, boolean notNull, 
			boolean unique, boolean pk, boolean fk, 
			String fkTable, String fkAttribute){
		Attribute a;
		if(fk){
			a = new Attribute(name,type,Integer.parseInt(size),notNull,unique,pk,fk,fkTable,fkAttribute);
		}else{
			a = new Attribute(name,type,Integer.parseInt(size),notNull,unique,pk,fk,"N/A","N/A");
		}
		return a;
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

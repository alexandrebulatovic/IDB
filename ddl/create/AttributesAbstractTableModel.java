package ddl.create;

import java.util.LinkedHashSet;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;


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
		attributes.remove(this.getAttributeAt(rowIndex));
		fireTableRowsDeleted(rowIndex, rowIndex);
	}
	
	/**
	 * @return ArrayList<Attribute>
	 */
	public LinkedHashSet<Attribute> getAttributes(){
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

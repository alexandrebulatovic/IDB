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
	private LinkedHashSet<AttributeModel> attributes = new LinkedHashSet<AttributeModel>();

	/**
	 * Initialise l'en-tête
	 */
	private final String[] header = {"Nom Attribut", "Type", "Taille", "NOT NULL","PRIMARY KEY"};

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
	 * @return AttributeModel
	 */
	public AttributeModel getAttributeAt(int rowIndex){
		int i = 0;
		Iterator <AttributeModel> iterator = this.attributes.iterator();
		AttributeModel result = null; //compilateur chiale
		
		while (iterator.hasNext() && i <= rowIndex) {
			result = iterator.next();
			i++;
		}
		return result;
	}
			
			
			
			
	public Object getValueAt(int rowIndex, int columnIndex) {
		AttributeModel a = this.getAttributeAt(rowIndex);
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
			return a.primaryKey;
		default:
			return null; 
		}
	}
	
	public void setAttributeValueAt(int rowIndex, String name, String type, int size, boolean notNull, boolean primaryKey){
		AttributeModel select = this.getAttributeAt(rowIndex);
		select.name=name;
		select.type=type;
		select.size=size;
		select.notNull=notNull;
		select.primaryKey=primaryKey;
		
	}
	
	public void setAttributeValueAt(int rowIndex, AttributeModel a){
		AttributeModel select = this.getAttributeAt(rowIndex);
		select.name=a.name;
		select.type=a.type;
		select.size=a.size;
		select.notNull=a.notNull;
		select.primaryKey=a.primaryKey;
	}
	/**
	 * @param a
	 * @return booleans
	 */
	public boolean isDuplicateAttributeName(String name){
		boolean res = false;
		for(AttributeModel object: attributes){
			if(object.name.equals(name)){
				res=true;
			}	
		}
		return res;
	}

	/**
	 * Ajoute un attribut dans la Table et dans l'ArrayList
	 * @param attribute
	 * @return int
	 */
	public int addAttribute(String name, String type, int size, boolean notNull, boolean primaryKey) {
		if(!(isDuplicateAttributeName(name))){
			AttributeModel attribute = new AttributeModel(name,type,size,notNull,primaryKey);
		attributes.add(attribute);
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
		AttributeModel select;
		AttributeModel overSelect;
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
		AttributeModel select;
		AttributeModel overSelect;
		select = new AttributeModel(this.getAttributeAt(rowIndex));
		overSelect= new AttributeModel(this.getAttributeAt(rowIndex+direction));
		this.setAttributeValueAt(rowIndex+direction, select.name, select.type, select.size, select.notNull, select.primaryKey);
		this.setAttributeValueAt(rowIndex, overSelect.name, overSelect.type, overSelect.size, overSelect.notNull, overSelect.primaryKey);
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
	public LinkedHashSet<AttributeModel> getAttributes(){
		return this.attributes;
	}
	
	public AttributeModel createAttribute(String name, String type, String size, boolean notNull, 
			boolean unique, boolean pk, boolean fk, 
			String fkTable, String fkAttribute){
		AttributeModel a;
		if(fk){
			a = new AttributeModel(name,type,Integer.parseInt(size),notNull,pk);
		}else{
			a = new AttributeModel(name,type,Integer.parseInt(size),notNull,pk);
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
	
	public int checkSizeAttributes(int type,int size){
		if(type == 1){
			if(size == 0 || size > 255){
				return -1;
			}else{
				return 1;
			}
		}else if (type == 2){
			if(size == 0 || size > 38){
				return -2;
			}else{
				return 2;
			}
		}else if (type == 4){
			if(size == 0 || size > 255){
				return -3;
			}else{
				return 3;
			}
	
		}else{
			return 0;
		}
	}
	public String attributeSizeError(int i){
		switch (i){
			case -1 : return "1 <= taille VARCHAR <= 255";
			case -2 : return "1 <= taille NUMBER <= 38";
			case -3 : return "1 <= taille CHAR <= 255";
			default : return "";
		}
	}

}

package gui.ddl.tools;

import java.util.LinkedHashSet;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import controller.DDLController;


@SuppressWarnings("serial")
public class AttributesAbstractTableModel 
extends AbstractTableModel 
{
	private DDLController control;
	
	/**
	 * Liste des attributs présent dans le tableau
	 */
	private LinkedHashSet<I_AttributeModel> attributes = new LinkedHashSet<I_AttributeModel>();

	/**
	 * Initialise l'en-tête
	 */
	private final String[] header = {"Nom Attribut", "Type", "Taille", "NOT NULL","PRIMARY KEY"};

	/**
	 * Constructeur 
	 */
	public AttributesAbstractTableModel(DDLController control) {
		super();
		this.control = control;
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
	public I_AttributeModel getAttributeAt(int rowIndex){
		int i = 0;
		Iterator <I_AttributeModel> iterator = this.attributes.iterator();
		I_AttributeModel result = null; //compilateur chiale

		while (iterator.hasNext() && i <= rowIndex) {
			result = iterator.next();
			i++;
		}
		return result;
	}




	public Object getValueAt(int rowIndex, int columnIndex) {
		I_AttributeModel a = this.getAttributeAt(rowIndex);
		switch(columnIndex){
		case 0:
			return a.getName();
		case 1:
			return a.getType();
		case 2:
			return a.getSize();
		case 3:
			return a.isNotNull();
		case 4:
			return a.isPrimaryKey();
		default:
			return null; 
		}
	}

	public void setAttributeValueAt(int rowIndex, String name, String type, int size, boolean notNull, boolean primaryKey){
		I_AttributeModel select = this.getAttributeAt(rowIndex);
		select.setName(name);
		select.setType(type);
		select.setSize(size);
		select.setNotNull(notNull);
		select.setPrimaryKey(primaryKey);

	}

	public void setAttributeValueAt(int rowIndex, I_AttributeModel a){
		I_AttributeModel select = this.getAttributeAt(rowIndex);
		select.setName(a.getName());
		select.setType(a.getType());
		select.setSize(a.getSize());
		select.setNotNull(a.isNotNull());
		select.setPrimaryKey(a.isPrimaryKey());
	}
	/**
	 * @param a
	 * @return booleans
	 */
	public boolean isDuplicateAttributeName(I_AttributeModel attribute){
		boolean res = false;
		for(I_AttributeModel object: attributes){
			if(object.getName().equals(attribute.getName())){
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
	public int addAttribute(I_AttributeModel attribute) {
		if(!(isDuplicateAttributeName(attribute))){
			attributes.add(attribute);
			this.fireTableRowsInserted(0, attributes.size());
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
		I_AttributeModel select = this.getAttributeAt(rowIndex);
		I_AttributeModel overSelect = this.getAttributeAt(rowIndex+direction);
		I_AttributeModel overSelectCache = this.control.getAttributeModel(overSelect.getName(), overSelect.getType(), overSelect.getSize(), overSelect.isNotNull(), overSelect.isPrimaryKey());
		this.setAttributeValueAt(rowIndex+direction, select.getName(), select.getType(), select.getSize(), select.isNotNull(), select.isPrimaryKey());
		this.setAttributeValueAt(rowIndex, overSelectCache.getName(), overSelectCache.getType(), overSelectCache.getSize(), overSelectCache.isNotNull(), overSelectCache.isPrimaryKey());
		this.fireTableRowsUpdated(rowIndex, rowIndex+direction);
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
	public LinkedHashSet<I_AttributeModel> getAttributes(){
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

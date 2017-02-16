package gui.ddl.tools;

import java.util.List;

public class AttributeQBE {
	private String tableName;

	private String attributeName;
	
	private boolean selected;
	
	private List<String> where;
	
	public AttributeQBE(String tableName,String attributeName,boolean selected,List<String> where){
		this.tableName = tableName;
		this.attributeName=attributeName;
		this.selected = selected;
		this.where=where;
	}

	@Override
	public String toString() {
		return "AttributeQBE [tableName=" + tableName + ", attributeName=" + attributeName + ", selected=" + selected
				+ ", where=" + where + "]";
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public List<String> getWhere() {
		return where;
	}

	public void setWhere(List<String> where) {
		this.where = where;
	}
}

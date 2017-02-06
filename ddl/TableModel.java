package ddl;

import java.util.ArrayList;
import java.util.List;

public class TableModel implements I_Table{

	private String name;

	private List<I_Attribute> attributes;

	public TableModel(String name){
		this.setName(name);
		attributes = new ArrayList<I_Attribute>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public List<I_Attribute> getAttributes() {
		return this.attributes;
	}

	@Override
	public void setName(String name) {
		this.name = name;		
	}

	@Override
	public I_Attribute getAttribute(String name) {
		for (I_Attribute attribute : attributes){
			if(attribute.getName().equals(name)){
				return attribute;
			}
		}
		return null;
	}

	@Override
	public void addAttribute(I_Attribute attribute) {
		if(!attributes.contains(attribute))
			this.attributes.add(attribute);
	}

	@Override
	public void removeAttribute(I_Attribute attribute) {
		this.attributes.remove(attribute);
	}

	@Override
	public String toString() {
		return "TableModel [name=" + name + ", attributes=" + attributes + "]";
	}

}

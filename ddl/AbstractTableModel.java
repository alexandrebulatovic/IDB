package ddl;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTableModel 
implements I_TableModel
{

	private String name;

	private List<I_AttributeModel> attributes;

	protected AbstractTableModel()
	{
		this("t");
	}
	
	public AbstractTableModel(String name){
		this.setName(name);
		attributes = new ArrayList<I_AttributeModel>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public List<I_AttributeModel> getAttributes() {
		return this.attributes;
	}

	@Override
	public void setName(String name) {
		this.name = name;		
	}

	@Override
	public I_AttributeModel getAttribute(String name) {
		for (I_AttributeModel attribute : attributes){
			if(attribute.getName().equals(name)){
				return attribute;
			}
		}
		return null;
	}

	@Override
	public void addAttribute(I_AttributeModel attribute) {
		if(!attributes.contains(attribute))
			this.attributes.add(attribute);
	}

	@Override
	public void removeAttribute(I_AttributeModel attribute) {
		this.attributes.remove(attribute);
	}

	
	@Override
	public List<String[]> attributesToArray()
	{
		List<String[]>result = new ArrayList<String[]>();
		for (I_AttributeModel ia : this.attributes) {
			result.add(ia.toArray());
		}
		return result;
	}
	
	
	@Override
	public String toString() {
		return "TableModel [name=" + name + ", attributes=" + attributes + "]";
	}

}

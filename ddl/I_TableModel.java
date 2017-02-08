package ddl;

import java.util.List;

public interface I_TableModel {

	/**
	 * @return le nom de la table.
	 */
	public abstract String getName();
	
	public abstract List<I_AttributeModel> getAttributes();
	
	public abstract void setName(String name);
	
	public abstract I_AttributeModel getAttribute(String name);
	
	public abstract void addAttribute(I_AttributeModel attribute);
	
	public abstract void removeAttribute(I_AttributeModel attribute);
}

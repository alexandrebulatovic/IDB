package ddl;

import java.util.List;

public interface I_Table {

	/**
	 * @return le nom de la table.
	 */
	public abstract String getName();
	
	public abstract List<I_Attribute> getAttributes();
	
	public abstract void setName(String name);
	
	public abstract I_Attribute getAttribute(String name);
	
	public abstract void addAttribute(I_Attribute attribute);
	
	public abstract void removeAttribute(I_Attribute attribute);
}

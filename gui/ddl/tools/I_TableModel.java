package gui.ddl.tools;

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
	
	/**
	 * @return une liste d'attributs, chaque attribut contenant :<br/>
	 * - le nom de l'attribut,<br/>
	 * - le type de données de l'attribut, <br/>
	 * - la taille de l'attribut,<br/>
	 * - NOTNULL ssi l'attribut est sous contrainte not null,<br/>
	 * - PRIMARY ssi l'attribut est membre de la clée primaire.
	 */
	public abstract List<String[]> attributesToArray();
}

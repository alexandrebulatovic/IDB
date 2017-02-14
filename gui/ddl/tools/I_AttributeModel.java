package gui.ddl.tools;

public interface I_AttributeModel 
{
	/**
	 * @return le nom de l'attribut.
	 */
	public abstract String getName();

	
	/**
	 * @return le type de l'attribut.
	 */
	public abstract String getType();

	
	/**
	 * @return la taille de l'attribut.
	 */
	public abstract int getSize();

	
	/**
	 * @return vrai si et seulement si l'attribut ne peut pas être null, faux sinon.
	 */
	public abstract boolean isNotNull();

	
	/**
	 * @return vrai si et seulement si l'attribut est membre de la clée primaire, faux sinon.
	 */
	public abstract boolean isPrimaryKey();

	
	/**
	 * Définit le nom de l'attribut.
	 * 
	 * @param name : null interdit.
	 */
	public abstract void setName(String name);


	/**
	 * Définit le typde de donnée de l'attribut.
	 * 
	 * @param type : null interdit.
	 */
	public abstract void setType(String type);

	
	/**
	 * Définit si l'attribut admet des valeurs nulles ou non.
	 * 
	 * @param notNull : vrai si et seulement si l'attribut ne peut pas être null, 
	 * faux sinon.
	 */
	public abstract void setNotNull(boolean notNull);

	
	/**
	 * Définit la taille de l'attribut.
	 * 
	 * @param size : 0 < size.
	 */
	public abstract void setSize(int size);
	
	/**
	 * Définit si l'attribut est membre de la clée primaire.
	 * 
	 * @param primaryKey : vrai si et seulement si l'attribut est memebre de la clée primaire,
	 * faux sinon.
	 */
	public abstract void setPrimaryKey(boolean primaryKey);
	
	
	/**
	 * @return vrai si et seulement si l'attribut possède une taille accordée avec
	 * son type, faux sinon.
	 */
	public abstract boolean checkSize();
	
	
	/**
	 * @return un message indiquant si la taille de l'attribut est cohérente avec son type.
	 */
	public abstract String sizeErrorMsg();
	
	
	/**
	 * @return un tableau contenant :<br/>
	 * - le nom de l'attribut,<br/>
	 * - le type de données de l'attribut, <br/>
	 * - la taille de l'attribut,<br/>
	 * - NOTNULL ssi l'attribut est sous contrainte not null,<br/>
	 * - PRIMARY ssi l'attribut est membre de la clée primaire.
	 */
	public abstract String [] toArray();

}

package gui.ddl.tools;

public class MySQLAttributeModel
extends AbstractAttributeModel
{
	//Constructeur
	/**
	 * Constructeur vide.
	 */
	public MySQLAttributeModel()
	{
		super();
	}
	
	
	/**
	 * Constructeur commun.
	 * 
	 * @param name : le nom de l'attribut, null interdit.
	 * @param type : le type de l'attribut, null interdit.
	 * @param size : la taille de l'attribut.
	 * @param notNull : vrai si et seulement si l'attribut ne peut pas être null, 
	 * faux sinon.
	 * @param primaryKey : vrai si et seulement si l'attribut est membre de la 
	 * clée primaire, faux sinon.
	 */
	public MySQLAttributeModel
	(String name, String type, int size, boolean notNull, boolean primaryKey) 
	{
		super(name, type ,size, notNull, primaryKey);
	}


	/**
	 * Constructeur par recopie.
	 * 
	 * @param attribute : null interdit.
	 */
	public MySQLAttributeModel(MySQLAttributeModel attributeAt) 
	{
		super(attributeAt);
	}


	//Méthodes.
	@Override
	public String sizeErrorMsg()
	{
		switch (this.errorSizeCode()){
		case -1 : return "1 <= taille VARCHAR <= 255";
		case -2 : return "1 <= taille NUMERIC <= 64";
		case -3 : return "1 <= taille CHAR <= 255";
		default : return "";
		}
	}


	@Override
	protected int errorSizeCode()
	{
		switch (this.type) {
		case "VARCHAR" : return (size == 0 || size > 255) ? -1 : 1;
		case "NUMERIC" 	: return (size == 0 || size > 64)  ? -2 : 2;
		case "CHAR"		: return (size == 0 || size > 255) ? -3 : 3;
		default 		: return 0;
		}
	}


	@Override
	public String toString() {
		return "MySQLAttribute [name=" + name + ", type=" + type + ", notNull="
				+ notNull + ", size=" + size + ", primaryKey=" + primaryKey
				+ "]";
	}
	
	
}

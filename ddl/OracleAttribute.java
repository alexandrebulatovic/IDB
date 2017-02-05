package ddl;

public class OracleAttribute 
extends AbstractAttribute
{
	//Constructeur
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
	public OracleAttribute
	(String name, String type, int size, boolean notNull, boolean primaryKey) 
	{
		super(name, type ,size, notNull, primaryKey);
	}

	
	/**
	 * Constructeur par recopie.
	 * 
	 * @param attribute : null interdit.
	 */
	public OracleAttribute(AbstractAttribute attributeAt) 
	{
		super(attributeAt);
	}

	
	@Override
	public String sizeErrorMsg()
	{
		switch (this.errorSizeCode()){
		case -1 : return "1 <= taille VARCHAR <= 255";
		case -2 : return "1 <= taille NUMBER <= 38";
		case -3 : return "1 <= taille CHAR <= 255";
		default : return "";
		}
	}

	
	@Override
	protected int errorSizeCode()
	{
		switch (this.type) {
		case "VARCHAR2" : return (size == 0 || size > 255) ? -1 : 1;
		case "NUMBER" 	: return (size == 0 || size > 38)  ? -2 : 2;
		case "CHAR"		: return (size == 0 || size > 255) ? -3 : 3;
		default 		: return 0;
		}
	}
}

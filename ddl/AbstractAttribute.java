package ddl;

public abstract class AbstractAttribute 
implements I_Attribute
{
	//Attributs
	/** le nom de l'attribut.*/
	protected String name;
	
	/** le type de l'attribut.*/
	protected String type;
	
	/** vrai si et seulement si l'attribut ne peut pas être null, faux sinon.*/
	protected boolean notNull;
	
	/** la taille de l'attribut.*/
	protected int size;
	
	/** vrai si et seulement si l'attribut est membre de la clée primaire, faux sinon.*/
	protected boolean primaryKey;
	
	
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
	protected AbstractAttribute
	(String name, String type, int size, boolean notNull, boolean primaryKey)
	{
		this.name = name;
		this.type = type;
		this.size = size;
		this.notNull = notNull;
		this.primaryKey = primaryKey;
	}
	
	
	/**
	 * Constructeur par recopie.
	 * 
	 * @param attribute : null interdit.
	 */
	protected AbstractAttribute(AbstractAttribute attribute) 
	{
		this.name = attribute.name;
		this.type = attribute.type;
		this.size = attribute.size;
		this.notNull = attribute.notNull;
		this.primaryKey = attribute.primaryKey;
	}
	
	
	//Méthodes
	/**
	 * @return un entier négatif si et seulement si la taille de l'attribut est en
	 * désaccord avec sa taille, un entier supérieur ou égal à zéro sinon.
	 */
	protected abstract int errorSizeCode();


	@Override
	public  String getName(){
		return this.name;
	}

	
	@Override
	public String getType(){
		return this.type;
	}
	
	
	@Override
	public  int getSize(){
		return this.size;
	}

	
	@Override
	public  boolean isNotNull(){
		return this.notNull;
	}

	
	@Override
	public  boolean isPrimaryKey(){
		return this.primaryKey;
	}


	@Override
	public  void setName(String name){
		this.name = name;
	}

	
	@Override
	public  void setType(String type){
		this.type = type;
	}

	
	@Override
	public  void setNotNull(boolean notNull){
		this.notNull = notNull;
	}

	
	@Override
	public  void setSize(int size){
		this.size = size;
	}

	
	@Override
	public  void setPrimaryKey(boolean primaryKey){
		this.primaryKey = primaryKey;
	}


	@Override
	public boolean checkSize() {
		return this.errorSizeCode() >= 0;
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		return (o == null 
				? false 
				: ((AbstractAttribute)o).name.equals(this.name));
	}
}

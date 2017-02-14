package gui.ddl.tools;

public abstract class AbstractAttributeModel 
implements I_AttributeModel
{
	//Attributs
	/** le nom de l'attribut.*/
	protected String name;
	
	/** le type de l'attribut.*/
	protected String type;
	
	/** la taille de l'attribut.*/
	protected int size;

	/** vrai si et seulement si l'attribut ne peut pas être null, faux sinon.*/
	protected boolean notNull;
	
	/** vrai si et seulement si l'attribut est membre de la clée primaire, faux sinon.*/
	protected boolean primaryKey;
	
	
	//Constructeur
	protected AbstractAttributeModel()
	{
		this("a", "CHAR", 1, false, false);
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
	protected AbstractAttributeModel
	(String name, String type, int size, boolean notNull, boolean primaryKey)
	{
		this.setName(name);
		this.setType(type);
		this.setSize(size);
		this.setNotNull(notNull);
		this.setPrimaryKey(primaryKey);
	}
	
	
	/**
	 * Constructeur par recopie.
	 * 
	 * @param attribute : null interdit.
	 */
	protected AbstractAttributeModel(AbstractAttributeModel attribute) 
	{
		this(attribute.name, attribute.type, attribute.size,
				attribute.notNull, attribute.primaryKey);
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
	public String [] toArray()
	{
		String [] result = new String [5];
		result[0] = this.name;
		result[1] = this.type;
		result[2] = "" + this.size;
		result[3] = notNull ? "NOTNULL" : "";
		result[4] = primaryKey ? "PRIMARY" : "";
		return result;
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		return (o == null 
				? false 
				: ((AbstractAttributeModel)o).name.equals(this.name));
	}
}

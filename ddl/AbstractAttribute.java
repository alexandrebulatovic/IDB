package ddl;

public abstract class AbstractAttribute implements I_Attribute{

	protected String name;
	protected String type;
	protected boolean notNull;
	protected int size;
	protected boolean primaryKey;
	
	protected AbstractAttribute(String name, String type, int size, boolean notNull, boolean primaryKey) {
		this.name = name;
		this.type = type;
		this.size = size;
		this.notNull = notNull;
		this.primaryKey = primaryKey;
	}
	
	protected AbstractAttribute(OracleAttribute attributeAt) {
		this.name = attributeAt.name;
		this.type = attributeAt.type;
		this.size = attributeAt.size;
		this.notNull = attributeAt.notNull;
		this.primaryKey = attributeAt.primaryKey;
	}
	
	public  String getName(){
		return this.name;
	}

	public String getType(){
		return this.type;
	}
	public  int getSize(){
		return this.size;
	}

	public  boolean isNotNull(){
		return this.notNull;
	}

	public  boolean isPk(){
		return this.primaryKey;
	}


	public  void setName(String name){
		this.name = name;
	}

	public  void setType(String type){
		this.type = type;
	}

	public  void setNotNull(boolean notNull){
		this.notNull = notNull;
	}

	public  void setSize(int size){
		this.size = size;
	}

	public  void setPk(boolean primaryKey){
		this.primaryKey = primaryKey;
	}
}

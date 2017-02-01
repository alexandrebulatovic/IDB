package business;

public class ForeignKeyContraint extends Contraints {

	
	public boolean deleteCascade = false;
	
	/**
	 * La table ou pointe la clé étrangère
	 */
	private Table tableDestination;
	
	private Attribute attributeDestination;
	
	public ForeignKeyContraint(){
		this.keyWord = "FOREIGN KEY";
		this.prefix = "fk";
	}
	
	@Override
	public String getNameSQL() {
		return this.getEntete()+" ("+this.attributes.get(0).name+") REFERENCES "+this.tableDestination.getName()+"("+getAttributeDestination().name+")";
	}

	/**
	 * @return the attributeDestination
	 */
	public Attribute getAttributeDestination() {
		return attributeDestination;
	}

	/**
	 * @param attributeDestination the attributeDestination to set
	 */
	public void setAttributeDestination(Attribute attributeDestination) {
		this.attributeDestination = attributeDestination;
	}

	public void setTableDestination(Table table) {
		this.tableDestination = table;
		
	}

}

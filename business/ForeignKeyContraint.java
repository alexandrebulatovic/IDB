package business;

public class ForeignKeyContraint extends Contraints {

	
	boolean deleteCascade = false;
	
	/**
	 * La table ou pointe la clé étrangère
	 */
	Table tableDestination;
	
	Attribute attributeDestination;
	
	public ForeignKeyContraint(){
		this.keyWord = "FOREIGN KEY";
		this.prefix = "fk";
	}
	
	@Override
	public String getNameSQL() {
		return this.getEntete()+" ("+this.name+") REFERENCES "+this.tableDestination.getName()+"("+attributeDestination.name+") "+(this.deleteCascade?"ON DELETE CASCADE":"");
	}

}

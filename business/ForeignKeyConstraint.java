package business;

import java.util.ArrayList;
import java.util.List;

public class ForeignKeyConstraint extends Contraints {

	
	public boolean deleteCascade = false;
	
	/**
	 * La table ou pointe la clé étrangère
	 */
	private Table tableDestination;
	
	private List<Attribute> attributesDestination;
	
	public ForeignKeyConstraint(){
		this.attributesDestination = new ArrayList<Attribute>();
		
		this.keyWord = "FOREIGN KEY";
		this.prefix = "fk";
	}
	
	@Override
	public String getNameSQL() {
		String attDest = "";

		
		int i=0;
		for (Attribute dest : attributesDestination){
			if (i!=0){
				attDest+=",";
			}
			attDest+=dest.name;
			i++;
		}


		return this.getEntete()+" ("+this.attributes.get(0).name+") REFERENCES "+this.tableDestination.getName()+"("+attDest+")";
	}

	/**
	 * @return the attributeDestination
	 */
	public List<Attribute> getAttributesDestination() {
		return attributesDestination;
	}

	/**
	 * @param attributeDestination the attributeDestination to set
	 */
	public void addAttributeDestination(Attribute attributeDestination) {
		this.attributesDestination.add(attributeDestination);
	}

	public void setTableDestination(Table table) {
		this.tableDestination = table;
		
	}

}

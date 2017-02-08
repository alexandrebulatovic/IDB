package business;

import java.util.ArrayList;
import java.util.List;

public class ForeignKeyConstraint extends Constraint {

	
	public boolean deleteCascade = false;
	
	/**
	 * La table ou pointe la clé étrangère
	 */
	
	private Table tableDestination;
	private List<Attribute> attributesDestination;
	
	
	/**
	 * Il faut renseigner plus tard les paramètres suivants pour garantir le bon fonctionnement
	 * @param tableSource
	 * @param attributesSources
	 * @param tableDestination
	 * @param attributesDestinations
	 * 
	 * @see business.ForeignKeyConstraint#ForeignKeyConstraint(Table, List, Table, List)
	 */
	public ForeignKeyConstraint(){
		super();
		this.attributesDestination = new ArrayList<Attribute>();
		
		this.keyWord = "FOREIGN KEY";
		this.prefix = "fk";
	}
	
	/**
	 * @deprecated il est LARGEMENT préférable d'utiliser les add
	 * afin d'éviter les incohérences ou ajouts de contrôles
	 * @param tableSource
	 * @param attributesSources
	 * @param tableDestination
	 * @param attributesDestinations
	 */
	public ForeignKeyConstraint(Table tableSource, List<Attribute> attributesSources, Table tableDestination,List<Attribute> attributesDestinations){
		this.setTable(tableSource);
		this.setTableDestination(tableDestination);
		this.attributes = attributesSources;
		this.attributesDestination = attributesDestinations;
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
	
	public Table getTableDestination(){
		return this.tableDestination;
	}
	

	public void cleanAll(){
		super.cleanAll();
		this.tableDestination = null;
		this.attributesDestination.clear();
	}

	
	public List<Object> toListOfString() {
		ArrayList<Object> liste = new ArrayList();
		
		liste.add(this.name);
		liste.add(this.getTable());
		liste.add(this.getAttributesNames());
		liste.add(this.tableDestination);
		liste.add(this.getAttributesDestinationNames());
		
		
		
		return liste;
	}

	public List<String> getAttributesDestinationNames() {
		ArrayList<String> retour = new ArrayList<String>();
		for (Attribute att : this.getAttributesDestination()){
			retour.add(att.getName());
		}
		return retour;
	}


}

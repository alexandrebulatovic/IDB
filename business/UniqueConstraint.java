package business;

public class UniqueConstraint extends Constraint {

	public UniqueConstraint(){
		this.prefix = "un";
		
		this.keyWord = "UNIQUE";
	}
	
	@Override
	public String getNameSQL() {
		
		String lesUniques = "";
		int i =0;
		for (Attribute att : this.attributes){
			if (i!=0){
				lesUniques +=",";
			}
			lesUniques+=att.name;
			i++;
		}
		return this.getEntete()+"("+lesUniques+")";
	}
	
	Attribute getAttribute(){
		if (this.getAttributes().size()==1){
			return this.attributes.get(0);
		}
		return null;
	}

}

package business;

import java.util.ArrayList;
import java.util.List;

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

	public List<String> getAttributesNames() {
		List<String> retour = new ArrayList<String>();
		
		for (Attribute att : this.attributes){
			retour.add(att.getName());
		}
		
		return retour;
	}

}

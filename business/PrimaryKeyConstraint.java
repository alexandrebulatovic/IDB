package business;

import java.util.List;

public class PrimaryKeyConstraint extends Constraint{

	public PrimaryKeyConstraint(){
		this.prefix = "pk";
		this.keyWord = "PRIMARY KEY";
	}
	
	@Override
	public String getNameSQL() {
		String lesPks = "";
		int i =0;
		for (Attribute att : this.attributes){
			if (i!=0){
				lesPks+=",";
			}
			lesPks+=att.name;
			i++;
		}
		return this.getEntete()+"("+lesPks+")";
	}

}

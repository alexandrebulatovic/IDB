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
	
	/**
	 * @note le nom de dépassera jamais les 30 caractères
	 * @exemple pk_TableName_att1_att2
	 * @see business.Constraint#createAndSetName()
	 * @return le nom de la contrainte, déterminé par : <br/>
	 * - son prefix,<br/>
	 * - sa table, <br/>
	 * - la liste de ses attributs.
	 */
	public String createName()
	{
		StringBuilder start =  createStartName();
		
		
		if (start.length()>30){
			return this.prefix+"_"+String.valueOf(this.getId());
		}
		start.deleteCharAt(start.length()-1);
		return start.toString();

	}
	

}

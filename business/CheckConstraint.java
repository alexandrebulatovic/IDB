package business;

public class CheckConstraint extends Contraints{
	
	/**
	 * Décrit la contrainte SQL
	 */
	private String constraint;
	
	
	/**
	 * Nous devons préciser le table et l'attribut 
	 * d'ou s'applique la contrainte
	 * 
	 * il faut préciser aussi la contrainte à appliquer
	 * exemple : machin<5
	 * @param tableSource
	 * @param AttributeSource
	 * @param contraints
	 */
	public CheckConstraint(Table tableSource,Attribute AttributeSource,String constraint){
		this.constraint = constraint;
		this.keyWord = "CHECK";
		this.prefix = "ck";
	}
	
	@Override
	public String getNameSQL(){
		return this.getEntete()+"("+getConstraint()+")";
	}

	/**
	 * @return the constraint
	 */
	public String getConstraint() {
		return constraint;
	}

	/**
	 * @param constraint the constraint to set
	 */
	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}

	

}

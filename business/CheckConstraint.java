package business;

public class CheckConstraint extends Contraints{
	
	/**
	 * Décrit la contrainte SQL
	 */
	private String constraint;
	
	
	public CheckConstraint(String constraint){
		this.constraint = constraint;
		this.setKeyWord("CHECK");
	}
	
	@Override
	protected String getNameSQL(){
		return "CHECK("+getConstraint()+")";
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

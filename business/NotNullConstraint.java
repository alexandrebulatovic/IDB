package business;

public class NotNullConstraint extends CheckConstraint {

	public NotNullConstraint() {
		super("erreur");
	}
	
	@Override
	public void addAttribute(Attribute att){
		this.attributes.add(att);
		this.setConstraint(att.name+" IS NOT NULL");
	}


}

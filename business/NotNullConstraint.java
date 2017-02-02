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

	public Attribute getAttribute() {
		if (this.getAttributes().size()==1){
			return this.attributes.get(0);
		}
		return null;
	}


}

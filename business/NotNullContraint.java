package business;

public class NotNullContraint extends CheckConstraint {

	public NotNullContraint(Table tableSource, Attribute attributeSource) {
		super(attributeSource.name+" IS NOT NULL");
		this.attributes.add(attributeSource);
	}

}

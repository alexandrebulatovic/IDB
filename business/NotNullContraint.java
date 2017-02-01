package business;

public class NotNullContraint extends CheckConstraint {

	public NotNullContraint(Table tableSource, Attribute attributeSource) {
		super(tableSource, attributeSource, attributeSource.name+" IS NOT NULL");
	}

}

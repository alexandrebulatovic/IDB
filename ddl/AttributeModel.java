package ddl;

public class AttributeModel {

	public String name;
	public String type;
	public boolean notNull;
	public int size;
	public boolean primaryKey;

	public AttributeModel(String name, String type, int size, boolean notNull, boolean primaryKey) {
		this.name = name;
		this.type = type;
		this.size = size;
		this.notNull = notNull;
		this.primaryKey = primaryKey;
	}

	public AttributeModel(AttributeModel attributeAt) {
		this.name = attributeAt.name;
		this.type = attributeAt.type;
		this.size = attributeAt.size;
		this.notNull = attributeAt.notNull;
		this.primaryKey = attributeAt.primaryKey;
	}
}

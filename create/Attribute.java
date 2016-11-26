package create;

/**
 * @author Adrian
 *
 */
public class Attribute {
	private String name;

	private String type;

	private int size;

	private Boolean notNull;

	private Boolean unique;

	private Boolean primaryKey;

	private Boolean foreignKey;

	private String fkTable;



	private String fkAttribute;

	public Attribute(String name,String type,int size,Boolean notNull,Boolean unique,Boolean pk,Boolean fk,String fkTable,String fkAttribute ){
		this.name=name;
		this.type=type;
		this.size=size;
		this.notNull=notNull;
		this.unique=unique;
		this.primaryKey=pk;
		this.foreignKey=fk;
		this.fkTable=fkTable;
		this.fkAttribute=fkAttribute;

	}




	public String getName() {
		return name;
	}

	public void setName(String n) {
		this.name = n;
	}

	public String getType() {
		return type;
	}

	public void setType(String t) {
		this.type = t;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int s) {
		this.size = s;
	}

	public Boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(Boolean nn) {
		this.notNull = nn;
	}

	public Boolean isUnique() {
		return unique;
	}

	public void setUnique(Boolean u) {
		this.unique = u;
	}

	public Boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Boolean pk) {
		this.primaryKey = pk;
	}

	public Boolean isForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(Boolean fk) {
		this.foreignKey = fk;
	}

	public String getFkTable() {
		return fkTable;
	}

	public void setFkTable(String fkT) {
		this.fkTable = fkT;
	}

	public String getFkAttribute() {
		return fkAttribute;
	}

	public void setFkAttribute(String fkA) {
		this.fkAttribute = fkA;
	}
}

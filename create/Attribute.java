package create;

/**
 * @author Adrian
 *
 */
public class Attribute {
	/**
	 * Mot-clef pour déclarer une contrainte sql.
	 */
	private static final String CST = ",\nCONSTRAINT ";
	
	private String name;

	private String type;

	private int size;

	private boolean notNull;

	private boolean unique;

	private boolean primaryKey;

	private boolean foreignKey;

	private String fkTable;



	private String fkAttribute;

	public Attribute(String name, String type, int size, boolean notNull, 
			boolean unique, boolean pk, boolean fk, 
			String fkTable, String fkAttribute ){
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


	//Accesseurs, mutateurs
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
	
	
	//Méthodes
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o)
	{
		Attribute a = (Attribute) o;
		return this.name.equals(a.name);
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui synthétise
	 * $this en morceau de requête SQL.
	 * 
	 * @return String
	 */
	public String toSQL()
	{
		StringBuilder result = new StringBuilder();
		result.append(this.sqlAttribute());
		if (this.notNull) 		result.append(this.sqlNotNull());
		if (this.unique) 		result.append(this.sqlUnique());
		if (this.primaryKey) 	result.append(this.sqlPrimaryKey());
		if (this.foreignKey) 	result.append(this.sqlForeignKey());
		return result.toString();
	}
	
	
	//Privées
	/**
	 * Retourne une chaîne de caractères qui correspond à une
	 * déclaration d'attribut pour $this.
	 * 
	 * @return String
	 */
	private String sqlAttribute()
	{
		return this.name + " " + this.type + "(" + this.size + ")";
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui correspond à
	 * une déclaration de contrainte NOT NULL pour $this.
	 * 
	 * @return StringBuilder
	 */
	private String sqlNotNull()
	{
		return this.concatSqlConstraint("nn", "CHECK", "IS NOT NULL");
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui correspond à
	 * une déclaration de contrainte UNIQUE pour $this.
	 * 
	 * @return StringBuilder
	 */
	private String sqlUnique()
	{
		return this.concatSqlConstraint("un", "UNIQUE", "");
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui correspond à
	 * une déclaration de contrainte PRIMARY KEY pour $this.
	 * 
	 * @return StringBuilder
	 */
	private String sqlPrimaryKey()
	{
		return this.concatSqlConstraint("pk", "PRIMARY KEY", "");
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui correspond à
	 * une déclaration de contrainte FOREIGN KEY pour $this.
	 * 
	 * @return StringBuilder
	 */
	private String sqlForeignKey()
	{
		return this.concatSqlConstraint("fk", "FOREIGN KEY", "");
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui représentent
	 * une clause SQL CONSTRAINT.
	 * 
	 * @param constraintNamePrefix : préfixe du nom de la contrainte (pk, fk, nn, un, ck etc.)
	 * @param constraintType : mot-clef de contrainte (UNIQUE, PRIMARY KEY etc.)
	 * @param condition : à remplir si $constraintType est CHECK
	 * @return StringBuilder
	 */
	private String concatSqlConstraint(
			String constraintNamePrefix, String constraintType,
			String condition)
	{
		StringBuilder result = new StringBuilder();
		result.append(CST + constraintNamePrefix + "_" + this.name + " ");
		result.append(constraintType + " (" + this.name + " ");
		result.append(condition + ")");
		if (constraintType.equals("FOREIGN KEY")) {
			result.append(" REFERENCES " 
						+ this.fkTable 
						+ "(" 
						+ this.fkAttribute 
						+ ")");
		}
		return result.toString();
	}
}

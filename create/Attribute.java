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
	
	public final String name;

	public final String type;

	public final int size;

	public final boolean notNull;

	public final boolean unique;

	public final boolean primaryKey;

	public final boolean foreignKey;

	public final String fkTable;

	public final String fkAttribute;

	
	/**
	 * Constructeur lambda.
	 * 
	 * @param name
	 * @param type
	 * @param size
	 * @param notNull
	 * @param unique
	 * @param pk
	 * @param fk
	 * @param fkTable
	 * @param fkAttribute
	 */
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
	 * {@inheritDoc}
	 */
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append(this.name + " : ");
		result.append(this.type + ", ");
		result.append(this.size + ", ");
		if (this.unique) result.append("unique, ");
		if (this.primaryKey) result.append("clef primaire, ");
		if (this.foreignKey) {
			result.append("clef étrangère : ");
			result.append(this.fkTable + "(");
			result.append(this.fkAttribute + ")");
		}
		return result.toString();
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
	
	public int checkAttributes(){
		if(this.type.equals("VHARCHAR")){
			if(this.size == 0 || this.size > 2000){
				return -1;
			}else{
				return 1;
			}
		}else if (this.type.equals("NUMBER")){
			if(this.size == 0){
				return -2;
			}else{
				return 2;
			}
		}else if (this.type.equals("CHAR")){
			if(this.size == 0 || this.size > 2000){
			return -3;
		}else{
			return 3;
		}
			
		}else{
			return 0;
	}
			
			
			
		}
	
	public String errorAttributes(int i){
		String error;
		if(i==-1){
			error = "Un VARCHAR doit avoir une Taille comprise entre 1 et 4000";
			return error;
		}else if(i==-2){
			error = "Un NUMBER doit avoir une Taille Supérieure à 0";
			return error;
	}else if(i==-3){
		error = "Un CHAR doit avoir une Taille comprise entre 1 et 2000";
		return error;
		
	}else{
		error = "";
		return error;
		
	}
		
	}
}


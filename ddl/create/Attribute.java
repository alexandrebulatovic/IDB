package ddl.create;

/**
 * @author Adrian
 *
 */
public class Attribute {

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
	public Attribute(String name, String type, int size, 
			boolean notNull, boolean unique, boolean pk, boolean fk, 
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

	
	/**
	 * Constructeur pour attribut sans aucune contrainte.
	 * 
	 * @param name
	 * @param type
	 * @param taille
	 */
	public Attribute(String name, String type, int taille)
	{
		this(name, type, taille, false, false, false, false, null, null);
	}
	
	
	/**
	 * Constructeur pour attribut membre de la clée et c'est tout.
	 * 
	 * @param name
	 * @param type
	 * @param taille
	 * @param pk : vrai si et seulement si $this est membre de la clée.
	 */
	public Attribute(String name, String type, int taille, boolean pk)
	{
		this(name, type, taille, false, false, pk, false, null, null);
	}

	
	//Méthodes
	@Override
	public boolean equals(Object o)
	{
		Attribute a = (Attribute) o;
		return this.name.equals(a.name);
	}
	
	
	@Override
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
	 * Retourne une chaîne de caractères correspondant à un message 
	 * d'erreur concernant la taille du type de $this en fonction de $i.
	 * 
	 * @param i : -3 <= i <= 3
	 * @return String
	 */
	public String attributeSizeError(int i){
		switch (i){
			case -1 : return "1 <= taille VARCHAR <= 255";
			case -2 : return "1 <= taille NUMBER <= 38";
			case -3 : return "1 <= taille CHAR <= 255";
			default : return "";
		}
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
		result.append(this.attributeToSQL());
		if (this.notNull) 		result.append(this.notNullToSQL());
		if (this.unique) 		result.append(this.uniqueToSQL());
		if (this.foreignKey) 	result.append(this.foreignKeyToSQL());
		return result.toString();
	}
	
	
	/**
	 * Retourne un nombre négatif spécifique si et seulement si la taille
	 * de $this n'est pas cohérente avec son type, retourne
	 * un nombre positif dans l'autre cas.
	 * 
	 * @return int : -3 <= result <= 3
	 */
	public int checkAttributes(){
		if(this.type.equals("VARCHAR")){
			if(this.size == 0 || this.size > 255){
				return -1;
			}else{
				return 1;
			}
		}else if (this.type.equals("NUMBER")){
			if(this.size == 0 || this.size > 38){
				return -2;
			}else{
				return 2;
			}
		}else if (this.type.equals("CHAR")){
			if(this.size == 0 || this.size > 255){
				return -3;
			}else{
				return 3;
			}
	
		}else{
			return 0;
		}
	}


	//Privées
	/**
	 * Retourne une chaîne de caractères qui correspond à une
	 * déclaration d'attribut pour $this.
	 * 
	 * @return String
	 */
	private String attributeToSQL()
	{
		StringBuilder result = new StringBuilder();
		result.append(this.name + " " + this.type);
		if (! this.type.equals("DATE")) result.append("(" + this.size + ")");
		return result.toString();
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui correspond à
	 * une déclaration de contrainte NOT NULL pour $this.
	 * 
	 * @return StringBuilder
	 */
	private String notNullToSQL()
	{
		return this.concatToSQL("nn", "CHECK", "IS NOT NULL");
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui correspond à
	 * une déclaration de contrainte UNIQUE pour $this.
	 * 
	 * @return StringBuilder
	 */
	private String uniqueToSQL()
	{
		return this.concatToSQL("un", "UNIQUE", null);
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui correspond à
	 * une déclaration de contrainte FOREIGN KEY pour $this.
	 * 
	 * @return StringBuilder
	 */
	private String foreignKeyToSQL()
	{
		return this.concatToSQL("fk", "FOREIGN KEY", null);
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui représentent
	 * une clause SQL CONSTRAINT.
	 * 
	 * @param constraintNamePrefix : préfixe du nom de la contrainte (pk, fk, nn, un, ck)
	 * @param constraintType : mot-clef de contrainte (UNIQUE, CHECK, FOREIGN KEY)
	 * @param condition : null ssi $constraintType != "CHECK"
	 * @return StringBuilder
	 */
	private String concatToSQL(
			String constraintNamePrefix, String constraintType,
			String condition)
	{
		StringBuilder result = new StringBuilder();
		result.append(",\n" + this.constraintNameToSQL(constraintNamePrefix));
		result.append(" ");
		result.append(this.constraintTypeToSQL(constraintType, condition));
		if (constraintType.equals("FOREIGN KEY")) {
			result.append(" ");
			result.append(this.referencesToSQL());
		}
		return result.toString();
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui représente 
	 * le nom de la contrainte que va subir $this.
	 * 
	 * La chaîne retournée commence par une virgule et un saut de ligne.
	 * 
	 * @param constraintNamePrefix : préfixe du nom de la contrainte (pk, fk, nn, un, ck)
	 * @return String
	 */
	private String constraintNameToSQL(String constraintNamePrefix)
	{
		return "CONSTRAINT " + constraintNamePrefix + "_" + this.name;
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui représente 
	 * le type de contrainte que subit $this.
	 * 
	 * @param constraintType : mot-clef de contrainte (UNIQUE, CHECK, FOREIGN KEY)
	 * @param condition : null ssi $constraintType != "CHECK"
	 * @return String
	 */
	private String constraintTypeToSQL(String constraintType, String condition)
	{
		StringBuilder result = new StringBuilder();
		result.append(constraintType + " (" + this.name);
		if (condition != null) result.append(" " + condition);
		result.append(")");
		return result.toString();
	}
	
	/**
	 * Retourne une chaîne de caractères qui représente
	 * la référence d'une clef étrangère.
	 * 
	 * @return String
	 */
	private String referencesToSQL()
	{
		StringBuilder result = new StringBuilder();
		result.append("REFERENCES ");
		result.append(this.fkTable);
		result.append("(");
		result.append(this.fkAttribute);
		result.append(")");
		return result.toString();
	}
}


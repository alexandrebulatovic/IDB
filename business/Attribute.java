package business;


public class Attribute 
{

	/** Nom de l'attribut.*/
	public String name;

	/** Type de donnée.*/
	public String type;

	/** Nombre d'octets.*/
	public int size;

	/**
	 * Vrai si et seulement si $this ne peut pas être null, faux sinon.
	 */
	public boolean notNull;

	/**
	 * Vrai si et seulement si chaque valeur de this 
	 * doit être unique dans la table, faux sinon.
	 */
	public boolean unique;

	/**
	 * Vrai si et seulement si $this est membre de la clef primaire,
	 * faux sinon.
	 */
	public boolean primaryKey;

	/**
	 * Vrai si et seulement si $this (actuellement) une clef étrangère,
	 * faux sinon.
	 */
	public boolean foreignKey;

	/**
	 * Nom de la table de référence lorsque $this est une clef étrangère.
	 */
	public String fkTable;

	/**
	 * Nom de l'attribut de référence lorsque $this est une clef étrangère.
	 */
	public String fkAttribute;

	/** Nom de la table où se situe $this.*/
	private String tableName;
	
	
	/**
	 * Constructeur lambda.
	 * 
	 * @param name
	 * @param type
	 * @param size
	 * @param notNull
	 * @param unique
	 * @param pk
	 * @param boolean fk Vrai si et seulement si $this (actuellement) une clef étrangère,
	 * faux sinon.
	 * @param fkTable  Nom de la table de référence lorsque $this est une clef étrangère.
	 * @param fkAttribute Nom de l'attribut de référence lorsque $this est une clef étrangère.
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
		this.tableName = "";
	}
	
	
	/**
	 * Constructeur par recopie.
	 * 
	 * @param copy : un attribut à recopier.
	 */
	public Attribute(Attribute copy)
	{
		this.name = copy.name;
		this.type = copy.type;
		this.size = copy.size;
		this.notNull = copy.notNull;
		this.unique = copy.unique;
		this.primaryKey = copy.primaryKey;
		this.foreignKey = copy.foreignKey;
		this.fkTable = copy.fkTable;
		this.fkAttribute = copy.fkAttribute;
		this.tableName = copy.tableName;
	}
	
	
	/** 
	 * Définit le nom de la table de $this comme étant $name.
	 */
	public void setTableName(String name){this.tableName = name;}


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
	public String toCreate()
	{
		StringBuilder result = new StringBuilder();
		result.append(this.declarationToSQL());
		result.append(this.notNullToSQL());
		result.append(this.uniqueToSQL());
		result.append(this.foreignKeyToSQL());
		return result.toString();
	}
	
	
	/**
	 * Retourne un nombre négatif spécifique si et seulement si la taille
	 * de $this n'est pas cohérente avec son type, retourne
	 * un nombre positif dans l'autre cas.
	 * 
	 * @return int : -3 <= result <= 3
	 */
	public int checkSizeAttributes(){
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
	 * Retourne une chaîne de caractères qui représente le nom et le type de $this, 
	 * comme s'il s'agissait d'une requête SQL pour créer une table.
	 * La taille de $this est ajoutée uniquement lorsque c'est nécessaire.
	 * 
	 * @return String
	 */
	private String declarationToSQL()
	{
		StringBuilder result = new StringBuilder();
		result.append(this.name + " " + this.type);
		if (! this.type.equals("DATE")) result.append("(" + this.size + ")");
		return result.toString();
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui correspond à
	 * une déclaration de contrainte NOT NULL pour $this,
	 * si et seulement si $this possède cette contrainte.
	 * Retourne une chaîne vide sinon.
	 * 
	 * @return String
	 */
	private String notNullToSQL()
	{
		return this.notNull 
				? this.concatToSQL("nn", "CHECK", "IS NOT NULL")
				: "";
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui correspond à
	 * une déclaration de contrainte UNIQUE pour $this,
	 * si et seulement si $this possède cette contrainte.
	 * Retourne une chaîne vide sinon.
	 * 
	 * @return String
	 */
	private String uniqueToSQL()
	{
		return this.unique 
				? this.concatToSQL("un", "UNIQUE", null)
				: "";
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui correspond à
	 * une déclaration de contrainte FOREIGN KEY pour $this,
	 * si et seulement si $this possède cette contrainte.
	 * Retourne une chaîne vide sinon.
	 * 
	 * @return String
	 */
	private String foreignKeyToSQL()
	{
		return this.foreignKey
				? this.concatToSQL("fk", "FOREIGN KEY", null)
				: "";
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui représente une clause CONSTRAINT 
	 * complète, dont le nom commence par $prefix, le type $keyword et 
	 * éventuellement de condition $condition.
	 * 
	 * @param prefix : pk, fk, nn, un, ck.
	 * @param keyword : UNIQUE, CHECK, FOREIGN KEY.
	 * @param condition : null ssi $constraintType != "CHECK", représente une condition booléenne sinon.
	 * @return String
	 */
	private String concatToSQL(
			String prefix, String keyword,
			String condition)
	{
		StringBuilder result = new StringBuilder();
		result.append(",\n" + this.constraintNameToSQL(prefix));
		result.append(" ");
		result.append(this.constraintTypeToSQL(keyword, condition));
		if (keyword.equals("FOREIGN KEY")) {
			result.append(" ");
			result.append(this.referencesToSQL());
		}
		return result.toString();
	}


	/**
	 * Retourne une chaîne de caractères qui représente le début
	 * d'une clause CONSTRAINT en SQL, c'est à dire le mot CONSTRAINT et le nom de la contrainte.
	 * Le nom de la contrainte commence par $prefix.
	 * 	  
	 * @param prefix : préfixe du nom de la contrainte (pk, fk, nn, un, ck)
	 * @return String
	 */
	private String constraintNameToSQL(String prefix)
	{
		return "CONSTRAINT " + prefix + "_" 
				+ this.tableName + "_" + this.name;
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui représente 
	 * $constraintType imposée sur $this.
	 * 
	 * @param constraintType : mot-clef de contrainte (UNIQUE, CHECK, FOREIGN KEY)
	 * @param condition : null ssi $constraintType != "CHECK", expression booléenne sinon.
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
	 * la référence sur laquelle se base $this.
	 * Cette méthode ne doit pas être appellée si $this n'est pas une clef étrangère.
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


	public String toSQL() {
		return this.name+" "+this.type+" ("+this.size+")";

	}
}
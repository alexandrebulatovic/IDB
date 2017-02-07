package business;

import java.util.ArrayList;
import java.util.List;

public class Attribute 
{

	/** Nom de l'attribut.*/
	public String name;

	/** Type de donnée.*/
	public String type;

	/** Nombre d'octets.*/
	public int size;

	/** Nom de la table où se situe $this.*/
	private String tableName;
	
	
	private List<Constraint> constraints;
	
	private boolean notNull = false;
	
	
	/**
	 * attention, la taille des types ne doit exéder 38 octets pour des 
	 * types numériques
	 * @param name
	 * @param type
	 * @param size
	 * @param constraints
	 * @param tableName
	 */
	public Attribute(String name, String type, int size, List<Constraint> constraints,String tableName, boolean isNotNull){
		this.name=name;
		this.type=type;
		this.size=size;


		this.notNull = isNotNull;
		
		if (constraints == null)
			this.constraints = new ArrayList<Constraint>();
		else
			this.constraints = constraints;	
		
		
		this.tableName = tableName;
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
		this.tableName = copy.tableName;
		
		this.constraints = new ArrayList<Constraint>();
		for (Constraint c : copy.constraints){
			this.constraints.add(c);
		}
	}
	
	
	/**
	 * 
	 * Par défaut,
	 * La suppression ne se fait pas en DELETE CASCADE
	 * @param name
	 * @param type
	 * @param size
	 * @param isNotNull
	 */
	public Attribute(String name,String type, int size, boolean isNotNull) {
		this(name,type,size,new ArrayList<Constraint>(),null,isNotNull);
	}

	
	/**
	 * Par défaut, l'attribut peut être null et ne possede pas de contraintes.
	 * La suppression ne se fait pas en DELETE CASCADE
	 * @param name
	 * @param type
	 * @param size
	 */
	public Attribute(String name,String type, int size) {
		this(name,type,size,false);
	}


	public boolean isNotNull(){
		return this.notNull;
	}
	
	public boolean isUnique(){
		for (Constraint constraint : constraints){
			if (constraint instanceof UniqueConstraint){
				UniqueConstraint unique = (UniqueConstraint) constraint;
				if (unique.getAttribute()==this){
					return true;
				}

				
			}
		}
		return false;
	}
	
	
	public boolean isPk(){
		for (Constraint constraint : constraints){
			if (constraint instanceof PrimaryKeyConstraint){
				for ( Attribute pk : constraint.getAttributes()){
					if (pk==this){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Retourne vrai si l'attribut est clé étrangère
	 * @return
	 */
	public boolean isFk(){
		for (Constraint c : constraints){
			if (c instanceof ForeignKeyConstraint){
				ForeignKeyConstraint fk = (ForeignKeyConstraint)c;
				for (Attribute att : fk.getAttributes()){
					if (att == this){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public ForeignKeyConstraint getFk(){
		if (this.isFk()){
			for (Constraint c : constraints){
				if (c instanceof ForeignKeyConstraint){
					return (ForeignKeyConstraint) c;
				}
			}
		}
		return null;

	}
	
	
	/** 
	 * Définit le nom de la table de $this comme étant $name.
	 */
	public void setTableName(String name){this.tableName = name;}

	
	/**
	 * Ajoute une contrainte si elle n'existe pas
	 * 
	 * et retourne true si elle à bien été ajouté
	 * @param constraint
	 */
	public boolean addConstraint(Constraint constraint){		
		if (!this.constraints.contains(constraint)){
			
			return this.constraints.add(constraint);
		}
		return false;
	}

	//Méthodes
	@Override
	public boolean equals(Object o)
	{
		Attribute a = (Attribute) o;
		return this.name.equals(a.name);
	}
	
	
	/**
	 * Retourne une représentation de l'attribut
	 * @Exemple att1 NUMBER(8)
	 */
	@Override
	public String toString()
	{
		return this.toSQL();
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
	 * Retourne une liste de requettes
	 * @Exemple ALTER TABLE $nomTable ADD CONSTRAINT $nom CHECK($condition)
	 * @return liste de requettes SQL string
	 */
	public List<String> toCreateConstraintsSQL()
	{
		List<String> sqls = new ArrayList<String>();
		for (Constraint constraint : this.constraints){
			sqls.add(constraint.toAddConstraintSQL());
		}
		return sqls;
	}
	
	
	/**
	 * Retourne un nombre négatif spécifique si et seulement si la taille
	 * de $this n'est pas cohérente avec son type, retourne
	 * un nombre positif dans l'autre cas.
	 * 
	 * @return int : -3 <= result <= 3
	 */
	public int checkSizeAttributes(){
		if(this.type.equals("VARCHAR2")){
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



	/**
	 * Retourne une représentation de l'attribut
	 * @Exemple nomProduit VARCHAR (30) NOT NULL
	 * @return String
	 */
	public String toSQL() {
		String nn = this.notNull?" NOT NULL":"";
		if (this.type.equals("DATE")){
			return this.name+" "+this.type+nn;
		}
		return this.name+" "+this.type+" ("+this.size+")"+nn;
	}
	
	
	/**
	 * @Exemple ALTER TABLE $nomTable ADD $nomAttribut $type $taill
	 * @return
	 */
	public String toADDSQL(){
		return "ALTER TABLE "+this.tableName+"\nADD "+this.toSQL();
	}

	/**
	 * @Exemple ALTER TABLE $nomTable DROP $nomAttribut
	 * @return
	 */
	public String toDROPSQL() {
		return "ALTER TABLE "+this.tableName+"\nDROP "+this.name;
	}
	
	public String toModify(){
		return "ALTER TABLE "+this.tableName+"\nMODIFY "+this.toSQL();
	}

	public PrimaryKeyConstraint getPk() {
		for (Constraint c : this.constraints){
			if (c instanceof PrimaryKeyConstraint){
				return (PrimaryKeyConstraint) c;
			}
		}
		return null;
	}
	
	public List<Constraint> getConstraints(){
		return this.constraints;
	}

	
	/**
	 * Supprime toutes les contraintes
	 * @return une liste des contraintes qui ont été totalement supprimés	
	 */
	public ArrayList<Constraint> cleanConstraints() {
		ArrayList<Constraint> tmp = new ArrayList<Constraint>(this.constraints);
		this.constraints.clear();//on vide la liste
		return tmp;
	}
	
}














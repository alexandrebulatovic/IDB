package business;

import java.util.ArrayList;
import java.util.List;


public abstract class Constraint
{
	
	private static long id=0;
	
	private long thisId;
	
	//Attributs
	/** Mot clé définissant la contrainte exemple : PRIMARY KEY.*/

	protected String keyWord = "NO_KEY_WORD";
	
	/** Attributs ciblés par la contrainte.*/
	protected List<Attribute> attributes;
	
	/** Une contrainte appartient à une table.*/
	protected Table table;
	
	/** Nom unique de la contrainte.*/ 
	protected String name;

	/** Préfixe du nom de la contrainte, parmi nn, ck.*/
	protected String prefix;
	
	

	//Constrcuteur
	/**
	 * Constructeur vide.
	 */
	protected Constraint()
	{
		thisId = Constraint.id++;
		this.attributes = new ArrayList<Attribute>();
	}
	
	
	/**
	 * @return l'en-tête d'une contraine sous la forme "nom mot-clé".<br/>
	 * Exemple : ck_matable_at1_at2 CHECK.
	 */
	protected String getEntete()
	{
		return this.name + " " + this.keyWord;
	}


	//Méthodes
	/**
	 * @return le nom unique de la contrainte.
	 */
	public String getName(){return this.name;}
	
	public void setName(String name){
		this.name = name;
	}
	
	
	/**
	 * Détermine le nom de la contrainte.<br/>
	 * Assigne ce nom à la contrainte.
	 */
	public void createAndSetName()
	{
		this.setName(this.createName());
	}
	
	
	/**
	 * Retourne une représentation de la contrainte 
	 * brute
	 * @exemple CHECK(machin IS NOT NULL)
	 * @return String
	 */
	public abstract String getNameSQL();
	
	
	/** 
	 * exemple retourne 'nn_table_att UNIQUE'
	 * l'entete de la contrainte
	 * @return 
	 */
	public Table getTable() {return table;}

	
	/**
	 * @param table : la table visée par la contrainte, null interdit.
	 * @see business.Table
	 */
	public void setTable(Table table) {
		this.table = table;
		this.createAndSetName();
	}

	/**
	 * @return la liste des attributs visés par la contrainte.
	 */
	public List<Attribute> getAttributes(){
		return this.attributes;
	}


	/**
	 * TODO : empécher les doublons.
	 * TODO : mettre à jour le nom de la contrainte.
	 * Ajoute un attribut visé par la contrainte.
	 * @param att : null interdit
	 */
	public void addAttribute(Attribute att)
	{
		this.attributes.add(att);
		this.createAndSetName();
	}
	
	/**
	 * TODO : préciser exactement ce qu'il ressort.
	 * exemple : 
	 * ALTER TABLE tableTest
	 * ADD CONSTRAINT pk_pers_php UNIQUE(att)
	 * @return String
	 */
	public String toAddConstraintSQL(){
		return this.toAlterSQL()+"ADD CONSTRAINT "+this.getNameSQL();
	}
	
	/**
	 * exemple :
	 * ALTER TABLE tableTest
	 * ADD CONSTRAINT pk_pers_php
	 * @return String
	 */
	public String toDropConstraintSQL(){
		return this.toAlterSQL()+"DROP CONSTRAINT "+this.name;
	}
	
	/**
	 * Retourne ALTER TABLE {nomTable}\n
	 * @return String
	 */
	private String toAlterSQL(){
		if (this.table == null){
			return "spécifiez le nom de la table svp";
		}
		return "ALTER TABLE "+this.table.getName()+"\n";
	}


	/**
	 * @return le nom de la contrainte, déterminé par : <br/>
	 * - son prefix,<br/>
	 * - sa table, <br/>
	 * - la liste de ses attributs.
	 */
	public String createName()
	{
		StringBuilder result = new StringBuilder();
		
		if (this.prefix != null) result.append(this.prefix + '_');
		if (table != null) result.append(table.getName() + '_');
		for (Attribute attribute : this.attributes){
			result.append(attribute.name + '_');  
		}
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}


	public boolean dropAttribute(Attribute attributeToDrop) {
		return this.attributes.remove(attributeToDrop);
		
	}


	@Override
	public String toString() {
		return "Constraint [id= "+this.thisId+", keyWord=" + keyWord + ", attributes=" + attributes
				+ ", table=" + table + ", name=" + name + ", prefix=" + prefix
				+ "]";
	}
	
	/**
	 * Compare 2 contraintes d'après un id écrit en brut
	 */
	@Override
	public boolean equals(Object o){
		if (((Constraint)o).thisId==this.thisId ){
			return true;
		}
		return false;
	}


	/**
	 * supprime tous le contenu de la constrainte
	 */
	public void cleanAll() {
		this.attributes.clear();
		
		this.table = null;
		this.keyWord = "NO_KEYWORD";
		
		
	}
	
	
	
	
}

package business;

import java.util.ArrayList;
import java.util.List;


public abstract class Constraint
{
	//Attributs
	/** Mot clé définissant la contrainte exemple : PRIMARY KEY.*/

	protected String keyWord;
	
	/** Attributs ciblés par la contrainte.*/
	protected List<Attribute> attributes;
	
	/** Une contrainte appartient à une table.*/
	//TODO : voir les applications
	protected Table table;
	
	/** Nom unique de la contrainte.*/ 
	protected String name;

	/** Préfixe du nom de la contrainte, parmi nn, ck.*/
	protected String prefix; //TODO : inutile puisque déterminable depuis keyword.
	
	

	//Constrcuteur
	/**
	 * Constructeur vide.
	 */
	protected Constraint()
	{
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
	 * TODO : à repréciser, incompréhensible
	 * @return un type CHECK(machin IS NOT NULL)
	 */
	public abstract String getNameSQL();
	
	
	/** 
	 * exemple retourne 'nn_table_att UNIQUE'
	 * l'entete de la contrainte
	 * @return 
	 */
	public Table getTable() {return table;}

	
	/**
	 * TODO : confirmer que null est interdit.
	 * @param table : la table visée par la contrainte, null interdit.
	 */
	public void setTable(Table table) {
		this.table = table;
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
	
	
	
	
}

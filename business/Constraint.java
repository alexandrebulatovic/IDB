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
	public String getName(){
		return this.name;
	}
	
	/**
	 * set le nom de la contrainte
	 * @note la longueur ne doit dépasser les 30 caractères
	 * @param name
	 */
	public void setName(String name){
		if (name == null) return;
		if (name.length()>30){
			this.name = name.substring(0, 30);
		}
		
		else{
			this.name = name;
		}
	}
	
	
	/**
	 * Détermine le nom de la contrainte.<br/>
	 * Assigne ce nom à la contrainte.
	 * @see Constraint#createName()
	 */
	public void createAndSetName()
	{
		this.setName(this.createName());
	}
	
	
	/**
	 * Retourne une représentation de la contrainte 
	 * 'brute'
	 * @exemple CHECK(machin IS NOT NULL)
	 * @return String
	 */
	public abstract String getNameSQL();
	
	
	/** 
	 * @return la table source concerné par la contrainte
	 */
	public Table getTable() {
		return table;
	}
	
	/**
	 * retourne l'id unique de la constrainte
	 * @return thisId
	 */
	public long getId(){
		return this.thisId;
	}

	
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
	 * Ajoute un attribut visé par la contrainte.
	 * @param attribute : null interdit
	 * @note change le nom de la constrainte, ne pas faire setName au début ! ({@link Constraint#setName(String)})
	 */
	public void addAttribute(Attribute attribute)
	{
		if (!this.attributes.contains(attribute)){
			this.attributes.add(attribute);
			this.createAndSetName();
		}
	}
	
	/**
	 * retourne une requette sql qui ajoute la contrainte actuelle
	 * @exemple 
	 * ALTER TABLE tableName
	 * ADD CONSTRAINT un_tableName_att UNIQUE(att)
	 * @return String
	 */
	public String toAddConstraintSQL(){
		return this.toAlterSQL()+"ADD CONSTRAINT "+this.getNameSQL();
	}
	
	/**
	 * @exemple
	 * ALTER TABLE tableName<br>
	 * DROP CONSTRAINT pk_tableName_att
	 * @return String
	 */
	public String toDropConstraintSQL(){
		return this.toAlterSQL()+"DROP CONSTRAINT "+this.name;
	}
	
	
	public List<String> getAttributesNames() {
		List<String> retour = new ArrayList<String>();
		
		for (Attribute att : this.attributes){
			retour.add(att.getName());
		}
		
		return retour;
	}
	
	/**
	 * @exemple ALTER TABLE $tableName\n
	 * @return String
	 */
	private String toAlterSQL(){
		if (this.table == null){
			return "spécifiez le nom de la table svp";
		}
		return "ALTER TABLE "+this.table.getName()+"\n";
	}


	/**
	 * @note le nom de dépassera jamais les 30 caractères
	 * @exemple pk_TableName_att1_att2
	 * @see business.Constraint#createAndSetName()
	 * @return le nom de la contrainte, déterminé par : <br/>
	 * - son prefix,<br/>
	 * - sa table, <br/>
	 * - la liste de ses attributs.
	 */
	public String createName()
	{
		StringBuilder result = new StringBuilder();
		StringBuilder start =  createStartName();
		
		
		result.append(start.toString());
		
		for (Attribute attribute : this.attributes){
			result.append(attribute.name + '_');  
		}
		
		result.deleteCharAt(result.length()-1);
		
		if (result.length()>30){
			if (start.length()>30){
				return this.prefix+"_"+String.valueOf(this.thisId);
			}
			start.deleteCharAt(start.length()-1);
			return start.toString();
		}
		
		
		return result.toString();
	}


	/**
	 * @exemple pk_tableName
	 * @return
	 */
	protected StringBuilder createStartName() {
		StringBuilder start = new StringBuilder();
		if (this.prefix != null){
			start.append(this.prefix + '_');
		}
			
		if (table != null){
			start.append(table.getName() + '_');
		}
		return start;
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
	 * Compare 2 contraintes d'après leurs nom
	 * @return
	 */
	public boolean equalsName(Constraint c){
		if (c.getName().equals(this.getName())){
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

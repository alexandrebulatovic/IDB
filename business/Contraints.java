package business;

public abstract class Contraints {
	

	
	/**
	 * Mot clé définissant la contrainte 
	 * exemple : PRIMARY KEY
	 */
	protected String keyWord;
	
	/**
	 * Une contrainte est appliqué à un attribut
	 */
	protected Attribute attribute;
	
	/**
	 * Une contrainte appartient à une table
	 */
	protected Table table;
	
	
	/**
	 * C'est le nom de la contrainte contenue dans la bdd 
	 * comme pk_machin_truc
	 */
	protected String name;

	/**
	 * Préfixe du type nn ou ck seulon la contrainte
	 */
	protected String prefix;
	
	/**
	 *
	 * @return String nom
	 */
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	
	/**
	 * Créé le nom de la contrainte selon ses attributs
	 * et l'ajoute dans les attributs !
	 */
	public void createName(){
		this.setName(this.prefix+"_"+this.getTable().getName()+"_"+this.attribute.name);
	}
	

	/**
	 * Retourne un type CHECK(machin IS NOT NULL)
	 * @return String CHECK(machin IS NOT NULL)
	 */
	public abstract String getNameSQL();
	
	
	/**
	 * Un exemple sera plus parlant : 
	 * exemple retourne 'nn_table_att CHECK'
	 * l'entete de la contrainte
	 * @return 
	 */
	protected String getEntete(){
		return this.name+" "+this.keyWord;
	}

	/**
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(Table table) {
		this.table = table;
	}

	public Attribute getAttribute(){
		return this.attribute;
	}

	public void setAttribute(Attribute att){
		this.attribute = att;
	}
	
	
	
	
}

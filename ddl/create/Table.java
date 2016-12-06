package ddl.create;
import java.util.ArrayList;


public class Table {
	
	//Attributs
	private ArrayList<Attribute> attributes;
	
	private String name;
	
	
	/**
	 * Vrai si et seulement si la table doit être supprimée
	 * en cascade, faux sinon.
	 */
	private boolean cascade;

	
	//Constructeur
	public Table(ArrayList<Attribute> list, String name){
		this.attributes=list;
		this.name=name;		
	}
	
	
	public Table(Attribute a, String name)
	{
		this.attributes = new ArrayList<Attribute>();
		this.attributes.add(a);
		this.name = name;
	}
	
	
	public Table(Attribute [] as, String name)
	{
		this.attributes = new ArrayList<Attribute>();
		for (Attribute a : as) {
			this.attributes.add(a);
		}
		this.name = name;
	}
	
	
	public Table(boolean cascade, String name)
	{
		this.name = name;
		this.cascade = cascade;
		this.attributes = new ArrayList<Attribute>();
	}
	
	//Accesseurs
	public  ArrayList<Attribute> getListAttributes(){
		return this.attributes;
	}
	
	public  String getName(){
		return this.name;
	}
	
	
	//Méthodes
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append(this.name + ":\n");
		result.append(this.cascade ? "cascade constraint\n" : "");
		for (Attribute a : this.attributes) {
			result.append(a.toString());
			result.append('\n');
		}
		return result.toString();
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui synthétise $this
	 * en une requète SQL de création de table.
	 * 
	 * @return String
	 */
	public String toCreate()
	{
		StringBuilder result = new StringBuilder();

		result.append(this.tableNameToSQL());
		result.append("\n(\n");
		result.append(this.attributesToSQL());
		if (this.hasPrimaryKey()) {
			result.append(",\n");
			result.append(this.primaryKeyToSQL());
		}
		result.append("\n)");
		return result.toString();
	}
	
	/**
	 * Retourne une chaîne de caractères qui synthétise $this
	 * en une requète SQL de suppression de table.
	 * 
	 * @return String
	 */
	public String toDrop()
	{
		return "DROP TABLE " + this.name + 
				(this.cascade ? " CASCADE CONSTRAINTS" : "");
	}
	
	
	/**
	 * Retourne le nombre d'attributs qui
	 * composent la clef primaire de $this.
	 * 
	 * @return int
	 */
	private int countPrimaryKey()
	{
		int result = 0;
		for (Attribute a : this.attributes) {
			if (a.primaryKey) result++;
		}
		return result;
	}
	
	
	/**
	 * Retourne vrai si et seulement si $this possède
	 * au moins un attribut de clef primaire.
	 * 
	 * @return boolean
	 */
	private boolean hasPrimaryKey()
	{
		return this.countPrimaryKey() != 0;
	}
	
	
	/**
	 * Retourne une chaîne de caractères qui commence
	 * une requète SQL de création de table.
	 * 
	 * La chaîne retournée s'arrête après le nom de la table.
	 * 
	 * @return String
	 */
	private String tableNameToSQL()
	{
		return "CREATE TABLE " + this.name;
	}
	
	
	/**
	 * Retourne une chaine de caractères qui représente
	 * l'ensemble des attributs de $this sous la forme d'une 
	 * requête SQL. A l'exception des contraintes de clefs primaires,
	 * toutes les autres sont retournées.
	 * 
	 * @return String
	 */
	private String attributesToSQL()
	{
		StringBuilder result = new StringBuilder();
		for (Attribute a : this.attributes) {
			result.append(a.toSQL());
			result.append(",\n");
		}
		int resultLength = result.length();
		result.deleteCharAt(resultLength-1);
		result.deleteCharAt(resultLength-2);
		return result.toString();
	}
	
	
	/**
	 * Retourne une chaîne vide si et seulement si $this n'a pas de clée primaire.
	 * 
	 * Retourne une chaîne de caractères qui synthétise les attributs
	 * de la clée primaire de $this sous la forme d'une clause CONSTRAINT.
	 * 
	 * @return String
	 */
	private String primaryKeyToSQL()
	{
		StringBuilder result = new StringBuilder();
		if (this.hasPrimaryKey()) {
			result.append("CONSTRAINT pk_");
			result.append(this.name);
			result.append(" PRIMARY KEY (");
			for (Attribute a : this.attributes) {
				if (a.primaryKey) {
					result.append(a.name);
					result.append(", ");
				}
			}
			result.deleteCharAt(result.length()-1);
			result.deleteCharAt(result.length()-1);
			result.append(")");
		}
		return result.toString();
	}
	
	

}

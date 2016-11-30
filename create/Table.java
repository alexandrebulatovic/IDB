package create;
import java.util.ArrayList;

public class Table {
	
	//Attributs
	private ArrayList<Attribute> listAttributes;
	
	private String tableName;
	
	
	//Constructeur
	public Table(ArrayList<Attribute> list,String name){
		this.listAttributes=list;
		this.tableName=name;		
	}
	
	
	//Accesseurs
	public  ArrayList<Attribute> getListAttributes(){
		return this.listAttributes;
	}
	
	public  String getTableName(){
		return this.tableName;
	}
	
	
	//Méthodes
	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append(this.tableName + ":\n");
		for (Attribute a : this.listAttributes) {
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
	public String toSQL()
	{
		StringBuilder result = new StringBuilder();
		
		result.append("CREATE TABLE " + this.tableName + "\n(\n");
		for (Attribute a : this.listAttributes) {
			result.append(a.toSQL());
			result.append(",\n");
		}
		result.append(this.sqlPrimaryKeys());
		result.append(")");
		return result.toString();
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
		for (Attribute a : this.listAttributes) {
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
	 * Retourne une chaîne de caractères qui synthétise les attributs
	 * de la clée primaire de $this sous la forme d'une clause CONSTRAINT.
	 * 
	 * @return String
	 */
	private String sqlPrimaryKeys()
	{
		StringBuilder result = new StringBuilder();
		if (this.hasPrimaryKey()) {
			result.append("CONSTRAINT pk_");
			result.append(this.tableName);
			result.append(" PRIMARY KEY (");
			for (Attribute a : this.listAttributes) {
				if (a.primaryKey) {
					result.append(a.name);
					result.append(", ");
				}
			}
			result.deleteCharAt(result.length()-1);
			result.deleteCharAt(result.length()-1);
			result.append(")\n");
		}
		return result.toString();
	}
	
	

}

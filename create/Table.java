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
		
		result.append("CREATE TABLE " + this.tableName + "\n(");
		for (Attribute a : this.listAttributes) {
			result.append(a.toSQL());
			result.append(",\n");
		}
		result.deleteCharAt(result.length()-1);
		result.deleteCharAt(result.length()-1);

		result.append("\n)");
		System.out.println(result.toString());
		return result.toString();
	}
	
	

}

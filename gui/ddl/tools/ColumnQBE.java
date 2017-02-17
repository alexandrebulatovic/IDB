package gui.ddl.tools;


import java.util.ArrayList;
import java.util.List;

public class ColumnQBE 
{
	//Attributs
	private String table;

	private String attribute;
	
	private boolean visible;
	
	private List<String> where;
	
	//Constructeur
	public ColumnQBE
	(String tableName, String attributeName, boolean selected, List<String> where)
	{
		this.setTable(tableName);
		this.setAttribute(attributeName);
		this.setVisible(selected);
		this.setWhere(where);
	}

	
	//Accesseurs et mutateurs
	@Override
	public String toString() {
		return "AttributeQBE [tableName=" + table + ", attributeName=" + attribute + ", selected=" + visible
				+ ", where=" + where + "]";
	}

	public String getFrom() {
		return table;
	}

	public String getAttribute() {
		return attribute;
	}


	public boolean isVisible() {
		return visible;
	}


	public void setTable(String tableName) {
		this.table = tableName;
	}

	public void setAttribute(String attributeName) {
		this.attribute = attributeName;
	}

	public void setVisible(boolean selected) {
		this.visible = selected;
	}

	public void setWhere(List<String> where) {
		List<String> result = new ArrayList<String>();
		for (String s : where) {
			result.add(s.replace('\t', ' ').trim());
		}
		this.where = result;
	}
	
	
	//Méthodes
	/**
	 * @return un morceau de clause SELECT désignant la colonne.
	 */
	public String getSelect()
	{
		return this.table + '.' + this.attribute; 
	}
	
	
	/**
	 * @return vrai ssi la colonne possède au moins une clause where, faux sinon.
	 */
	public boolean hasWhere()
	{
		int i = 0, size = this.where.size();
		boolean has = false;
		
		while (i < size && !has) {
			has = this.hasWhereAtRow(i);
			i++;
		}
		return has;
	}
	
	
	/**
	 * @param i : numéro de ligne, 0 <= i < nombre de clauses WHERE
	 * @return vrai ssi la colonne possède une clause WHERE à la ligne $i.
	 */
	public boolean hasWhereAtRow(int i)
	{
		return this.where.size() > i
			   && ! this.where.get(i).equals("");
	}
	
	
	/**
	 * @return le nombre de lignes de clause WHERE de la colonne.
	 */
	public int countWhereRows()
	{
		return this.where.size();
	}
	
	
	/**
	 * @param i : numéro de ligne, 0 <= i < nombre de clauses WHERE
	 * @return l'expression de la condition WHERE de la colonne, à la ligne i.
	 * Si la colonne n'a pas de condition, retourne une chaîne vide.
	 */
	public String getWhereAtRow(int i) 
	{
		if (!this.hasWhereAtRow(i)) {
			return "";
		} else {
			return this.table + "." + this.attribute + ' ' + this.where.get(i);
		}
	}
}

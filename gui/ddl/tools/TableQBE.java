package gui.ddl.tools;

import java.util.ArrayList;
import java.util.List;

public class TableQBE 
{
	//Attributs
	private List<ColumnQBE> columns;


	//Constructeurs
	public TableQBE() {this.columns = new ArrayList<ColumnQBE>();}

	public TableQBE(List<ColumnQBE> columns)
	{
		this.columns = columns;
	}


	//Privées
	/**
	 * @return la clause SELECT de la requête.
	 */
	public String getSelect()
	{
		StringBuilder result = new StringBuilder("SELECT ");
		for (ColumnQBE c : this.columns) {
			if (c.isVisible()) {
				result.append(c.getSelect());
				result.append(", ");
			}
		}
		endOfClause(result, 2);
		return result.toString();
	}

	
	/**
	 * @return la clause FROM de la requête.
	 */
	public String getFrom()
	{
		String from;
		List<String> tables = new ArrayList<String>();
		StringBuilder result = new StringBuilder("FROM ");

		for (ColumnQBE c : this.columns) {
			from = c.getFrom();
			if (! tables.contains(from)) {
				tables.add(from);
				result.append(from);
				result.append(", ");
			}
		}
		endOfClause(result, 2);
		return result.toString();
	}

	
	/**
	 * @return vrai ssi la requête possède une clause WHERE.
	 */
	public boolean hasWhere()
	{
		for (ColumnQBE c : this.columns) {
			if (c.hasWhere()) return true; 
		}
		return false;
	}
	
	
	/**
	 * @param i : numéro de ligne, i <= 0 <= dernière ligne avec une condition 
	 * @return vrai ssi la requête possède une condition à la ligne $i.
	 */
	public boolean hasWhereAtRow(int i)
	{
		for (ColumnQBE c : this.columns) {
			if (c.hasWhereAtRow(i)) return true;
		}
		return false;
	}
	
	
	public int countWhereRows()
	{
		int i = 0, result = -1;
		int [] count = new int [this.columns.size()];
	
		for (ColumnQBE c : this.columns) {
			count [i] = c.countWhereRows();
			if (count [i] > result) {
				result = count [i];
			}
			i++;
		}
		return result;
	}

	
	public String getWhere()
	{
		final String or = "\nOR ";
		StringBuilder result = new StringBuilder("WHERE ");
		int turn = this.countWhereRows();
		
		for (int i = 0; i < turn; i++) {
			if (this.hasWhereAtRow(i)) {
				result.append(this.getWhereAtRow(i));
				result.append(or);
			}
		}
		endOfClause(result, or.length());
		return result.toString();
	}


	
	public String getWhereAtRow(int i)
	{
		final String and = " AND ";
		StringBuilder result = new StringBuilder("(");
		
		for (ColumnQBE c : this.columns) {
			if (c.hasWhereAtRow(i)) {
				result.append(c.getWhereAtRow(i));
				result.append(and);
			}
		}
		endOfClause(result, and.length());
		result.append(')');
		return result.toString();
	}



	//Méthodes
	public String getQuery()
	{
		StringBuilder result = new StringBuilder(this.getSelect());
		result.append('\n');
		result.append(this.getFrom());
		if (this.hasWhere()) {
			result.append('\n');
			result.append(this.getWhere());
		}
		return result.toString();
	}

	
	/**
	 * Retire les $x derniers caractères de $sb.
	 * 
	 * @param sb : null interdit.
	 * @param x : nombre de caractères à supprimer à partir de la fin, 0 < x.
	 */
	private static void endOfClause(StringBuilder sb, int x)
	{
		for (int i = 0; i < x; i++) {
			sb.deleteCharAt(sb.length()-1);
		}
	}
}

package ddl;

public class MySQLTableModel 
extends AbstractTableModel 
{
	//Constructeur
	/**
	 * Constructeur vide.
	 */
	public MySQLTableModel()
	{
		super("t");
	}
	
	/**
	 * Constructeur commun.
	 * 
	 * @param name : nom de la tavle, null interdit.
	 */
	public MySQLTableModel(String name)
	{
		super(name);
	}
}

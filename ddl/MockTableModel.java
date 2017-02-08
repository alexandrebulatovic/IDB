package ddl;

public class MockTableModel 
extends AbstractTableModel 
{
	//Constructeur
	/**
	 * Constructeur vide.
	 */
	public MockTableModel()
	{
		super("t");
	}
	
	/**
	 * Constructeur commun.
	 * 
	 * @param name : nom de la tavle, null interdit.
	 */
	public MockTableModel(String name)
	{
		super(name);
	}
}

package ddl;

public class OracleTableModel 
extends AbstractTableModel 
{
	//Constructeur
	/**
	 * Constructeur vide.
	 */
	public OracleTableModel()
	{
		super();
	}
	
	/**
	 * Constructeur commun.
	 * 
	 * @param name : nom de la tavle, null interdit.
	 */
	public OracleTableModel(String name)
	{
		super(name);
	}
	
	
	//Méthodes
	@Override
	public void setName(String name)
	{
		super.setName(name.toUpperCase());
	}
}

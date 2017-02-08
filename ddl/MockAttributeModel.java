package ddl;

public class MockAttributeModel 
extends AbstractAttributeModel
{
	//Constructeurs
	/**
	 * Constructeur vide.
	 */
	public MockAttributeModel()
	{
		super();
	}
	
	
	/**
	 * Constructeur commun.
	 * 
	 * @param name : le nom de l'attribut, null interdit.
	 * @param type : le type de l'attribut, null interdit.
	 * @param size : la taille de l'attribut.
	 * @param notNull : vrai si et seulement si l'attribut ne peut pas être null, 
	 * faux sinon.
	 * @param primaryKey : vrai si et seulement si l'attribut est membre de la 
	 * clée primaire, faux sinon.
	 */
	public MockAttributeModel(String name, String type, int size,
			boolean notNull, boolean primaryKey) 
	{
		super(name, type, size, notNull, primaryKey);

	}

	public MockAttributeModel(MockAttributeModel mock)
	{
		super(mock);
	}
	
	
	//Méthodes
	@Override
	public String sizeErrorMsg() 
	{
		return "Pas d'erreur.";
	}

	
	@Override
	protected int errorSizeCode() 
	{
		return 0;
	}


	@Override
	public String toString() {
		return "MockAttribute [name=" + name + ", type=" + type + ", notNull="
				+ notNull + ", size=" + size + ", primaryKey=" + primaryKey
				+ "]";
	}
}
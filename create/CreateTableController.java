package create;

import connect.ConnectionManager;

public class CreateTableController 
{
	//Attributs
	/**
	 * IHM pour créer une table et ses attributs.
	 */
	private CreateTableView mhi;
	
	/**
	 * TODO : créer une classe CreateTableManager
	 */
	
	
	//Contructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param cm : objet ConnectionManager obtenu lors de la connexion.
	 */
	public CreateTableController(ConnectionManager cm)
	{
		this.mhi = new CreateTableView(this);
	}
	
	
	
}

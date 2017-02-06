package facade;

import business.TableSet;
import manager.connection.I_ConnectionManager;
import manager.ddl.I_DDLManager;

public class CRUDFacade extends AbstractBusinessDDLFacade{

	private I_ConnectionManager connector;
	
	public CRUDFacade(I_DDLManager manager, I_ConnectionManager connector, TableSet tables) {
		super(manager,tables);
		this.connector = connector;
	}

}

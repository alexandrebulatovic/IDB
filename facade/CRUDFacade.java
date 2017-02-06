package facade;

import business.TableSet;
import manager.connection.I_ConnectionManager;
import manager.ddl.I_DDLManager;

public class CRUDFacade extends AbstractBusinessDDLFacade{

	private I_ConnectionManager connector;
	
	public CRUDFacade(I_DDLManager manager,TableSet tables,I_ConnectionManager connector) {
		super(tables,manager);
		this.connector = connector;
	}

}

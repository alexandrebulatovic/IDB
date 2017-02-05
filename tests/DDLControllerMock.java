package tests;

import java.sql.Connection;

import controller.DDLController;

import manager.ddl.I_DDLManager;

public class DDLControllerMock extends DDLController {

	public DDLControllerMock(I_DDLManager manager) {
		super(manager);
	}

}

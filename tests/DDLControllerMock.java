package tests;

import java.sql.Connection;

import ddl.DDLController;

public class DDLControllerMock extends DDLController {

	public DDLControllerMock(Connection connection) {
		super(connection);
	}

}

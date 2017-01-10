package tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import ddl.ModifyTableGUI;

public class TestModifyTableGUI {

	ModifyTableGUI gui;
	
	@Before
	public void before(){
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(null, null, null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui = new ModifyTableGUI(new DDLControllerMock(connection));
	}
	
	@Test
	public void test(){
		fail();
	}
}

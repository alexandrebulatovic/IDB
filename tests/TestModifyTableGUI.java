package tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import ddl.ModifyTableGUI;
import manager.DefaultValueManager;

//public class TestModifyTableGUI {
//
//	ModifyTableGUI gui;
//	
//	@Before
//	public void before(){
//		DefaultValueManager dvm = new DefaultValueManager();
//		gui = new ModifyTableGUI(new DDLControllerMock(dvm));
//	}
//	
//	@Test
//	public void test(){
//		fail();
//	}
//}

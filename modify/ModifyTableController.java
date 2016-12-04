package modify;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;

import connect.ConnectionManager;

public class ModifyTableController{
		
	
	public ModifyTableController(ConnectionManager connection){
		new ModifyTableView(connection);
	}
}
//SELECT TABLE_NAME FROM user_tables
//SELECT * FROM all_tables WHERE table_name='PROFS'
//DESCRIBE PROFS
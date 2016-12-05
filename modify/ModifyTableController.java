package modify;

import connect.ConnectionManager;
import create.Attribute;
import create.CreateTableController;
import create.Table;

public class ModifyTableController{
		
	ConnectionManager cm;
	
	public ModifyTableController(ConnectionManager cm){
		this.cm = cm;
		new ModifyTableView(this,cm);
	}

	public void modifier(String string) {
		System.out.println("Hello gyes");
		CreateTableController test = new CreateTableController(cm);
		//Table table = new Table(,"testingge");
		test.setView(new Attribute[]{new Attribute("att1","VARCHAR",20)},"TEGTEFGTEGTEF");
	}
	
	private Attribute[] getAttributes(){
		Attribute[] attributs = null;
		
		
		return attributs;
		
	}
}
//SELECT TABLE_NAME FROM user_tables
//SELECT * FROM all_tables WHERE table_name='PROFS'
//DESCRIBE PROFS
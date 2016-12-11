//package modify;
//
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import useful.ConnectionManager;
//import ddl.DDLController;
//import ddl.create.Attribute;
//import ddl.create.Table;
//
//public class ModifyTableController{
//		
//	ConnectionManager cm;
//	
//	public ModifyTableController(ConnectionManager cm){
//		this.cm = cm;
//		new ModifyTableView(this,cm);
//	}
//
//	public void modifier(String string) {
//		System.out.println("Je vais modifier la table"+string);
////		DDLController test = DDLController.getInstance();
////		
////		.setView(new Attribute[]{new Attribute("att1","VARCHAR",20, false, false, false, false, string, string)},"TEGTEFGTEGTEF");
//	}
//	
//	private Attribute[] getAttributes(String tableName){
////		this.name=name; v
////		this.type=type; v
////		this.size=size; v
////		this.notNull=notNull; v
////		this.unique=unique; 
////		this.primaryKey=pk; v
////		this.foreignKey=fk; 
////		this.fkTable=fkTable;
////		this.fkAttribute=fkAttribute;
//		//SELECT * FROm USER_TAB_COLUMNS WHERE table_name='PROFS'
//		Attribute[] attributs = null;
//		if (!this.cm.isConnected()){
//			cm.connect();
//			
//		}
//		Connection co = cm.dbms();
//		Statement st = null;
//		ResultSet rs = null;
//		String[] l = new String[3];
//		List<String[]> att = new ArrayList<String[]>();
//		
//		try {
//			st = co.createStatement();
//
//			rs = st.executeQuery("DESCRIBE "+tableName);
//			while (rs.next()){
//				l[0] = rs.getString(1);
//				l[1] = rs.getString(2);
//				l[2] = rs.getString(3);
//				att.add(l);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		attributs = new Attribute[att.size()];
//		for (int i = 0 ; i<att.size() ; i++){
//			attributs[i] = new Attribute(att.get(i)[0], tableName, i, false, false, false, false, tableName, tableName);
//			
//		}
//		return attributs;
//		
//	}
//}
////SELECT TABLE_NAME FROM user_tables
////SELECT * FROM all_tables WHERE table_name='PROFS'
////DESCRIBE PROFS
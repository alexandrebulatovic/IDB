package create;
import java.util.ArrayList;

public class Table {
	private ArrayList<Attribute> listAttributes;
	
	private String tableName;
	
	
	public Table(ArrayList<Attribute> list,String name){
		this.listAttributes=list;
		this.tableName=name;		
	}

}

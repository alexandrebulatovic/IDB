package business;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

public class testTable {

	private Table table;
	private Attribute att1;
	private Attribute att2;
	private Attribute att0;
	private PrimaryKeyConstraint pk1;
	private ArrayList<Constraint> constraints;

	@Before
	public void init(){
		constraints = new ArrayList<Constraint>();
		table = new Table("nomTable", false);
		
			att0 = new Attribute("id","NUMBER",500);
				pk1 = new PrimaryKeyConstraint();
				constraints.add(pk1);
				pk1.addAttribute(att0);
				pk1.setTable(table);
				pk1.createAndSetName();
			att0.addConstraint(pk1);
			
			
			att1 = new Attribute("nom","VARCHAR2",20);
			att1.setTableName(table.getName());
			
			att2 = new Attribute("prenom","VARCHAR",20);
			att2.setTableName(table.getName());
		
		table.addAttribute(att1);
		table.addAttribute(att2);
		table.addAttribute(att0);
		
		table.setConstraints(constraints);
		
	}
	
	@Test
	public void testToCreate(){
		for (String sql : this.table.toCreate()){
			System.out.println(sql);
		}
	}
	
	
	
}

package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

import business.Attribute;
import business.Constraint;
import business.PrimaryKeyConstraint;
import business.Table;

public class testTable {

	private Table table;
	private Attribute att1;
	private Attribute att2;
	private Attribute att0;
	private PrimaryKeyConstraint pk1;
	private ArrayList<Constraint> constraints;
	private Table table2;

	@Before
	public void init(){
		constraints = new ArrayList<Constraint>();
		table = new Table("PROFILS", false);
		
			att0 = new Attribute("id","NUMBER",10);
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
		
		
		
		table2 = new Table(this.table);
		
	}
	
	@Test
	public void testToCreate(){
		String attendu = "CREATE TABLE PROFILS\n"
				+ "(\n"
				+ "nom VARCHAR2 (20),\n"
				+ "prenom VARCHAR (20),\n"
				+ "id NUMBER (10)\n"
				+ ");"
				+ "ALTER TABLE PROFILS\n"
				+ "ADD CONSTRAINT pk_PROFILS_id PRIMARY KEY(id);\n";
		String total = "";
		for (String sql : this.table.toCreate()){
			total+=sql;
		}

		assertEquals(attendu,total);
	}
	
	
	@Test
	public void testToModify(){
		
	}
	
	
	
}

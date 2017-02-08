package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.*;

import business.Attribute;
import business.Constraint;
import business.ForeignKeyConstraint;
import business.PrimaryKeyConstraint;
import business.Table;
import business.oracle.TableOracle;

public class testTable {

	private Table table;
	private Attribute att1;
	private Attribute att2;
	private Attribute att0;
	private PrimaryKeyConstraint pk1;
	private ArrayList<Constraint> constraints;
	private Table table2;
	private Table tableRecupere;
	private Attribute att_a;
	private ArrayList<Constraint> constraints2;
	private PrimaryKeyConstraint pk_a;
	private Attribute att_b;
	private Attribute att_c;
	private Attribute att_d;

	@Before
	public void init(){
		table = new TableOracle("PROFILS");
		tableRecupere = new TableOracle("PROFILS");
		
		
			att0 = new Attribute("id","NUMBER",10);
				pk1 = new PrimaryKeyConstraint();
				table.addConstraint(pk1);
				pk1.addAttribute(att0);
				pk1.setTable(table);
				pk1.createAndSetName();
			att0.addConstraint(pk1);
			
			
			att1 = new Attribute("nom","VARCHAR2",20,true);//cet attribut est not null
			att1.setTableName(table.getName());
			
			att2 = new Attribute("prenom","VARCHAR2",20);
			att2.setTableName(table.getName());
		
		table.addAttribute(att1);
		table.addAttribute(att2);
		table.addAttribute(att0);
		
		
		
		
		table2 = new Table(this.table);
		
		
		
		att_a = new Attribute("id","NUMBER",10);
			pk_a = new PrimaryKeyConstraint();
			table2.addConstraint(pk_a);
			pk_a.addAttribute(att_a);
			pk_a.setTable(tableRecupere);
			pk_a.setName(this.pk1.getName());
			
		att_a.addConstraint(pk_a);
		
		att_b = new Attribute("nom","VARCHAR2",20,true);
		att_b.setTableName(this.tableRecupere.getName());
		
		att_c = new Attribute("prenom","VARCHAR2",27);
		att_c.setTableName(this.tableRecupere.getName());
		
		
		att_d = new Attribute("dateNaissance","DATE",13);
		att_d.setTableName(this.tableRecupere.getName());
		
		tableRecupere.addAttribute(att_a);
		//tableRecupere.addAttribute(att_b); //on n'ajoute pas le nom volontairement
		tableRecupere.addAttribute(att_c);//volontairement, le prénom à changé de taille & de nom
		tableRecupere.addAttribute(att_d);
		
			
		
		
		
	}
	
	@Test
	public void testToCreate(){
		String attendu = "CREATE TABLE PROFILS\n"
				+ "(\n"
				+ "nom VARCHAR2 (20) NOT NULL,\n"
				+ "prenom VARCHAR2 (20),\n"
				+ "id NUMBER (10)\n"
				+ ");\n"
				+ "ALTER TABLE PROFILS\n"
				+ "ADD CONSTRAINT pk_PROFILS PRIMARY KEY(id);\n";
		String total = "";
		for (String sql : this.table.toCreate()){
			total+=sql+";\n";
		}

		assertEquals(attendu,total);
	}
	
	
	@Test
	public void testToModify(){
		String attendu = "ALTER TABLE PROFILS\n"
					   + "ADD dateNaissance DATE;\n"
					   + "ALTER TABLE PROFILS\n"
					   + "DROP nom;\n"
					   + "ALTER TABLE PROFILS\n"
					   + "MODIFY prenom VARCHAR2 (27);\n";
		String actual = "";
//		System.out.println(this.tableRecupere);
		for (String sql : this.table.toModify(this.tableRecupere)){
			actual+=sql+";\n";
		}
//		System.out.println(actual);
		assertEquals(attendu,actual);
	}
	
	@Test
	public void testDeleteTable(){
		
		
		ForeignKeyConstraint fk = new ForeignKeyConstraint();
		fk.setTable(table);
		fk.setTableDestination(table2);
		fk.addAttribute(att2);
		fk.addAttributeDestination(att_b);
		
		att1.addConstraint(fk);
		
	
		assertTrue(this.table.cleanAll());

		assertEquals(0,att0.getConstraints().size());
		
		assertEquals(0,att1.getConstraints().size());
		assertEquals(0,att2.getConstraints().size());
		assertEquals(0,table.getAttributes().size());
		assertEquals(0,pk1.getAttributes().size());
		
//		for (Attribute c : fk.getAttributes()){
//			System.out.println("atteuheiffh ==>"+fk.attributes.get(0));//possède att2
//		}
		
		assertEquals(0,fk.getAttributes().size());
		
		
		
	}
	
	
	
}

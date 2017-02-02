package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import business.*;

public class testAttribute {
	
	private Attribute attPrincipal;
	private List<Constraint> constraints;
	private UniqueConstraint un1;
	private PrimaryKeyConstraint pk1;
	private Table tableBidon;
	private Attribute attSecond;
	private NotNullConstraint nn1;
	private ForeignKeyConstraint fk1;
	private Table tableBidon2;

	@Before
	public void init(){
		
		constraints = new ArrayList<Constraint>();
		un1 = new UniqueConstraint();
		pk1 = new PrimaryKeyConstraint();
		nn1 = new NotNullConstraint();
		fk1 = new ForeignKeyConstraint();
		
		tableBidon = new Table("tableBidon", false);
		tableBidon2 = new Table("tableBidon2", false);
		
		constraints.add(un1);
		constraints.add(pk1);
		constraints.add(nn1);
		constraints.add(fk1);
		
		attPrincipal = new Attribute("attTest", "VARCHAR", 2, constraints, "tablePersoTest");
		attSecond = 	new Attribute("attTest2", "VARCHAR", 2, constraints, "tablePersoTest");
		un1.setTable(tableBidon);
		pk1.setTable(tableBidon);
		nn1.setTable(tableBidon);
		fk1.setTable(tableBidon);
		fk1.setTableDestination(tableBidon2);
		fk1.addAttributeDestination(attSecond);
		
		
	}
	
	@Test
	public void testToSQL(){
		assertEquals("attTest VARCHAR (2)",attPrincipal.toSQL());
		System.out.println("attribute testé : "+attPrincipal.toSQL());
		
	}
	
	
	@Test
	public void testToCreateSQL(){
		fk1.addAttribute(attPrincipal);
		pk1.addAttribute(attPrincipal);
		nn1.addAttribute(attPrincipal);
		un1.addAttribute(attPrincipal);
		
		fk1.createAndSetName();
		pk1.createAndSetName();
		nn1.createAndSetName();
		un1.createAndSetName();
		String lesRequettes = "";
		String lesRequettesAttendus = "ALTER TABLE tableBidon\n"
				+ "ADD CONSTRAINT un_tableBidon_attTest UNIQUE(attTest);\n"
				+ "\n"
				+ "ALTER TABLE tableBidon\n"
				+ "ADD CONSTRAINT pk_tableBidon_attTest PRIMARY KEY(attTest);\n"
				+ "\n"
				+ "ALTER TABLE tableBidon\n"
				+ "ADD CONSTRAINT ck_tableBidon_attTest CHECK(attTest IS NOT NULL);\n"
				+ "\n"
				+ "ALTER TABLE tableBidon\n"
				+ "ADD CONSTRAINT fk_tableBidon_attTest FOREIGN KEY (attTest) REFERENCES tableBidon2(attTest2);\n"
				+ "\n";
		for (String requette : this.attPrincipal.toCreateConstraintsSQL()){
			lesRequettes += requette+";\n\n";
		}
		assertEquals(lesRequettesAttendus,lesRequettes);
	}
	
	
	@Test
	public void testIsUnique(){
		assertEquals(false,this.attPrincipal.isUnique());
		un1.addAttribute(attPrincipal);
		assertEquals(true,this.attPrincipal.isUnique());
	}
	
	@Test
	public void testIsPk(){
		assertEquals(false, this.attPrincipal.isPk());
		pk1.addAttribute(attPrincipal);
		assertEquals(true, this.attPrincipal.isPk());
		
	}
	
	@Test
	public void testIsNotNull(){
		assertEquals(false, this.attPrincipal.isNotNull());
		nn1.addAttribute(attPrincipal);
		assertEquals(true, this.attPrincipal.isNotNull());
		
	}
	
	
	
	@Test
	public void testIsFk(){
		assertEquals(false, this.attPrincipal.isFk());
		fk1.addAttribute(attPrincipal);
		assertEquals(true, this.attPrincipal.isFk());
		
	}
	
	
	
}










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

	@Before
	public void init(){
		
		constraints = new ArrayList<Constraint>();
		un1 = new UniqueConstraint();
		pk1 = new PrimaryKeyConstraint();
		
		tableBidon = new Table("tableBidon", false);
		
		constraints.add(un1);
		constraints.add(pk1);
		
		attPrincipal = new Attribute("attTest", "VARCHAR", 2, constraints, "tablePersoTest");
		un1.setTable(tableBidon);
		
		
		
	}
	
	@Test
	public void testToSQL(){
		assertEquals("attTest VARCHAR (2)",attPrincipal.toSQL());
		System.out.println(attPrincipal.toSQL());
		
	}
	
	
	@Test
	public void testIsUnique(){
		assertEquals(false,this.attPrincipal.isUnique());
		un1.addAttribute(attPrincipal);
		assertEquals(true,this.attPrincipal.isUnique());
	}
	
	
	
}










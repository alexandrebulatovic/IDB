package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import business.Attribute;
import business.*;

public class testContraints {

	@Test
	public void testFk() {
		ForeignKeyContraint fk = new ForeignKeyContraint();
		Attribute a1 = getAttribute("test");
		Attribute dest1 = getAttribute("testDest");
		Attribute dest2 = getAttribute("testDest2");
		
				
		fk.addAttribute(a1);
		fk.setTable(this.getTable("tableSrc"));
		
		fk.setTableDestination(new Table("tableDest",false));
		fk.addAttributeDestination(dest1);
		fk.addAttributeDestination(dest2);
		
		fk.createName();
		assertEquals("fk_tableSrc_test FOREIGN KEY (test) REFERENCES tableDest(testDest,testDest2)",fk.getNameSQL());
	}
	
	@Test
	public void testPk(){
		Attribute a1 = getAttribute("a1");
		Attribute a2 = getAttribute("a2");
		
		PrimaryKeyConstraint pk = new PrimaryKeyConstraint();
		pk.setTable(getTable("tableTest"));
		pk.addAttribute(a1);
		pk.addAttribute(a2);
		pk.createName();
		assertEquals("pk_tableTest_a1_a2 PRIMARY KEY PRIMARY KEY(a1,a2)",pk.getNameSQL());
		
	}
	
	
	@Test 
	public void testCheck(){
		CheckConstraint ck = new CheckConstraint("machin<(autre+3)");
//		ck.setTable(getTable("tableTest"));
		ck.createName();
		assertEquals("ck CHECK(machin<(autre+3))",ck.getNameSQL());
		ck.setName("ma_check_perso");
		assertEquals("ma_check_perso CHECK(machin<(autre+3))",ck.getNameSQL());
	}
	
	public Attribute getAttribute(String name){
		return new Attribute(name, null, 0, false, false, false, false, null, null);
	}
	public Table getTable(String name){
		return new Table(name, false);
	}


}

package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import business.*;

public class testConstraints {

	@Test
	public void testFk() {
		ForeignKeyConstraint fk = new ForeignKeyConstraint();
		Attribute a1 = getAttribute("test");
		Attribute dest1 = getAttribute("testDest");
		Attribute dest2 = getAttribute("testDest2");
		
				
		fk.addAttribute(a1);
		fk.setTable(this.getTable("tableSrc"));
		
		fk.setTableDestination(new Table("tableDest"));
		fk.addAttributeDestination(dest1);
		fk.addAttributeDestination(dest2);
		
		fk.createAndSetName();
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
		pk.createAndSetName();
		assertEquals("pk_tableTest PRIMARY KEY(a1,a2)",pk.getNameSQL());
		
	}
	
	
	@Test 
	public void testCheck(){
		CheckConstraint ck = new CheckConstraint("machin<(autre+3)");
//		ck.setTable(getTable("tableTest"));
		ck.createAndSetName();
		assertEquals("ck CHECK(machin<(autre+3))",ck.getNameSQL());
		ck.setName("ma_check_perso");
		assertEquals("ma_check_perso CHECK(machin<(autre+3))",ck.getNameSQL());
	}
	
	@Test
	public void testUnique(){
		UniqueConstraint unique = new UniqueConstraint();
		unique.setTable(getTable("tableTest"));
		unique.addAttribute(getAttribute("testAtt"));
		unique.createAndSetName();
		assertEquals("un_tableTest_testAtt UNIQUE(testAtt)",unique.getNameSQL());
	}

//	@Test
//	public void testNotNull(){
//		NotNullConstraint nn = new NotNullConstraint();
//		nn.addAttribute(getAttribute("attTest"));
//		nn.createAndSetName();
//		assertEquals("ck_attTest CHECK(attTest IS NOT NULL)",nn.getNameSQL());
//	}
	
	@Test
	public void testToAddConstraintSQL(){
		Attribute a1 = getAttribute("a1");
		Attribute a2 = getAttribute("a2");
		
		PrimaryKeyConstraint pk = new PrimaryKeyConstraint();
		pk.setTable(getTable("tableTest"));
		pk.addAttribute(a1);
		pk.addAttribute(a2);
		pk.setName("pk_pers_php");
		assertEquals("ALTER TABLE tableTest\nADD CONSTRAINT pk_pers_php PRIMARY KEY(a1,a2)",pk.toAddConstraintSQL());
		assertEquals("ALTER TABLE tableTest\nDROP CONSTRAINT pk_pers_php",pk.toDropConstraintSQL());
	}
	
	@Test
	public void testEquals(){
		UniqueConstraint c1 = new UniqueConstraint();
		UniqueConstraint c2 = new UniqueConstraint();
		Attribute att = new Attribute("attTest", "TYPE", 0, null, "tableName", false);
		c1.addAttribute(att);
		c2.addAttribute(att);
		System.out.println(att); 
		System.out.println(c1.toString());
		System.out.println(c2.toString());
		assertFalse(c1.equals(c2));
		assertTrue(c1.equals(c1));
	}
	
	
	@Test
	public void testLenghtOfNameConstraintName(){
		PrimaryKeyConstraint pk = new PrimaryKeyConstraint();
		pk.setName("azertyuiopqsdfghjklmwxcvbn123$3.14159165358979323");
		assertEquals("azertyuiopqsdfghjklmwxcvbn123$",pk.getName());
		Attribute a1 = new Attribute("zhdzhdgjzhdgjzhdjh", "STRING", 888);
		Attribute a2 = new Attribute("123456789999999", "VARCHAR", 465);
		Table t1 = new Table("tableBidon");
		pk.setTable(t1);
		pk.addAttribute(a1);
		pk.addAttribute(a2);
		pk.createAndSetName();
		assertEquals("pk_tableBidon",pk.getName());
		
		UniqueConstraint un = new UniqueConstraint();
		un.setTable(t1);
		
		un.addAttribute(new Attribute("rien", "TYPE", 66));
		un.addAttribute(new Attribute("rien2", "TYPE", 33));
		un.createAndSetName();
		assertEquals("un_tableBidon_rien_rien2",un.getName());
		un.addAttribute(new Attribute("rien3", "TYPE", 33));
		un.addAttribute(new Attribute("rien4", "TYPE", 33));
		un.addAttribute(new Attribute("rienzdzdzdzdzd5", "TYPE", 33));
		un.addAttribute(new Attribute("rien6", "TYPE", 33));
		un.addAttribute(new Attribute("rien7", "TYPE", 33));
		un.addAttribute(new Attribute("rien11", "TYPE", 33));
		un.addAttribute(new Attribute("rien22", "TYPE", 33));
		un.addAttribute(new Attribute("rien33", "TYPE", 33));
		
		
		un.createAndSetName();
		assertEquals("un_tableBidon",un.getName());
		
	}

	
	public Attribute getAttribute(String name){
		return new Attribute(name, null, 0, null, null, false);
	}
	public Table getTable(String name){
		return new Table(name);
	}


}


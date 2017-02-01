package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import business.Attribute;
import business.ForeignKeyContraint;
import business.Table;

public class testContraints {

	@Test
	public void testFk() {
		ForeignKeyContraint fk = new ForeignKeyContraint();
		Attribute a1 = new Attribute("test", null, 0, false, false, false, false, null, null);
		fk.addAttribute(a1);
		fk.setTable(new Table("tableSrc", false));
		
		fk.setTableDestination(new Table("tableDest",false));
		fk.setAttributeDestination(new Attribute("testDest", null, 0, false, false, false, false, null, null));
		
		fk.createName();
		assertEquals("fk_tableSrc_test FOREIGN KEY (test) REFERENCES tableDest(testDest)",fk.getNameSQL());
		System.out.println(fk.getNameSQL());
	}

}

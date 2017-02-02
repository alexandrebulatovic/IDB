package tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import business.Attribute;
import business.Constraint;

public class testAttribute {
	@Test
	public void testToSQL(){
		new Attribute("attTest", "VARCHAR", 2, lesContraintes, "tablePersoTest");
	}
	
	public List<Constraint> getContraintesBidons(){
		List<Constraint> c = new ArrayList<Constraint>();
		
		return null;
		
	}
}

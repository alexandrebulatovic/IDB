package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class autrestests {

	@Test
	public void test() {
		ArrayList<ClasseTest> liste1 = new ArrayList<ClasseTest>();
		ClasseTest a = new ClasseTest(2,4);
		ClasseTest b = new ClasseTest(3,14);
		
		liste1.add(a);
		liste1.add(b);
		
		ArrayList<ClasseTest> liste2 = new ArrayList<ClasseTest>(liste1);
		
		liste2.get(1).v1=789;
		System.out.println(liste1.get(1).v1);
		System.out.println(liste1.get(0) == liste2.get(0));
		System.out.println(liste1 == liste2);
		liste1.clear();
		
	}

}



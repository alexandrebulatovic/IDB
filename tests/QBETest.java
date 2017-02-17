package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import gui.ddl.tools.ColumnQBE;
import gui.ddl.tools.TableQBE;

import org.junit.Before;
import org.junit.Test;

public class QBETest 
{
	private TableQBE t1;
	private TableQBE t2;
	private TableQBE t3;
	private TableQBE t4;
	
	private ColumnQBE c1;
	private ColumnQBE c2;
	private ColumnQBE c3;
	private ColumnQBE c4;
	private ColumnQBE c5;
	
	
	@Before
	public void before()
	{
		List<String> w1 = new ArrayList<String>();
		
		List<String> w2 = new ArrayList<String>();
		w2.add("");
		
		List<String> w3 = new ArrayList<String>();
		w3.add("   <3\t\t");
		
		List<String> w4 = new ArrayList<String>();
		w4.add("   "); w4.add("   >  4\t\t");
		
		List<String> w5 = new ArrayList<String>();
		w5.add(" <      5"); w5.add("   >  5\t\t"); w5.add("!= 5");
		
		c1 = new ColumnQBE("UN", "idun", true, w1); 	//Pas de where
		c2 = new ColumnQBE("UN", "codeun", true, w2); //Pas de where, mais fait semblant
		c3 = new ColumnQBE("UN", "numun", true, w3);	//Un et un seul where
		c4 = new ColumnQBE("UN", "invisibleun", false, w4);	//un faux where et un vrai where
		c5 = new ColumnQBE("DEUX", "iddeux", true, w5);	//plusieurs where
		

		List<ColumnQBE> columns = new ArrayList<ColumnQBE>(); //Table sans WHERE
		columns.add(c1);
		columns.add(c2);
		t1 = new TableQBE(columns);
		
		columns = new ArrayList<ColumnQBE>(); //Table avec un seul WHERE en première ligne
		columns.add(c1);
		columns.add(c2);
		columns.add(c3);
		t2 = new TableQBE(columns);
		
		columns = new ArrayList<ColumnQBE>(); //Table avec un seul WHERE pas en première ligne
		columns.add(c1);
		columns.add(c2);
		columns.add(c4);
		t3 = new TableQBE(columns);
		
		columns = new ArrayList<ColumnQBE>(); //Table avec un seul WHERE pas en première ligne
		columns.add(c1);
		columns.add(c2);
		columns.add(c3);
		columns.add(c4);
		columns.add(c5);
		t4 = new TableQBE(columns);
	}
	
	
	@Test
	public void checkGetTable()
	{
		assertEquals("UN", c1.getFrom());
		assertEquals("UN", c2.getFrom());
		assertEquals("UN", c3.getFrom());
		assertEquals("UN", c4.getFrom());
		assertEquals("DEUX", c5.getFrom());
		
	}
	
	@Test
	public void checkGetAttribute()
	{
		assertEquals("idun", c1.getAttribute());
		assertEquals("codeun", c2.getAttribute());
		assertEquals("numun", c3.getAttribute());
		assertEquals("invisibleun", c4.getAttribute());
		assertEquals("iddeux", c5.getAttribute());
	}
	
	
	@Test
	public void checkIsVisible()
	{
		assertEquals(true, c1.isVisible());
		assertEquals(true, c2.isVisible());
		assertEquals(true, c3.isVisible());
		assertEquals(false, c4.isVisible());
		assertEquals(true, c5.isVisible());
	}
	
	
	@Test
	public void checkGetSelect()
	{
		assertEquals("UN.idun", c1.getSelect());
		assertEquals("UN.codeun", c2.getSelect());
		assertEquals("UN.numun", c3.getSelect());
		assertEquals("UN.invisibleun", c4.getSelect());
		assertEquals("DEUX.iddeux", c5.getSelect());
	}


	@Test
	public void checkHasWhere()
	{
		assertEquals(false, c1.hasWhere());
		assertEquals(false, c2.hasWhere());
		assertEquals(true, c3.hasWhere());
		assertEquals(true, c4.hasWhere());
		assertEquals(true, c4.hasWhere());
	}
	
	
	@Test
	public void checkHasWhereAtRow()
	{
		assertEquals(false, c1.hasWhereAtRow(0));
		assertEquals(false, c1.hasWhereAtRow(1));
		assertEquals(false, c1.hasWhereAtRow(2));
		
		assertEquals(false, c2.hasWhereAtRow(0));
		assertEquals(false, c2.hasWhereAtRow(1));
		assertEquals(false, c2.hasWhereAtRow(2));
		
		assertEquals(true, c3.hasWhereAtRow(0));
		assertEquals(false, c3.hasWhereAtRow(1));
		assertEquals(false, c3.hasWhereAtRow(2));
		
		assertEquals(false, c4.hasWhereAtRow(0));
		assertEquals(true, c4.hasWhereAtRow(1));
		assertEquals(false, c4.hasWhereAtRow(2));
		
		assertEquals(true, c5.hasWhereAtRow(0));
		assertEquals(true, c5.hasWhereAtRow(1));
		assertEquals(true, c5.hasWhereAtRow(2));
	}
	
	
	@Test
	public void checkGetWhereAtRow()
	{
		assertEquals("", c1.getWhereAtRow(0));
		assertEquals("", c1.getWhereAtRow(1));
		
		assertEquals("", c2.getWhereAtRow(0));
		assertEquals("", c2.getWhereAtRow(1));
		assertEquals("", c2.getWhereAtRow(2));
		
		assertEquals("UN.numun <3", c3.getWhereAtRow(0));
		assertEquals("", c3.getWhereAtRow(1));
		assertEquals("", c3.getWhereAtRow(2));
		
		assertEquals("", c4.getWhereAtRow(0));
		assertEquals("UN.invisibleun >  4", c4.getWhereAtRow(1));
		assertEquals("", c4.getWhereAtRow(2));
		
		assertEquals("DEUX.iddeux <      5", c5.getWhereAtRow(0));
		assertEquals("DEUX.iddeux >  5", c5.getWhereAtRow(1));
		assertEquals("DEUX.iddeux != 5", c5.getWhereAtRow(2));
	}
	
	
	@Test
	public void checkGetSelectTable()
	{
		assertEquals("SELECT UN.idun, UN.codeun", t1.getSelect());
		assertEquals("SELECT UN.idun, UN.codeun, UN.numun", t2.getSelect());
		assertEquals("SELECT UN.idun, UN.codeun", t3.getSelect());
		assertEquals("SELECT UN.idun, UN.codeun, UN.numun, DEUX.iddeux",t4.getSelect());
	}
	
	
	@Test
	public void checkGetFromTable()
	{
		assertEquals("FROM UN", t1.getFrom());
		assertEquals("FROM UN", t2.getFrom());
		assertEquals("FROM UN", t3.getFrom());
		assertEquals("FROM UN, DEUX", t4.getFrom());
	}
	
	
	@Test
	public void checkHasWhereTable()
	{
		assertEquals(false, t1.hasWhere());
		assertEquals(true, t2.hasWhere());
		assertEquals(true, t3.hasWhere());
		assertEquals(true, t4.hasWhere());
	}
	
	
	@Test
	public void checkHasWhereAtRowTable()
	{
		assertEquals(false, t1.hasWhereAtRow(0));
		assertEquals(false, t1.hasWhereAtRow(1));
		assertEquals(false, t1.hasWhereAtRow(2));
		
		assertEquals(true, t2.hasWhereAtRow(0));
		assertEquals(false, t2.hasWhereAtRow(1));
		assertEquals(false, t2.hasWhereAtRow(2));
		
		assertEquals(false, t3.hasWhereAtRow(0));
		assertEquals(true, t3.hasWhereAtRow(1));
		assertEquals(false, t3.hasWhereAtRow(2));
		
		assertEquals(true, t4.hasWhereAtRow(0));
		assertEquals(true, t4.hasWhereAtRow(1));
		assertEquals(true, t4.hasWhereAtRow(2));
	}
	
	
	@Test
	public void countWhereRows()
	{
		assertEquals(1, t1.countWhereRows());
		assertEquals(1, t2.countWhereRows());
		assertEquals(2, t3.countWhereRows());
		assertEquals(3, t4.countWhereRows());
	}
	
	@Test
	public void rien()
	{
		System.out.println(t1.getQuery());
		System.out.println();
		
		System.out.println(t2.getQuery());
		System.out.println();

		System.out.println(t3.getQuery());
		System.out.println();

		System.out.println(t4.getQuery());
		
	}
}

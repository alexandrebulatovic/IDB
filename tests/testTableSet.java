package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import business.TableSet;

public class testTableSet {

	private TableSet ensembleTable;

	@Before
	public void init() {
		ensembleTable = new TableSet();
	}
	
	@Test
	public void test(){
		List<String >tablesNames = new ArrayList<String>();
		
		tablesNames.add("table1");
		tablesNames.add("table2");
		tablesNames.add("table3");
		
		ensembleTable.loadTables(tablesNames);
		ensembleTable.addAttributeToTable("table1", "att1", "VARCHAR2", 20, false, true);
		System.out.println(ensembleTable.getSQLTableToCreate("table1"));
	}

}

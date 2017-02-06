package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import business.Attribute;
import business.Constraint;
import business.Table;
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
		String sqlAttendu = "CREATE TABLE table1\n"
				+ "(\n"
				+ "att1 VARCHAR2 (20)\n"
				+ ");\n"
				+ "ALTER TABLE table1\n"
				+ "ADD CONSTRAINT pk_table1_att1 PRIMARY KEY(att1);\n";
		String sqlTotal = "";
		for (String sql : ensembleTable.getSQLTableToCreate("table1")){
			sqlTotal+=sql+";\n";
		}
		assertEquals (sqlAttendu, sqlTotal);
		
		ensembleTable.addAttributeToTable("table1", "att2", "VARCHAR23", 44, false, true);
		ensembleTable.addAttributeToTable("table1", "att3", "VARCHAR2", 22, false, false);
		
		assertEquals("L'attribut ne doit pas pouvoir être créé en double",false,ensembleTable.addAttributeToTable("table1", "att2", "VARCHAR2", 22, false, false));
		
		
		sqlAttendu = "CREATE TABLE table1\n"
				+ "(\n"
				+ "att1 VARCHAR2 (20)\n"
				+ ");\n"
				+ "ALTER TABLE table1\n"
				+ "ADD CONSTRAINT pk_table1_att1 PRIMARY KEY(att1);\n";
		
		sqlTotal = "";
		for (String sql : ensembleTable.getSQLTableToCreate("table1")){
			sqlTotal+=sql+";\n";
		}
		System.out.println(sqlTotal);
		
		
	}

}

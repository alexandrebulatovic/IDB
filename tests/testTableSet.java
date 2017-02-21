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
		List<String >tablesNames = new ArrayList<String>();
		
		tablesNames.add("table1");
		tablesNames.add("table2");
		tablesNames.add("table3");
		
		ensembleTable.loadTables(tablesNames);
		
		ensembleTable.addAttribute("table1", "att1", "VARCHAR2", 20, false, true);
	}
	
	@Test
	public void testGlobal(){

		String sqlAttendu = "CREATE TABLE table1\n"
				+ "(\n"
				+ "att1 VARCHAR2 (20)\n"
				+ ");\n"
				+ "ALTER TABLE table1\n"
				+ "ADD CONSTRAINT pk_table1 PRIMARY KEY(att1);\n";
		String sqlTotal = "";
		for (String sql : ensembleTable.getSQLTableToCreate("table1")){
			sqlTotal+=sql+";\n";
		}
		assertEquals (sqlAttendu, sqlTotal);
		
		ensembleTable.addAttribute("table1", "att2", "VARCHAR23", 44, false, true);
		ensembleTable.addAttribute("table1", "att3", "VARCHAR2", 22, true, false);
		ensembleTable.addAttribute("table1", "attDate", "DATE", 13, false, false);
		
		assertEquals("L'attribut ne doit pas pouvoir être créé en double",false,ensembleTable.addAttribute("table1", "att2", "VARCHAR2", 22, false, false));
		
		
		sqlAttendu = "CREATE TABLE table1\n"
				+ "(\n"
				+ "att1 VARCHAR2 (20),\n"
				+ "att2 VARCHAR23 (44),\n"
				+ "att3 VARCHAR2 (22) NOT NULL,\n"
				+ "attDate DATE\n"
				+ ");\n"
				+ "ALTER TABLE table1\n"
				+ "ADD CONSTRAINT pk_table1 PRIMARY KEY(att1,att2);\n";
		
		sqlTotal = "";
		for (String sql : ensembleTable.getSQLTableToCreate("table1")){
			sqlTotal+=sql+";\n";
		}
		assertEquals(sqlAttendu,sqlTotal);
		
//		System.out.println(ensembleTable.getSQLTableToCreate("table1"));
		
	}
	
	@Test
	public void testAddForeignKey(){
		ensembleTable.addAttribute("table1", "attFk1", "VARCHAR2", 2, false, false);
		ensembleTable.addAttribute("table1", "attFk2", "VARCHAR2", 2, false, false);
		
		ensembleTable.addAttribute("table2", "attPk1", "VARCHAR2", 2, false, true);
		ensembleTable.addAttribute("table2", "attPk2", "VARCHAR2", 2, false, true);

		
		ensembleTable.addForeignKey("table1", new String[] {"attFk1","attFk2"}, "table2", new String[] {"attPk1","attPk2"});
		System.out.println(ensembleTable.getSQLADDConstraint("table1", "attFk1", "fk_table1_attFk1_attFk2"));
		assertEquals("ALTER TABLE table1\n"
				+ "ADD CONSTRAINT fk_table1_attFk1_attFk2 "
				+ "FOREIGN KEY (attFk1,attFk2) "
				+ "REFERENCES table2(attPk1,attPk2)",
				
				ensembleTable.getSQLTableToCreate("table1").get(2));
	}
	
	@Test
	public void testAccesseurs(){
		ensembleTable.addAttribute("table1", "att2", "VARCHAR2", 2, false, false);
		String name = ensembleTable.addUnique("table1", new String[] {"att2"});
		assertEquals("ALTER TABLE table1\nADD CONSTRAINT un_table1_att2 UNIQUE(att2)",ensembleTable.getSQLADDConstraint("table1", "att2", name));
		
	}
	
	
	@Test
	public void testDropConstraint(){
		ensembleTable.addAttribute("table1", "att2", "VARCHAR2", 2, false, false);
		String name = ensembleTable.addUnique("table1", new String[] {"att2"});
		assertEquals("ALTER TABLE table1\nDROP CONSTRAINT un_table1_att2",ensembleTable.getSQLDropConstraint("table1", "att2", name));
	}
	
	
	@Test
	public void testGetSQLTableToModify(){
		ensembleTable.addAttribute("table1", "att2", "VARCHAR2", 2, false, false);
		ensembleTable.addAttribute("table1", "att3", "VARCHAR2", 2, false, false);
		
		ensembleTable.addAttribute("table1", "attASupprimer", "TYPEBIDON", 314, false, false);
		
//		System.out.println(ensembleTable.getSQLTableToCreate("table1"));
		
		List<Object[]> attributes = new ArrayList<Object[]>();
		
		attributes.add(new Object[]{"att1","VARCHAR2",20,false,true});//on ne change pas l'attribut1
		attributes.add(new Object[]{"att2","VARCHAR2",2,false,true});//on ajjoute cette pk
		attributes.add(new Object[]{"att3","VARCHAR2",2,true,false});//on a un nouvel notNull
		attributes.add(new Object[] {"att4","NUMERIC",33,false,false});//on en ajoute un
		//et on ne met pas le dernier attribute attASupprimer
		System.out.println();
		System.out.println();
		assertEquals("[ALTER TABLE table1\nADD att4 NUMERIC (33), ALTER TABLE table1\nDROP attASupprimer, ALTER TABLE table1\nMODIFY att3 VARCHAR2 (2) NOT NULL, ALTER TABLE table1\nDROP CONSTRAINT pk_table1, ALTER TABLE table1\nADD CONSTRAINT pk_table1 PRIMARY KEY(att1,att2)]",ensembleTable.getSQLTableToModify("table1", attributes).toString());
	}
	
	
	/**
	 * Verifie qu'on ne peut pas ajouter une contrainte unique dont l'attribut à déjà une pk
	 */
	@Test
	public void testAddUnPk(){
		System.out.println("testAddPkUnOrUnPk");
		//att1 est pk
		ensembleTable.addAttribute("table1", "att2", "VARCHAR2", 20, false, false);
		ensembleTable.addUnique("table1", new String[]{"att1"});
		List<String> modifyStrings = ensembleTable.getSQLTableToCreate("table1");
		assertEquals("[CREATE TABLE table1\n(\natt1 VARCHAR2 (20),\natt2 VARCHAR2 (20)\n), ALTER TABLE table1\nADD CONSTRAINT pk_table1 PRIMARY KEY(att1)]",modifyStrings.toString());
		
	}
	
	@Test
	public void testGetMultiPk(){
		//att1 pk
		List<String> pks = new ArrayList<String>();
		System.out.println(ensembleTable.addAttribute("table1", "att2", "VARCHAR2", 20, false, true));
		for (String[] att : ensembleTable.getAttributes("table1")){
			pks.add(att[4]);		
		}
		assertEquals("[PRIMARY, PRIMARY]",pks.toString());	
		
	}
	

	
	
	
}

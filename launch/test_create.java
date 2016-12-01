package launch;

import connect.ConnectionManager;
import connect.ConnectionStrings;
import connect.CustomizedResponse;
import connect.OracleConnectionManager;
import create.Attribute;
import create.CreateTableManager;
import create.Table;
import java.util.ArrayList;


/**
 * Classe statique pour tester l'insertion des tables dans
 * la base de données oracle.
 */
public class test_create {
	
	private static void t(boolean expr)
	{
		System.out.println(expr ? "SUCCES": "ECHEC");
	}
	
	public static void main(String[] args) {
		//CONNEXION
		ArrayList <Table> col = new ArrayList<Table>();
		ConnectionManager connector = new OracleConnectionManager();
		ConnectionStrings param = new ConnectionStrings(
				"oracle.jdbc.OracleDriver", 
				"162.38.222.149", 
				"login", 						//LOGIN
				"mdp", 							//MDP
				"IUT", 
				"1521");
		connector.connect(param);
		CreateTableManager creator = new CreateTableManager(connector);
		
		
		//QUELQUES ATTRIBUTS
		Attribute 
		var1 = new Attribute("normal1", "VARCHAR", 30),
		var2 = new Attribute("normal2", "VARCHAR", 256),
		var3 = new Attribute("normal3", "VARCHAR", 80),

		pri1 = new Attribute("pri1", "CHAR", 5, true),
		pri2 = new Attribute("pri2", "DATE", 0, true),
		pri3 = new Attribute("pri3", "VARCHAR", 10, true),

		num1 = new Attribute("num1", "NUMBER", 10),
		num2 = new Attribute("numNotNull", "NUMBER", 10, true, false, false, false, null, null),
		num3 = new Attribute("numUnique", "NUMBER", 10, false, true, false, false, null, null),
		num4 = new Attribute("numNotNullUnique", "NUMBER", 10, true, true, false, false, null, null),

		dat1 = new Attribute("date1", "DATE", 1),
		dat2 = new Attribute("dateNotNull", "DATE", 1, true, false, false, false, null, null),
		dat3 = new Attribute("dateUnique", "DATE", 1, false, true, false, false, null, null),
		dat4 = new Attribute("dateNotNullUnique", "DATE", 1, true, true, false, false, null, null);	 

		
		//REGROUPEMENT 
		Attribute [] 
		tx2 = {var1, var2},
		tx3 = {pri1, dat1, num1},
		tx4 = {pri1, var1},
		tx5 = {pri1, pri2},
		tx6 = {pri3, dat4, dat3, num3, pri2},
		tx7 = {num1, num2, num3, num4, var3};
		
		
		//FUTURES TABLES
		col.add(new Table(var1, "un_champ")); 		
		col.add(new Table(tx2, "deux_champ"));
		col.add(new Table(pri1, "une_primaire"));	
		col.add(new Table(tx4, "une_primaire_un_champ")); 
		col.add(new Table(tx5, "deux_primaires")); 
		col.add(new Table(tx6, "pleins_trucs")); 
		col.add(new Table(tx7, "nombre_et_textes"));
		
		
		/*
		 * PAS TOUCHER !
		 */
		CustomizedResponse response;
		for (Table t : col) {
			response = creator.createTable(t.toSQL());
			t(response.success());
			if (! response.success()) {
				System.out.println(t.toSQL());
				System.out.println(response.message());
			}
			else{
				creator.createTable("DROP TABLE " + t.getTableName());
			}
		}
		
	}

}

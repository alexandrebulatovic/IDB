package tests;

import static org.junit.Assert.*;
import manager.sql.SQLManager;

import org.junit.Test;


public class SQLManagerTest {

	SQLManager sqlManager = new SQLManager(null, -1);

	@Test
	public void testFormatSQL() throws Exception {
		String requete = "SELECT * FROM tables;";
		String requeteFormated = sqlManager.formatSQL(requete);

		assertEquals("retrait du point virgule", "SELECT * FROM tables", requeteFormated);
	}
}

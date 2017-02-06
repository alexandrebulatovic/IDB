package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import manager.sql.SQLManager;


@RunWith(PowerMockRunner.class)
@PrepareForTest({SQLManager.class})
public class SQLManagerTest {
	
	SQLManager sqlManager = new SQLManager(null, -1);

	@Test
	public void testFormatSQL() throws Exception {
		String requete = "SELECT * FROM tables;";
		String requeteFormated = sqlManager.formatSQL(requete);

		assertEquals("retrait du point virgule", "SELECT * FROM tables", requeteFormated);
	}
}

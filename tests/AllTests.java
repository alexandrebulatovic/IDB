package tests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import business.testTable;

@RunWith(Suite.class)
@SuiteClasses({ testAttribute.class,
				testConstraints.class,
				testTable.class
        })
public class AllTests {

}
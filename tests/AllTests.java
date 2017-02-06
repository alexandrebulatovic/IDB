package tests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ testAttribute.class,
				testConstraints.class,
				testTable.class,
				testTableSet.class
        })
public class AllTests {

}
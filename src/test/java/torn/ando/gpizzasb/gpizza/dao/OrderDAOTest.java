package torn.ando.gpizzasb.gpizza.dao;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import resources.archetype.AppTest;
import torn.ando.gpizzasb.gpizza.entity.Order;

public class OrderDAOTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public OrderDAOTest(String testName)
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue(true);
    }

}

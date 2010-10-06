/**
 * 
 */
package au.org.theark.gdmi;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.PortableInterceptor.SUCCESSFUL;

import au.org.theark.gdmi.util.GWASImport;

/**
 * @author elam
 * 
 */
public class JUnitTestGWASImport {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void TestOne() {
		GWASImport gi = new GWASImport();
		try {
			gi.processMap("/home/elam/TestData/first100.map");
		}
		catch (IOException ioe) {
			fail(ioe.toString());
		}
	}	
}

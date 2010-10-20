/**
 * 
 */
package au.org.theark.gdmi.test;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.*;	//requires JUnit4
//import org.junit.After;	//requires JUnit4
//import org.junit.AfterClass;	//requires JUnit4
//import org.junit.Before;	//requires JUnit4
//import org.junit.BeforeClass;	//requires JUnit4
//import org.junit.Rule;  //requires JUnit 4.8.2
import org.junit.Test;	//requires JUnit4
import org.junit.runner.RunWith;	//requires JUnit4
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractTransactionalJUnit38SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.gdmi.exception.FileFormatException;
import au.org.theark.gdmi.exception.GDMISystemException;
import au.org.theark.gdmi.model.dao.GwasDao;
import au.org.theark.gdmi.model.dao.IGwasDao;
import au.org.theark.gdmi.model.dao.IMapStorage;
import au.org.theark.gdmi.model.dao.MarkerDao;
import au.org.theark.gdmi.model.dao.MarkerFlatFile;
import au.org.theark.gdmi.model.entity.MarkerGroup;
import au.org.theark.gdmi.model.entity.MarkerType;
import au.org.theark.gdmi.util.GWASImport;


/**
 * @author elam
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration
@Transactional
public class TestGWASImport extends AbstractTransactionalJUnit38SpringContextTests {

//	@Rule
//	public TemporarySpringContext ctx = new TemporarySpringContext("applicationContext.xml");

	private IGwasDao gwasDao;
	private IMapStorage markerDao;
	
	@Autowired
	public void setGwasDao(IGwasDao gwasDao) {
		this.gwasDao = gwasDao;
	}

	public IGwasDao getGwasDao() {
		return gwasDao;
	}

	@Autowired
	public void setMapStorage(IMapStorage markerDao) {
		this.markerDao = markerDao;
	}

	public IMapStorage getMapStorage() {
		return markerDao;
	}
	
	//Requires JUnit4...
	/**
	 * @throws java.lang.Exception
	 */
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}

	/**
	 * @throws java.lang.Exception
	 */
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}

	/**
	 * @throws java.lang.Exception
	 */
//	@Before
//	public void setUp() throws Exception {
//	}

	/**
	 * @throws java.lang.Exception
	 */
//	@After
//	public void tearDown() throws Exception {
//	}

	@Test
	public void TestOne() {
		String userId = "test12345";
		Date dateNow = new Date(System.currentTimeMillis());

		MarkerType markerType = gwasDao.getMarkerType("SNP");
		MarkerGroup markerGroup = new MarkerGroup();
		markerGroup.setStudyId(new Long(1));
		markerGroup.setUploadId(new Long(1));
		markerGroup.setMarkerType(markerType);
		markerGroup.setUserId(userId);
		markerGroup.setInsertTime(dateNow);
		
		// if whichever is the correct IMapStorage
		// then pass this to GWASImport
		// assuming Database is the target...
		GWASImport gi = null;
		if (true) {
			((MarkerDao)markerDao).setup(markerGroup, userId);
			gi = new GWASImport(markerDao, null);
		}
//		else {
//			gi = new GWASImport(new MarkerFlatFile(), null);
//		}
		// MarkerDao md = new MarkerDao(markerGroup, userId);
		// GWASImport gi = new GWASImport(md, null);
		try {
			File mapFile = new File("/home/ark/TestData/first100.map");
			InputStream is = new FileInputStream(mapFile);
			gi.processMap(is, mapFile.length());
			((MarkerDao)markerDao).flush();
		}
		catch (IOException ioe) {
			fail(ioe.toString());	//requires JUnit4
		}
		catch (FileFormatException ffe) {
			fail(ffe.toString());	//requires JUnit4
		}
		catch (GDMISystemException gse) {
			fail(gse.toString());	//requires JUnit4
		}
	}	
}

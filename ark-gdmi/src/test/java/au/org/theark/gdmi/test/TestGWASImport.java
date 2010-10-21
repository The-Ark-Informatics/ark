/**
 * 
 */
package au.org.theark.gdmi.test;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractTransactionalJUnit38SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.gdmi.exception.DataAcceptorIOException;
import au.org.theark.gdmi.exception.FileFormatException;
import au.org.theark.gdmi.exception.GDMISystemException;
import au.org.theark.gdmi.model.dao.IGwasDao;
import au.org.theark.gdmi.model.entity.Marker;
import au.org.theark.gdmi.model.entity.MarkerGroup;
import au.org.theark.gdmi.model.entity.MarkerType;
import au.org.theark.gdmi.service.GDMIService;
import au.org.theark.gdmi.util.IMapDataAcceptor;
import au.org.theark.gdmi.util.PedMapImport;


/**
 * @author elam
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration
@Transactional
public class TestGWASImport extends AbstractTransactionalJUnit38SpringContextTests {

	final Logger log = LoggerFactory.getLogger(TestGWASImport.class);

	private IGwasDao gwasDao;
	
	@Autowired
	public void setGwasDao(IGwasDao gwasDao) {
		this.gwasDao = gwasDao;
	}

	public IGwasDao getGwasDao() {
		return gwasDao;
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
		MapPipeToMaker mapPipeToMarker = this.new MapPipeToMaker();
		mapPipeToMarker.setup(markerGroup, userId);
		PedMapImport gi = new PedMapImport(mapPipeToMarker, null);

		try {
			File mapFile = new File("/home/ark/TestData/first100.map");
			InputStream is = new FileInputStream(mapFile);
			gi.processMap(is, mapFile.length());
			//markerDao.flush();
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
	
	@Test
	public void TestTwo() {
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
		MapPipeToMaker mapPipeToMarker = this.new MapPipeToMaker();
		mapPipeToMarker.setup(markerGroup, userId);
		PedMapImport gi = new PedMapImport(mapPipeToMarker, null);

		try {
			File pedFile = new File("/home/ark/TestData/first100.ped");
			InputStream is = new FileInputStream(pedFile);
			gi.processPed(is, pedFile.length());
			//markerDao.flush();
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
	
	
	/** 
	 * This is an inner class that will accept the data from 
	 * PedMapImport.processMap 
	 */
	private class MapPipeToMaker implements IMapDataAcceptor {

		private MarkerGroup markerGroup;
		private Marker marker;
		private String userId;
		private Date dateNow;
		
		/**
		 * Called to setup the MarkerGroup id and user id for the storage 
		 * @param markerGroupId
		 * @param userId
		 */
		public void setup (MarkerGroup markerGroup, String userId) {
			this.markerGroup = markerGroup;
			this.marker = null;
			this.userId = userId;
			dateNow = new Date(System.currentTimeMillis());
			
		}
		
		/* (non-Javadoc)
		 * @see au.org.theark.gdmi.util.IMapDataAcceptor#init()
		 */
		public void init() throws DataAcceptorIOException {
			marker = new Marker();
			//getSession().save(markerGroup);
			
			marker.setMarkerGroup(markerGroup);
			marker.setUserId(userId);
			marker.setInsertTime(dateNow);
		}

		/* (non-Javadoc)
		 * @see au.org.theark.gdmi.util.IMapDataAcceptor#setChromoNum(long)
		 */
		public void setChromosome(String chromosome) {
			marker.setChromosome(chromosome);

		}

		/* (non-Javadoc)
		 * @see au.org.theark.gdmi.util.IMapDataAcceptor#setMarkerName(java.lang.String)
		 */
		public void setMarkerName(String mkrName) {
			marker.setName(mkrName);

		}

		/* (non-Javadoc)
		 * @see au.org.theark.gdmi.util.IMapDataAcceptor#setGeneDist(long)
		 */
		public void setGeneDist(long geneDist) {
			/* ignored genetic distance because...
			* rlawrence does not believe it is used much, except in the
			* case of microsatellites (morgans will not be unique for   
			* SNPs that are close - i.e. they overlap).  Also, for this
			* reason it is not uncommon for this column to be zero.
			*/
		}

		/* (non-Javadoc)
		 * @see au.org.theark.gdmi.util.IMapDataAcceptor#setBpPos(long)
		 */
		public void setBpPos(long bpPos) {
			marker.setPosition(bpPos);
		}

		/* (non-Javadoc)
		 * @see au.org.theark.gdmi.util.IMapDataAcceptor#commit()
		 */
		public void sync() throws DataAcceptorIOException {
			try {
				gwasDao.createMarker(marker);
			}
			catch (Exception ex) {
				log.error("commit Exception stacktrace: ", ex);
				throw new DataAcceptorIOException("Couldn't commit new marker record to database");
			}
			
		}
	}
}

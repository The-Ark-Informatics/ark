/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.geno.tests;


import org.apache.wicket.spring.injection.annot.SpringBean;
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

import au.org.theark.geno.service.Constants;
import au.org.theark.geno.service.IGenoService;


/**
 * @author elam
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration
@Transactional
public class TestGWASImport extends AbstractTransactionalJUnit38SpringContextTests {

	static final Logger log = LoggerFactory.getLogger(TestGWASImport.class);

	@Autowired
	@SpringBean(name = Constants.GENO_SERVICE)
	private IGenoService serviceInterface;
	
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
		serviceInterface.testGWASImport();
	}
	
	@Test
	public void TestTwo() {
		//TODO: Write a proper test
		return;
	}
	
}

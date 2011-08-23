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
package au.org.theark.core;

import junit.framework.TestCase;

/**
 * Simple test using the WicketTester
 */
public class TestHomePage extends TestCase {
	// private WicketTester tester;

	@Override
	public void setUp() {
		// tester = new WicketTester(new WicketApplication());
	}

	public void testRenderMyPage() {
		// start and render the test page
		// tester.startPage(HomePage.class);

		// assert rendered page class
		// tester.assertRenderedPage(HomePage.class);

		// assert rendered label component
		// tester.assertLabel("message", "If you see this message wicket is properly configured and running");
	}
}

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
package au.org.theark.geno.web;


public class Constants {
	
	// Web constants
	public static final String COLLECTION_DAO = "collectionDao";

	public static final String GENO_SUBMENU = "GenoSubMenus";
	public static final String GENO_SUBMENU_COLLECTION = "Collection";
	public static final String GENO_SUBMENU_TEST = "Test";
	public static final boolean GENO_SUBMENU_TEST_FORCE_ON = true;
	public static final String GENO_SUBMENU_UPLOAD = "Upload";

	public static final String GENO_RESOURCEKEY_COLLECTION = "tab.module.geno.collection";
	public static final String GENO_RESOURCEKEY_UPLOAD = "tab.module.geno.upload";
	public static final String GENO_RESOURCEKEY_TEST = "tab.module.geno.test";

	public static final String FIRETEST = "fireInTheHole";
	public static final String WATERTEST = "theGreatFlood";
	public static final String NOAHTEST = "noahsRescue";
	public static final String HELLOTEST = "helloTest";
	
	public static final String HELLOSERVLET = "helloServlet";	//TODO: Must match the servlet's filter in web.xml 
	
	public static final String SESSION_GENO_COLLECTION_ID = "sessionGenoCollectionId";

	public static final String MSG_NO_STUDY_CONTEXT = "There is no study in context. Please select a study";
	public static final String MSG_NO_GENOCOLLECTION_CONTEXT = "There is no Geno collection in context. Please select a Geno collection";

	public static final String	GENOTYPIC_MAIN_TAB	= "Genotypic";

	
}

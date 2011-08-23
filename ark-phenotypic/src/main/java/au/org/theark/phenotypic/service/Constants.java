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
package au.org.theark.phenotypic.service;

public class Constants {
	public static final String	TABLE_SCHEMA						= "pheno";

	public static final String	STATUS_CREATED						= "Created";
	public static final String	STATUS_ACTIVE						= "Active";
	public static final String	STATUS_EXPIRED						= "Expired";
	public static final String	STATUS_DISABLED					= "Disabled";

	// Attributes
	public static final String	COLLECTION_ID						= "collectionId";

	// Testing
	public static final String	TEST_FILE							= "/home/ark/TestData/testPhenoData.txt";

	// Services
	public static final String	PHENOTYPIC_SERVICE				= "phenotypicService";

	// Exception messages
	public static final String	IO_EXCEPTION						= "IOException: Input error. ";
	public static final String	FILE_FORMAT_EXCEPTION			= "File Format Exception: Input error. ";
	public static final String	PHENOTYPIC_SYSTEM_EXCEPTION	= "Phenotypic System Exception: Input error. ";
}

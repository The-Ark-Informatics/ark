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
package au.org.theark.geno.service;

import java.text.DecimalFormat;


public class Constants {
	
	// Service-level definitions
	public static final String GENO_SERVICE = "genoService";
	
	public static final String STATUS_CREATED = "Created"; 
	public static final String STATUS_ACTIVE = "Active"; 
	public static final String STATUS_EXPIRED = "Expired"; 
	public static final String STATUS_DISABLED = "Disabled"; 

	// Default display formats
	public static final DecimalFormat TWO_DECPLACES = new DecimalFormat("0.00");

	// Entity field name definitions (for HQL criteria building)
	public static final String GENOCOLLECTION = "genoCollection";
	public static final String GENOCOLLECTION_ID = "id";
	public static final String GENOCOLLECTION_STUDY = "study";
	public static final String GENOCOLLECTION_NAME = "name";
	public static final String GENOCOLLECTION_DESCRIPTION = "description";
	public static final String GENOCOLLECTION_STATUS = "status";
	public static final String GENOCOLLECTION_STARTDATE = "startDate";
	public static final String GENOCOLLECTION_EXPIRYDATE = "expiryDate";
	public static final String GENOCOLLECTION_UPDATETIME = "updateTime";
	public static final String GENOCOLLECTION_UPDATEUSERID = "updateUserId";
	public static final String GENOCOLLECTION_INSERTTIME = "insertTime";
	public static final String GENOCOLLECTION_USERID = "userId";
	
	public static final String STATUS = "status";
	public static final String STATUS_ID = "id";
	public static final String STATUS_NAME = "name";
	
	public static final String FILEFORMAT = "fileFormat";
	public static final String FILEFORMAT_ID = "id";
	public static final String FILEFORMAT_NAME = "name";

	public static final String DELIMITERTYPE = "delimiterType";
	public static final String DELIMITERTYPE_ID = "id";
	public static final String DELIMITERTYPE_NAME = "name";

	public static final String UPLOAD = "upload";

	public static final String UPLOADCOLLECTION = "uploadCollection";
	public static final String UPLOADCOLLECTION_ID = "id";
	public static final String UPLOADCOLLECTION_GENOCOLLECTION = "collection";
	public static final String UPLOADCOLLECTION_UPLOAD = "upload";
	public static final String UPLOADCOLLECTION_UPLOAD_FILEFORMAT = "upload.fileFormat";
	public static final String UPLOADCOLLECTION_UPLOAD_DELIMITERTYPE = "upload.delimiterType";
	public static final String UPLOADCOLLECTION_UPLOAD_FILENAME = "upload.filename";
	public static final String UPLOADCOLLECTION_USERID = "userId";
	public static final String UPLOADCOLLECTION_INSERTTIME = "insertTime";
	public static final String UPLOADCOLLECTION_UPDATEUSERID = "updateUserId";
	public static final String UPLOADCOLLECTION_UPDATETIME = "updateTime";
	
	// GenoCollectionVO to CompoundPropertyModel
	public static final String GENO_COLLECTION_VO_ID = "genoCollection.id";
	public static final String GENO_COLLECTION_VO_NAME = "genoCollection.name";
	public static final String GENO_COLLECTION_VO_DESCRIPTION = "genoCollection.description";
	public static final String GENO_COLLECTION_VO_START_DATE = "genoCollection.startDate";
	public static final String GENO_COLLECTION_VO_EXPIRY_DATE = "genoCollection.expiryDate";
	public static final String GENO_COLLECTION_VO_STATUS = "genoCollection.status";

	// UploadCollectionVO to CompoundPropertyModel
	public static final String UPLOADCOLLECTION_VO_ID = "uploadCollection.id";
	public static final String UPLOADCOLLECTION_VO_UPLOAD_FILENAME = "uploadCollection.upload.filename";
	public static final String UPLOADCOLLECTION_VO_UPLOAD_FILEFORMAT = "uploadCollection.upload.fileFormat";
	public static final String UPLOADCOLLECTION_VO_UPLOAD_DELIMITERTYPE = "uploadCollection.upload.delimiterType";

	// Test related definitions
	public static final String TEST = "Test1";
	public static final int TEST_STUDY_ID = 701;	//TODO: Matches ATR for now (24 Nov 2010)
	public static final String TEST_BLOB_INFILE = "/home/ark/TestData/testException.txt";
	public static final String TEST_BLOB_OUTFILE = "/home/ark/TestData/blahOut.txt";
	public static final String TEST_MAP_INFILE = "/home/ark/TestData/first100.map";
	public static final String TEST_SUBJECT_UID = "U601";	//TODO: Matches ATR Subject 15 for now (24 Nov 2010)

}

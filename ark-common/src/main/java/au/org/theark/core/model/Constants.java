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
package au.org.theark.core.model;

// Constants for Entity-to-database definitions
public class Constants {

	public static final String	STUDY_SCHEMA = "study";
	public static final String	REPORT_SCHEMA = "reporting";
	// Pheno
	public static final String	PHENO_TABLE_SCHEMA			= "pheno";

	// Geno
	public static final String	GENO_TABLE_SCHEMA				= "GENO";
	public static final String	COLLECTION_PK_SEQ				= GENO_TABLE_SCHEMA + ".COLLECTION_PK_SEQ";
	public static final String	COLLECTION_IMPORT_PK_SEQ	= GENO_TABLE_SCHEMA + ".COLLECTION_IMPORT_PK_SEQ";
	public static final String	ENCODED_DATA_PK_SEQ			= GENO_TABLE_SCHEMA + ".ENCODED_DATA_PK_SEQ";
	public static final String	MARKER_PK_SEQ					= GENO_TABLE_SCHEMA + ".MARKER_PK_SEQ";
	public static final String	MARKER_GROUP_PK_SEQ			= GENO_TABLE_SCHEMA + ".MARKER_GROUP_PK_SEQ";
	public static final String	META_DATA_PK_SEQ				= GENO_TABLE_SCHEMA + ".META_DATA_PK_SEQ";
	public static final String	META_DATA_FIELD_PK_SEQ		= GENO_TABLE_SCHEMA + ".META_DATA_FIELD_PK_SEQ";
	public static final String	META_DATA_TYPE_PK_SEQ		= GENO_TABLE_SCHEMA + ".META_DATA_TYPE_PK_SEQ";
	public static final String	UPLOAD_PK_SEQ					= GENO_TABLE_SCHEMA + ".UPLOAD_PK_SEQ";
	public static final String	UPLOAD_COLLECTION_PK_SEQ	= GENO_TABLE_SCHEMA + ".UPLOAD_COLLECTION_PK_SEQ";
	public static final String	UPLOAD_MARKER_PK_SEQ			= GENO_TABLE_SCHEMA + ".UPLOAD_MARKER_PK_SEQ";

	// Reporting
	public static final String	REPORT_TABLE_SCHEMA			= "reporting";

	// Lims
	public static final String	LIMS_TABLE_SCHEMA				= "lims";
	public static final String	LIMS_COLLECTION_PK_SEQ		= LIMS_TABLE_SCHEMA + ".COLLECTION_PK_SEQ";
}

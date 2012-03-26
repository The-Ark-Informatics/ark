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
package au.org.theark.core.security;

/**
 * These Role Constants and Permissions are for the new user management. The role names here should match 'name' in the ark_role table
 * @author cellis
 *
 */
public class RoleConstants {
	public static final String	ARK_ROLE_ADMINISTATOR			= "Administrator";
	public static final String	ARK_ROLE_SUPER_ADMINISTATOR	= "Super Administrator";
	public static final String	ARK_ROLE_STUDY_ADMINISTATOR	= "Study Administrator";
	public static final String	ARK_ROLE_SUBJECT_ADMINISTATOR	= "Subject Administrator";
	public static final String	ARK_ROLE_PHENO_ADMINISTATOR	= "Pheno Administrator";
	public static final String	ARK_ROLE_LIMS_ADMINISTATOR		= "LIMS Administrator";
}
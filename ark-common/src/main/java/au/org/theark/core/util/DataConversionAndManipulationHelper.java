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
package au.org.theark.core.util;

public class DataConversionAndManipulationHelper {

	/***
	 * isSomethingLikeTrue will return true IF stringLooselyResemblingBoolean passed in is;
	 * 	- not null or empty
	 *  - "true", "yes", "y", "1" (ignoring case);
	 * @param stringLooselyResemblingBoolean
	 * @return
	 */
	public static boolean isSomethingLikeTrue(String stringLooselyResemblingBoolean) {
		if(stringLooselyResemblingBoolean!=null & !stringLooselyResemblingBoolean.isEmpty()){
			if(	stringLooselyResemblingBoolean.equalsIgnoreCase("true") ||
				stringLooselyResemblingBoolean.equalsIgnoreCase("yes") ||
				stringLooselyResemblingBoolean.equalsIgnoreCase("1") ||
				stringLooselyResemblingBoolean.equalsIgnoreCase("y") ){
				return true;
			}
		}
		return false;
	}

	/***
	 * isSomethingLikeABoolean will return true IF stringLooselyResemblingBoolean passed in is;
	 * 	- not null or empty
	 *  - "true", "yes", "y", "1" (ignoring case);
	 *  - "false", "no", "n", "0" (ignoring case);
	 * @param stringLooselyResemblingBoolean
	 * @return
	 */
	public static boolean isSomethingLikeABoolean(String stringLooselyResemblingBoolean) {
		if(stringLooselyResemblingBoolean!=null & !stringLooselyResemblingBoolean.isEmpty()){
			if(	stringLooselyResemblingBoolean.equalsIgnoreCase("true") ||
				stringLooselyResemblingBoolean.equalsIgnoreCase("yes") ||
				stringLooselyResemblingBoolean.equalsIgnoreCase("1") ||
				stringLooselyResemblingBoolean.equalsIgnoreCase("y") ||
				stringLooselyResemblingBoolean.equalsIgnoreCase("false") ||
				stringLooselyResemblingBoolean.equalsIgnoreCase("no") ||
				stringLooselyResemblingBoolean.equalsIgnoreCase("0") ||
				stringLooselyResemblingBoolean.equalsIgnoreCase("n") ){
				return true;
			}
		}
		return false;
	}

}

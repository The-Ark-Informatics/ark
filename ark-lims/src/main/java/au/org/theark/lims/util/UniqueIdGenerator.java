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
package au.org.theark.lims.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Generate a unique ID, based on a simple formatted current timestamp. The method is synchronized, (thread safe) to hopefully maintain uniqueness
 * @author cellis
 * 
 */
public class UniqueIdGenerator {
	private static String	uniqueID	= new String();
	private static Calendar	calendar;

	/**
	 * Generate a unique ID, based on a simple formatted current timestamp
	 * 
	 * @return String
	 */
	public synchronized static String generateUniqueId() {
		calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		uniqueID = simpleDateFormat.format(now);
		return uniqueID;
	}
}

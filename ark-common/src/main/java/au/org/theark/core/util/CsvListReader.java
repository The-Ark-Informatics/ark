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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;

public class CsvListReader {
	/**
	 * From an input stream, reads every line in the file, and returns a List<String> of values
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static List<String> readColumnIntoList(InputStream inputStream) throws IOException {
		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		List<String> list = new ArrayList<String>(0);
		
		inputStreamReader = new InputStreamReader(inputStream);
		csvReader = new CsvReader(inputStreamReader);

		// Loop through all rows in file
		while (csvReader.readRecord()) {
			list.add(csvReader.getRawRecord());
		}
		return list;
	}
}

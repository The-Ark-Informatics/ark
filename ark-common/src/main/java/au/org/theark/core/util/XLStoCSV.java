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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.Cell;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;

public class XLStoCSV {
	private static final Logger	log						= LoggerFactory.getLogger(XLStoCSV.class);
	char delimiterCharacter = ',';
	
	public XLStoCSV(char delimiterCharacter){
		this.delimiterCharacter = delimiterCharacter;
	}
	
	/**
	 * Return the inputstream of the converted workbook as csv
	 * 
	 * @return inputstream of the converted workbook as csv
	 */
	public InputStream convertXlsToCsv(Workbook w) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DD_MM_YYYY);
		try {
			OutputStreamWriter osw = new OutputStreamWriter(out);
			// Gets first sheet from workbook
			Sheet s = w.getSheet(0);
			Cell[] row = null;

			for (int i = 0; i < s.getRows(); i++) {
				row = s.getRow(i);

				if (row.length > 0) {
					osw.write(row[0].getContents());
					for (int j = 1; j < row.length; j++) {
						osw.write(delimiterCharacter);
						Cell cell = row[j];
						if (row[j].getContents().contains(",")) {  
							osw.write('"');  
							if(cell instanceof DateCell) {
								DateCell dc = (DateCell) cell;
							   Date d = dc.getDate();
							   osw.write(sdf.format(d));
							}
							else {
								osw.write(cell.getContents());
							}
							osw.write('"');  
						} else {
							if(cell instanceof DateCell) {
								DateCell dc = (DateCell) cell;
							   Date d = dc.getDate();
							   osw.write(sdf.format(d));
							}
							else {
								osw.write(cell.getContents());
							}
						}
					}
				}
				osw.write("\n");
			}

			osw.flush();
			osw.close();
		}
		catch (UnsupportedEncodingException e) {
			System.err.println(e.toString());
		}
		catch (IOException e) {
			System.err.println(e.toString());
		}
		catch (Exception e) {
			System.err.println(e.toString());
		}
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	/**
	 * Converts an XLS inputStream to a CSV file
	 * 
	 * @param inputStream
	 */
	public InputStream convertXlsInputStreamToCsv(InputStream inputStream) {
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputStream);
			delimiterCharacter = ',';
			XLStoCSV xlsToCsv = new XLStoCSV(delimiterCharacter);
			inputStream = xlsToCsv.convertXlsToCsv(w);
			inputStream.reset();
		}
		catch (BiffException e) {
			log.error(e.getMessage());
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return inputStream;
	}
}

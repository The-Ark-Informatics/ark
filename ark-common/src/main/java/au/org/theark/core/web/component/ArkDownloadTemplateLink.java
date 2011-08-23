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
package au.org.theark.core.web.component;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.io.ByteArrayOutputStream;

import au.org.theark.core.util.ArkSheetMetaData;

public class ArkDownloadTemplateLink extends Link {
	/**
	 * 
	 */
	private static final long				serialVersionUID	= -6828409003257031738L;
	private String								templateFilename	= null;
	private String[]							templateHeader		= null;
	private transient ArkSheetMetaData	sheetMetaData;

	public ArkDownloadTemplateLink(String id, String templateFilename, String[] templateHeader) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.templateFilename = templateFilename;
		this.setTemplateHeader(templateHeader);
		this.sheetMetaData.setRows(1);
		this.sheetMetaData.setCols(templateHeader.length);

		// Only show link if filename or templateHeader != null
		setVisible(templateFilename != null && templateHeader.length > 0);
	}

	public byte[] writeOutXlsFileToBytes() {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			WritableWorkbook w = Workbook.createWorkbook(output);
			WritableSheet writableSheet = w.createSheet("Sheet", 0);

			for (int row = 0; row < sheetMetaData.getRows(); row++) {
				for (int col = 0; col < sheetMetaData.getCols(); col++) {
					String cellData = getTemplateHeader()[col];
					jxl.write.Label label = new jxl.write.Label(col, row, cellData);
					writableSheet.addCell(label);
				}
			}

			w.write();
			w.close();
			bytes = output.toByteArray();
			output.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}

	public void setTemplateHeader(String[] templateHeader) {
		this.templateHeader = templateHeader;
	}

	public String[] getTemplateHeader() {
		return templateHeader;
	}

	@Override
	public void onClick() {
		byte[] data = writeOutXlsFileToBytes();
		if (data != null) {
			getRequestCycle().setRequestTarget(new au.org.theark.core.util.ByteDataRequestTarget("application/vnd.ms-excel", data, templateFilename + ".xls"));
		}
	}
}

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
package au.org.theark.core.web.component.button;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.io.ByteArrayOutputStream;

import au.org.theark.core.util.ArkSheetMetaData;

public class ArkDownloadAjaxButton extends AjaxButton {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6203972937232570604L;
	private String					filename;
	private String					body;
	private String					fileType;
	private String[]				xlsHeader;

	public ArkDownloadAjaxButton(String id, String filename, String body, String fileType) {
		super(id);
		this.filename = filename;
		this.body = body;
		this.fileType = fileType;

		// Do not submit parent form
		this.setDefaultFormProcessing(false);

		// Only show button if filename or body != null
		setVisible(filename != null || (body != null && body.length() > 0));
	}

	public ArkDownloadAjaxButton(String id, String filename, String fileType, String[] xslHeader) {
		super(id);
		this.filename = filename;
		this.fileType = fileType;
		this.xlsHeader = xlsHeader.clone();

		// Do not submit parent form
		this.setDefaultFormProcessing(false);

		// Only show button if filename not null
		setVisible(filename != null);
		;
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		if (fileType.equalsIgnoreCase("TXT") || fileType.equalsIgnoreCase("CSV")) {
			getRequestCycle().setRequestTarget(new au.org.theark.core.util.ByteDataRequestTarget("text/plain", body.getBytes(), filename + "." + fileType));
		}
		else if (fileType.equalsIgnoreCase("XLS")) {
			getRequestCycle().setRequestTarget(new au.org.theark.core.util.ByteDataRequestTarget("application/vnd.ms-excel", writeOutXlsFileToBytes(xlsHeader), filename + "." + fileType));
		}
	}

	public byte[] writeOutXlsFileToBytes(String[] xlsHeader) {
		byte[] bytes = null;
		ArkSheetMetaData sheetMetaData = new ArkSheetMetaData();
		sheetMetaData.setRows(1);
		sheetMetaData.setCols(xlsHeader.length);

		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			WritableWorkbook w = Workbook.createWorkbook(output);
			WritableSheet writableSheet = w.createSheet("Sheet", 0);

			for (int row = 0; row < sheetMetaData.getRows(); row++) {
				for (int col = 0; col < sheetMetaData.getCols(); col++) {
					String cellData = xlsHeader[col];
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
}

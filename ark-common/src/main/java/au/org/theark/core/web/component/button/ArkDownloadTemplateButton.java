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
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.io.ByteArrayOutputStream;

import au.org.theark.core.util.ArkSheetMetaData;

public abstract class ArkDownloadTemplateButton extends AjaxButton {

	/**
	 * 
	 */
	private static final long				serialVersionUID	= -838971531745438763L;
	private String								templateFilename	= null;
	private String[]							templateHeader		= null;
	private String[][]						templateCells		= null;
	private transient ArkSheetMetaData	sheetMetaData;

	public ArkDownloadTemplateButton(String id, String templateFilename, String[] templateHeader) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.templateFilename = templateFilename;
		this.setTemplateHeader(templateHeader);
		this.setTemplateCells(new String[][] { templateHeader });
		this.sheetMetaData.setRows(1);
		this.sheetMetaData.setCols(templateHeader.length);

		// Do not submit parent form
		this.setDefaultFormProcessing(false);

		// Only show button if filename or templateHeader != null
		setVisible(templateFilename != null && templateHeader.length > 0);
	}

	public ArkDownloadTemplateButton(String id, String templateFilename, String[][] templateCells) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.templateFilename = templateFilename;
		this.setTemplateHeader(templateCells[0]);
		this.setTemplateCells(templateCells);
		this.sheetMetaData.setRows(templateCells.length);
		this.sheetMetaData.setCols(templateHeader.length);

		// Do not submit parent form
		this.setDefaultFormProcessing(false);

		// Only show button if filename or templateHeader != null
		setVisible(templateFilename != null && templateHeader.length > 0);
	}

	public byte[] writeOutXlsFileToBytes() {
		byte[] bytes = null;

		WritableFont normalFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
		WritableFont boldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
		WritableCellFormat cellFormat = null;

		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			WritableWorkbook w = Workbook.createWorkbook(output);
			WritableSheet writableSheet = w.createSheet("Sheet", 0);

			for (int row = 0; row < getTemplateCells().length; row++) {
				for (int col = 0; col < sheetMetaData.getCols(); col++) {
					String cellData = getTemplateCells()[row][col];
					jxl.write.Label label = new jxl.write.Label(col, row, cellData);

					if (row == 0) {
						// Header row in bold
						cellFormat = new WritableCellFormat(boldFont);
					}
					else {
						cellFormat = new WritableCellFormat(normalFont);
					}

					label.setCellFormat(cellFormat);
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

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		byte[] data = writeOutXlsFileToBytes();
		if (data != null) {
			getRequestCycle().scheduleRequestHandlerAfterCurrent(new au.org.theark.core.util.ByteDataResourceRequestHandler("application/vnd.ms-excel", data, templateFilename + ".xls"));
		}
	}

	/**
	 * @param templateHeader
	 *           the templateHeader to set
	 */
	public void setTemplateHeader(String[] templateHeader) {
		this.templateHeader = templateHeader;
	}

	/**
	 * @return the templateHeader
	 */
	public String[] getTemplateHeader() {
		return templateHeader;
	}

	/**
	 * @param templateCells
	 *           the templateCells to set
	 */
	public void setTemplateCells(String[][] templateCells) {
		this.templateCells = templateCells;
	}

	/**
	 * @return the templateCells
	 */
	public String[][] getTemplateCells() {
		return templateCells;
	}
}

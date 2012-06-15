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
package au.org.theark.core.web.component.worksheet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashSet;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.io.ByteArrayOutputStream;

import au.org.theark.core.model.study.entity.UploadType;
import au.org.theark.core.util.ArkSheetMetaData;

import com.csvreader.CsvReader;

public class ArkExcelWorkSheetAsGrid extends Panel {

	private static final long				serialVersionUID				= 2950851261474110946L;
	private Label								updateLegendLabel = new Label("updateLegendLabel", "Indicates data to update");
	private transient Sheet					sheet;																										// an instance of an Excel
	// WorkSheet
	private UploadType 						uploadType;
	private transient ArkSheetMetaData	sheetMetaData;
	private byte[]								workBookAsBytes;
	private char								delimiterType;
	private HashSet<Integer>				insertRows;
	private HashSet<Integer>				updateRows;
	private HashSet<ArkGridCell>			insertCells;
	private HashSet<ArkGridCell>			updateCells;
	private HashSet<ArkGridCell>			warningCells;
	private HashSet<ArkGridCell>			errorCells;
	private String								fileFormat;
	private WebMarkupContainer				wizardDataGridKeyContainer	= new WebMarkupContainer("wizardDataGridKeyContainer");
	private WebMarkupContainer				wizardDataGridKeyContainerForCustom	= new WebMarkupContainer("wizardDataGridKeyContainerForCustom");
	private int									rowsToDisplay					= au.org.theark.core.Constants.ROWS_PER_PAGE;
	private Behavior		errorCellBehavior				= new Behavior() {
																			private static final long	serialVersionUID	= 7204106018358344579L;
																			@Override
																			public void onComponentTag(Component component, ComponentTag tag) {
																				super.onComponentTag(component, tag);
																				tag.put("style", "background: red;");
																			};
																		};

	private Behavior		warningCellBehavior			= new Behavior() {
																			private static final long	serialVersionUID	= 6075625860319512723L;
																			@Override
																			public void onComponentTag(Component component, ComponentTag tag) {
																				super.onComponentTag(component, tag);
																				tag.put("style", "background: orange;");
																			};
																		};

	private Behavior		updateCellBehavior			= new Behavior() {
																			private static final long	serialVersionUID	= -8113218633824905372L;
																			@Override
																			public void onComponentTag(Component component, ComponentTag tag) {
																				super.onComponentTag(component, tag);
																				tag.put("style", "background: lightblue;");
																			};
																		};

	private Behavior		insertCellBehavior			= new Behavior() {
																			private static final long	serialVersionUID	= 7204106018358344579L;
																			@Override
																			public void onComponentTag(Component component, ComponentTag tag) {
																				super.onComponentTag(component, tag);
																				tag.put("style", "background: lightgreen;");
																			};
																		};

	public ArkExcelWorkSheetAsGrid(String id) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.updateRows = new HashSet<Integer>();
		this.insertRows = new HashSet<Integer>();
		this.insertCells = new HashSet<ArkGridCell>();
		this.updateCells = new HashSet<ArkGridCell>();
		this.warningCells = new HashSet<ArkGridCell>();
		this.errorCells = new HashSet<ArkGridCell>();
		initialiseGrid();
	}

	public ArkExcelWorkSheetAsGrid(String id, InputStream inputStream, String fileFormat, char delimChar, FileUpload fileUpload, int rowsToDisplay, UploadType uploadType) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.updateRows = new HashSet<Integer>();
		this.insertRows = new HashSet<Integer>();
		this.insertCells = new HashSet<ArkGridCell>();
		this.updateCells = new HashSet<ArkGridCell>();
		this.warningCells = new HashSet<ArkGridCell>();
		this.errorCells = new HashSet<ArkGridCell>();
		this.fileFormat = fileFormat;
		this.rowsToDisplay = rowsToDisplay;
		this.uploadType = uploadType;
		initialiseWorkbook(inputStream, delimChar);
		initialiseGrid();
		initialiseGridKey(fileUpload, uploadType);
	}

	public ArkExcelWorkSheetAsGrid(String id, InputStream inputStream, String fileFormat, char delimChar, FileUpload fileUpload, HashSet<Integer> updateRows, HashSet<ArkGridCell> errorCells, UploadType uploadType) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.updateRows = updateRows;
		this.insertCells = new HashSet<ArkGridCell>();
		this.updateCells = new HashSet<ArkGridCell>();
		this.warningCells = new HashSet<ArkGridCell>();
		this.errorCells = errorCells;
		this.fileFormat = fileFormat;
		this.uploadType = uploadType;
		initialiseWorkbook(inputStream, delimChar);
		initialiseGrid();
		initialiseGridKey(fileUpload, uploadType);
	}


	public ArkExcelWorkSheetAsGrid(String id, InputStream inputStream, String fileFormat, char delimChar, FileUpload fileUpload, int rowsToDisplay) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.updateRows = new HashSet<Integer>();
		this.insertRows = new HashSet<Integer>();
		this.insertCells = new HashSet<ArkGridCell>();
		this.updateCells = new HashSet<ArkGridCell>();
		this.warningCells = new HashSet<ArkGridCell>();
		this.errorCells = new HashSet<ArkGridCell>();
		this.fileFormat = fileFormat;
		this.rowsToDisplay = rowsToDisplay;
		initialiseWorkbook(inputStream, delimChar);
		initialiseGrid();
		initialiseGridKey(fileUpload, uploadType);
	}

	public ArkExcelWorkSheetAsGrid(String id, InputStream inputStream, String fileFormat, char delimChar, FileUpload fileUpload, HashSet<Integer> updateRows, HashSet<ArkGridCell> errorCells) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.updateRows = updateRows;
		this.insertCells = new HashSet<ArkGridCell>();
		this.updateCells = new HashSet<ArkGridCell>();
		this.warningCells = new HashSet<ArkGridCell>();
		this.errorCells = errorCells;
		this.fileFormat = fileFormat;
		initialiseWorkbook(inputStream, delimChar);
		initialiseGrid();
		initialiseGridKey(fileUpload, uploadType);
	}


	public ArkExcelWorkSheetAsGrid(String id, InputStream inputStream, String fileFormat, char delimChar, FileUpload fileUpload, HashSet<Integer> insertRows, HashSet<Integer> updateRows,
			HashSet<ArkGridCell> errorCells) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.insertRows = insertRows;
		this.updateRows = updateRows;
		this.insertCells = new HashSet<ArkGridCell>();
		this.updateCells = new HashSet<ArkGridCell>();
		this.warningCells = new HashSet<ArkGridCell>();
		this.errorCells = errorCells;
		this.fileFormat = fileFormat;
		initialiseWorkbook(inputStream, delimChar);
		initialiseGrid();
		initialiseGridKey(fileUpload, uploadType);
	}

	public ArkExcelWorkSheetAsGrid(String id, InputStream inputStream, String fileFormat, char delimChar, FileUpload fileUpload, HashSet<Integer> insertRows, HashSet<Integer> updateRows,
			HashSet<ArkGridCell> errorCells, UploadType uploadType) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.insertRows = insertRows;
		this.uploadType = uploadType;
		this.updateRows = updateRows;
		this.insertCells = new HashSet<ArkGridCell>();
		this.updateCells = new HashSet<ArkGridCell>();
		this.warningCells = new HashSet<ArkGridCell>();
		this.errorCells = errorCells;
		this.fileFormat = fileFormat;
		initialiseWorkbook(inputStream, delimChar);
		initialiseGrid();
		initialiseGridKey(fileUpload, uploadType);
	}

	public ArkExcelWorkSheetAsGrid(String id, InputStream inputStream, String fileFormat, char delimChar, FileUpload fileUpload, HashSet<Integer> insertRows, HashSet<Integer> updateRows,
			HashSet<ArkGridCell> updateCells, HashSet<ArkGridCell> errorCells) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.insertRows = insertRows;
		this.updateRows = updateRows;
		this.insertCells = new HashSet<ArkGridCell>();
		this.updateCells = new HashSet<ArkGridCell>();
		this.warningCells = new HashSet<ArkGridCell>();
		this.errorCells = errorCells;
		this.fileFormat = fileFormat;
		initialiseWorkbook(inputStream, delimChar);
		initialiseGrid();
		initialiseGridKey(fileUpload, uploadType);
	}

	public ArkExcelWorkSheetAsGrid(String id, InputStream inputStream, String fileFormat, char delimChar, FileUpload fileUpload, HashSet<Integer> insertRows, HashSet<Integer> updateRows,
			HashSet<ArkGridCell> insertCells, HashSet<ArkGridCell> updateCells, HashSet<ArkGridCell> warningCells, HashSet<ArkGridCell> errorCells) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.insertRows = insertRows;
		this.updateRows = updateRows;
		this.insertCells = insertCells;
		this.updateCells = updateCells;
		this.warningCells = warningCells;
		this.errorCells = errorCells;
		this.fileFormat = fileFormat;
		initialiseWorkbook(inputStream, delimChar);
		initialiseGrid();
		initialiseGridKey(fileUpload, uploadType);
	}

	public ArkExcelWorkSheetAsGrid(String id, InputStream inputStream, String fileFormat, char delimChar, FileUpload fileUpload, HashSet<Integer> insertRows, HashSet<Integer> updateRows,
			HashSet<ArkGridCell> insertCells, HashSet<ArkGridCell> updateCells, HashSet<ArkGridCell> warningCells, HashSet<ArkGridCell> errorCells, int rowsToDisplay) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.insertRows = insertRows;
		this.updateRows = updateRows;
		this.insertCells = insertCells;
		this.updateCells = updateCells;
		this.warningCells = warningCells;
		this.errorCells = errorCells;
		this.fileFormat = fileFormat;
		this.rowsToDisplay = rowsToDisplay;
		initialiseWorkbook(inputStream, delimChar);
		initialiseGrid();
		initialiseGridKey(fileUpload, uploadType);
	}

	private void initialiseGrid() {
		add(new Label("rowsToDisplay", rowsToDisplay + " rows of file"));
		add(createHeadings());
		add(createMainGrid());
	}

	@SuppressWarnings( { "unchecked" })
	private Loop createHeadings() {
		return new Loop("heading", new PropertyModel(sheetMetaData, "cols")) {


			private static final long	serialVersionUID	= -7027878243061138904L;

			public void populateItem(LoopItem item) {
				final int col = item.getIndex();

				/*
				 * this model used for Label component gets data from cell instance Because we are interacting directly with the sheet instance which gets
				 * updated each time we upload a new Excel File, the value for each cell is automatically updated
				 */
				IModel<Object> model = new Model() {
					private static final long	serialVersionUID	= 1144128566137457199L;

					@Override
					public Serializable getObject() {
						Cell cell = sheet.getCell(col, 0);
						return cell.getContents();
					}
				};
				Label cellData = new Label("cellHead", model);
				item.add(cellData);
			}
		};
	}

	/*
	 * generating rows using the Loop class and the PropertyModel with SheetMetaData instance works magicWe bound the numbers of rows stored in
	 * SheetMetaData instance to the Loop using PropertyModel. No table will be displayed before an upload.
	 */
	@SuppressWarnings( { "serial", "unchecked" })
	private Loop createMainGrid() {
		
		// We create a Loop instance and uses PropertyModel to bind the Loop iteration to ExcelMetaData "rows" value
		return new Loop("rows", new PropertyModel(sheetMetaData, "rows")) {
			
			@Override
			public void populateItem(LoopItem item) {
				
				final int row = item.getIndex();

				if (!insertRows.isEmpty() || !updateRows.isEmpty()) {
					setRowCssStyle(row, item);
				}

				if (row > 0 && row <= rowsToDisplay) {
					// creates the row numbers
					item.add(new Label("rowNo", new Model(String.valueOf(row))));

					// We create an inner Loop instance and uses PropertyModel to bind the Loop iteration to ExcelMetaData "cols" value
					item.add(new Loop("cols", new PropertyModel<Integer>(sheetMetaData, "cols")) {
						@Override
						public void populateItem(LoopItem item) {
							final int col = item.getIndex();
							/*
							 * this model used for Label component gets data from cell instance Because we are interacting directly with the sheet instance
							 * which gets updated each time we upload a new Excel File, the value for each cell is automatically updated
							 */
							IModel<Object> model = new Model() {
								/**
								 * 
								 */
								private static final long	serialVersionUID	= 1144128566137457199L;

								@Override
								public Serializable getObject() {
									Cell cell = sheet.getCell(col, row);
									return cell.getContents();
								}
							};
							Label cellData = new Label("cellData", model);
							item.add(cellData);

							ArkGridCell cell = new ArkGridCell(col, row);
							if (errorCells.contains(cell)) {
								item.add(errorCellBehavior);
							}
							else if (warningCells.contains(cell)) {
								item.add(warningCellBehavior);
							}
							else if (updateCells.contains(cell)) {
								item.add(updateCellBehavior);
							}
							else if (insertCells.contains(cell)) {
								item.add(insertCellBehavior);
							}
						}
					});
				}
				else {
					item.add(new Label("rowNo", new Model("")));
					item.setVisible(false);
					item.add(new Loop("cols", new PropertyModel(sheetMetaData, "cols")) {
						@Override
						protected void populateItem(LoopItem item) {
							Label cellData = new Label("cellData", new Model(""));
							item.add(cellData);
							
						}
					});
					item.setVisible(false);
				}
			}

			/**
			 * Determines whether row data is an insert or an update and amends the css of the
			 * <tr>
			 * accordingly
			 * 
			 * @param cell
			 */
			private void setRowCssStyle(Integer row, LoopItem item) {
				if (updateRows.contains(row)) {
					item.add(updateCellBehavior);
				}
				else {
					item.add(insertCellBehavior);
				}

			}
		};
	}

	public void initialiseWorkbook(InputStream inputStream, char delimChar) {
		delimiterType = delimChar;
		if (fileFormat.equalsIgnoreCase("XLS")) {
			try {
				// Try to get the XLS workbook/sheet
				// Streams directly from inputStream into Workbook.getWorkbook(Inputstream)
				Workbook wkb = Workbook.getWorkbook(inputStream);
				sheet = wkb.getSheet(0); // get First Work Sheet
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (BiffException e) {
				e.printStackTrace();
			}
		}
		else {
			// Error when reading XLS file type, so must be CSV or TXT
			// Thus attempt a convert from csv or text to xls format
			try {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				CsvReader csvReader = new CsvReader(inputStreamReader, delimiterType);
				WritableWorkbook writableWorkBook = Workbook.createWorkbook(output);
				jxl.write.WritableSheet wsheet = writableWorkBook.createSheet("Sheet", 0);
				int row = 0;

				WritableFont normalFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
				WritableCellFormat insertCellFormat = new WritableCellFormat(normalFont);
				insertCellFormat.setBackground(Colour.LIGHT_GREEN);

				WritableCellFormat updateCellFormat = new WritableCellFormat(normalFont);
				updateCellFormat.setBackground(Colour.LIGHT_BLUE);

				WritableFont errorFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
				WritableCellFormat errorCellFormat = new WritableCellFormat(errorFont);
				errorCellFormat.setBackground(Colour.RED);

				// Loop through all rows in file
				while (csvReader.readRecord()) {
					String[] stringArray = csvReader.getValues();

					for (int col = 0; col < stringArray.length; col++) {
						jxl.write.Label label = new jxl.write.Label(col, row, stringArray[col]);

						if (!errorCells.isEmpty()) {
							ArkGridCell cell = new ArkGridCell(col, row);
							if (errorCells.contains(cell)) {
								label.setCellFormat(errorCellFormat);
							}
							else if (updateRows.contains(row)) {
								label.setCellFormat(updateCellFormat);
							}
							else {
								label.setCellFormat(insertCellFormat);
							}
						}
						wsheet.addCell(label);
					}
					row++;
				}

				// All sheets and cells added. Now write out the workbook
				writableWorkBook.write();

				sheet = writableWorkBook.getSheet(0); // get First Work Sheet to display in webpage

				writableWorkBook.close();
				output.flush();
				inputStream.close();
				output.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (RowsExceededException e) {
				e.printStackTrace();
			}
			catch (WriteException e) {
				e.printStackTrace();
			}
		}

		// Store validated/formated workbook as bytes[] for download
		workBookAsBytes = writeOutValidationXlsFileToBytes();

		/*
		 * Sets Sheet meta data. The HTML table creation needs this object to know about the rows and columns
		 */
		sheetMetaData.setRows(sheet.getRows());
		sheetMetaData.setCols(sheet.getColumns());
	}

	private void initialiseGridKey(FileUpload fileUpload, UploadType uploadType) {
																							//TODO ASAP FIX HARDCODING
		if(uploadType !=null && uploadType.getName().equalsIgnoreCase("Study-specific (custom) Data")){
			updateLegendLabel.setDefaultModelObject("Indicates subject does not exist");
			updateLegendLabel.setOutputMarkupId(true);
		}

		wizardDataGridKeyContainerForCustom.setVisible(false);
		wizardDataGridKeyContainer.setVisible((!insertRows.isEmpty() || !updateRows.isEmpty()));
		wizardDataGridKeyContainer.setOutputMarkupId(true);
		wizardDataGridKeyContainer.add(updateLegendLabel);
		// Download file link button
		wizardDataGridKeyContainer.add(buildDownloadButton(fileUpload));
		add(wizardDataGridKeyContainer);

	}

	private AjaxButton buildDownloadButton(final FileUpload fileUpload) {
		AjaxButton ajaxButton = new AjaxButton("downloadGridData") {

			private static final long	serialVersionUID	= 2409955824467683966L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				byte[] data = writeOutValidationXlsFileToBytes();
				if (data != null) {
					getRequestCycle().scheduleRequestHandlerAfterCurrent(new au.org.theark.core.util.ByteDataResourceRequestHandler("application/vnd.ms-excel", data, "DataValidationFile.xls"));
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("An error occured while downloading. Please contact Administrator");
			};
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);

		if (errorCells.isEmpty()) {
			ajaxButton.setVisible(false);
		}

		return ajaxButton;
	}

	public byte[] writeOutValidationXlsFileToBytes() {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			WritableWorkbook w = Workbook.createWorkbook(output);
			WritableSheet writableSheet = w.createSheet("Sheet", 0);
			WritableFont normalFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
			WritableCellFormat insertCellFormat = new WritableCellFormat(normalFont);
			insertCellFormat.setBackground(Colour.LIGHT_GREEN);
			WritableCellFormat updateCellFormat = new WritableCellFormat(normalFont);
			// Override to light blue
			w.setColourRGB(Colour.LIGHT_BLUE, 173, 216, 230);
			updateCellFormat.setBackground(Colour.LIGHT_BLUE);
			WritableCellFormat errorCellFormat = new WritableCellFormat(normalFont);
			errorCellFormat.setBackground(Colour.RED);
			WritableCellFormat warningCellFormat = new WritableCellFormat(normalFont);
			warningCellFormat.setBackground(Colour.LIGHT_ORANGE);

			for (int row = 0; row < sheetMetaData.getRows(); row++) {
				for (int col = 0; col < sheetMetaData.getCols(); col++) {
					Cell cell = sheet.getCell(col, row);
					String cellData = cell.getContents();
					jxl.write.Label label = new jxl.write.Label(col, row, cellData);

					ArkGridCell gridCell = new ArkGridCell(col, row);
					if (row > 0) {
						if (errorCells.contains(gridCell)) {
							label.setCellFormat(errorCellFormat);
						}
						else if (warningCells.contains(gridCell)) {
							label.setCellFormat(warningCellFormat);
						}
						else if (updateCells.contains(gridCell)) {
							label.setCellFormat(updateCellFormat);
						}
						else {
							label.setCellFormat(insertCellFormat);
						}
					}
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

	/**
	 * @return the errorCols
	 */
	public HashSet<Integer> getErrorCols() {
		return insertRows;
	}

	/**
	 * @param insertRows
	 *           the insertRows to set
	 */
	public void setInsertRows(HashSet<Integer> insertRows) {
		this.insertRows = insertRows;
	}

	/**
	 * @return the updateCols
	 */
	public HashSet<Integer> getUpdateRows() {
		return updateRows;
	}

	/**
	 * @param updateRows
	 *           the updateRows to set
	 */
	public void setUpdateRows(HashSet<Integer> updateRows) {
		this.updateRows = updateRows;
	}

	/**
	 * @param errorCols
	 *           the errorCols to set
	 */
	public void setErrorCols(HashSet<Integer> errorCols) {
		this.insertRows = errorCols;
	}

	/**
	 * @return the insertRows
	 */
	public HashSet<Integer> getInsertRows() {
		return insertRows;
	}

	/**
	 * @param wizardDataGridKeyContainer
	 *           the wizardDataGridKeyContainer to set
	 */
	public void setWizardDataGridKeyContainer(WebMarkupContainer wizardDataGridKeyContainer) {
		this.wizardDataGridKeyContainer = wizardDataGridKeyContainer;
	}

	/**
	 * @return the wizardDataGridKeyContainer
	 */
	public WebMarkupContainer getWizardDataGridKeyContainer() {
		return wizardDataGridKeyContainer;
	}

	/**
	 * @param wizardDataGridKeyContainer
	 *           the wizardDataGridKeyContainer to set
	 */
	public void setWizardDataGridKeyContainerForCustom(WebMarkupContainer wizardDataGridKeyContainerForCustom) {
		this.wizardDataGridKeyContainerForCustom = wizardDataGridKeyContainerForCustom;
	}

	/**
	 * @return the wizardDataGridKeyContainer
	 */
	public WebMarkupContainer getWizardDataGridKeyContainerForCustom() {
		return wizardDataGridKeyContainerForCustom;
	}

	
	/**
	 * @param errorCells
	 *           the errorCells to set
	 */
	public void setErrorCells(HashSet<ArkGridCell> errorCells) {
		this.errorCells = errorCells;
	}

	/**
	 * @return the errorCells
	 */
	public HashSet<ArkGridCell> getErrorCells() {
		return errorCells;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	/**
	 * @param workBookAsBytes
	 *           the workBookAsBytes to set
	 */
	public void setWorkBookAsBytes(byte[] workBookAsBytes) {
		this.workBookAsBytes = workBookAsBytes;
	}

	/**
	 * @return the workBookAsBytes
	 */
	public byte[] getWorkBookAsBytes() {
		return workBookAsBytes;
	}

	/**
	 * @param insertCells
	 *           the insertCells to set
	 */
	public void setInsertCells(HashSet<ArkGridCell> insertCells) {
		this.insertCells = insertCells;
	}

	/**
	 * @return the insertCells
	 */
	public HashSet<ArkGridCell> getInsertCells() {
		return insertCells;
	}

	/**
	 * @param updateCells
	 *           the updateCells to set
	 */
	public void setUpdateCells(HashSet<ArkGridCell> updateCells) {
		this.updateCells = updateCells;
	}

	/**
	 * @return the updateCells
	 */
	public HashSet<ArkGridCell> getUpdateCells() {
		return updateCells;
	}

	/**
	 * @param warningCells
	 *           the warningCells to set
	 */
	public void setWarningCells(HashSet<ArkGridCell> warningCells) {
		this.warningCells = warningCells;
	}

	/**
	 * @return the warningCells
	 */
	public HashSet<ArkGridCell> getWarningCells() {
		return warningCells;
	}

	public void setUploadType(UploadType uploadType) {
		this.uploadType = uploadType;
	}

	public UploadType getUploadType() {
		return uploadType;
	}
}

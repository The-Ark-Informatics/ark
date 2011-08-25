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
package au.org.theark.lims.web.component.inventory.panel.box.display;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.ByteArrayOutputStream;

import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.util.ArkSheetMetaData;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;

public class GridBoxPanel extends Panel {
	/**
	 * 
	 */
	private static final long					serialVersionUID		= -4769477913855966069L;
	private transient Sheet						sheet;
	private transient ArkSheetMetaData		sheetMetaData;
	private byte[]									workBookAsBytes;
	private WebMarkupContainer					gridBoxKeyContainer	= new WebMarkupContainer("gridBoxKeyContainer");
	public String									exportXlsFileName;

	private static final ResourceReference	EMPTY_CELL_ICON		= new ResourceReference(GridBoxPanel.class, "emptyCell.gif");
	private static final ResourceReference	USED_CELL_ICON			= new ResourceReference(GridBoxPanel.class, "usedCell.gif");

	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService							iLimsService;

	public GridBoxPanel(String id, InvBox invBox, String exportXlsfileName) {
		super(id);
		this.sheetMetaData = new ArkSheetMetaData();
		this.exportXlsFileName = exportXlsfileName;
		if (this.exportXlsFileName.isEmpty() || this.exportXlsFileName == null) {
			this.exportXlsFileName = "GridBoxData";
		}
		initialiseGrid(invBox);
		initialiseGridKey();

		add(JavascriptPackageResource.getHeaderContribution("js/addEvent.js"));
		add(JavascriptPackageResource.getHeaderContribution("js/sweetTitles.js"));
	}

	private void initialiseGrid(InvBox invBox) {
		workBookAsBytes = initialiseWorkBook(invBox);

		System.out.println("Adding headings");
		add(createHeadings(invBox));
		add(createMainGrid());
		System.out.println("Added panel");
	}

	/**
	 * Initialise a WorkBook object representing the Box from the database and store as byte array
	 * 
	 * @param invBox
	 * @return WorkBook as a byte array
	 */
	private byte[] initialiseWorkBook(InvBox invBox) {
		invBox = iInventoryService.getInvBox(invBox.getId());
		List<InvCell> invCellList = invBox.getInvCells();

		byte[] bytes = null;
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			WritableWorkbook writableWorkBook = Workbook.createWorkbook(output);
			WritableSheet writableSheet = writableWorkBook.createSheet("Sheet", 0);
			WritableFont normalFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
			WritableCellFormat usedCellFormat = new WritableCellFormat(normalFont);
			usedCellFormat.setBackground(Colour.BLUE);

			int col = 1;
			int row = 1;
			int cellCount = 0;

			for (Iterator<InvCell> iterator = invCellList.iterator(); iterator.hasNext();) {
				InvCell invCell = (InvCell) iterator.next();
				row = invCell.getRowno().intValue();
				col = invCell.getColno().intValue();

				Biospecimen biospecimen = new Biospecimen();
				biospecimen = invCell.getBiospecimen();
				String cellData = null;
				jxl.write.Label label = null;

				if (biospecimen != null) {
					cellData = biospecimen.getBiospecimenUid();
					label = new jxl.write.Label(col, row, cellData);
					label.setCellFormat(usedCellFormat);
				}
				else {
					label = new jxl.write.Label(col, row, "");
				}
				writableSheet.addCell(label);
				cellCount = cellCount + 1;
			}

			System.out.println("Cell count: " + cellCount);
			// get First Work Sheet to display in webpage
			sheet = writableWorkBook.getSheet(0);

			bytes = output.toByteArray();
			writableWorkBook.write();
			writableWorkBook.close();

			output.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Sets Sheet meta data. The HTML table creation needs this object to know about the rows and columns
		sheetMetaData.setRows(sheet.getRows() - 1);
		sheetMetaData.setCols(sheet.getColumns() - 1);

		return bytes;
	}

	@SuppressWarnings( { "unchecked" })
	private Loop createHeadings(final InvBox invBox) {
		return new Loop("heading", new PropertyModel(sheetMetaData, "cols")) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -7027878243061138904L;

			public void populateItem(LoopItem item) {
				// Note, added 1 to correct 0th element start
				final int col = item.getIteration() + 1;

				/*
				 * this model used for Label component gets data from cell instance Because we are interacting directly with the sheet instance which gets
				 * updated each time we upload a new Excel File, the value for each cell is automatically updated
				 */
				IModel<Object> model = new Model() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1144128566137457199L;

					@Override
					public Serializable getObject() {
						String label = new String();
						String colNoType = invBox.getColnotype();
						if (colNoType.equalsIgnoreCase("ALPHABET")) {
							char character = (char) (col + 65);
							label = new Character(character).toString();
						}
						else {
							label = new Integer(col).toString();
						}
						return label;
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
			public void populateItem(LoopItem item) {
				// Note, added 1 to correct 0th element start
				final int row = item.getIteration() +1;

				// Create the row number/label
				Label rowLabel = new Label("rowNo", new Model(String.valueOf(row)));
				rowLabel.add(new AbstractBehavior() {
					public void onComponentTag(Component component, ComponentTag tag) {
						super.onComponentTag(component, tag);
						tag.put("style", "background: none repeat scroll 0 0 #FFFFFF; color: black; font-weight: bold; padding: 1px;");
					};
				});
				item.add(rowLabel);
				
					// We create an inner Loop instance and uses PropertyModel to bind the Loop iteration to ExcelMetaData "cols" value
					item.add(new Loop("cols", new PropertyModel(sheetMetaData, "cols")) {
						public void populateItem(LoopItem item) {
							final int col = item.getIteration() +1;
							//if(col>0) {
								/*
								 * this model used for Label component gets data from cell instance Because we are interacting directly with the sheet instance
								 * which gets updated each time we upload a new Excel File, the value for each cell is automatically updated
								 */						
								IModel<Biospecimen> biospecimenModel = new Model() {
									@Override
									public Serializable getObject() {
										Cell cell = sheet.getCell(col, row);
										Biospecimen biospecimen = iLimsService.getBiospecimenByUid(cell.getContents());
										return biospecimen;
									}
								};
		
								//Label cellData = new Label("cellData", model);
								//item.add(cellData);
		
								// Cell used/empty icon
								Component cellIconComponent = cellIconComponent("cellIcon", biospecimenModel);
								
								// tooltip of cell
								StringBuffer stringBuffer = new StringBuffer();
								stringBuffer.append("Column: ");
								stringBuffer.append(col);
								stringBuffer.append("\t");
								stringBuffer.append("Row: ");
								stringBuffer.append(row);
								stringBuffer.append("\t");
								stringBuffer.append("Status: ");
								
								if(biospecimenModel.getObject() != null) {
									stringBuffer.append("Used");
									stringBuffer.append("\t");
									stringBuffer.append("BiospecimenUID: ");
									stringBuffer.append(biospecimenModel.getObject().getBiospecimenUid());
									stringBuffer.append("\t");
									stringBuffer.append("Sample Type: ");
									stringBuffer.append(biospecimenModel.getObject().getSampleType().getName());
									stringBuffer.append("\t");
									stringBuffer.append("Quantity: ");
									stringBuffer.append(biospecimenModel.getObject().getQuantity());
								}
								else {
									stringBuffer.append("Empty");
								}
								
								String toolTip = stringBuffer.toString();
								cellIconComponent.add(new AttributeModifier("showtooltip", true, new Model("true")));
								cellIconComponent.add(new AttributeModifier("title", true, new Model(toolTip)));
								cellIconComponent.add(new AbstractBehavior() {
									public void onComponentTag(Component component, ComponentTag tag) {
										super.onComponentTag(component, tag);
										tag.put("style", "padding: 1px;");
									};
								});
								item.add(cellIconComponent);		
							}
						//}
					});
				//}
			}
		};
	}

	protected Component cellIconComponent(String componentId, final IModel<Biospecimen> biospecimenModel) {
		return new Image(componentId) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected ResourceReference getImageResourceReference() {
				return getIconResourceReference(biospecimenModel.getObject());
			}

		};
	}

	/**
	 * Determine what icon to display on the cell
	 * 
	 * @param object
	 *           the referece object of the node
	 * @return resourceReference to the icon for the cell in question
	 */
	private ResourceReference getIconResourceReference(Object object) {
		ResourceReference resourceReference = null;
		if (object == null) {
			resourceReference = EMPTY_CELL_ICON;
		}
		else {
			resourceReference = USED_CELL_ICON;
		}
		return resourceReference;
	}

	private void initialiseGridKey() {
		gridBoxKeyContainer.setOutputMarkupId(true);

		gridBoxKeyContainer.add(new Image("emptyCellIcon", EMPTY_CELL_ICON));
		gridBoxKeyContainer.add(new Image("usedCellIcon", USED_CELL_ICON));

		// Download file link button
		gridBoxKeyContainer.add(buildDownloadButton());
		add(gridBoxKeyContainer);
	}

	private AjaxButton buildDownloadButton() {
		AjaxButton ajaxButton = new AjaxButton("downloadGridBoxData", new StringResourceModel("downloadGridBoxData", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 2409955824467683966L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println("Downloading grid as XLS");
				byte[] data = workBookAsBytes;
				String filename = exportXlsFileName + ".xls";
				if (data != null) {
					getRequestCycle().setRequestTarget(new au.org.theark.core.util.ByteDataRequestTarget("application/vnd.ms-excel", data, filename));
				}
			};
		};

		ajaxButton.setVisible(true);
		ajaxButton.setDefaultFormProcessing(false);
		return ajaxButton;
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
}
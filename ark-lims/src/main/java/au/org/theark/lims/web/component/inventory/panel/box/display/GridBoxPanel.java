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

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;

/**
 * 
 * @author cellis
 *
 */
public class GridBoxPanel extends Panel {

	private static final long					serialVersionUID		= -4769477913855966069L;
	private static final Logger				log						= LoggerFactory.getLogger(GridBoxPanel.class);
	
	private byte[]									workBookAsBytes;
	private WebMarkupContainer					gridBoxKeyContainer	= new WebMarkupContainer("gridBoxKeyContainer");
	public String									exportXlsFileName;

	private static final PackageResourceReference	EMPTY_CELL_ICON		= new PackageResourceReference(GridBoxPanel.class, "emptyCell.gif");
	private static final PackageResourceReference	USED_CELL_ICON			= new PackageResourceReference(GridBoxPanel.class, "usedCell.gif");
	private static final PackageResourceReference	BARCODE_CELL_ICON		= new PackageResourceReference(GridBoxPanel.class, "barcodeCell.gif");
	
	private AbstractDetailModalWindow		modalWindow;
	private LimsVO limsVo;
	private Boolean allocating = false;
	
	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;

	public GridBoxPanel(String id, LimsVO limsVo, AbstractDetailModalWindow modalWindow, Boolean allocating) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		
		this.exportXlsFileName = "GridBoxData";
		gridBoxKeyContainer.setVisible(false);
		
		this.modalWindow = modalWindow;
		this.limsVo = limsVo;
		
		if(limsVo.getInvBox().getId() != null) {
			log.info("InvBox ID: " + limsVo.getInvBox().getId());
		}
		setVisible(limsVo.getInvBox().getId() != null);
		this.allocating = allocating;
		initialiseGrid();
	}
	
	public GridBoxPanel(String id, LimsVO limsVo, String exportXlsfileName, AbstractDetailModalWindow modalWindow) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		
		this.exportXlsFileName = exportXlsfileName;
		if (this.exportXlsFileName == null) {
			this.exportXlsFileName = "GridBoxData";
		}
		
		this.modalWindow = modalWindow;
		this.limsVo = limsVo;
		setVisible(limsVo.getInvBox().getId() != null);
		initialiseGrid();
	}
	
	@Override
	protected void onBeforeRender() {
		initialiseGrid();
		super.onBeforeRender();
	}

	/**
	 * Initialises the grid to display for the InvBox in question
	 * @param invBox
	 */
	public void initialiseGrid() {
		limsVo.setInvBox(iInventoryService.getInvBox(limsVo.getInvBox().getId()));
		
		List<InvCell> invCellList = new ArrayList<InvCell>(0);
		invCellList = limsVo.getInvBox().getInvCells();
		
		// Handle for no cells in InvCell table!
		int cells = limsVo.getInvBox().getNoofcol() * limsVo.getInvBox().getNoofrow();
		if (invCellList.size() != cells) {
			log.error("InvCell table is missing data for invBox.id " + limsVo.getInvBox().getId());
			this.error("InvCell table is missing data for invBox.id " + limsVo.getInvBox().getId());
			this.setVisible(false);
			addOrReplace(createHeadings(new InvBox()));
			addOrReplace(createMainGrid(new InvBox(), invCellList));
		}
		else {
			addOrReplace(createHeadings(limsVo.getInvBox()));
			addOrReplace(createMainGrid(limsVo.getInvBox(), invCellList));
			initialiseGridKey(invCellList);
		}
	}

	/**
	 * Creates the header of the table for the represented InvBox in question
	 * @param invBox
	 * @return
	 */
	@SuppressWarnings( { "unchecked" })
	private Loop createHeadings(final InvBox invBox) {		
		String colRowNoType = "";
		
		if(invBox.getId() != null) {
			colRowNoType = invBox.getColnotype().getName();
		}
		
		final String colNoType = colRowNoType;
		
		// Outer Loop instance, using a PropertyModel to bind the Loop iteration to invBox "noofcol" value
		return new Loop("heading", new PropertyModel(invBox, "noofcol")) {

			private static final long	serialVersionUID	= -7027878243061138904L;

			public void populateItem(LoopItem item) {
				final int col = item.getIndex();
				

				IModel<String> colModel = new Model() {
					private static final long	serialVersionUID	= 1144128566137457199L;

					@Override
					public Serializable getObject() {
						String label = new String();
						if (colNoType.equalsIgnoreCase("ALPHABET")) {
							char character = (char) (col + 65);
							label = new Character(character).toString();
						}
						else {
							label = new Integer(col+1).toString();
						}
						return label;
					}
				};
				
				// Create the col number/label
				Label colLabel = new Label("cellHead", colModel);
				item.add(colLabel);
			}
		};
	}
	
	/**
	 * Creates the table data that represents the cells of the InvBox in question
	 * @param invBox
	 * @param invCellList
	 * @return
	 */
	@SuppressWarnings( { "unchecked" })
	private Loop createMainGrid(final InvBox invBox, final List<InvCell> invCellList) {
		String colRowNoType = "";
		
		if(invBox.getId() != null) {
			colRowNoType = invBox.getRownotype().getName();
		}
		final String rowNoType = colRowNoType;
		final int noOfCols = invBox.getNoofcol();
		
		// Outer Loop instance, using a PropertyModel to bind the Loop iteration to invBox "noofrow" value
		Loop loop = new Loop("rows", new PropertyModel(invBox, "noofrow")) {

			private static final long	serialVersionUID	= 1L;

			public void populateItem(LoopItem item) {
				final int row = item.getIndex();
				
				// Create the row number/label
				String label = new String();
				
				if (rowNoType.equalsIgnoreCase("ALPHABET")) {
					char character = (char) (row + 65);
					label = new Character(character).toString();
				}
				else {
					label = new Integer(row+1).toString();
				}

				Label rowLabel = new Label("rowNo", new Model(label));
				rowLabel.add(new Behavior() {
					private static final long	serialVersionUID	= 1L;

					@Override
					public void onComponentTag(Component component, ComponentTag tag) {
						super.onComponentTag(component, tag);
						tag.put("style", "background: none repeat scroll 0 0 #FFFFFF; color: black; font-weight: bold; padding: 1px;");
					};
				});
				rowLabel.setOutputMarkupId(true);
				item.add(rowLabel);
				
				// We create an inner Loop instance and uses PropertyModel to bind the Loop iteration to invBox "noofcol" value
				item.add(new Loop("cols", new PropertyModel(invBox, "noofcol")) {
					private static final long	serialVersionUID	= 1L;

					public void populateItem(LoopItem item) {
						final int col = item.getIndex();
						final int index = (row * noOfCols) + col;
						
						InvCell invCell = invCellList.get(index);
						GridCellContentPanel gridCellContentPanel;
						// add the gridCell
						if(allocating) {
							gridCellContentPanel = new GridCellContentPanel("cell", limsVo, invCell, modalWindow, true);
						}
						else {
							gridCellContentPanel = new GridCellContentPanel("cell", limsVo, invCell, modalWindow, false);
						}
						gridCellContentPanel.setOutputMarkupId(true);
						gridCellContentPanel.setMarkupId("invCell" + invCell.getId().toString());
						item.add(gridCellContentPanel);
					}					
				});
			}
		};
		return loop;
	}
	
   /**
    * Initialise the key for the box 
    * @param invCellList
    */
	private void initialiseGridKey(List<InvCell> invCellList) {
		gridBoxKeyContainer.setOutputMarkupId(true);

		gridBoxKeyContainer.addOrReplace(new Image("emptyCellIcon", EMPTY_CELL_ICON));
		gridBoxKeyContainer.addOrReplace(new Image("usedCellIcon", USED_CELL_ICON));
		gridBoxKeyContainer.addOrReplace(new Image("barcodeCellIcon", BARCODE_CELL_ICON));

		// Download file link
		gridBoxKeyContainer.addOrReplace(buildXLSDownloadLink(invCellList));
		addOrReplace(gridBoxKeyContainer);
		gridBoxKeyContainer.setVisible(!allocating);
	}
	
	/**
	 * Initialise a WorkBook object representing the Box from the database and store as byte array
	 * 
	 * @param invCellList
	 * @return WorkBook as a byte array
	 */
	private byte[] createWorkBookAsByteArray(List<InvCell> invCellList) {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			WritableWorkbook writableWorkBook = Workbook.createWorkbook(output);
			WritableSheet writableSheet = writableWorkBook.createSheet("Sheet", 0);

			int col = 0;
			int row = 0;
			int cellCount = 0;

			for (Iterator<InvCell> iterator = invCellList.iterator(); iterator.hasNext();) {
				InvCell invCell = (InvCell) iterator.next();
				row = invCell.getRowno().intValue();
				col = invCell.getColno().intValue();

				Biospecimen biospecimen = new Biospecimen();
				biospecimen = invCell.getBiospecimen();
				jxl.write.Label label = null;

				if (biospecimen != null) {
					label = new jxl.write.Label(col, row, biospecimen.getBiospecimenUid());
				}
				else {
					label = new jxl.write.Label(col, row, "");
				}
				writableSheet.addCell(label);
				cellCount = cellCount + 1;
			}

			writableWorkBook.write();
			writableWorkBook.close();
			bytes = output.toByteArray();
			output.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return bytes;
	}
	
	/**
	 * Return a download link for the gridBox contents as an Excel Worksheet 
	 * @param invCellList
	 * @return
	 */
	protected Link<String> buildXLSDownloadLink(final List<InvCell> invCellList) {
		Link<String> link = new Link<String>("downloadGridBoxDataLink") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			public void onClick() {
				byte[] data = createWorkBookAsByteArray(invCellList);
				if (data != null) {
					InputStream inputStream = new ByteArrayInputStream(data);
					OutputStream outputStream;
					try {
						final String tempDir = System.getProperty("java.io.tmpdir");
						final java.io.File file = new File(tempDir, limsVo.getInvBox().getName() + ".xls");
						final String fileName = limsVo.getInvBox().getName() + ".xls";
						outputStream = new FileOutputStream(file);
						IOUtils.copy(inputStream, outputStream);

						IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(file));
						getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream) {
							@Override
							public void respond(IRequestCycle requestCycle) {
								super.respond(requestCycle);
								Files.remove(file);
							}
						}.setFileName(fileName).setContentDisposition(ContentDisposition.ATTACHMENT));
					}
					catch (FileNotFoundException e) {
						log.error(e.getMessage());
					}
					catch (IOException e) {
						log.error(e.getMessage());
					}
				}
			}
		};
		
		return link;
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
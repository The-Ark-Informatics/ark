package au.org.theark.lims.web.component.inventory.panel.box.display;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;

public class GridCellContentPanel extends Panel {
	/**
	 * 
	 */
	private static final long								serialVersionUID				= 435929363844198235L;

	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService								iInventoryService;

	private static final PackageResourceReference	EMPTY_CELL_ICON				= new PackageResourceReference(GridCellContentPanel.class, "emptyCell.gif");
	private static final PackageResourceReference	USED_CELL_ICON					= new PackageResourceReference(GridCellContentPanel.class, "usedCell.gif");
	private static final PackageResourceReference	BARCODE_CELL_ICON				= new PackageResourceReference(GridBoxPanel.class, "barcodeCell.gif");
	private static final PackageResourceReference	SELECTED_EMPTY_CELL_ICON	= new PackageResourceReference(GridCellContentPanel.class, "selectedEmptyCell.gif");
	private static final PackageResourceReference	SELECTED_USED_CELL_ICON		= new PackageResourceReference(GridCellContentPanel.class, "selectedUsedCell.gif");
	private static final PackageResourceReference	SELECTED_BARCODE_CELL_ICON	= new PackageResourceReference(GridCellContentPanel.class, "selectedBarcodeCell.gif");
	private Component											gridCellContent				= new EmptyPanel("gridCellContent");
	private LimsVO												limsVo;
	private InvCell											invCell;
	private AbstractDetailModalWindow					modalWindow;
	private Boolean											allocating						= false;

	/**
	 * A representation of a cell contained within in a GridBox
	 * 
	 * @param id
	 * @param limsVo
	 * @param invCell
	 * @param modalWindow
	 * @param allocating
	 */
	public GridCellContentPanel(String id, LimsVO limsVo, InvCell invCell, AbstractDetailModalWindow modalWindow, Boolean allocating) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.limsVo = limsVo;
		this.invCell = invCell;
		this.modalWindow = modalWindow;
		this.allocating = allocating;
	}

	@Override
	protected void onBeforeRender() {
		initialiseContentPanel();
		super.onBeforeRender();
	}

	private void initialiseContentPanel() {
		if (allocating) {
			if (this.invCell.getBiospecimen() == null) {
				gridCellContent = new GridCellLink("gridCellContent", getIconResourceReference(invCell), getIconOverResourceReference(invCell), limsVo, invCell, modalWindow, allocating);
			}
			else {
				gridCellContent = new GridCellIcon("gridCellContent", getIconResourceReference(invCell), getIconOverResourceReference(invCell), invCell.getId().toString());
			}
		}
		else {
			if (this.invCell.getBiospecimen() == null) {
				gridCellContent = new GridCellIcon("gridCellContent", getIconResourceReference(invCell), getIconOverResourceReference(invCell), invCell.getId().toString());
			}
			else {
				gridCellContent = new GridCellLink("gridCellContent", getIconResourceReference(invCell), getIconOverResourceReference(invCell), limsVo, invCell, modalWindow, allocating);
			}
		}
		
		addOrReplace(gridCellContent);
		addToolTip();
	}

	/**
	 * Adds a ToolTip, using the invCell details
	 */
	private void addToolTip() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Row: ");
		stringBuffer.append(invCell.getRowno());
		stringBuffer.append("\t");
		stringBuffer.append("Column: ");
		stringBuffer.append(invCell.getColno());
		stringBuffer.append("\t");
		
		stringBuffer.append("Status: ");

		if (invCell.getBiospecimen() != null) {
			stringBuffer.append("Used");
			stringBuffer.append("\t");
			stringBuffer.append("BiospecimenUID: ");
			stringBuffer.append(invCell.getBiospecimen().getBiospecimenUid());
			stringBuffer.append("\t");
			stringBuffer.append("Sample Type: ");
			stringBuffer.append(invCell.getBiospecimen().getSampleType().getName());
			stringBuffer.append("\t");
			stringBuffer.append("Quantity: ");
			if (invCell.getBiospecimen().getQuantity() != null) {
				stringBuffer.append(invCell.getBiospecimen().getQuantity());
				stringBuffer.append(invCell.getBiospecimen().getUnit().getName());
			}
			else {
				stringBuffer.append("0");
			}
		}
		else {
			stringBuffer.append("Empty");
		}

		String toolTip = stringBuffer.toString();
		this.add(new AttributeModifier("showtooltip", new Model<Boolean>(true)));
		this.add(new AttributeModifier("title", new Model<String>(toolTip)));
	}

	/**
	 * Determine what icon to display on the cell
	 * 
	 * @param invCell
	 *           the reference cell
	 * @return resourceReference to the icon for the cell in question
	 */
	private ResourceReference getIconResourceReference(InvCell invCell) {
		ResourceReference resourceReference = null;
		Biospecimen biospecimen = iInventoryService.getBiospecimenByInvCell(invCell);
		if (biospecimen == null) {
			resourceReference = EMPTY_CELL_ICON;
		}
		else {
			if (biospecimen.getBarcoded()) {
				resourceReference = BARCODE_CELL_ICON;
			}
			else {
				resourceReference = USED_CELL_ICON;
			}
		}
		return resourceReference;
	}

	/**
	 * Determine what icon to display on the cell onMouseover
	 * 
	 * @param invCell
	 *           the reference cell
	 * @return resourceReference to the icon for the cell in question
	 */
	private ResourceReference getIconOverResourceReference(InvCell invCell) {
		ResourceReference resourceReference = null;
		Biospecimen biospecimen = invCell.getBiospecimen();
		if (biospecimen == null) {
			resourceReference = SELECTED_EMPTY_CELL_ICON;
		}
		else {
			if (biospecimen.getBarcoded()) {
				resourceReference = SELECTED_BARCODE_CELL_ICON;
			}
			else {
				resourceReference = SELECTED_USED_CELL_ICON;
			}
		}
		return resourceReference;
	}
}

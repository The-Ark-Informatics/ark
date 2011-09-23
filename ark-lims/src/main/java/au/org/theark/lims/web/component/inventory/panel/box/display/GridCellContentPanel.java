package au.org.theark.lims.web.component.inventory.panel.box.display;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.web.component.AbstractDetailModalWindow;

public class GridCellContentPanel extends Panel {

	/**
	 * 
	 */
	private static final long					serialVersionUID				= 435929363844198235L;

	private static final PackageResourceReference	EMPTY_CELL_ICON				= new PackageResourceReference(GridCellContentPanel.class, "emptyCell.gif");
	private static final PackageResourceReference	USED_CELL_ICON					= new PackageResourceReference(GridCellContentPanel.class, "usedCell.gif");
	private static final PackageResourceReference	BARCODE_CELL_ICON				= new PackageResourceReference(GridBoxPanel.class, "barcodeCell.gif");
	private static final PackageResourceReference	SELECTED_EMPTY_CELL_ICON	= new PackageResourceReference(GridCellContentPanel.class, "selectedEmptyCell.gif");
	private static final PackageResourceReference	SELECTED_USED_CELL_ICON		= new PackageResourceReference(GridCellContentPanel.class, "selectedUsedCell.gif");
	private static final PackageResourceReference	SELECTED_BARCODE_CELL_ICON	= new PackageResourceReference(GridCellContentPanel.class, "selectedBarcodeCell.gif");

	/**
	 * A representation of a cell contained within in a GridBox
	 * 
	 * @param id
	 * @param invCell
	 */
	public GridCellContentPanel(String id, InvCell invCell, AbstractDetailModalWindow modalWindow) {
		super(id);
		setOutputMarkupPlaceholderTag(true);

		Component gridCellContent = new EmptyPanel("gridCellContent");
		
		if(invCell.getBiospecimen() == null) {
			gridCellContent = new GridCellIcon("gridCellContent", getIconResourceReference(invCell), getIconOverResourceReference(invCell), invCell.getId().toString());
		}
		else {
			gridCellContent = new GridCellLink("gridCellContent", getIconResourceReference(invCell), getIconOverResourceReference(invCell), invCell, modalWindow);
		}

		addOrReplace(gridCellContent);
		addToolTip(invCell);
	}

	/**
	 * Adds a ToolTip, using the invCell details
	 * 
	 * @param invCell
	 */
	private void addToolTip(InvCell invCell) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Column: ");
		stringBuffer.append(invCell.getColno());
		stringBuffer.append("\t");
		stringBuffer.append("Row: ");
		stringBuffer.append(invCell.getRowno());
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
			if (invCell.getBiospecimen().getQuantity() != null)
				stringBuffer.append(invCell.getBiospecimen().getQuantity());
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
		Biospecimen biospecimen = invCell.getBiospecimen();
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

package au.org.theark.lims.web.component.inventory.panel.box.display;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.resource.ResourceReference;

import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.core.web.component.image.MouseOverImage;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.subjectlims.lims.biospecimen.BiospecimenModalDetailPanel;

/**
 * The link content of a GridCell, that opens a modalWindow to the Biospecimen reference in the cell
 * 
 * @author cellis
 * 
 */
public class GridCellLink extends Panel {

	/**
	 * 
	 */
	private static final long					serialVersionUID				= -7296587386654258036L;
	private AbstractDetailModalWindow		modalWindow;
	private Panel									modalContentPanel;

	public GridCellLink(String id, ResourceReference iconResourceReference, ResourceReference iconOverResourceReference, final InvCell invCell, AbstractDetailModalWindow modalWindow) {
		super(id);
		this.modalWindow = modalWindow;
		
		ArkBusyAjaxLink<String> link = new ArkBusyAjaxLink<String>("link"){

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
				newModel.getObject().getBiospecimen().setId(invCell.getBiospecimen().getId());
				showModalWindow(target, newModel);
			}
		};
		
		MouseOverImage image = new MouseOverImage("icon", iconResourceReference, iconOverResourceReference, invCell.getId().toString());
		link.add(image);
		this.add(link);
	}

	/**
	 * Show the Biospecimen detail for the cell that was clicked
	 * @param target
	 * @param cpModel
	 */
	protected void showModalWindow(AjaxRequestTarget target, CompoundPropertyModel<LimsVO> cpModel) {
		modalContentPanel = new BiospecimenModalDetailPanel("content", modalWindow, cpModel);
		// Set the modalWindow title and content
		modalWindow.setTitle("Biospecimen Detail");
		modalWindow.setContent(modalContentPanel);
		modalWindow.show(target);
	}
}
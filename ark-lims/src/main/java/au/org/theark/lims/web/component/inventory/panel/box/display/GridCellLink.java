package au.org.theark.lims.web.component.inventory.panel.box.display;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.image.MouseOverImage;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.service.ILimsService;
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
	private static final Logger				log						= LoggerFactory.getLogger(GridCellLink.class);
	private AbstractDetailModalWindow		modalWindow;
	private Panel									modalContentPanel;
	private LimsVO limsVo;
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_SERVICE)
	private ILimsService											iLimsService;
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService								iInventoryService;

	/**
	 * Constructor
	 * @param id
	 * @param iconResourceReference
	 * @param iconOverResourceReference
	 * @param limsVo
	 * @param modalWindow
	 * @param allocating
	 */
	public GridCellLink(String id, ResourceReference iconResourceReference, ResourceReference iconOverResourceReference, LimsVO limsVo, final InvCell invCell, final AbstractDetailModalWindow modalWindow, final Boolean allocating) {
		super(id);
		this.modalWindow = modalWindow;
		this.limsVo = limsVo;
		
		ArkBusyAjaxLink<String> link = new ArkBusyAjaxLink<String>("link"){
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				if(!allocating) {
					// Show biospecimen detail
					CompoundPropertyModel<LimsVO> newModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
					newModel.getObject().getBiospecimen().setId(invCell.getBiospecimen().getId());
					showModalWindow(target, newModel);
				}
				else {
					allocateBiospecimenToCell(target, invCell);
				}
			}
		};
		
		MouseOverImage image = new MouseOverImage("icon", iconResourceReference, iconOverResourceReference, invCell.getId().toString());
		link.add(image);
		this.add(link);
	}

	protected void allocateBiospecimenToCell(AjaxRequestTarget target, InvCell invCell) {
		// Allocating, so simply put selected cell into context
		StringBuilder alert = new StringBuilder();
		alert.append("alert(\"Allocating Biospecimen: ");
		alert.append(limsVo.getBiospecimen().getBiospecimenUid());
		alert.append(" to Cell.id: ");
		alert.append(invCell.getId());
		alert.append("\");");
		target.appendJavaScript(alert);
		
		//TODO: use CellStatus
		invCell.setBiospecimen(limsVo.getBiospecimen());
		invCell.setStatus("Not Empty");
		iInventoryService.updateInvCell(invCell);
		
		modalWindow.close(target);
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
		modalWindow.repaintComponent(GridCellLink.this.getParent());
	}
}
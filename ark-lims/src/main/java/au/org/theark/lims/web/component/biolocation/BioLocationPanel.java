package au.org.theark.lims.web.component.biolocation;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;

/**
 * Panel displaying the location details for a Biospecimen in context
 * 
 * @author nivedan
 * @author cellis
 * 
 */
public abstract class BioLocationPanel extends Panel {
	/**
	 * 
	 */
	private static final long						serialVersionUID	= 1L;
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService									iInventoryService;
	protected CompoundPropertyModel<LimsVO>	cpModel;
	private Label										siteNameLbl;
	private Label										tankNameLbl;
	private Label										trayNameLbl;
	private Label										boxNameLbl;
	private Label										rowLbl;
	private Label										cellLbl;
	private ArkBusyAjaxButton						unallocateButton;

	

	public BioLocationPanel(String id, CompoundPropertyModel<LimsVO> cpModel) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.cpModel = cpModel;

		initialisePanel();
		addComponents();// Not adding it to Form here
	}

	public void initialisePanel() {
		siteNameLbl = new Label("biospecimenLocationVO.site", cpModel.getObject().getBiospecimenLocationVO().getSiteName());
		tankNameLbl = new Label("biospecimenLocationVO.freezer", cpModel.getObject().getBiospecimenLocationVO().getFreezerName());
		trayNameLbl = new Label("biospecimenLocationVO.rack", cpModel.getObject().getBiospecimenLocationVO().getRackName());
		boxNameLbl = new Label("biospecimenLocationVO.box", cpModel.getObject().getBiospecimenLocationVO().getBoxName());
		rowLbl = new Label("biospecimenLocationVO.row", cpModel.getObject().getBiospecimenLocationVO().getRowLabel());
		cellLbl = new Label("biospecimenLocationVO.column", cpModel.getObject().getBiospecimenLocationVO().getColLabel());
		unallocateButton = new ArkBusyAjaxButton("unallocate") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				unallocateBiospecimen(target);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		};
		unallocateButton.setDefaultFormProcessing(false);
	}

	public void addComponents() {
		add(siteNameLbl);
		add(tankNameLbl);
		add(trayNameLbl);
		add(boxNameLbl);
		add(rowLbl);
		add(cellLbl);
		add(unallocateButton);
	}
	
	protected void unallocateBiospecimen(AjaxRequestTarget target) {
		InvCell invCell = iInventoryService.getInvCellByBiospecimen(cpModel.getObject().getBiospecimen());
		invCell.setBiospecimen(null);
		invCell.setStatus("Empty");
		iInventoryService.updateInvCell(invCell);
		
		// Increment available cell of box
		invCell.getInvBox().setAvailable(invCell.getInvBox().getAvailable() + 1);
		cpModel.getObject().setInvBox(invCell.getInvBox());
		iInventoryService.updateInvBox(cpModel.getObject());
		
		try {
			cpModel.getObject().setBiospecimenLocationVO(iInventoryService.getInvCellLocation(invCell));
			cpModel.getObject().getBiospecimenLocationVO().setIsAllocated(false);
			cpModel.getObject().setInvCell(invCell);
		}
		catch (ArkSystemException e) {
		}
		refreshParentPanel(target);
	}

	public abstract void refreshParentPanel(AjaxRequestTarget target);
	
	public ArkBusyAjaxButton getUnallocateButton() {
		return unallocateButton;
	}

	public void setUnallocateButton(ArkBusyAjaxButton unallocateButton) {
		this.unallocateButton = unallocateButton;
	}
}
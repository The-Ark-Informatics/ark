package au.org.theark.lims.web.component.biolocation;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.service.ILimsService;

/**
 * Panel displaying the location/not allocated details for a Biospecimen in context
 * 
 * @author nivedan
 * @author cellis
 * 
 */
public class BioLocationDetailPanel extends Panel {
	/**
	 * 
	 */
	private static final long						serialVersionUID	= 1L;
	private static final Logger					log					= LoggerFactory.getLogger(BioLocationDetailPanel.class);
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_SERVICE)
	private ILimsService								iLimsService;
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService						iInventoryService;
	protected CompoundPropertyModel<LimsVO>	cpModel;
	private Panel										locationPanel;
	private Label										locationDetails;

	public BioLocationDetailPanel(String id, CompoundPropertyModel<LimsVO> cpModel) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.cpModel = cpModel;

		initialisePanel();
		addComponents();
	}

	public void initialisePanel() {
		locationDetails = new Label("locationDetails", "Location Details:");
		locationPanel = new BioLocationNotAllocatedPanel("locationPanel", cpModel);
	}

	public void addComponents() {
		add(locationDetails);
		add(locationPanel);
	}
	
	@Override
	protected void onBeforeRender() {
		if (cpModel.getObject().getBiospecimenLocationVO() != null && cpModel.getObject().getBiospecimenLocationVO().getIsAllocated()) {
			locationPanel = new BioLocationPanel("locationPanel", cpModel){
				/**
				 * 
				 */
				private static final long	serialVersionUID	= 1L;

				@Override
				public void refreshParentPanel(AjaxRequestTarget target) {
					refreshPanel(target);
				}
			};	
		}
		else
		{
			locationPanel = new BioLocationNotAllocatedPanel("locationPanel", cpModel);
		}
		addOrReplace(locationPanel);
		super.onBeforeRender(); 
	}
	
	public void refreshPanel(AjaxRequestTarget target) {
		target.add(BioLocationDetailPanel.this);
	}
}
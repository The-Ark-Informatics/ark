package au.org.theark.lims.web.component.biolocation;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.lims.model.vo.LimsVO;

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
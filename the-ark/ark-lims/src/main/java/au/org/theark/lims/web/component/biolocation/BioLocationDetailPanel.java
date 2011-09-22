package au.org.theark.lims.web.component.biolocation;

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
		addComponents();// Not adding it to Form here
	}

	public void initialisePanel() {
		locationDetails = new Label("locationDetails", "Location Details:");
		
		if(cpModel.getObject().getBiospecimenLocationVO().getIsAllocated()) {
			locationPanel = new BioLocationPanel("locationPanel", cpModel);
		}
		else {
			locationPanel = new BioLocationNotAllocatedPanel("locationPanel");
		}
	}

	public void addComponents() {
		add(locationDetails);
		add(locationPanel);
	}
}
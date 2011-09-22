package au.org.theark.lims.web.component.biolocation;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.lims.model.vo.LimsVO;

/**
 * Panel displaying the location details for a Biospecimen in context
 * 
 * @author nivedan
 * @author cellis
 * 
 */
public class BioLocationPanel extends Panel {
	/**
	 * 
	 */
	private static final long						serialVersionUID	= 1L;
	protected CompoundPropertyModel<LimsVO>	cpModel;
	private Label										siteNameLbl;
	private Label										tankNameLbl;
	private Label										trayNameLbl;
	private Label										boxNameLbl;
	private Label										rowLbl;
	private Label										cellLbl;

	public BioLocationPanel(String id, CompoundPropertyModel<LimsVO> cpModel) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.cpModel = cpModel;

		initialisePanel();
		addComponents();// Not adding it to Form here
	}

	public void initialisePanel() {
		siteNameLbl = new Label("biospecimenLocationVO.siteName", cpModel.getObject().getBiospecimenLocationVO().getSiteName());
		tankNameLbl = new Label("biospecimenLocationVO.tankName", cpModel.getObject().getBiospecimenLocationVO().getTankName());
		trayNameLbl = new Label("biospecimenLocationVO.trayName", cpModel.getObject().getBiospecimenLocationVO().getTrayName());
		boxNameLbl = new Label("biospecimenLocationVO.boxName", cpModel.getObject().getBiospecimenLocationVO().getBoxName());
		rowLbl = new Label("biospecimenLocationVO.row", cpModel.getObject().getBiospecimenLocationVO().getRowLabel());
		cellLbl = new Label("biospecimenLocationVO.column", cpModel.getObject().getBiospecimenLocationVO().getColLabel());
	}

	public void addComponents() {
		add(siteNameLbl);
		add(tankNameLbl);
		add(trayNameLbl);
		add(boxNameLbl);
		add(rowLbl);
		add(cellLbl);
	}
}
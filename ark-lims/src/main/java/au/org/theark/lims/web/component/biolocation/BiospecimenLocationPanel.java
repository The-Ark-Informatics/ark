package au.org.theark.lims.web.component.biolocation;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.lims.model.vo.LimsVO;

public class BiospecimenLocationPanel extends Panel{
	protected CompoundPropertyModel<LimsVO>			cpModel;
	private Label												siteNameLbl;
	private Label												tankNameLbl;
	private Label												trayNameLbl;
	private Label												boxNameLbl;
	private Label												rowLbl;
	private Label												cellLabel;
	
	
	public BiospecimenLocationPanel(String id,CompoundPropertyModel<LimsVO> cpModel) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.cpModel = cpModel;
	
		initialisePanel();
		addComponents();//Not adding it to Form here
	}
	
	public void initialisePanel(){
		siteNameLbl = new Label("biospecimenLocationVO.siteName", cpModel.getObject().getBiospecimenLocationVO().getSiteName());
		tankNameLbl = new Label("biospecimenLocationVO.tankName", cpModel.getObject().getBiospecimenLocationVO().getTankName());
		trayNameLbl = new Label("biospecimenLocationVO.trayName", cpModel.getObject().getBiospecimenLocationVO().getTrayName());
		boxNameLbl = new Label("biospecimenLocationVO.boxName", cpModel.getObject().getBiospecimenLocationVO().getBoxName());
		rowLbl = new Label("biospecimenLocationVO.row",   cpModel.getObject().getBiospecimenLocationVO().getRow().toString());
		cellLabel = new Label("biospecimenLocationVO.column", cpModel.getObject().getBiospecimenLocationVO().getColumn().toString());
	}
	
	public void addComponents(){
		add(siteNameLbl);
		add(tankNameLbl);
		add(trayNameLbl);
		add(boxNameLbl);
		add(rowLbl);
		add(cellLabel);
	}

}

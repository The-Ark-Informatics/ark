package au.org.theark.genomics.web.component.datacenter;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.genomics.model.vo.DataSourceVo;
import au.org.theark.genomics.web.component.datacenter.form.SourceContainerForm;

public class DataSourceContainerPanel extends AbstractContainerPanel<DataSourceVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected WebMarkupContainer dataSourcePanelContainer;

	protected DataSourcePanel dataSourcePanel;

	protected AbstractDetailModalWindow modalWindow;

	private SourceContainerForm containerForm;

	public DataSourceContainerPanel(String id, AbstractDetailModalWindow modalWindow) {
		super(id);
		setOutputMarkupId(true);
		this.modalWindow = modalWindow;

		cpModel = new CompoundPropertyModel<DataSourceVo>(new DataSourceVo());

		/* Bind the CPM to the Form */
		containerForm = new SourceContainerForm("containerForm", cpModel);

		containerForm.add(initialiseFeedBackPanel());
		
		containerForm.add(initialiseDetailPanel());

		add(containerForm);
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		dataSourcePanelContainer = new WebMarkupContainer("dataSourceContainer");
		dataSourcePanelContainer.setOutputMarkupPlaceholderTag(true);

		dataSourcePanel = new DataSourcePanel("dataSourcePanel", feedBackPanel, arkCrudContainerVO, modalWindow);
		dataSourcePanel.setOutputMarkupId(true);
		dataSourcePanel.initialisePanel(cpModel);
		dataSourcePanelContainer.add(dataSourcePanel);
		

		return dataSourcePanelContainer;
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setPropertyModelObject(final DataSourceVo dataSourceVo){
		cpModel.setObject(dataSourceVo);
	}
	
	public void enableDeleteButton(final boolean enable){
		 AjaxButton deleteButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
		 deleteButton.setEnabled(enable);
	}

}

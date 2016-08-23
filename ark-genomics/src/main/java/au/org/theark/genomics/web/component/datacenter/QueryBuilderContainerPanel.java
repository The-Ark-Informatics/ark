package au.org.theark.genomics.web.component.datacenter;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.genomics.model.vo.DataCenterVo;
import au.org.theark.genomics.web.component.datacenter.form.QueryContainerForm;

public class QueryBuilderContainerPanel extends AbstractContainerPanel<DataCenterVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected WebMarkupContainer dataSourcePanelContainer;

	protected QueryPanel queryPanel;

	protected AbstractDetailModalWindow modalWindow;

	private QueryContainerForm containerForm;

	public QueryBuilderContainerPanel(String id, AbstractDetailModalWindow modalWindow) {
		super(id);
		setOutputMarkupId(true);
		this.modalWindow = modalWindow;

		cpModel = new CompoundPropertyModel<DataCenterVo>(new DataCenterVo());

		/* Bind the CPM to the Form */
		containerForm = new QueryContainerForm("containerForm", cpModel);

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
		dataSourcePanelContainer = new WebMarkupContainer("queryContainer");
		dataSourcePanelContainer.setOutputMarkupPlaceholderTag(true);

		queryPanel = new QueryPanel("queryPanel", feedBackPanel, arkCrudContainerVO, modalWindow);
		queryPanel.setOutputMarkupId(true);
		queryPanel.initialisePanel(cpModel);
		dataSourcePanelContainer.add(queryPanel);
		

		return dataSourcePanelContainer;
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setPropertyModelObject(final DataCenterVo dataCenterVo){
		cpModel.setObject(dataCenterVo);
	}
	
	public void enableDeleteButton(final boolean enable){
		 AjaxButton deleteButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
		 deleteButton.setEnabled(enable);
	}

}

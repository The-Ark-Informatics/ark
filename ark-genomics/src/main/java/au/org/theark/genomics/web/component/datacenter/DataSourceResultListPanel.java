package au.org.theark.genomics.web.component.datacenter;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.genomics.model.vo.DataCenterVo;
import au.org.theark.genomics.model.vo.DataSourceVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.datacenter.form.ContainerForm;

public class DataSourceResultListPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ContainerForm containerForm;

	private ArkCrudContainerVO arkCrudContainerVO;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;

	private PageableListView<DataSourceVo> sitePageableListView;
	
	private FeedbackPanel feedbackPanel;
	
	public DataSourceResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm studyCompContainerForm, FeedbackPanel feedbackPanel) {
		super(id);
		setOutputMarkupId(true);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
		this.feedbackPanel = feedbackPanel;
	}
	
	public PageableListView<DataSourceVo> buildPageableListView(IModel iModel) {
		this.sitePageableListView = new PageableListView<DataSourceVo>("dataSourceList", iModel, iArkCommonService.getRowsPerPage()) {

			@Override
			protected void populateItem(final ListItem<DataSourceVo> item) {
				DataSourceVo dataSource = item.getModelObject();
			
				
				DataSourceViewPanel dataSourcePanel = new DataSourceViewPanel("dataSourcePanel", feedbackPanel, arkCrudContainerVO);
				dataSourcePanel.setOutputMarkupId(true);
				dataSourcePanel.initialisePanel(new CompoundPropertyModel(dataSource));
				item.add(dataSourcePanel);
				
//				item.add(buildNameLink(dataSource));
				
//				if (dataSource.getDirectory() != null) {
//					item.add(new Label(Constants.DATA_SOURCE_VO_DIRECTORY, dataSource.getDirectory()));
//				} else {
//					item.add(new Label(Constants.DATA_SOURCE_VO_DIRECTORY, ""));
//				}
//				if (dataSource.getPath() != null) {
//					item.add(new Label(Constants.DATA_SOURCE_VO_PATH, dataSource.getPath()));
//				} else {
//					item.add(new Label(Constants.DATA_SOURCE_VO_PATH, ""));
//				}
//				if (dataSource.getDataSource() != null && dataSource.getDataSource().getStatus() != null) {
//					item.add(new Label(Constants.DATA_SOURCE_VO_STATUS, dataSource.getDataSource().getStatus()));
//				} else {
//					item.add(new Label(Constants.DATA_SOURCE_VO_STATUS, ""));
//				}

				
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		
		sitePageableListView.setOutputMarkupId(true);

		return sitePageableListView;
	}

	@SuppressWarnings({ "serial" })
	private AjaxLink buildNameLink(final DataSourceVo dataSourceVo) {

		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.DATA_SOURCE_VO_FILE_NAME) {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				// TODO Auto-generated method stub

				DataCenterVo dataCenterVo = containerForm.getModelObject();
				
				
				
			}
			
			@Override
			public boolean isEnabled() {
				return "Yes".equalsIgnoreCase(dataSourceVo.getDirectory());
			}
		};

		Label nameLinkLabel = new Label("nameLbl", dataSourceVo.getFileName());
		link.add(nameLinkLabel);
		return link;
	}	

}

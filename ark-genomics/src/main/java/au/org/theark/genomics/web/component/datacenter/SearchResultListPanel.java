package au.org.theark.genomics.web.component.datacenter;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.genomics.model.vo.DataSourceVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.datacenter.form.ContainerForm;

public class SearchResultListPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ContainerForm containerForm;
	
	private ArkCrudContainerVO arkCrudContainerVO;

	private AbstractDetailModalWindow modalWindow;
	
	private DataSourceContainerPanel dataSourceContainerPanel;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;

	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm studyCompContainerForm) {
		super(id);
		setOutputMarkupId(true);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
		initializeDataSourceModelWindow();
		addDataSourceModelWindow();
	}

	protected void initializeDataSourceModelWindow() {
		
		
		
		modalWindow = new AbstractDetailModalWindow("detailModalWindow") {

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {

			}
		};
	}
	
	protected void addDataSourceModelWindow() {
		add(modalWindow);
	}

	public PageableListView<DataSourceVo> buildPageableListView(IModel iModel) {
		PageableListView<DataSourceVo> sitePageableListView = new PageableListView<DataSourceVo>("dataSourceList", iModel, iArkCommonService.getRowsPerPage()) {

			@Override
			protected void populateItem(final ListItem<DataSourceVo> item) {
				DataSourceVo dataSource = item.getModelObject();

				if (dataSource.getFileName() != null) {
					item.add(new Label(Constants.DATA_SOURCE_VO_FILE_NAME, dataSource.getFileName()));
				} else {
					item.add(new Label(Constants.DATA_SOURCE_VO_FILE_NAME, ""));
				}
				if (dataSource.getDirectory() != null) {
					item.add(new Label(Constants.DATA_SOURCE_VO_DIRECTORY, dataSource.getDirectory()));
				} else {
					item.add(new Label(Constants.DATA_SOURCE_VO_DIRECTORY, ""));
				}
				if (dataSource.getPath() != null) {
					item.add(new Label(Constants.DATA_SOURCE_VO_PATH, dataSource.getPath()));
				} else {
					item.add(new Label(Constants.DATA_SOURCE_VO_PATH, ""));
				}
				if (dataSource.getStatus() != null) {
					item.add(new Label(Constants.DATA_SOURCE_VO_STATUS, dataSource.getStatus()));
				} else {
					item.add(new Label(Constants.DATA_SOURCE_VO_STATUS, ""));
				}

				item.add(buildLink(dataSource));

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};

		return sitePageableListView;
	}

	@SuppressWarnings({ "serial" })
	private AjaxLink buildLink(final DataSourceVo dataSourceVo) {

		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.DATA_SOURCE_VO_SOURCE) {

			@Override
			public void onClick(AjaxRequestTarget target) {
				
				DataSource dataSource =iGenomicService.getDataSource(dataSourceVo);
				
				dataSourceContainerPanel = new DataSourceContainerPanel("content", modalWindow);
				
				if(dataSource!=null){
					dataSourceVo.setDataSource(dataSource);
					dataSourceVo.setMode(Constants.MODE_EDIT);
					dataSourceContainerPanel.enableDeleteButton(true);
				}else{
					dataSourceVo.pupulateDataSource();
					dataSourceContainerPanel.enableDeleteButton(false);
				}
				
				
				modalWindow.setTitle("Source");
				modalWindow.setInitialWidth(90);
				modalWindow.setInitialHeight(100);
				dataSourceContainerPanel.setPropertyModelObject(dataSourceVo);
				modalWindow.setContent(dataSourceContainerPanel);
				modalWindow.show(target);
			}
		};

		Label nameLinkLabel = new Label("nameLbl", "Source");
		link.add(nameLinkLabel);
		return link;
	}

}

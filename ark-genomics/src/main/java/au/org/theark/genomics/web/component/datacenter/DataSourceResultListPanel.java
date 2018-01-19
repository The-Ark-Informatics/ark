package au.org.theark.genomics.web.component.datacenter;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.genomics.jobs.DataSourceUploadExecutor;
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

	private PageableListView<DataSource> sitePageableListView;

	private AbstractDetailModalWindow queryModalWindow;
	
	private QueryBuilderContainerPanel queryBuilderContainerPanel;
	
	private FeedbackPanel feedbackPanel;
	
	public DataSourceResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm studyCompContainerForm, FeedbackPanel feedbackPanel) {
		super(id);
		setOutputMarkupId(true);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
		this.feedbackPanel = feedbackPanel;
		initializeComponents();
		addComponents();
	}
	
	protected void initializeComponents() {
		queryModalWindow = new AbstractDetailModalWindow("queryModalWindow1") {
			
			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				
			}
		};
	}
	
	protected void addComponents() {
		add(queryModalWindow);
	}
	
	public PageableListView<DataSource> buildPageableListView(IModel iModel) {
		this.sitePageableListView = new PageableListView<DataSource>("dataSourceEntityList", iModel, iArkCommonService.getRowsPerPage()) {

			@Override
			protected void populateItem(final ListItem<DataSource> item) {
				DataSource dataSource = item.getModelObject();
				
				item.add(new Label(Constants.DATA_SOURCE_ID, dataSource.getId().toString()));				
				item.add(new Label(Constants.DATA_SOURCE_NAME, dataSource.getName()));
				
//				DataSourceViewPanel dataSourcePanel = new DataSourceViewPanel("dataSourcePanel", feedbackPanel, arkCrudContainerVO);
//				dataSourcePanel.setOutputMarkupId(true);
//				dataSourcePanel.initialisePanel(new CompoundPropertyModel(dataSource));
//				item.add(dataSourcePanel);
				
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

				item.add(buildUploadBtn(dataSource));
				item.add(buildDeleteBtn(dataSource));
				item.add(buildQuaryBtn(dataSource));
				
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
	
	private AjaxButton buildUploadBtn(final DataSource dataSource){
		AjaxButton uploadBtn = new AjaxButton("dsUpload") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				//super.onSubmit(target, form);
				try{
					String processUID = iGenomicService.executeDataSourceUpload(dataSource, Constants.STATUS_UNPROCESSED);
				
					dataSource.setStatus(Constants.STATUS_PROCESSING);

					iGenomicService.saveOrUpdate(dataSource);

					DataSourceUploadExecutor executor = new DataSourceUploadExecutor(dataSource, processUID, iGenomicService,getPlinkDataSourceList(),Constants.STATUS_UNPROCESSED);
					executor.run();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		
		
		if (!ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SAVE) 
				|| Constants.STATUS_PROCESSED.equalsIgnoreCase(dataSource.getStatus())
				|| Constants.STATUS_PROCESSING.equalsIgnoreCase(dataSource.getStatus())) {
			uploadBtn.setEnabled(false);
		}
		else {
			uploadBtn.setEnabled(true);
		}


		
		return uploadBtn;
	}
	
	private AjaxButton buildDeleteBtn(final DataSource dataSource){
		AjaxButton uploadBtn = new AjaxButton("dsDelete") {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				try{
					String processUID = iGenomicService.executeDataSourceUpload(dataSource, Constants.STATUS_PROCESSED);
					dataSource.setStatus(Constants.STATUS_PROCESSING);
					iGenomicService.saveOrUpdate(dataSource);
					DataSourceUploadExecutor executor = new DataSourceUploadExecutor(dataSource, processUID, iGenomicService,getPlinkDataSourceList(), Constants.STATUS_PROCESSED);
					executor.run();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		
		if (!ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SAVE) 
				|| Constants.STATUS_UNPROCESSED.equalsIgnoreCase(dataSource.getStatus())
				|| Constants.STATUS_PROCESSING.equalsIgnoreCase(dataSource.getStatus())) {
			uploadBtn.setEnabled(false);
		}
		else {
			uploadBtn.setEnabled(true);
		}
		
		return uploadBtn;
	}
	
	private AjaxButton buildQuaryBtn(final DataSource dataSource){
		AjaxButton uploadBtn = new AjaxButton("dsQuery") {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				queryBuilderContainerPanel = new QueryBuilderContainerPanel("content", queryModalWindow);
				queryBuilderContainerPanel.enableDeleteButton(false);
				queryModalWindow.setTitle("Query Builder");
				queryModalWindow.setInitialWidth(90);
				queryModalWindow.setInitialHeight(100);
				queryBuilderContainerPanel.setPropertyModelObject(containerForm.getModelObject());
				queryModalWindow.setContent(queryBuilderContainerPanel);
				queryModalWindow.show(target);
			}
		};
		
		
		if (!ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SAVE) 
				|| Constants.STATUS_UNPROCESSED.equalsIgnoreCase(dataSource.getStatus())
				|| Constants.STATUS_PROCESSING.equalsIgnoreCase(dataSource.getStatus())) {
			uploadBtn.setEnabled(false);
		}
		else {
			uploadBtn.setEnabled(true);
		}
		return uploadBtn;
	}
	
	private List<DataSource> getPlinkDataSourceList(){
		List<DataSourceVo> dataSourceList =   containerForm.getModelObject().getDataSourceList();
		
		List<DataSource> plinkSourceList = new ArrayList<DataSource>();
		
		for(DataSourceVo vo:dataSourceList){
			if(vo.getFileName().contains(".map") || 
					vo.getFileName().contains(".ped") ||
					vo.getFileName().contains(".bim") ||
					vo.getFileName().contains(".fam") ||
					vo.getFileName().contains(".bed") 
					){
				
				DataSource dataSource = vo.getDataSource();
				dataSource.setStatus(Constants.STATUS_NOT_READY);
				iGenomicService.saveOrUpdate(dataSource);
				plinkSourceList.add(dataSource);
			}
		}
		
		return plinkSourceList;
	}

}

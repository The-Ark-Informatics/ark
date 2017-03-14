package au.org.theark.genomics.web.component.datacenter;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.button.ArkAjaxButton;
import au.org.theark.genomics.jobs.DataSourceUploadExecutor;
import au.org.theark.genomics.model.vo.DataCenterVo;
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

	private AbstractDetailModalWindow detailModalWindow;
	
	private AbstractDetailModalWindow queryModalWindow;

	private DataSourceContainerPanel dataSourceContainerPanel;

	private QueryBuilderContainerPanel queryBuilderContainerPanel;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;

	private AjaxButton plinkUploadBtn;

	private AjaxButton plinkDeleteBtn;

	private AjaxButton queryBtn;

	private Label statusLabel;
	
	private PageableListView<DataSourceVo> sitePageableListView;

	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm studyCompContainerForm) {
		super(id);
		setOutputMarkupId(true);
		arkCrudContainerVO = crudContainerVO;
		containerForm = studyCompContainerForm;
		initializeComponents();
		addComponents();
	}

	protected void initializeComponents() {

		detailModalWindow = new AbstractDetailModalWindow("detailModalWindow") {

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				
				DataCenterVo dataCenterVo = containerForm.getModelObject();
				CompoundPropertyModel<DataCenterVo> cpmModel= (CompoundPropertyModel<DataCenterVo>)containerForm.getModel();
				List<DataSourceVo> resultList = iGenomicService.searchDataSources(containerForm.getModelObject());

				Component uploadBtn = arkCrudContainerVO.getSearchResultPanelContainer().get("searchResults").get("plinkUpload");
				Component deleteBtn = arkCrudContainerVO.getSearchResultPanelContainer().get("searchResults").get("plinkDelete");
				Component queryBtn = arkCrudContainerVO.getSearchResultPanelContainer().get("searchResults").get("queryBtn");

				
				
				if (resultList != null && resultList.size() == 0) {
					String dataCenter = dataCenterVo.getName();

					MicroService microService = dataCenterVo.getMicroService();

					if (microService == null || dataCenter == null) {
						this.error("Need to select a service and data centre prior to make a search.");
					} else {
//						this.info(getModelObject().getName() + " cannot be reach in the " + getModelObject().getMicroService().getName() + " Service");
						this.info("Cannot find any directories or files in the search location");
					}
					target.add(feedbackPanel);

					uploadBtn.setEnabled(false);
					deleteBtn.setEnabled(false);
					queryBtn.setEnabled(false);

					cpmModel.getObject().setStatus(null);
				} else {
					
					DataSourceVo dataSourceVo = new DataSourceVo();
					dataSourceVo.setDataCenter(cpmModel.getObject().getName());
					dataSourceVo.setMicroService(cpmModel.getObject().getMicroService());

					String dir = cpmModel.getObject().getDirectory();
					dataSourceVo.setPath(dir == null ? "/" : dir.charAt(0) == '/' ? dir : ("/" + dir));

					DataSource dataSource = iGenomicService.getDataSource(dataSourceVo);

					if (dataSource != null && dataSource.getStatus() != null) {
						cpmModel.getObject().setStatus(dataSource.getStatus());
					} else {
						cpmModel.getObject().setStatus(Constants.STATUS_UNPROCESSED);
					}
								
					for(DataSourceVo vo : resultList){
						if(vo.getDataSource().getStatus() == null || vo.getDataSource().getStatus().trim().length() == 0){
							if(vo.getFileName().contains(".map") || 
									vo.getFileName().contains(".ped") ||
									vo.getFileName().contains(".bim") ||
									vo.getFileName().contains(".fam") ||
									vo.getFileName().contains(".bed") 
									){
								
								if("Deleting".equalsIgnoreCase(cpmModel.getObject().getStatus()) ||
										"Uploading".equalsIgnoreCase(cpmModel.getObject().getStatus())
										){
									vo.getDataSource().setStatus(Constants.STATUS_NOT_READY);
								}else{
									vo.getDataSource().setStatus(Constants.STATUS_READY);
								}
							
							}else{
								vo.getDataSource().setStatus(Constants.STATUS_NOT_REQUIRED);
							}
						}
					}
					
					uploadBtn.setEnabled(false);
					deleteBtn.setEnabled(false);
					queryBtn.setEnabled(false);
					
					if (dataCenterVo.getDirectory() != null) {
						
						if(dataSource == null ){
							uploadBtn.setEnabled(true);
						}else{
							if(dataSource.getStatus() == null || dataSource.getStatus().trim().length() == 0){
								uploadBtn.setEnabled(true);
							}else if(Constants.STATUS_UNPROCESSED.equalsIgnoreCase(dataSource.getStatus())){
								uploadBtn.setEnabled(true);
							}
						}

						if(dataSource !=null && Constants.STATUS_PROCESSED.equalsIgnoreCase(dataSource.getStatus())){
							deleteBtn.setEnabled(true);
							queryBtn.setEnabled(true);
						}
					}
				}
				
				sitePageableListView.removeAll();
				containerForm.getModelObject().setDataSourceList(resultList);
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
//				target.add(arkCrudContainerVO.getSearchPanelContainer());	
			}
		};

		queryModalWindow = new AbstractDetailModalWindow("queryModalWindow") {
			
			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				
			}
		};

		plinkUploadBtn = new AjaxButton("plinkUpload") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				try {
					DataCenterVo dataCenter = containerForm.getModelObject();
					dataCenter.setStatus(Constants.STATUS_PROCESSING);

					String processUID = iGenomicService.executeDataSourceUpload(dataCenter, Constants.STATUS_UNPROCESSED);

					DataSourceVo dataSourceVo = new DataSourceVo();
					dataSourceVo.setDataCenter(dataCenter.getName());
					dataSourceVo.setMicroService(dataCenter.getMicroService());

					String dir = dataCenter.getDirectory();
					dataSourceVo.setPath(dir == null ? "/" : dir.charAt(0) == '/' ? dir : ("/" + dir));

					String[] dirList = dir.split("/");
					if (dirList.length > 0) {
						dataSourceVo.setFileName(dirList[dirList.length - 1]);
					}

					DataSource dataSource = iGenomicService.getDataSource(dataSourceVo);

					if (dataSource == null) {
						dataSourceVo.pupulateDataSource();
						dataSource = dataSourceVo.getDataSource();
					}
					
					dataSource.setStatus(Constants.STATUS_PROCESSING);

					iGenomicService.saveOrUpdate(dataSource);

					DataSourceUploadExecutor executor = new DataSourceUploadExecutor(dataSource, processUID, iGenomicService,getPlinkDataSourceList(),Constants.STATUS_UNPROCESSED);
					executor.run();
					
					this.setEnabled(false);
					plinkDeleteBtn.setEnabled(false);
					queryBtn.setEnabled(false);

					target.add(SearchResultListPanel.this);
				} catch (Exception e) {

				}

			}

		};

		plinkUploadBtn.setEnabled(false);

		plinkDeleteBtn = new AjaxButton("plinkDelete") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try{
				
					DataCenterVo dataCenter = containerForm.getModelObject();
					dataCenter.setStatus(Constants.STATUS_PROCESSING);

					String processUID = iGenomicService.executeDataSourceUpload(dataCenter, Constants.STATUS_PROCESSED);

					DataSourceVo dataSourceVo = new DataSourceVo();
					dataSourceVo.setDataCenter(dataCenter.getName());
					dataSourceVo.setMicroService(dataCenter.getMicroService());

					String dir = dataCenter.getDirectory();
					dataSourceVo.setPath(dir == null ? "/" : dir.charAt(0) == '/' ? dir : ("/" + dir));

					String[] dirList = dir.split("/");
					if (dirList.length > 0) {
						dataSourceVo.setFileName(dirList[dirList.length - 1]);
					}

					DataSource dataSource = iGenomicService.getDataSource(dataSourceVo);

					if (dataSource == null) {
						dataSourceVo.pupulateDataSource();
						dataSource = dataSourceVo.getDataSource();
					}
					
					dataSource.setStatus(Constants.STATUS_PROCESSING);

					iGenomicService.saveOrUpdate(dataSource);

					DataSourceUploadExecutor executor = new DataSourceUploadExecutor(dataSource, processUID, iGenomicService,getPlinkDataSourceList(), Constants.STATUS_PROCESSED);
					executor.run();
					
					this.setEnabled(false);
					queryBtn.setEnabled(false);
					plinkUploadBtn.setEnabled(false);

					target.add(SearchResultListPanel.this);
					
				}catch(Exception e){
					
				}
			}
		};

		plinkDeleteBtn.setEnabled(false);
		
		queryBtn = new AjaxButton("queryBtn") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {				
//				DataSource dataSource = new DataSource();
				queryBuilderContainerPanel = new QueryBuilderContainerPanel("content", queryModalWindow);
//				if (dataSource.getId() != null) {
//					queryBuilderContainerPanel.enableDeleteButton(true);
//				} else {
//					queryBuilderContainerPanel.enableDeleteButton(false);
//				}
				
				queryBuilderContainerPanel.enableDeleteButton(false);
				queryModalWindow.setTitle("Query Builder");
				queryModalWindow.setInitialWidth(90);
				queryModalWindow.setInitialHeight(100);
				queryBuilderContainerPanel.setPropertyModelObject(containerForm.getModelObject());
				queryModalWindow.setContent(queryBuilderContainerPanel);
				queryModalWindow.show(target);
			}
		};
		queryBtn.setEnabled(false);

		statusLabel = new Label("status");

	}

	protected void addComponents() {
		add(detailModalWindow);
		add(queryModalWindow);
		add(plinkUploadBtn);
		add(plinkDeleteBtn);
		add(queryBtn);
		add(statusLabel);
	}

	public PageableListView<DataSourceVo> buildPageableListView(IModel iModel) {
		this.sitePageableListView = new PageableListView<DataSourceVo>("dataSourceList", iModel, iArkCommonService.getRowsPerPage()) {

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
				if (dataSource.getDataSource() != null && dataSource.getDataSource().getStatus() != null) {
					item.add(new Label(Constants.DATA_SOURCE_VO_STATUS, dataSource.getDataSource().getStatus()));
				} else {
					item.add(new Label(Constants.DATA_SOURCE_VO_STATUS, ""));
				}

				item.add(buildSourceLink(dataSource));
				// item.add(buildUploadLink(dataSource));
				// item.add(buildDeleteLink(dataSource));

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
	private ArkAjaxButton buildSourceLink(final DataSourceVo dataSourceVo) {

		ArkAjaxButton link = new ArkAjaxButton("details") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				super.onSubmit(target, form);
				DataSource dataSource = dataSourceVo.getDataSource();

				dataSourceContainerPanel = new DataSourceContainerPanel("content", detailModalWindow);

				if (dataSource.getId() != null) {
					dataSourceContainerPanel.enableDeleteButton(true);
				} else {
					dataSourceContainerPanel.enableDeleteButton(false);
				}

				detailModalWindow.setTitle("Details");
				detailModalWindow.setInitialWidth(90);
				detailModalWindow.setInitialHeight(100);
				dataSourceContainerPanel.setPropertyModelObject(dataSourceVo);
				detailModalWindow.setContent(dataSourceContainerPanel);
				detailModalWindow.show(target);

			}
		};

		return link;
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

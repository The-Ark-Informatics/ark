package au.org.theark.genomics.web.component.datacenter.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.genomics.model.vo.DataCenterVo;
import au.org.theark.genomics.model.vo.DataSourceVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;

public class SearchForm extends AbstractSearchForm<DataCenterVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;

	private PageableListView<DataSourceVo> listView;

	private PageableListView<DataSourceVo> sourceView;

	private CompoundPropertyModel<DataCenterVo> cpmModel;

	private DropDownChoice<MicroService> microServicesDDC;
	private DropDownChoice<String> dataCentersDDC;
	private TextField<String> fileNameTextField;
	private TextField<String> directoryTextField;

	private AjaxButton newButton;

	private List<MicroService> microServiceList;

	private List<String> dataSourceList;
	
	private WebMarkupContainer	        dataSourcePanelContainer;

	public SearchForm(String id, CompoundPropertyModel<DataCenterVo> cpmModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<DataSourceVo> listView, PageableListView<DataSourceVo> sourceView, WebMarkupContainer dataSourcePanelContainer) {

		super(id, cpmModel, feedBackPanel, arkCrudContainerVO);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		this.listView = listView;
		this.sourceView = sourceView;
		this.cpmModel = cpmModel;
		this.dataSourcePanelContainer = dataSourcePanelContainer;

		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study select. Please select a study.");
	}

	private void initialiseSearchForm() {
		PropertyModel<MicroService> pm = new PropertyModel<MicroService>(cpmModel, "microService");

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		MicroService searchCriteria = new MicroService();
		searchCriteria.setStudyId(studyId);

		this.microServiceList = iGenomicService.searchMicroService(searchCriteria);

		initMicroServiceDropDown(pm);
		this.dataCentersDDC = new DropDownChoice<String>(Constants.DATA_CENTER_NAME);
		dataCentersDDC.setOutputMarkupId(true);
		this.fileNameTextField = new TextField<String>(Constants.DATA_CENTER_FILE_NAME);
		this.directoryTextField = new TextField<String>(Constants.DATA_CENTER_DIRECTORY);

		this.newButton = new AjaxButton(au.org.theark.core.Constants.NEW) {
		};
		newButton.setVisible(false);
	}

	private void addSearchComponentsToForm() {
		add(microServicesDDC);
		add(dataCentersDDC);
		add(fileNameTextField);
		add(directoryTextField);
		addOrReplace(newButton);
	}

	private void initMicroServiceDropDown(PropertyModel<MicroService> microService) {
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		this.microServicesDDC = new DropDownChoice(Constants.DATA_CENTER_MICRO_SERVICE, microService, this.microServiceList, defaultChoiceRenderer);
		this.microServicesDDC.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				DataCenterVo model = cpmModel.getObject();
				model.setName(null);
				dataCentersDDC.setChoices(iGenomicService.searchDataCenters(model.getMicroService()));
				target.add(dataCentersDDC);
			}

			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e) {

			}
		});

	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);

		DataCenterVo dataCenterVo = getModelObject();

		List<DataSourceVo> resultList = iGenomicService.searchDataSources(getModelObject());

		Component uploadBtn = arkCrudContainerVO.getSearchResultPanelContainer().get("searchResults").get("plinkUpload");
		Component deleteBtn = arkCrudContainerVO.getSearchResultPanelContainer().get("searchResults").get("plinkDelete");
		Component queryBtn = arkCrudContainerVO.getSearchResultPanelContainer().get("searchResults").get("queryBtn");

		if (resultList != null && resultList.size() == 0) {
			String dataCenter = dataCenterVo.getName();

			MicroService microService = dataCenterVo.getMicroService();

			if (microService == null || dataCenter == null) {
				this.error("You need to select a service and data centre prior to performing a search.");
			} else {
//				this.info(getModelObject().getName() + " cannot be reach in the " + getModelObject().getMicroService().getName() + " Service");
				this.info("Cannot find any directories or files in the search location.");
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
		getModelObject().setDataSourceList(resultList);
		listView.removeAll();
		sourceView.removeAll();
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
		target.add(dataSourcePanelContainer);
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		// TODO Auto-generated method stub

	}
	
}

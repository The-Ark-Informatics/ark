package au.org.theark.genomics.web.component.microservice.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.genomics.model.vo.MicroServiceVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;


public class SearchForm extends AbstractSearchForm<MicroServiceVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;
	
	private TextField<String>				microServiceIdTxtFld;
	private TextField<String>				microServiceNameTxtFld;
	
	private DropDownChoice<String>			microServiceStatusDDL;
	private TextArea<String>				microServiceURLTxtArea;

	
	private PageableListView<MicroService>	listView;
	
	private CompoundPropertyModel<MicroServiceVo>	cpmModel;


	public SearchForm(String id, CompoundPropertyModel<MicroServiceVo> microService, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<MicroService> listView) {

		super(id, microService, feedBackPanel, arkCrudContainerVO);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		this.listView = listView;
		
		this.cpmModel=cpmModel;

		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study selected. Please select a study.");
	}
	
	protected void initialiseSearchForm() {
		microServiceIdTxtFld = new TextField<String>(Constants.MICRO_SERVICE_ID);
		microServiceNameTxtFld = new TextField<String>(Constants.MICRO_SERVICE_NAME);
		microServiceNameTxtFld.setOutputMarkupId(true);
		this.initMicroServiceStatusDDL();
		microServiceURLTxtArea = new TextArea<String>(Constants.MICRO_SERVICE_URL);
		microServiceURLTxtArea.setOutputMarkupId(true);
	}
	
	private void initMicroServiceStatusDDL(){
		List<String> statusList = new ArrayList<String>();
		statusList.add(Constants.STATUS_ONLINE);
		statusList.add(Constants.STATUS_OFFLINE);
		microServiceStatusDDL = new DropDownChoice<String>(Constants.MICRO_SERVICE_STATUS,statusList);
			
	}
	
	protected void addSearchComponentsToForm() {
		add(microServiceIdTxtFld);
		add(microServiceNameTxtFld);
		add(microServiceURLTxtArea);
		add(microServiceStatusDDL);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		getModelObject().getMicroService().setStudyId(studyId);
		List<MicroService> resultList = iGenomicService.searchMicroService(getModelObject().getMicroService());;
		if (resultList != null && resultList.size() == 0) {
			this.info("Micro service with the specified criteria does not exist in the system.");
			target.add(feedbackPanel);
		}
		getModelObject().setMicroServiceList(resultList);
		listView.removeAll();
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		getModelObject().setMode(Constants.MODE_NEW);
		getModelObject().getMicroService().setId(null);
		
		TextField microserviceNameTxtFld = (TextField)arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.MICRO_SERVICE_NAME);
		TextArea microserviceUrlTxtArea = (TextArea)arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.MICRO_SERVICE_URL);
		preProcessDetailPanel(target);
		
		microserviceNameTxtFld.setEnabled(true);
		microserviceUrlTxtArea.setEnabled(true);
		
		target.add(microserviceNameTxtFld);
		target.add(microserviceUrlTxtArea);
	}

}

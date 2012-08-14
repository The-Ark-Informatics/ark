package au.org.theark.worktracking.web.component.workrequest.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.WorkRequest;
import au.org.theark.core.model.worktracking.entity.WorkRequestStatus;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.worktracking.model.vo.WorkRequestVo;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.Constants;


public class SearchForm  extends AbstractSearchForm<WorkRequestVo> {
	private static final long				serialVersionUID	= 1L;
	
	@SpringBean(name = Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService iWorkTrackingService;
	
	private ArkCrudContainerVO				arkCrudContainerVO;

	private TextField<String>				workRequestIdTxtField;
	private TextField<String>				workRequestItemNameTxtField;
	
	private DateTextField					workRequestRequestedDateDp;
	private DateTextField					workRequestCommencedDateDp;
	private DateTextField					workRequestCompletedDateDp;
	
	private DropDownChoice<WorkRequestStatus>		 workRequestStatuses;
	private DropDownChoice<Researcher>		 		 workRequestResearchers;
	
	private PageableListView<WorkRequest>	listView;
	
	private CompoundPropertyModel<WorkRequestVo>	cpmModel;
		
	private List<WorkRequestStatus> workRequestStatusList;
	private List<Researcher> 		researcherList;
	

	/**
	 * 
	 * @param id
	 * @param cpmModel
	 * @param arkCrudContainerVO
	 * @param feedBackPanel
	 * @param listView
	 */
	public SearchForm(String id, CompoundPropertyModel<WorkRequestVo> cpmModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<WorkRequest> listView) {

		super(id, cpmModel, feedBackPanel, arkCrudContainerVO);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		this.listView = listView;
		
		this.cpmModel=cpmModel;

		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a Study.");
	}

	protected void addSearchComponentsToForm() {		
		add(workRequestIdTxtField);
		add(workRequestItemNameTxtField);
		add(workRequestRequestedDateDp);
		add(workRequestCommencedDateDp);
		add(workRequestCompletedDateDp);
		add(workRequestStatuses);
		add(workRequestResearchers);
		
	}

	protected void initialiseSearchForm() {
		
		workRequestIdTxtField=new TextField<String>(Constants.WORK_REQUEST_ID);             
		workRequestItemNameTxtField=new TextField<String>(Constants.WORK_REQUEST_ITEM_NAME);       
		workRequestRequestedDateDp=new DateTextField(Constants.WORK_REQUEST_REQUESTED_DATE,au.org.theark.core.Constants.DD_MM_YYYY);
		initDateTextField(workRequestRequestedDateDp);
		workRequestCommencedDateDp=new DateTextField(Constants.WORK_REQUEST_COMMENCED_DATE,au.org.theark.core.Constants.DD_MM_YYYY);      
		initDateTextField(workRequestCommencedDateDp);
		workRequestCompletedDateDp=new DateTextField(Constants.WORK_REQUEST_COMPLETED_DATE,au.org.theark.core.Constants.DD_MM_YYYY);
		initDateTextField(workRequestCompletedDateDp);

		CompoundPropertyModel<WorkRequestVo> workRequestCmpModel = (CompoundPropertyModel<WorkRequestVo>) cpmModel;
		
		// Create a propertyModel to bind the components of this form, the root which is WorkRequest Container
		PropertyModel<WorkRequest> pm = new PropertyModel<WorkRequest>(workRequestCmpModel, "workRequest");	
		
		this.workRequestStatusList=this.iWorkTrackingService.getWorkRequestStatuses();
		
		PropertyModel<WorkRequestStatus> pmWorkRequestStatus = new PropertyModel<WorkRequestStatus>(pm, "requestStatus");
		this.initWorkRequestStatusDropDown(pmWorkRequestStatus);
		
		PropertyModel<Researcher> pmResearcher = new PropertyModel<Researcher>(pm, "researcher");
		this.initResearcherDropDown(pmResearcher);
	}
	
	private void initDateTextField(DateTextField dateTextField){
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateTextField);
		dateTextField.add(datePicker);
	}
	
	private void initWorkRequestStatusDropDown(
		PropertyModel<WorkRequestStatus> workRequestStatus) {
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		workRequestStatuses = new DropDownChoice(Constants.WORK_REQUEST_REQUEST_STATUS, workRequestStatus, this.workRequestStatusList, defaultChoiceRenderer);
		
	}
	
	private void initResearcherDropDown(
			PropertyModel<Researcher> workRequestResearcher) {
		
		Researcher researcher=new Researcher();
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		researcher.setStudyId(studyId);
		this.researcherList=iWorkTrackingService.searchResearcher(researcher);
		
		IChoiceRenderer<Researcher> customChoiceRenderer = new IChoiceRenderer<Researcher>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Object getDisplayValue(Researcher researcher) {
				return researcher.getFirstName()+" "+researcher.getLastName();
			}

			public String getIdValue(Researcher researcher, int index) {
				return researcher.getId().toString();
			}
			
		};
		workRequestResearchers = new DropDownChoice(Constants.WORK_REQUEST_RESEARCHER,  this.researcherList, customChoiceRenderer);
		workRequestResearchers.setModel(workRequestResearcher);
				
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
		setModelObject(new WorkRequestVo());
		getModelObject().setMode(Constants.MODE_NEW);
		preProcessDetailPanel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.form.AbstractSearchForm#onSearch(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSearch(AjaxRequestTarget target) {

			target.add(feedbackPanel);
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			getModelObject().getWorkRequest().setStudyId(studyId);
			
			List<WorkRequest> resultList= iWorkTrackingService.searchWorkRequest(getModelObject().getWorkRequest());
			
			if(resultList != null && resultList.size() == 0){
				this.info("Billable Item Type with the specified criteria does not exist in the system.");
				target.add(feedbackPanel);
			}
			
			getModelObject().setWorkRequestList(resultList);
			listView.removeAll();
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}
}
package au.org.theark.worktracking.web.component.researcher.form;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.worktracking.entity.Researcher;
import au.org.theark.core.model.worktracking.entity.ResearcherRole;
import au.org.theark.core.model.worktracking.entity.ResearcherStatus;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.worktracking.model.vo.ResearcherVo;
import au.org.theark.worktracking.service.IWorkTrackingService;
import au.org.theark.worktracking.util.Constants;


public class SearchForm extends AbstractSearchForm<ResearcherVo> {
	private static final long				serialVersionUID	= 1L;
	
	@SpringBean(name = Constants.WORK_TRACKING_SERVICE)
	private IWorkTrackingService iWorkTrackingService;
	
	private ArkCrudContainerVO				arkCrudContainerVO;
	
	private TextField<String>				researcherIdTxtField;
	private TextField<String>				firstNameTxtFld;
	private TextField<String>				lastNameTxtFld;
	private TextField<String>				instituteTxtFld;
	
	private DateTextField					createdDateDp;
	private DropDownChoice<ResearcherStatus>		 researcherStatuses;
	private DropDownChoice<ResearcherRole>		 	 researcherRoles;

	private PageableListView<Researcher>	listView;
	
	private CompoundPropertyModel<ResearcherVo>	cpmModel;
	
	private List<ResearcherStatus> researcherStatusList;
	private List<ResearcherRole> researcherRoleList;

	
	

	/**
	 * 
	 * @param id
	 * @param cpmModel
	 * @param arkCrudContainerVO
	 * @param feedBackPanel
	 * @param listView
	 */
	public SearchForm(String id, CompoundPropertyModel<ResearcherVo> cpmModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<Researcher> listView) {

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
		add(researcherIdTxtField);
		add(firstNameTxtFld);
		add(lastNameTxtFld);
		add(instituteTxtFld);
		
		add(createdDateDp);
		add(researcherStatuses);
		add(researcherRoles);		
	}

	protected void initialiseSearchForm() {

		researcherIdTxtField = new TextField<String>(Constants.RESEARCHER_ID);
		firstNameTxtFld=new TextField<String>(Constants.RESEARCHER_FIRST_NAME);
		lastNameTxtFld=new TextField<String>(Constants.RESEARCHER_LAST_NAME);
		instituteTxtFld=new TextField<String>(Constants.RESEARCHER_INSTITUTE);
		
		createdDateDp=new DateTextField(Constants.RESEARCHER_CREATED_DATE,au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(createdDateDp);
		createdDateDp.add(datePicker);
		
		
		
		CompoundPropertyModel<ResearcherVo> researcherCmpModel = (CompoundPropertyModel<ResearcherVo>) cpmModel;
		
		// Create a propertyModel to bind the components of this form, the root which is ResearcherContainer
		PropertyModel<Researcher> pm = new PropertyModel<Researcher>(researcherCmpModel, "researcher");	
		
		this.researcherStatusList=this.iWorkTrackingService.getResearcherStatuses();
		// Another PropertyModel for rendering the DropDowns and pass in the Property Model instance of Researcher Status
		PropertyModel<ResearcherStatus> pmResearcherStatus = new PropertyModel<ResearcherStatus>(pm, "researcherStatus");
		this.initResearcherStatusDropDown(pmResearcherStatus);
		
	
		this.researcherRoleList=this.iWorkTrackingService.getResearcherRoles();
		// Another PropertyModel for rendering the DropDowns and pass in the Property Model instance of Researcher Role
		PropertyModel<ResearcherRole> pmResearcherRole = new PropertyModel<ResearcherRole>(pm, "researcherRole");
		initResearcherRoleDropDown(pmResearcherRole);
		
	}
	
	private void initResearcherStatusDropDown(PropertyModel<ResearcherStatus> researcherStatus) {
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		researcherStatuses = new DropDownChoice(Constants.RESEARCHER_STATUS, researcherStatus, this.researcherStatusList, defaultChoiceRenderer);
	}
	
	private void initResearcherRoleDropDown(PropertyModel<ResearcherRole> researcherRole) {
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		researcherRoles = new DropDownChoice(Constants.RESEARCHER_ROLE, researcherRole, this.researcherRoleList, defaultChoiceRenderer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
		setModelObject(new ResearcherVo());
		getModelObject().setMode(Constants.MODE_NEW);
		getModelObject().getResearcher().setCreatedDate(new Date());
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
			getModelObject().getResearcher().setStudyId(studyId);

			List<Researcher> resultList = iWorkTrackingService.searchResearcher(getModelObject().getResearcher());

			if (resultList != null && resultList.size() == 0) {
				this.info("Researcher with the specified criteria does not exist in the system.");
				target.add(feedbackPanel);
			}

			getModelObject().setResearcherList(resultList);
			listView.removeAll();

			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}
}

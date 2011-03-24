package au.org.theark.study.web.component.correspondence.form;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.CorrespondenceStatusType;
import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.vo.CorrespondenceVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.address.DetailPanel;

public class SearchForm extends AbstractSearchForm<CorrespondenceVO> {

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

	private DetailPanel detailPanel;
	private PageableListView<Correspondences> pageableListView;
	
	private DropDownChoice<CorrespondenceStatusType> statusTypeChoice;
	private TextField<String> studyManagerTxtFld;
//	private TextField<String> dateFld;	// TODO: use proper type
	private TextField<String> timeTxtFld;
	private TextField<String> reasonTxtFld;
	private DropDownChoice<CorrespondenceModeType> modeTypeChoice;
	private DropDownChoice<CorrespondenceDirectionType> directionTypeChoice;
	private DropDownChoice<CorrespondenceOutcomeType> outcomeTypeChoice;
	private TextField<String> detailsTxtFld;
	
	
	public SearchForm(String id,
			CompoundPropertyModel<CorrespondenceVO> model,
			PageableListView<Correspondences> listView,
			FeedbackPanel feedbackPanel,
			WebMarkupContainer listContainer,
			WebMarkupContainer searchMarkupContainer,
			WebMarkupContainer detailContainer,
			WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer)	{
		
		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedbackPanel);
		this.pageableListView = listView;
		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionPersonId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		disableSearchButtons(sessionPersonId, "There is no subject or contact in context. Please select a subject or contact.");
		
	}


	private void initialiseSearchForm() {

		initialiseStatusTypeDropDown();
		studyManagerTxtFld = new TextField<String>("correspondence.studyManager");
//		dateFld = new TextField<String>("correspondence.date");	// TODO: fix this
		timeTxtFld = new TextField<String>("correspondence.time");
		reasonTxtFld = new TextField<String>("correspondence.reason");
		initialiseModeTypeDropDown();
		initialiseDirectionTypeDropDown();
		initialiseOutcomeTypeDropDown();
		detailsTxtFld = new TextField<String>("correspondence.details");	
	}
	

	private void initialiseStatusTypeDropDown() {
		
		List<CorrespondenceStatusType> list = studyService.getCorrespondenceStatusTypes();
		ChoiceRenderer<CorrespondenceStatusType> defaultRenderer = new ChoiceRenderer<CorrespondenceStatusType>("name", "id");
		statusTypeChoice = new DropDownChoice<CorrespondenceStatusType>("correspondence.correspondenceStatusType", list, defaultRenderer); 
	}

	
	private void initialiseModeTypeDropDown() {
		
		List<CorrespondenceModeType> list = studyService.getCorrespondenceModeTypes();
		ChoiceRenderer<CorrespondenceModeType> defaultRenderer = new ChoiceRenderer<CorrespondenceModeType>("name", "id");
		modeTypeChoice = new DropDownChoice<CorrespondenceModeType>("correspondence.correspondenceModeType", list, defaultRenderer);
	}
	
	
	private void initialiseDirectionTypeDropDown() {
		
		List<CorrespondenceDirectionType> list = studyService.getCorrespondenceDirectionTypes();
		ChoiceRenderer<CorrespondenceDirectionType> defaultRenderer = new ChoiceRenderer<CorrespondenceDirectionType>("name", "id");
		directionTypeChoice = new DropDownChoice<CorrespondenceDirectionType>("correspondence.correspondenceDirectionType", list, defaultRenderer);
	}
	
	
	private void initialiseOutcomeTypeDropDown() {
		
		List<CorrespondenceOutcomeType> list = studyService.getCorrespondenceOutcomeTypes();
		ChoiceRenderer<CorrespondenceOutcomeType> defaultRenderer = new ChoiceRenderer<CorrespondenceOutcomeType>("name", "id");
		outcomeTypeChoice = new DropDownChoice<CorrespondenceOutcomeType>("correspondence.correspondenceOutcomeType", list, defaultRenderer);
	}
	
	
	private void addSearchComponentsToForm() {

		add(statusTypeChoice);
		add(studyManagerTxtFld);
//		add(dateFld);	// TODO: fix this
		add(timeTxtFld);
		add(reasonTxtFld);
		add(modeTypeChoice);
		add(directionTypeChoice);
		add(outcomeTypeChoice);
		add(detailsTxtFld);	
	}
	
	
	@Override
	protected boolean isSecure(String actionType) {
		return true;
	}

	
	@Override
	protected void onNew(AjaxRequestTarget target) {
		
		setModelObject(new CorrespondenceVO());
		preProcessDetailPanel(target);
	}

	
	@Override
	protected void onSearch(AjaxRequestTarget target) {
		
		target.addComponent(feedbackPanel);
		Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Long sessionPersonId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		
		try {
			Correspondences correspondence = getModelObject().getCorrespondence();
			correspondence.setPerson(studyService.getPerson(sessionPersonId));
			Collection<Correspondences> correspondenceList = studyService.getPersonCorrespondenceList(sessionPersonId, correspondence);
			if(correspondenceList != null && correspondenceList.size() == 0) {
				this.info("Fields with the specified criteria do no exist in the system.");
				target.addComponent(feedbackPanel);
			}
			
			getModelObject().setCorrespondenceList(correspondenceList);
			pageableListView.removeAll();
			listContainer.setVisible(true);
			target.addComponent(listContainer);
			
		}catch(EntityNotFoundException ex) {
			this.warn("There are no correspondences available for the specified criteria.");
			target.addComponent(feedbackPanel);
		}catch(ArkSystemException ex) {
			this.error("The Ark application has encountered a system error.");
			target.addComponent(feedbackPanel);
		}
	}

}

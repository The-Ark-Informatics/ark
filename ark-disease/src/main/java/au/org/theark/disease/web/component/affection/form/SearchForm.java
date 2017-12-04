package au.org.theark.disease.web.component.affection.form;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.disease.entity.Affection;
import au.org.theark.core.model.disease.entity.AffectionStatus;
import au.org.theark.core.model.disease.entity.Disease;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.disease.service.IArkDiseaseService;
import au.org.theark.disease.vo.AffectionVO;

public class SearchForm extends AbstractSearchForm<AffectionVO> {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(SearchForm.class);
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_DISEASE_SERVICE)
	private IArkDiseaseService iArkDiseaseService;
	
	private CompoundPropertyModel<AffectionVO> cpModel;
	
	private WebMarkupContainer arkContextMarkup;
	
	private Long sessionStudyId;
	
	private DropDownChoice<Disease> diseaseDdc;
	private DropDownChoice<AffectionStatus> affectionStatusDDC;
	private DateTextField recordDateTxtFld;
	
	private ContainerForm containerForm;
	
	
	public SearchForm(String id, CompoundPropertyModel<AffectionVO> cpModel, PageableListView<AffectionVO> listView, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id, cpModel, feedBackPanel, arkCrudContainerVO);
		this.cpModel = cpModel;
		this.containerForm = containerForm;
		
		sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		initialiseSearchForm();
		addSearchComponentsToForm();
		disableSearchForm(sessionStudyId, Constants.MESSAGE_NO_STUDY_IN_CONTEXT);
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		disableSearchForm(sessionPersonId, Constants.MESSAGE_NO_SUBJECT_IN_CONTEXT);
	}
	
	
	protected void initialiseSearchForm() {	
		initDiseaseDdc();
		initAffectionStatusDDC();
		PatternDateConverter pdc = new PatternDateConverter( au.org.theark.core.Constants.DD_MM_YYYY, false); 
		recordDateTxtFld = new DateTextField("affection.recordDate",pdc);
		ArkDatePicker recordDatePicker = new ArkDatePicker();
		recordDatePicker.bind(recordDateTxtFld);
		recordDateTxtFld.add(recordDatePicker);
	}
	
	protected void addSearchComponentsToForm() {
		add(diseaseDdc);
		add(affectionStatusDDC);
		add(recordDateTxtFld);
	}
	
	@SuppressWarnings("unchecked")
	private void initAffectionStatusDDC() {
		CompoundPropertyModel<AffectionVO> affectionCPM = cpModel;
		PropertyModel<Affection> affectionPM = new PropertyModel<Affection>(affectionCPM, "affection");
		PropertyModel<AffectionStatus> affectionStatusPM = new PropertyModel<AffectionStatus>(affectionPM, "affectionStatus");
		ChoiceRenderer affectionStatusRenderer = new ChoiceRenderer("name", "id");
		List<AffectionStatus> possibleAffectionStatus = iArkDiseaseService.getAffectionStatus();
		affectionStatusDDC = new DropDownChoice<AffectionStatus>("affection.affectionStatus", affectionStatusPM, possibleAffectionStatus, affectionStatusRenderer);
	}
	
	@SuppressWarnings("unchecked")
	private void initDiseaseDdc() {
		CompoundPropertyModel<AffectionVO> affectionCpm = cpModel;
		PropertyModel<Affection> affectionPm = new PropertyModel<Affection>(affectionCpm, "affection");
		PropertyModel<Disease> diseasePm = new PropertyModel<Disease>(affectionPm, "disease");
		Collection<Disease> diseases = iArkDiseaseService.getAvailableDiseasesForStudy(iArkCommonService.getStudy(sessionStudyId)); 
		ChoiceRenderer diseaseRenderer = new ChoiceRenderer("name", "id");
		diseaseDdc = new DropDownChoice<Disease>("disease.name", diseasePm, (List) diseases, diseaseRenderer);
	}
	
	protected void onNew(AjaxRequestTarget target) {
		preProcessDetailPanel(target);
	}
	
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}
	
}

package au.org.theark.phenotypic.web.component.phenoCollection.form;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenoCollection.DetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "serial", "unchecked" })
public class SearchForm extends AbstractSearchForm<PhenoCollectionVO>
{
	@SpringBean(name = au.org.theark.phenotypic.service.Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService									phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService									iArkCommonService;

	private PageableListView<PhenoCollection>				listView;
	private CompoundPropertyModel<PhenoCollectionVO>	cpmModel;
	private TextField<String>									phenoCollectionIdTxtFld;
	private TextField<String>									phenoCollectionNameTxtFld;
	private TextArea<String>									phenoCollectionDescriptionTxtAreaFld;
	private DropDownChoice<Status>							statusDdc;
	private DateTextField									phenoCollectionStartDateFld;
	private DateTextField									phenoCollectionExpiryDateFld;
	private DetailPanel											detailPanel;
	private Long 													sessionStudyId;
	private WebMarkupContainer arkContextMarkup;

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoCollectionVO> model, PageableListView<PhenoCollection> listView, FeedbackPanel feedBackPanel, DetailPanel detailPanel,
			WebMarkupContainer listContainer, WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailContainer, WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer,
			WebMarkupContainer arkContextMarkup)
	{

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.listView = listView;
		this.detailPanel = detailPanel;
		this.arkContextMarkup = arkContextMarkup;
		initialiseFieldForm();
		
		this.sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);		
		disableSearchButtons(sessionStudyId, "There is no study in context. Please select a study");
	}

	/**
	 * @param id
	 */
	public SearchForm(String id, CompoundPropertyModel<PhenoCollectionVO> compoundPropertyModel)
	{
		super(id, compoundPropertyModel);
		this.cpmModel = compoundPropertyModel;
		initialiseFieldForm();
	}

	private void initStatusDdc()
	{
		java.util.Collection<Status> statusCollection = phenotypicService.getStatus();
		CompoundPropertyModel<PhenoCollectionVO> phenoCollectionCpm = cpmModel;
		PropertyModel<PhenoCollection> phenoCollectionPm = new PropertyModel<PhenoCollection>(phenoCollectionCpm, au.org.theark.phenotypic.web.Constants.PHENO_COLLECTION);
		PropertyModel<Status> statusPm = new PropertyModel<Status>(phenoCollectionPm, au.org.theark.phenotypic.web.Constants.STATUS);
		ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.STATUS_NAME, au.org.theark.phenotypic.web.Constants.STATUS_ID);
		statusDdc = new DropDownChoice<Status>(au.org.theark.phenotypic.web.Constants.STATUS, statusPm, (List) statusCollection, fieldTypeRenderer);
	}

	public void initialiseFieldForm()
	{
		phenoCollectionIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_ID);
		phenoCollectionNameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_NAME);
		phenoCollectionDescriptionTxtAreaFld = new TextArea<String>(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_DESCRIPTION);
		phenoCollectionStartDateFld = new DateTextField(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_START_DATE, Constants.DD_MM_YYYY);
		phenoCollectionExpiryDateFld = new DateTextField(au.org.theark.phenotypic.web.Constants.PHENO_COLLECTIONVO_PHENO_COLLECTION_EXPIRY_DATE, Constants.DD_MM_YYYY);
		 
		DatePicker startDatePicker = new DatePicker()
		{ 
			@Override 
			protected boolean enableMonthYearSelection() 
			{ 
			return true; 
			} 
		}; 
		startDatePicker.bind(phenoCollectionStartDateFld);
		phenoCollectionStartDateFld.add(startDatePicker);
		
		DatePicker endDatePicker = new DatePicker()
		{ 
			@Override 
			protected boolean enableMonthYearSelection() 
			{ 
				return true; 
			} 
		}; 
		endDatePicker.bind(phenoCollectionExpiryDateFld);
		phenoCollectionExpiryDateFld.add(endDatePicker);
		
		initStatusDdc();
		addFieldComponents();
	}

	private void addFieldComponents()
	{
		add(phenoCollectionIdTxtFld);
		add(phenoCollectionNameTxtFld);
		add(statusDdc);
		add(phenoCollectionDescriptionTxtAreaFld);
		add(phenoCollectionStartDateFld);
		add(phenoCollectionExpiryDateFld);
	}

	@Override
	protected void onNew(AjaxRequestTarget target)
	{
		// Show the details panel name and description
		PhenoCollectionVO phenoCollectionVo = new PhenoCollectionVO();
		phenoCollectionVo.setMode(au.org.theark.core.Constants.MODE_NEW);

		// Set study for the new collection and fields available
		this.sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		phenoCollectionVo.getPhenoCollection().setStudy(study);
		phenoCollectionVo.getField().setStudy(study);
		
		phenoCollectionVo.setFieldsAvailable(phenotypicService.searchField(phenoCollectionVo.getField()));

		setModelObject(phenoCollectionVo);
		preProcessDetailPanel(target);
		
		// Reset context item
		ContextHelper contextHelper = new ContextHelper();
		contextHelper.setPhenoContextLabel(target, "", arkContextMarkup);
		
		// Hide Delete button on New
		detailPanel.getDetailForm().getDeleteButton().setVisible(false);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target)
	{
		// Refresh the FB panel if there was an old message from previous search result
		target.addComponent(feedbackPanel);

		// Set study in context
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		// Get a list of all Fields for the Study in context
		Study study = iArkCommonService.getStudy(studyId);

		PhenoCollection phenoCollection = getModelObject().getPhenoCollection();
		phenoCollection.setStudy(study);

		java.util.Collection<PhenoCollection> phenoCollectionCollection = phenotypicService.searchPhenotypicCollection(phenoCollection);

		if (phenoCollectionCollection != null && phenoCollectionCollection.size() == 0)
		{
			this.info("Phenotypic Collections with the specified criteria does not exist in the system.");
			target.addComponent(feedbackPanel);
		}
		getModelObject().setPhenoCollectionCollection(phenoCollectionCollection);
		listView.removeAll();
		listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(listContainer);// For ajax this is required so
	}
	
	protected boolean isSecure(String actionType)
	{
		boolean flag = false;
		if (actionType.equalsIgnoreCase(au.org.theark.core.Constants.NEW))
		{
			SecurityManager securityManager =  ThreadContext.getSecurityManager();
			Subject currentUser = SecurityUtils.getSubject();		
			if(		securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) ||
					securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)){
				flag = true;
			};
		}
		else{
			flag = true;
		}
		return flag;
	}
}
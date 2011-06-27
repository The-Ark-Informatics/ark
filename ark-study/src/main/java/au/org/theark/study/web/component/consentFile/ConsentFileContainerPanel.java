package au.org.theark.study.web.component.consentFile;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.consentFile.form.ContainerForm;


public class ConsentFileContainerPanel extends AbstractContainerPanel<ConsentVO> {

	private static final long serialVersionUID = 1L;

	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	//Container Form
	private ContainerForm containerForm;
	//Panels
	//private SearchResultListPanel searchResultListPanel;
	private SearchPanel searchPanel;
	private DetailPanel detailPanel;
	private PageableListView<ConsentFile> pageableListView;
	
	
	/**
	 * Constructor
	 * @param id
	 */
	public ConsentFileContainerPanel(String id) {
		
		super(id);
		cpModel = new CompoundPropertyModel<ConsentVO>(new ConsentVO());
		containerForm = new ContainerForm("containerForm",cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());		
		add(containerForm);
	}
	
	public ConsentFileContainerPanel(String id, ConsentVO consentVo) {
		super(id);
		// Use consentVo in context from consent page
		cpModel = new CompoundPropertyModel<ConsentVO>(consentVo);
		containerForm = new ContainerForm("containerForm",cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel().setVisible(true));
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel().setVisible(false));
		add(containerForm);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseDetailPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseDetailPanel() {

		detailPanel = new DetailPanel("detailsPanel",
										feedBackPanel,
										searchResultPanelContainer, 
										detailPanelContainer,
										detailPanelFormContainer,
										searchPanelContainer,
										viewButtonContainer,
										editButtonContainer,
										containerForm);
		detailPanel.initialisePanel();
		detailPanelContainer.add(detailPanel);
		return detailPanelContainer;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		
		//Get the Person in Context and determine the Person Type
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		
		try{
			Collection<ConsentFile> consentFileList = new ArrayList<ConsentFile>();
			Long sessionConsentId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_CONSENT_ID);
			
			if(sessionPersonId != null && sessionConsentId != null){
				Consent consent = new Consent();
				consent = studyService.getConsent(sessionConsentId);
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				Study study =	iArkCommonService.getStudy(sessionStudyId);
				LinkSubjectStudy linkSubjectStudy = studyService.getSubjectLinkedToStudy(sessionPersonId,study);
				consent.setLinkSubjectStudy(linkSubjectStudy);
				containerForm.getModelObject().setConsent(consent);
			}
				
			//All the phone items related to the person if one found in session or an empty list
			cpModel.getObject().setConsentFileList(consentFileList);
			searchPanel = new SearchPanel("searchComponentPanel", 
											feedBackPanel,
											searchPanelContainer,
											pageableListView,
											searchResultPanelContainer,
											detailPanelContainer,
											detailPanel,
											containerForm,
											viewButtonContainer,
											editButtonContainer,
											detailPanelFormContainer);
			searchPanel.initialisePanel(cpModel);
			searchPanelContainer.add(searchPanel);
			
		}catch(EntityNotFoundException entityNotFoundException){
			containerForm.error("The Person/Subject cannot be found in the system.Please contact Support");
			
		}catch(ArkSystemException arkSystemException){
			containerForm.error("A System Error has occured.Please contact Support");
		}
		
		return searchPanelContainer;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchResults()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		
		SearchResultListPanel searchResultPanel = new SearchResultListPanel("searchResults",
				detailPanelContainer,
				detailPanelFormContainer,
				searchPanelContainer,
				searchResultPanelContainer,
				viewButtonContainer,
				editButtonContainer,
				containerForm);
		
		iModel = new LoadableDetachableModel<Object>() {

			private static final long serialVersionUID = 1L;
			@Override
			protected Object load() 
			{	
				//Get the PersonId from session and get the phoneList from back end
				Collection<ConsentFile> consentFileList = new ArrayList<ConsentFile>();
				// Use consent in context
				Long sessionConsentId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_CONSENT_ID);
				try 
				{
					if(sessionConsentId != null)
					{	
						if(isActionPermitted()){
							Consent consent = studyService.getConsent(sessionConsentId);
							ConsentFile consentFile = new ConsentFile();
							consentFile.setConsent(consent);
							consentFileList = studyService.searchConsentFile(consentFile);
						}
					}
				} 
				catch (EntityNotFoundException e) 
				{
					e.printStackTrace();
				} 
				catch (ArkSystemException e) 
				{
					e.printStackTrace();
				}
				
				pageableListView.removeAll();
				return consentFileList;
			}
		};
	
		pageableListView  = searchResultPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(pageableListView);
		searchResultPanelContainer.add(searchResultPanel);
		return searchResultPanelContainer;	
	}
}
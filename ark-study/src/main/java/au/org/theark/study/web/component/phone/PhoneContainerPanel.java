/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.phone;

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
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.PhoneVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.phone.form.ContainerForm;
import au.org.theark.study.web.component.studycomponent.Details;


/**
 * @author nivedann
 *
 */
public class PhoneContainerPanel extends AbstractContainerPanel<PhoneVO>{

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	//Container Form
	private ContainerForm containerForm;
	//Panels
	private SearchResultListPanel searchResultListPanel;
	private SearchPanel searchPanel;
	private DetailPanel detailPanel;
	private PageableListView<Phone> pageableListView;
	
	
	/**
	 * Constructor
	 * @param id
	 */
	public PhoneContainerPanel(String id) {
		
		super(id);
		cpModel = new CompoundPropertyModel<PhoneVO>(new PhoneVO());
		containerForm = new ContainerForm("containerForm",cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
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
		String sessionPersonType = (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);//Subject or Contact: Denotes if it was a subject or contact placed in session
		
		try{
			
			if(sessionPersonId != null && sessionPersonType != null){
				
				//Get a list of phone for the subject/contact
				Phone phone = containerForm.getModelObject().getPhone();
				
				phone.setPerson(studyService.getPerson(sessionPersonId));//Can be a Subject or Contact
			
				Collection<Phone> personPhoneList = studyService.getPersonPhoneList(sessionPersonId);
				//All the phone items related to the person would be fetched
				cpModel.getObject().setPhoneList(personPhoneList);
				//containerForm.getModelObject().setPhoneList(personPhoneList);
				
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
			}else{
				
			}
			
		}catch(EntityNotFoundException entityNotFoundException){
			//Report this to the user
			
		}catch(ArkSystemException arkSystemException){
			//Logged by the back end. Report this to the user
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
																			containerForm																				);
		iModel = new LoadableDetachableModel<Object>() {

			private static final long serialVersionUID = 1L;
			@Override
			protected Object load() {
				//Get the PersonId from session and get the phoneList from backend
				return containerForm.getModelObject().getPhoneList();
				//Hit the backend always and page it
			}
		};
	
		pageableListView  = searchResultPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("phoneNavigator", pageableListView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(pageableListView);
		searchResultPanelContainer.add(searchResultPanel);
		return searchResultPanelContainer;
		
		
	}

}

/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.address;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.AddressVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.address.form.ContainerForm;

/**
 * @author nivedann
 *
 */
public class AddressContainerPanel extends  AbstractContainerPanel<AddressVO>{

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	private ContainerForm containerForm;
	
	//Panels
	private SearchPanel searchPanel;
	private SearchResultListPanel searchResultListPanel;
	private DetailPanel detailPanel;
	private PageableListView<Address> pageableListView;
	
	/**
	 * @param id
	 */
	public AddressContainerPanel(String id) {
		super(id);
		cpModel = new CompoundPropertyModel<AddressVO>( new AddressVO());
		containerForm = new ContainerForm("containerForm",cpModel);
		containerForm.add(initialiseFeedBackPanel());
		//containerForm.add(initialiseDetailPanel());
		//containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		add(containerForm);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseDetailPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		String sessionPersonType = (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);//Subject or Contact: Denotes if it was 
		//Set the person who this address should be associated with 
		Collection<Address> addressList = new ArrayList<Address>();
		try {
			
			containerForm.getModelObject().getAddress().setPerson(studyService.getPerson(sessionPersonId));
			addressList = studyService.getPersonAddressList(sessionPersonId, containerForm.getModelObject().getAddress());
			cpModel.getObject().setAddresses(addressList);
			
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
		} catch (EntityNotFoundException e) {
			//Report this to the user
		} catch (ArkSystemException e) {
			//Logged by the back end. Report this to the user
		}
		return searchPanelContainer;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchResults()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		// TODO Auto-generated method stub
		return null;
	}

}

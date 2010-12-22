package au.org.theark.study.web.component.phone.form;

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
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.PhoneVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.phone.DetailPanel;

/**
 * @author Nivedan
 * 
 */
@SuppressWarnings( { "serial", "unchecked" })
public class SearchForm extends AbstractSearchForm<PhoneVO>
{

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

	private DetailPanel detailPanel;
	private PageableListView<Phone> pageableListView;
	private CompoundPropertyModel<PhoneVO>	cpmModel;
	
	private TextField<String> areaCodeTxtFld;
	private TextField<String> phoneNumberTxtFld;
	private DropDownChoice<PhoneType> phoneTypeChoice;
	
	
	/**
	 * @param id
	 */
	public SearchForm(String id,
						CompoundPropertyModel<PhoneVO> model, 
						PageableListView<Phone> listView, 
						FeedbackPanel feedBackPanel, 
						WebMarkupContainer listContainer,
						WebMarkupContainer searchMarkupContainer, 
						WebMarkupContainer detailContainer, 
						WebMarkupContainer detailPanelFormContainer, 
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer)
	{

		super(id, model, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = model;
		this.pageableListView = listView;
		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		disableSearchButtons(sessionPersonId, "There is no subject or contact in context. Please select a Subject or Contact.");
	}

	protected void initialiseSearchForm(){
		
		areaCodeTxtFld = new TextField<String>("phone.areaCode");
		phoneNumberTxtFld = new TextField<String>("phone.phoneNumber");

		List<PhoneType> phoneTypeList = iArkCommonService.getListOfPhoneType();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		phoneTypeChoice = new DropDownChoice("phone.phoneType",phoneTypeList,defaultChoiceRenderer);
		
	}

	protected void addSearchComponentsToForm(){
		add(areaCodeTxtFld);
		add(phoneNumberTxtFld);
		add(phoneTypeChoice);
	}
	
	@Override
	protected void onSearch(AjaxRequestTarget target)
	{
		target.addComponent(feedbackPanel);
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		String sessionPersonType = (String)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);//Subject or Contact: Denotes if it was a subject or contact placed in session
		try{
			
			Phone phone = getModelObject().getPhone();
			phone.setPerson(studyService.getPerson(sessionPersonId));

//			if(sessionPersonType.equalsIgnoreCase(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT)){
//				
//			}else if(sessionPersonType.equalsIgnoreCase(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_CONTACT)){
//				
//			}
			Collection<Phone> phones = studyService.getPersonPhoneList(sessionPersonId, getModelObject().getPhone());
			if (phones != null && phones.size() == 0)
			{
				this.info("Fields with the specified criteria does not exist in the system.");
				target.addComponent(feedbackPanel);
			}
			
			getModelObject().setPhoneList(phones);
			pageableListView.removeAll();
			listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
			target.addComponent(listContainer);

			
		}catch(EntityNotFoundException entityNotFoundException){
			this.warn("There are no phone items available for the specified criteria.");
			target.addComponent(feedbackPanel);
			
		}catch(ArkSystemException arkException){
			this.error("The Ark Application has encountered a system error.");
			target.addComponent(feedbackPanel);
		}
		
	}
	
	@Override
	protected void onNew(AjaxRequestTarget target)
	{
		setModelObject(new PhoneVO());
		preProcessDetailPanel(target);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#isSecure(java.lang.String)
	 */
	@Override
	protected boolean isSecure(String actionType) {
		// TODO Auto-generated method stub
		return true;
	}
}
/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.address.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.AddressVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.address.DetailPanel;

/**
 * @author nivedann
 *
 */
public class SearchForm extends AbstractSearchForm<AddressVO>
{

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

	private DetailPanel detailPanel;
	private PageableListView<Address> pageableListView;
	private CompoundPropertyModel<AddressVO> cpmModel;
	private TextField<String> phoneNumberTxtFld;
	private DropDownChoice<PhoneType> phoneTypeChoice;
	
	
	/**
	 * @param id
	 */
	public SearchForm(String id,
						CompoundPropertyModel<AddressVO> model, 
						PageableListView<Address> listView, 
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
		
		
	}

	protected void addSearchComponentsToForm(){
	}
	
	@Override
	protected void onSearch(AjaxRequestTarget target)
	{

		
	}
	
	@Override
	protected void onNew(AjaxRequestTarget target)
	{
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

/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.customfield.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class SearchForm extends AbstractSearchForm<CustomFieldVO>{

	/**
	 * Search Form Fields
	 */
	private TextField<String> fieldTitleTxtFld;
	private TextField<String> fieldNameTxtFld;
	private ArkCrudContainerVO arkCrudContainerVO;
	
	
	/**
	 * Constructor
	 * @param id
	 * @param cpmModel
	 */
	public SearchForm(	String id, 
						IModel<CustomFieldVO> cpmModel,
						PageableListView<SubjectCustmFld> pageableListView, 
						FeedbackPanel feedBackPanel,
						ArkCrudContainerVO arkCrudContainerVO) {
		
		super(id, cpmModel,feedBackPanel,arkCrudContainerVO);
		
		this.arkCrudContainerVO = arkCrudContainerVO;
		initialiseSearchForm();
		addSearchComponentsToForm();
		
		Long studySessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchButtons(studySessionId, "There is no study in context. Please select one.");
		
	}

	
	/**
	 * Initialise all the form components for the search
	 */
	protected void initialiseSearchForm(){
		fieldTitleTxtFld = new TextField<String>(Constants.CUSTOM_FIELD_FIELD_TITLE);
		fieldNameTxtFld = new TextField<String>(Constants.CUSTOM_FIELD_FIELD_NAME);
	}
	
	protected void addSearchComponentsToForm(){
		add(fieldTitleTxtFld);
		add(fieldNameTxtFld);
	}
	
	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#isSecure(java.lang.String)
	 */
	@Override
	protected boolean isSecure(String actionType) {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#onSearch(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSearch(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
		
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		
	}

}

/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.customfield.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.customfield.DetailPanel;

/**
 * @author nivedann
 *
 */
public class SearchForm extends AbstractSearchForm<CustomFieldVO>{

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService iArkCommonService;
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
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
						DetailPanel detailPanel,
						ArkCrudContainerVO arkCrudContainerVO) {
		
		super(id, cpmModel,feedBackPanel,arkCrudContainerVO);
		
		this.arkCrudContainerVO = arkCrudContainerVO;
		initialiseSearchForm();
		addSearchComponentsToForm();
		
		Long studySessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(studySessionId, "There is no study in context. Please select one.");
		
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
		target.addComponent(feedbackPanel);

		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study =	iArkCommonService.getStudy(sessionStudyId);
		//Get the list of Study Related Custom Fields
		getModelObject().getCustomField().setStudy(study);
		
		CustomFieldVO vo  = getModelObject();
		SubjectCustmFld customField = vo.getCustomField();
		
		List<SubjectCustmFld> subjectCustomFldList  = studyService.searchStudyFields(customField);
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		
	}

}

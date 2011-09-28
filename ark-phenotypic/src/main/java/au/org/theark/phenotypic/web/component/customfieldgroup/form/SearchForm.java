package au.org.theark.phenotypic.web.component.customfieldgroup.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.web.form.AbstractSearchForm;

/**
 * @author nivedann
 *
 */
public class SearchForm extends AbstractSearchForm<CustomFieldGroupVO>{

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	private ArkCrudContainerVO	arkCrudContainerVO;
	private TextField<String> groupNameTxtFld;
	private DropDownChoice<YesNo> publishedStatusChoice;
	
	
	/**
	 * @param id
	 * @param cpmModel
	 */
	public SearchForm(String id,CompoundPropertyModel<CustomFieldGroupVO> cpmModel, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, cpmModel,feedBackPanel,arkCrudContainerVO);
		this.feedbackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		initialiseSearchForm();
		addSearchComponentsToForm();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
		// TODO NN
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractSearchForm#onSearch(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
		final Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		getModelObject().getCustomFieldGroup().setStudy(study);
		
		int count = iArkCommonService.getCustomFieldGroupCount(getModelObject().getCustomFieldGroup());

		if (count <= 0) {
			this.info("No records match the specified criteria.");
			target.addComponent(feedbackPanel);
		}
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
	}
	
	protected void initialiseSearchForm(){
		groupNameTxtFld = new TextField<String>("customFieldGroup.name");
		List<YesNo> yesNoListSource = iArkCommonService.getYesNoList();
		ChoiceRenderer<YesNo> yesNoRenderer = new ChoiceRenderer<YesNo>("name", "id");
		publishedStatusChoice = new DropDownChoice<YesNo>("customFieldGroup.published", (List) yesNoListSource, yesNoRenderer);
	}
	
	protected void addSearchComponentsToForm() {
		add(groupNameTxtFld);
		add(publishedStatusChoice);
	}

}

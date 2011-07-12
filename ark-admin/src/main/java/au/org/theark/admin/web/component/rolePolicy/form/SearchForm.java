package au.org.theark.admin.web.component.rolePolicy.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;

public class SearchForm extends AbstractSearchForm<AdminVO>
{
	/**
	 * 
	 */
	private static final long					serialVersionUID	= -204010204180506704L;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	/* The Input Components that will be part of the Search Form */
	@SuppressWarnings("unused")
	private DropDownChoice<Study>				studyDpChoices;
	private TextField<String>					idTxtFld;
	private CompoundPropertyModel<AdminVO>	cpmModel;
	private ArkCrudContainerVO					arkCrudContainerVO;
	private ContainerForm						containerForm;
	private FeedbackPanel						feedbackPanel;

	private List<StudyStatus>					studyList;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param model
	 * @param ArkCrudContainerVO
	 * @param containerForm
	 */
	public SearchForm(String id, CompoundPropertyModel<AdminVO> cpmModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedbackPanel, ContainerForm containerForm)
	{
		super(id, cpmModel, feedbackPanel, arkCrudContainerVO);

		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedbackPanel;
		setMultiPart(true);

		this.setCpmModel(cpmModel);

		initialiseSearchForm();
		addSearchComponentsToForm();
	}

	@SuppressWarnings("unchecked")
	protected void initialiseSearchForm()
	{
		this.setStudyList(iArkCommonService.getStudy(containerForm.getModelObject().getStudy()));
		idTxtFld = new TextField<String>("arkRolePolicyTemplate.id");
	}

	@SuppressWarnings("unchecked")
	protected void onSearch(AjaxRequestTarget target)
	{
		ArkRolePolicyTemplate arkRolePolicyTemplate = containerForm.getModelObject().getArkRolePolicyTemplate();
		List<ArkRolePolicyTemplate> resultList = iArkCommonService.searchArkRolePolicyTemplate(arkRolePolicyTemplate);
		if (resultList != null && resultList.size() == 0)
		{
			containerForm.getModelObject().setArkRolePolicyTemplateList(resultList);
			this.info("There are no records that matched your query. Please modify your filter");
			target.addComponent(feedbackPanel);
		}

		containerForm.getModelObject().setArkRolePolicyTemplateList(resultList);
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
	}

	private void addSearchComponentsToForm()
	{
		// add(studyDpChoices);
		add(idTxtFld);
	}

	protected void onNew(AjaxRequestTarget target)
	{
		containerForm.setModelObject(new AdminVO());
		preProcessDetailPanel(target, arkCrudContainerVO);
	}

	/**
	 * @param studyList
	 *           the studyList to set
	 */
	public void setStudyList(List<StudyStatus> studyList)
	{
		this.studyList = studyList;
	}

	/**
	 * @return the studyList
	 */
	public List<StudyStatus> getStudyList()
	{
		return studyList;
	}

	/**
	 * @param cpmModel
	 *           the cpmModel to set
	 */
	public void setCpmModel(CompoundPropertyModel<AdminVO> cpmModel)
	{
		this.cpmModel = cpmModel;
	}

	/**
	 * @return the cpmModel
	 */
	public CompoundPropertyModel<AdminVO> getCpmModel()
	{
		return cpmModel;
	}
}
package au.org.theark.phenotypic.web.component.customfieldgroup;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.CustomFieldGroupVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.phenotypic.web.component.customfieldgroup.form.ContainerForm;

/**
 * @author nivedann
 *
 */
public class CustomFieldGroupContainerPanel extends AbstractContainerPanel<CustomFieldGroupVO>{

	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService	iArkCommonService;
	
	
	private ContainerForm containerForm;
	
	
	
	/**
	 * Constructor that uses the ArkCrudContainerVO
	 * @param id
	 * @param useArkCrudContainerVO
	 */
	public CustomFieldGroupContainerPanel(String id,boolean useArkCrudContainerVO, ArkFunction associatedPrimaryFn) {

		super(id, useArkCrudContainerVO);
		cpModel = new CompoundPropertyModel<CustomFieldGroupVO>( new CustomFieldGroupVO());
		cpModel.getObject().getCustomFieldGroup().setArkFunction(associatedPrimaryFn);//The AssociatedPrimaryFunction is passed in for Pheno it will be DataDictionary
		
		prerenderContextCheck();
		
		containerForm = new ContainerForm("containerForm",cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		//containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		add(containerForm);
	}

	
	protected void prerenderContextCheck() {
		
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);

		if ((sessionStudyId != null) && (sessionModuleId != null)) {
			Study study =  iArkCommonService.getStudy(sessionStudyId);
			ArkModule arkModule =  iArkCommonService.getArkModuleById(sessionModuleId);
			if (study != null && arkModule != null) {
				cpModel.getObject().getCustomFieldGroup().setStudy(study);
			}
		}
		
	}
	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseDetailPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		Panel detailsPanel = new EmptyPanel("detailsPanel");
		detailsPanel.setOutputMarkupPlaceholderTag(true);	//ensure this is replaceable
		arkCrudContainerVO.getDetailPanelContainer().add(detailsPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		SearchPanel searchPanel = new SearchPanel("searchComponentPanel", cpModel,arkCrudContainerVO,feedBackPanel);
		searchPanel.initialisePanel();
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchResults()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected WebMarkupContainer initialiseFeedBackPanel() {
		/* Feedback Panel */
		feedBackPanel = new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		return feedBackPanel;
	}

}

package au.org.theark.study.web.component.familycustomdata;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.study.model.vo.FamilyCustomDataVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

public class FamilyCustomDataContainerPanel extends Panel {
	private static final long										serialVersionUID		= 1L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService<Void>							iArkCommonService;
	
	@SpringBean(name = Constants.STUDY_SERVICE)
	protected IStudyService							iStudyService;

	protected CompoundPropertyModel<FamilyCustomDataVO>	cpModel;

	protected FeedbackPanel											feedbackPanel;
	protected WebMarkupContainer									customDataEditorWMC;

	private CustomField												customFieldCriteria	= new CustomField();
	
	private ModalWindow modalWindow;

	/**
	 * @param id
	 */
	public FamilyCustomDataContainerPanel(String id, ModalWindow modalWindow) {
		super(id);
		cpModel = new CompoundPropertyModel<FamilyCustomDataVO>(new FamilyCustomDataVO());
		this.modalWindow = modalWindow;
	}

	public FamilyCustomDataContainerPanel initialisePanel() {
		add(initialiseFeedbackPanel());
		add(initialiseCustomDataEditorWMC());
		if (!ArkPermissionHelper.isModuleFunctionAccessPermitted()) {
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
			customDataEditorWMC.setVisible(false);
		}
		return this;
	}
	
	protected WebMarkupContainer initialiseCustomDataEditorWMC() {
		customDataEditorWMC = new WebMarkupContainer("customDataEditorWMC");
		Panel dataEditorPanel;
		boolean contextLoaded = prerenderContextCheck();

		if (contextLoaded && isActionPermitted()) {
			//Need to get the DB object of Family.
			/*CustomFieldType type = new CustomFieldType();
			type.setName(au.org.theark.core.Constants.FAMILY);
			customFieldCriteria.setCustomFieldType(type);*/
			long fieldCount = iArkCommonService.getCustomFieldCount(customFieldCriteria);
			if (fieldCount <= 0L) {
				dataEditorPanel = new EmptyPanel("customDataEditorPanel");
				this.error("There are currently no custom fields defined.");
			}
			else {
				dataEditorPanel = new FamilyCustomDataEditorPanel("customDataEditorPanel", cpModel, feedbackPanel,modalWindow).initialisePanel();
				
			}
		}
		else if (!contextLoaded) {
			dataEditorPanel = new EmptyPanel("customDataEditorPanel");
			this.error(au.org.theark.core.Constants.MESSAGE_NO_SUBJECT_IN_CONTEXT);
		}
		else {
			dataEditorPanel = new EmptyPanel("customDataEditorPanel");
			this.error("You do not have sufficient permissions to access this function");
		}
		customDataEditorWMC.add(dataEditorPanel);
		return customDataEditorWMC;
	}


	protected WebMarkupContainer initialiseFeedbackPanel() {
		/* Feedback Panel doesn't have to sit within a form */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}

	protected boolean isActionPermitted() {
		boolean flag = false;
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ)) {
			flag = true;
		}
		else {
			flag = false;
		}
		return flag;
	}

	protected boolean prerenderContextCheck() {
		// Get the Study, SubjectUID and ArkModule from Context
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		Long sessionArkModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);

		boolean contextLoaded = false;
		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy linkSubjectStudy = null;
			ArkModule arkModule = null;
			Study study = null;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				cpModel.getObject().getLinkSubjectStudy().setStudy(study);
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID, study);
				cpModel.getObject().setLinkSubjectStudy(linkSubjectStudy);
				arkModule = iArkCommonService.getArkModuleById(sessionArkModuleId);
				// cpModel.getObject().setArkModule(arkModule);
				if (study != null && linkSubjectStudy != null && arkModule != null) {
					contextLoaded = true;
					cpModel.getObject().setArkFunction(iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD));
					customFieldCriteria.setStudy(study);
					customFieldCriteria.setArkFunction(cpModel.getObject().getArkFunction());
					//Get the subject persistance object from the custom field type.
					customFieldCriteria.setCustomFieldType(iArkCommonService.getCustomFieldTypeByName(au.org.theark.core.Constants.FAMILY));
				}
			}
			catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return contextLoaded;
	}

}

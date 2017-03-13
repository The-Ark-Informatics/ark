package au.org.theark.study.web.component.pedigree.form;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyPedigreeConfiguration;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.checkbox.ArkConfirmCheckBox;
import au.org.theark.study.model.vo.PedigreeVo;
import au.org.theark.study.service.IStudyService;

public class ConfigurationForm extends Form<PedigreeVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService studyService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	protected FeedbackPanel feedbackPanel;

	protected ArkCrudContainerVO arkCrudContainerVO;

	protected AbstractDetailModalWindow modalWindow;

	private DropDownChoice<CustomField> effectedStatusDDL;

	private CompoundPropertyModel<PedigreeVo> cpmModel;

	private List<CustomField> affectedStatusList;

	private List<CustomField> familyIdList;

	private CheckBox dobChkBox;

	private CheckBox statusChkBox;

	private CheckBox ageChkBox;

	private ArkConfirmCheckBox inbreedingChkBox;

	protected AjaxButton saveButton;

	protected AjaxButton cancelButton;

	public ConfigurationForm(String id, CompoundPropertyModel<PedigreeVo> cpmModel, FeedbackPanel feedbackPanel, ArkCrudContainerVO arkCrudContainerVO, AbstractDetailModalWindow modalWindow) {
		super(id, cpmModel);
		this.cpmModel = cpmModel;
		this.feedbackPanel = feedbackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.modalWindow = modalWindow;

		initialiseSearchForm();
		addSearchComponentsToForm();
	}

	protected void initialiseSearchForm() {

		this.setOutputMarkupId(true);

		final Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		final Study study = iArkCommonService.getStudy(studyId);
		StudyPedigreeConfiguration config = study.getPedigreeConfiguration();

		if (config != null) {
			cpmModel.getObject().setPedigreeConfig(config);
		}
	
		affectedStatusList = studyService.getBinaryCustomFieldsForPedigreeRelativesList(studyId);
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		effectedStatusDDL = new DropDownChoice("pedigreeConfig.customField", this.affectedStatusList, defaultChoiceRenderer);
		effectedStatusDDL.setOutputMarkupId(true);

		if (config != null && config.isStatusAllowed() != null && config.isStatusAllowed()) {
			effectedStatusDDL.setEnabled(true);
		} else {
			effectedStatusDDL.setEnabled(false);
			cpmModel.getObject().getPedigreeConfig().setCustomField(null);
		}

		dobChkBox = new CheckBox("pedigreeConfig.dobAllowed");

		statusChkBox = new CheckBox("pedigreeConfig.statusAllowed");

		statusChkBox.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = -4514605801401294450L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				cpmModel.getObject().getPedigreeConfig().setCustomField(null);
				if (cpmModel.getObject().getPedigreeConfig().isStatusAllowed()) {
					effectedStatusDDL.setEnabled(true);
				} else {
					effectedStatusDDL.setEnabled(false);
				}
				target.add(effectedStatusDDL);
			}
			
		});

		ageChkBox = new CheckBox("pedigreeConfig.ageAllowed");
			
		inbreedingChkBox = new ArkConfirmCheckBox("pedigreeConfig.inbreedAllowed",new StringResourceModel("confirmChange", this, null),new Model<Boolean>(isInbreedAllowed(config)) ){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				cpmModel.getObject().getPedigreeConfig().setInbreedAllowed(getModel().getObject());				
			}
			
			@Override
			public boolean isEnabled() {
				// TODO Auto-generated method stub
				return BooleanUtils.isNotTrue(isInbreedAllowed(cpmModel.getObject().getPedigreeConfig()));
			}
		};

		saveButton = new AjaxButton(au.org.theark.core.Constants.SAVE) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				PedigreeVo obj = (PedigreeVo) form.getModelObject();

				StudyPedigreeConfiguration configObject = obj.getPedigreeConfig();
				if (configObject.getStudy() == null) {
					configObject.setStudy(study);
				}
				studyService.saveOrUpdateStudyPedigreeConfiguration(configObject);
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.study.web.Constants.INBREED_ALLOWED, configObject.getInbreedAllowed());
				modalWindow.close(target);

			}
		};

		cancelButton = new AjaxButton("close") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.close(target);
			}
		};
	}

	protected void addSearchComponentsToForm() {
		add(effectedStatusDDL);
		add(dobChkBox);
		add(statusChkBox);
		add(ageChkBox);
		add(saveButton);
		add(cancelButton);
		add(inbreedingChkBox);
	}
	
	private Boolean isInbreedAllowed(StudyPedigreeConfiguration config){
		Boolean result=false;
		if (config != null && BooleanUtils.isTrue(config.getInbreedAllowed())) {
			result=true;
		} else {
			result=false;
		}
		return result;
	}

}

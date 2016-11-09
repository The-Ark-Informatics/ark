package au.org.theark.genomics.web.component.microservice.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.genomics.model.vo.MicroServiceVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;


public class DetailForm extends AbstractDetailForm<MicroServiceVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger	log	= org.slf4j.LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;

	private TextField<String> microServiceIdTxtFld;
	private TextField<String> microServiceNameTxtFld;
	private TextArea<String> microServiceDescription;
	private TextArea<String> microServiceTxtArea;

	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
	}

	public void onBeforeRender() {
		super.onBeforeRender();
	}

	public void initialiseDetailForm() {
		microServiceIdTxtFld = new TextField<String>(Constants.MICRO_SERVICE_ID);
		microServiceIdTxtFld.setEnabled(false);
		microServiceNameTxtFld = new TextField<String>(Constants.MICRO_SERVICE_NAME);
		microServiceNameTxtFld.setOutputMarkupId(true);
		microServiceDescription = new TextArea<String>(Constants.MICRO_SERVICE_DESCRIPTION);
		microServiceTxtArea = new TextArea<String>(Constants.MICRO_SERVICE_URL);
		microServiceTxtArea.setOutputMarkupId(true);
		addDetailFormComponents();
		attachValidators();
	}

	@Override
	protected void attachValidators() {
		microServiceNameTxtFld.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_MICRO_SERVICE_NAME_REQUIRED, microServiceNameTxtFld, new Model<String>(Constants.ERROR_MICRO_SERVICE_NAME_TAG)));
		microServiceTxtArea.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_MICRO_SERVICE_URL_REQUIRED, microServiceNameTxtFld, new Model<String>(Constants.ERROR_MICRO_SERVICE_URL_TAG)));
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		MicroServiceVo microServiceVo = new MicroServiceVo();
		containerForm.setModelObject(microServiceVo);
	}

	@Override
	protected void onSave(Form<MicroServiceVo> containerForm, AjaxRequestTarget target) {
		try {

			if (containerForm.getModelObject().getMicroService().getId() == null) {
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				containerForm.getModelObject().getMicroService().setStudyId(studyId);
				iGenomicService.saveOrUpdate(containerForm.getModelObject().getMicroService());
				this.info("Micro Service " + containerForm.getModelObject().getMicroService().getName() + " was created successfully");
			} else {
				iGenomicService.saveOrUpdate(containerForm.getModelObject().getMicroService());
				this.info("Micro Service " + containerForm.getModelObject().getMicroService().getName() + " was updated successfully");
			}
			processErrors(target);
			onSavePostProcess(target);
			
			AjaxButton deleteButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
			deleteButton.setEnabled(false);
			this.microServiceNameTxtFld.setEnabled(false);
			this.microServiceTxtArea.setEnabled(false);
			
			target.add(deleteButton);
			target.add(this.microServiceNameTxtFld);
			target.add(this.microServiceTxtArea);
			
		} catch (Exception e) {
			log.error("Error in saving micro service entity ",e);
			this.error("A System error occured, we will have someone contact you.");
			processErrors(target);
		}
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {

			iGenomicService.delete(containerForm.getModelObject().getMicroService());
			MicroServiceVo researcherVo = new MicroServiceVo();
			containerForm.setModelObject(researcherVo);
			containerForm.info("Micro Service was deleted successfully.");
			editCancelProcess(target);

		} catch (Exception e) {
			containerForm.error("A System Error has occured please contact support.");
			processErrors(target);
		}

	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);

	}

	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getMicroService().getId() == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(microServiceIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(microServiceNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(microServiceDescription);
		arkCrudContainerVO.getDetailPanelFormContainer().add(microServiceTxtArea);

	}

}

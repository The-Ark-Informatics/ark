package au.org.theark.genomics.web.component.computation.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
import au.org.theark.genomics.model.vo.ComputationVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;
import au.org.theark.genomics.web.component.computation.form.ContainerForm;


public class DetailForm extends AbstractDetailForm<ComputationVo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger	log	= org.slf4j.LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;

	private TextField<String> computationIdTxtFld;
	private TextField<String> computationNameTxtFld;
//	private TextArea<String> microServiceDescription;
//	private TextArea<String> microServiceTxtArea;

	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
	}

	public void onBeforeRender() {
		super.onBeforeRender();
	}

	public void initialiseDetailForm() {
		computationIdTxtFld = new TextField<String>(Constants.COMPUTATION_ID);
		computationIdTxtFld.setEnabled(false);
		computationNameTxtFld = new TextField<String>(Constants.COMPUTATION_NAME);
//		microServiceDescription = new TextArea<String>(Constants.MICRO_SERVICE_DESCRIPTION);
//		microServiceTxtArea = new TextArea<String>(Constants.MICRO_SERVICE_URL);
		addDetailFormComponents();
		attachValidators();
	}

	@Override
	protected void attachValidators() {
//		microServiceNameTxtFld.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_MICRO_SERVICE_NAME_REQUIRED, microServiceNameTxtFld, new Model<String>(Constants.ERROR_MICRO_SERVICE_NAME_TAG)));
//		microServiceTxtArea.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_MICRO_SERVICE_URL_REQUIRED, microServiceNameTxtFld, new Model<String>(Constants.ERROR_MICRO_SERVICE_URL_TAG)));
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		ComputationVo computationVo = new ComputationVo();
		containerForm.setModelObject(computationVo);
	}

	@Override
	protected void onSave(Form<ComputationVo> containerForm, AjaxRequestTarget target) {
		try {

			if (containerForm.getModelObject().getComputation().getId() == null) {
//				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
//				containerForm.getModelObject().getMicroService().setStudyId(studyId);
				iGenomicService.saveOrUpdate(containerForm.getModelObject().getComputation());
				this.info("Computation " + containerForm.getModelObject().getComputation().getName() + " was created successfully");
			} else {
				iGenomicService.saveOrUpdate(containerForm.getModelObject().getComputation());
				this.info("Computation " + containerForm.getModelObject().getComputation().getName() + " was updated successfully");
			}
			processErrors(target);
			onSavePostProcess(target);
		} catch (Exception e) {
			log.error("Error in saving micro service entity ",e);
			this.error("A System error occured, we will have someone contact you.");
			processErrors(target);
		}
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {

			iGenomicService.delete(containerForm.getModelObject().getComputation());
			ComputationVo computaionVo = new ComputationVo();
			containerForm.setModelObject(computaionVo);
			containerForm.info("Computation was deleted successfully.");
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
		if (containerForm.getModelObject().getComputation().getId() == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(computationIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(computationNameTxtFld);
//		arkCrudContainerVO.getDetailPanelFormContainer().add(microServiceDescription);
//		arkCrudContainerVO.getDetailPanelFormContainer().add(microServiceTxtArea);

	}

}

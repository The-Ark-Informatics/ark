package au.org.theark.genomics.web.component.computation.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;

import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.genomics.jobs.CompilationExecutor;
import au.org.theark.genomics.model.vo.ComputationVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;

public class DetailForm extends AbstractDetailForm<ComputationVo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = org.slf4j.LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;

	private ArkCrudContainerVO arkCrudContainerVO;

	private TextField<String> computationIdTxtFld;
	private TextField<String> computationNameTxtFld;
	private TextArea<String> computationDescTxtArea;

	private TextField<String> computationStatusTxtFld;
	private DropDownChoice<MicroService> microServicesDDC;

	private FileUploadField fileUploadField;
	private AjaxButton clearButton;
	private AjaxButton deleteButton;
	private Label fileNameLbl;

	private CheckBox availableChkBox;

	private List<MicroService> microServiceList;

	private AjaxButton uploadButton;
	private AjaxButton compileButton;

	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}

	public void onBeforeRender() {
		super.onBeforeRender();
	}

	public void initialiseDetailForm() {

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		MicroService searchCriteria = new MicroService();
		searchCriteria.setStudyId(studyId);
		this.microServiceList = iGenomicService.searchMicroService(searchCriteria);

		this.computationIdTxtFld = new TextField<String>(Constants.COMPUTATION_ID);
		this.computationIdTxtFld.setEnabled(false);
		this.computationNameTxtFld = new TextField<String>(Constants.COMPUTATION_NAME);

		this.computationDescTxtArea = new TextArea<String>(Constants.COMPUTATION_DESCRIPTION);
		this.computationStatusTxtFld = new TextField<String>(Constants.COMPUTATION_STATUS);
		computationStatusTxtFld.setEnabled(false);

		// PropertyModel<Computation> pm = new
		// PropertyModel<Computation>(cpmModel, "computation");
		this.initMicroServiceDropDown();

		fileUploadField = new FileUploadField("file");
		// fileUploadField.setOutputMarkupId(true);

		fileNameLbl = new Label("computation.programName");
		fileNameLbl.setOutputMarkupId(true);

		clearButton = new AjaxButton("clearButton") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				fileUploadField.clearInput();
				getFormModelObject().getComputation().setProgramName(null);
				target.add(fileUploadField);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				fileUploadField.clearInput();
				target.add(fileUploadField);
			}
		};
		clearButton.add(new AttributeModifier("title", new Model<String>("Clear Attachment")));

		deleteButton = new AjaxButton("deleteButton") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				containerForm.getModelObject().getComputation().setProgramName(null);
				// containerForm.getModelObject().getCorrespondence().setAttachmentFilename(null);
				this.setVisible(false);
				target.add(fileNameLbl);
				target.add(this);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				containerForm.getModelObject().getComputation().setProgramName(null);
				// containerForm.getModelObject().getCorrespondence().setAttachmentFilename(null);
				this.setVisible(false);
				target.add(fileNameLbl);
				target.add(this);
			}

			@Override
			public boolean isVisible() {
				return (containerForm.getModelObject().getComputation().getProgramName() != null) && !containerForm.getModelObject().getComputation().getProgramName().isEmpty();
			}
		};
		deleteButton.add(new AttributeModifier("title", new Model<String>("Delete Attachment")));
		deleteButton.setOutputMarkupId(true);

		this.uploadButton = new AjaxButton("upload") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				Computation computation = containerForm.getModelObject().getComputation();
				try {
					iGenomicService.uploadComputation(computation);
				} catch (Exception e) {
					this.error("An unexpected error occurred. Computation upload failed.");
					computation.setStatus(Constants.STATUS_UPLOAD_FAILED);
					e.printStackTrace();
				}
				
				try{
					iGenomicService.update(computation, null, null);
				}catch(Exception e){
					e.printStackTrace();
				}
				target.add(computationStatusTxtFld);
				target.add(feedBackPanel);
				target.add(uploadButton);
				target.add(this);
			}

			@Override
			public boolean isEnabled() {

				// boolean enabled=true;
				// Computation
				// computation=getFormModelObject().getComputation();
				// if(computation!=null && computation.getStatus() !=null &&
				// (computation.getStatus().equalsIgnoreCase(Constants.STATUS_UPLOADED)
				// ||
				// computation.getStatus().equalsIgnoreCase(Constants.STATUS_PROCESSED))){
				// enabled=false;
				// }
				// return enabled;

				boolean enabled = false;
				Computation computation = getFormModelObject().getComputation();
				if (computation.getId() != null && 
						!(Constants.STATUS_UPLOADING.equalsIgnoreCase(computation.getStatus()) 
								|| Constants.STATUS_UPLOADED.equalsIgnoreCase(computation.getStatus()) 
								|| Constants.STATUS_PROCESSED.equalsIgnoreCase(computation.getStatus()) )) {
					enabled = true;
				}
				return enabled;
			}
		};
		this.uploadButton.setOutputMarkupId(true);

		this.compileButton = new AjaxButton("compile") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				try {

					Computation computation = getFormModelObject().getComputation();

					String processUID = iGenomicService.compileComputation(computation);

					computation.setStatus(Constants.STATUS_PROCESSING);

					iGenomicService.saveOrUpdate(computation);

					CompilationExecutor executor = new CompilationExecutor(computation, processUID, iGenomicService);

					executor.run();

				} catch (Exception e) {
					this.error("An unexpected error occurred. Computation compilation failled.");
					e.printStackTrace();
				}

				target.add(computationStatusTxtFld);
				target.add(feedBackPanel);
				target.add(compileButton);
			}

			@Override
			public boolean isEnabled() {

				boolean enabled = false;
				Computation computation = getFormModelObject().getComputation();
				if (computation.getId() != null && 
						(Constants.STATUS_UPLOADED.equalsIgnoreCase(computation.getStatus()))) {
					enabled = true;
				}
				return enabled;
			}
		};
		this.compileButton.setOutputMarkupId(true);

		arkCrudContainerVO.getEditButtonContainer().add(uploadButton);
		arkCrudContainerVO.getEditButtonContainer().add(compileButton);

		this.availableChkBox = new CheckBox(Constants.COMPUTATION_AVAILABLE);

		addDetailFormComponents();
		attachValidators();
	}

	private void initMicroServiceDropDown() {
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		this.microServicesDDC = new DropDownChoice(Constants.COMPUTATION_MICROSERVICE, this.microServiceList, defaultChoiceRenderer);
		// this.microServicesDDC.add(new
		// AjaxFormComponentUpdatingBehavior("onChange") {
		//
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// protected void onUpdate(AjaxRequestTarget target) {
		// DataCenterVo model = cpmModel.getObject();
		// model.setName(null);
		// dataCentersDDC.setChoices(iGenomicService.searchDataCenters(model.getMicroService()));
		// target.add(dataCentersDDC);
		// }
		//
		// @Override
		// protected void onError(AjaxRequestTarget target, RuntimeException e)
		// {
		//
		// }
		// });

	}

	@Override
	protected void attachValidators() {
		computationNameTxtFld.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_COMPUTATION_NAME_REQUIRED, computationNameTxtFld, new Model<String>(Constants.ERROR_COMPUTATION_NAME_TAG)));
		microServicesDDC.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_COMPUTATION_MICROSERVICE_REQUIRED, microServicesDDC, new Model<String>(Constants.ERROR_COMPUTATION_MICROSERVICE_TAG)));
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

				byte[] uploadData = null;

				if (fileUploadField != null && fileUploadField.getFileUpload() != null) {
					FileUpload fileUpload = fileUploadField.getFileUpload();

					byte[] byteArray = fileUpload.getMD5();
					String checksum = getHex(byteArray);
					uploadData = fileUpload.getBytes();
					containerForm.getModelObject().getComputation().setProgramName(fileUpload.getClientFileName());
					containerForm.getModelObject().getComputation().setChecksum(checksum);
				}

				this.iGenomicService.save(containerForm.getModelObject().getComputation(), uploadData);
				this.info("Computation " + containerForm.getModelObject().getComputation().getName() + " was created successfully.");
			} else {

				String checksum = null;
				byte[] uploadData = null;

				if (fileUploadField != null && fileUploadField.getFileUpload() != null) {
					// retrieve file and store as Blob in database
					FileUpload fileUpload = fileUploadField.getFileUpload();

					byte[] byteArray = fileUpload.getMD5();
					checksum = getHex(byteArray);
					uploadData = fileUpload.getBytes();
					containerForm.getModelObject().getComputation().setProgramName(fileUpload.getClientFileName());
				}

				iGenomicService.update(containerForm.getModelObject().getComputation(), uploadData, checksum);
				this.info("Computation " + containerForm.getModelObject().getComputation().getName() + " was updated successfully.");
			}
			target.add(uploadButton);
			processErrors(target);
			onSavePostProcess(target);
		} catch (Exception e) {
			log.error("Error in computation entity ", e);
			this.error("A system error occurred. Please contact the system administrator.");
			processErrors(target);
		}
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {

			Computation computation = containerForm.getModelObject().getComputation();

			// int analysisCount =
			// iGenomicService.getAnalysisCount(computation.getId());

			// if(analysisCount == 0){
			if (computation.getStatus() == null) {
				iGenomicService.delete(computation);
				ComputationVo computaionVo = new ComputationVo();
				containerForm.setModelObject(computaionVo);
				containerForm.info("Computation was deleted successfully.");
				editCancelProcess(target);
			} else {
				containerForm.error("Computation package is already uploaded.");
				processErrors(target);
			}
		} catch (Exception e) {
			log.error("Error in deleting computation entity ", e);
			containerForm.error("An unexpected error occurred. Please contact the system administrator.");
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

	private ComputationVo getFormModelObject() {
		return containerForm.getModelObject();
	}

	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(computationIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(computationNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(computationDescTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(computationStatusTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(microServicesDDC);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileUploadField);
		arkCrudContainerVO.getDetailPanelFormContainer().add(clearButton);
		arkCrudContainerVO.getDetailPanelFormContainer().add(deleteButton);
		arkCrudContainerVO.getDetailPanelFormContainer().add(fileNameLbl);
		arkCrudContainerVO.getDetailPanelFormContainer().add(availableChkBox);
	}

}

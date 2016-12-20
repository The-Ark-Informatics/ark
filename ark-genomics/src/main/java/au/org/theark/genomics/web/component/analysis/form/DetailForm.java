package au.org.theark.genomics.web.component.analysis.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
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

import au.org.theark.core.model.spark.entity.Analysis;
import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.genomics.jobs.AnalysisExecutor;
import au.org.theark.genomics.jobs.QueueExecutor;
import au.org.theark.genomics.model.vo.AnalysisVo;
import au.org.theark.genomics.service.IGenomicService;
import au.org.theark.genomics.util.Constants;

public class DetailForm extends AbstractDetailForm<AnalysisVo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = org.slf4j.LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = Constants.GENOMIC_SERVICE)
	private IGenomicService iGenomicService;

	private ArkCrudContainerVO arkCrudContainerVO;

	private TextField<String> analysisIdTxtFld;
	private TextField<String> analysisNameTxtFld;
	private TextArea<String> analysisDescTxtArea;

	private Label analysisStatusLbl;
	private Label analysisJobIdLbl;

	private DropDownChoice<MicroService> microServicesDDC;
	private DropDownChoice<DataSource> dataSourcesDDC;
	private DropDownChoice<Computation> computationsDDC;

	private TextArea<String> parametersTxtArea;
	private TextField<String> resultTxtFld;

	private FileUploadField fileUploadField;
	private AjaxButton clearButton;
	private AjaxButton deleteButton;
	private Label fileNameLbl;

	private List<MicroService> microServiceList;

	private AjaxButton executeButton;
	private AjaxButton resultButton;
	private AjaxButton jobButton;
	private AjaxButton queueButton;

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

		this.analysisIdTxtFld = new TextField<String>(Constants.ANALYIS_ID);
		this.analysisIdTxtFld.setEnabled(false);
		this.analysisNameTxtFld = new TextField<String>(Constants.ANALYIS_NAME);

		this.analysisDescTxtArea = new TextArea<String>(Constants.ANALYIS_DESCRIPTION);
		this.analysisStatusLbl = new Label(Constants.ANALYIS_STATUS);
		this.analysisStatusLbl.setOutputMarkupId(true);
		// this.analysisStatusLbl.setEnabled(false);

		this.parametersTxtArea = new TextArea<String>(Constants.ANALYIS_PARAMETERS);
		this.resultTxtFld = new TextField<String>(Constants.ANALYIS_RESULT);

		this.analysisJobIdLbl = new Label(Constants.ANALYIS_JOB_ID);
		this.analysisJobIdLbl.setOutputMarkupId(true);
		// this.analysisJobIdLbl.setEnabled(false);

		this.initMicroServiceDropDown();
		this.initDataSourceDropDown();
		this.initComputationDropDown();

		fileUploadField = new FileUploadField("file");
		// fileUploadField.setOutputMarkupId(true);

		fileNameLbl = new Label(Constants.ANALYIS_SCRIPT_NAME);
		fileNameLbl.setOutputMarkupId(true);

		this.clearButton = new AjaxButton("clearButton") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				fileUploadField.clearInput();
				target.add(fileUploadField);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				fileUploadField.clearInput();
				target.add(fileUploadField);
			}
		};
		clearButton.add(new AttributeModifier("title", new Model<String>("Clear Attachment")));

		this.deleteButton = new AjaxButton("deleteButton") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				containerForm.getModelObject().getAnalysis().setScriptName(null);
				// containerForm.getModelObject().getCorrespondence().setAttachmentFilename(null);
				this.setVisible(false);
				target.add(fileNameLbl);
				target.add(this);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				containerForm.getModelObject().getAnalysis().setScriptName(null);
				// containerForm.getModelObject().getCorrespondence().setAttachmentFilename(null);
				this.setVisible(false);
				target.add(fileNameLbl);
				target.add(this);
			}

			@Override
			public boolean isVisible() {
				return (containerForm.getModelObject().getAnalysis().getScriptName() != null) && !containerForm.getModelObject().getAnalysis().getScriptName().isEmpty();
			}
		};
		deleteButton.add(new AttributeModifier("title", new Model<String>("Delete Attachment")));
		deleteButton.setOutputMarkupId(true);

		this.executeButton = new AjaxButton("execute") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				try {

					Analysis analysis = getFormModelObject().getAnalysis();

					String processUID = iGenomicService.executeAnalysis(analysis);

					analysis.setStatus("Running");

					iGenomicService.saveOrUpdate(analysis);

					AnalysisExecutor executor = new AnalysisExecutor(analysis, processUID, iGenomicService);

					executor.run();

				} catch (Exception e) {
					this.error("Execution failled");
					e.printStackTrace();
				}

				target.add(analysisStatusLbl);
				target.add(feedBackPanel);
			}
		};
		this.executeButton.setVisible(false);

		this.resultButton = new AjaxButton("result") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {
					Analysis analysis = getFormModelObject().getAnalysis();
					byte[] data = iGenomicService.getAnalysisResult(analysis);
					String output = null;
					if (analysis.getResult() == null) {
						output = "" + analysis.getId() + "results.txt";
					} else {
						output = analysis.getResult();
					}
					getRequestCycle().scheduleRequestHandlerAfterCurrent(new au.org.theark.core.util.ByteDataResourceRequestHandler("", data, output));
				} catch (Exception e) {
					this.error("Results download failled");
					e.printStackTrace();
				}
				target.add(feedBackPanel);
				target.add(resultButton);
			}

			@Override
			public boolean isEnabled() {
				boolean enabled = false;
				Analysis analysis = getFormModelObject().getAnalysis();
				if (Constants.STATUS_COMPLETED.equalsIgnoreCase(analysis.getStatus())) {
					enabled = true;
				}
				return enabled;
			}

		};
		this.resultButton.setOutputMarkupId(true);

		this.jobButton = new AjaxButton("job") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {

					Analysis analysis = getFormModelObject().getAnalysis();

					String status = iGenomicService.submitJob(analysis);

					analysis.setStatus(status);

					iGenomicService.saveOrUpdate(analysis);

				} catch (Exception e) {
					this.error("Execution failled");
					e.printStackTrace();
				}

				target.add(analysisStatusLbl);
				target.add(feedBackPanel);
			}
		};
		this.jobButton.setVisible(false);

		this.queueButton = new AjaxButton("queue") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {

					Analysis analysis = getFormModelObject().getAnalysis();

					iGenomicService.submitToQueue(analysis);

					QueueExecutor executor = new QueueExecutor(analysis, iGenomicService);

					executor.run();

				} catch (Exception e) {
					this.error("Analysis Execution failled");
					e.printStackTrace();
				}

				target.add(analysisStatusLbl);
				target.add(analysisJobIdLbl);
				target.add(feedBackPanel);
				target.add(queueButton);
				target.add(resultButton);
			}

			@Override
			public boolean isEnabled() {
				boolean enabled = false;
				Analysis analysis = getFormModelObject().getAnalysis();
				if (analysis.getId() != null && Constants.STATUS_UNDEFINED.equalsIgnoreCase(analysis.getStatus())) {
					enabled = true;
				}
				return enabled;
			}
		};
		this.queueButton.setOutputMarkupId(true);

		addDetailFormComponents();
		attachValidators();
	}

	private void initMicroServiceDropDown() {
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		this.microServicesDDC = new DropDownChoice(Constants.ANALYIS_MICRO_SERVICE, this.microServiceList, defaultChoiceRenderer);
		this.microServicesDDC.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Analysis model = getFormModelObject().getAnalysis();
				model.setComputation(null);
				model.setDataSource(null);
				dataSourcesDDC.setChoices(iGenomicService.searchDataSources(model.getMicroService()));
				computationsDDC.setChoices(iGenomicService.searchComputation(model.getMicroService()));
				target.add(dataSourcesDDC);
				target.add(computationsDDC);
			}

			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e) {

			}
		});

	}

	private void initDataSourceDropDown() {
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.PATH, Constants.ID);
		this.dataSourcesDDC = new DropDownChoice<DataSource>(Constants.ANALYIS_DATA_SOURCE, new ArrayList<DataSource>(), defaultChoiceRenderer);

	}

	private void initComputationDropDown() {
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		this.computationsDDC = new DropDownChoice<Computation>(Constants.ANALYIS_COMPUTAION, new ArrayList<Computation>(), defaultChoiceRenderer);
		this.computationsDDC.setOutputMarkupId(true);
	}

	@Override
	protected void attachValidators() {
		analysisNameTxtFld.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_ANALYSIS_NAME_REQUIRED, analysisNameTxtFld, new Model<String>(Constants.ERROR_ANALYSIS_NAME_TAG)));
		microServicesDDC.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_ANALYSIS_MICROSERVICE_REQUIRED, microServicesDDC, new Model<String>(Constants.ERROR_ANALYSIS_MICROSERVICE_TAG)));
//		computationsDDC.setRequired(true).setLabel(new StringResourceModel(Constants.ERROR_ANALYSIS_COMPUTATION_REQUIRED, computationsDDC, new Model<String>(Constants.ERROR_ANALYSIS_COMPUTATION_TAG)));

	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		AnalysisVo analysisVo = new AnalysisVo();
		containerForm.setModelObject(analysisVo);
	}

	@Override
	protected void onSave(Form<AnalysisVo> containerForm, AjaxRequestTarget target) {
		// iGenomicService.saveOrUpdate(containerForm.getModelObject().getAnalysis());
		try {
			if (containerForm.getModelObject().getAnalysis().getId() == null) {

				byte[] uploadData = null;

				if (fileUploadField != null && fileUploadField.getFileUpload() != null) {
					FileUpload fileUpload = fileUploadField.getFileUpload();

					byte[] byteArray = fileUpload.getMD5();
					String checksum = getHex(byteArray);
					uploadData = fileUpload.getBytes();
					containerForm.getModelObject().getAnalysis().setScriptName(fileUpload.getClientFileName());
					containerForm.getModelObject().getAnalysis().setChecksum(checksum);
				}

				this.iGenomicService.save(containerForm.getModelObject().getAnalysis(), uploadData);
				this.info("Computation " + containerForm.getModelObject().getAnalysis().getName() + " was created successfully");
			} else {

				String checksum = null;
				byte[] uploadData = null;

				if (fileUploadField != null && fileUploadField.getFileUpload() != null) {
					// retrieve file and store as Blob in database
					FileUpload fileUpload = fileUploadField.getFileUpload();

					byte[] byteArray = fileUpload.getMD5();
					checksum = getHex(byteArray);
					uploadData = fileUpload.getBytes();
					containerForm.getModelObject().getAnalysis().setScriptName(fileUpload.getClientFileName());
				}

				iGenomicService.update(containerForm.getModelObject().getAnalysis(), uploadData, checksum);
				this.info("Computation " + containerForm.getModelObject().getAnalysis().getName() + " was updated successfully");
			}
			target.add(queueButton);
			processErrors(target);
			onSavePostProcess(target);
			target.add(arkCrudContainerVO.getEditButtonContainer());
		} catch (Exception e) {
			log.error("Error in saving micro service entity ", e);
			this.error("A System error occured, we will have someone contact you.");
			processErrors(target);
		}
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		Analysis analysis = containerForm.getModelObject().getAnalysis();
		if (Constants.STATUS_UNDEFINED.equals(analysis.getStatus())) {
			iGenomicService.delete(analysis);
			containerForm.info("Analysis was deleted successfully.");
			editCancelProcess(target);
		} else {
			containerForm.error("Unable to delete analysis submitted to process");
			processErrors(target);
		}

	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getAnalysis().getId() == null) {
			return true;
		} else {
			return false;
		}
	}

	private AnalysisVo getFormModelObject() {
		return containerForm.getModelObject();
	}

	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(analysisIdTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(analysisNameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(analysisDescTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(analysisStatusLbl);
		arkCrudContainerVO.getDetailPanelFormContainer().add(microServicesDDC);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dataSourcesDDC);
		arkCrudContainerVO.getDetailPanelFormContainer().add(computationsDDC);
		// arkCrudContainerVO.getDetailPanelFormContainer().add(parametersTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resultTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(analysisJobIdLbl);

		// arkCrudContainerVO.getDetailPanelFormContainer().add(fileUploadField);
		// arkCrudContainerVO.getDetailPanelFormContainer().add(clearButton);
		// arkCrudContainerVO.getDetailPanelFormContainer().add(deleteButton);
		// arkCrudContainerVO.getDetailPanelFormContainer().add(fileNameLbl);

		arkCrudContainerVO.getEditButtonContainer().add(executeButton);
		arkCrudContainerVO.getEditButtonContainer().add(resultButton);
		arkCrudContainerVO.getEditButtonContainer().add(jobButton);
		arkCrudContainerVO.getEditButtonContainer().add(queueButton);
	}

}

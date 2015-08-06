package au.org.theark.genomics.web.component.analysis.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;

import au.org.theark.core.model.spark.entity.Analysis;
import au.org.theark.core.model.spark.entity.Computation;
import au.org.theark.core.model.spark.entity.DataSource;
import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.genomics.jobs.AnalysisExecutor;
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

	private TextField<String> analysisStatusTxtFld;

	private DropDownChoice<MicroService> microServicesDDC;
	private DropDownChoice<DataSource> dataSourcesDDC;
	private DropDownChoice<Computation> computationsDDC;

	private TextArea<String> parametersTxtArea;
	private TextField<String> resultTxtFld;

	private List<MicroService> microServiceList;

	private AjaxButton executeButton;
	private AjaxButton resultButton;

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
		this.analysisStatusTxtFld = new TextField<String>(Constants.ANALYIS_STATUS);
		this.analysisStatusTxtFld.setEnabled(false);

		this.parametersTxtArea = new TextArea<String>(Constants.ANALYIS_PARAMETERS);
		this.resultTxtFld = new TextField<String>(Constants.ANALYIS_RESULT);

		this.initMicroServiceDropDown();
		this.initDataSourceDropDown();
		this.initComputationDropDown();

		this.executeButton = new AjaxButton("execute") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				// super.onSubmit(target, form);

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

				target.add(analysisStatusTxtFld);
				target.add(feedBackPanel);
			}
		};

		resultButton = new AjaxButton("result") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Analysis analysis = getFormModelObject().getAnalysis();
				byte[] data = iGenomicService.getAnalysisResult(analysis);
				String output = null;
				if (analysis.getResult() == null) {
					output = "" + analysis.getId() + "results.txt";
				} else {
					output = analysis.getResult();
				}
				getRequestCycle().scheduleRequestHandlerAfterCurrent(new au.org.theark.core.util.ByteDataResourceRequestHandler("", data, output));
			}
		};

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
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		this.dataSourcesDDC = new DropDownChoice<DataSource>(Constants.ANALYIS_DATA_SOURCE, new ArrayList<DataSource>(), defaultChoiceRenderer);

	}

	private void initComputationDropDown() {
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		this.computationsDDC = new DropDownChoice<Computation>(Constants.ANALYIS_COMPUTAION, new ArrayList<Computation>(), defaultChoiceRenderer);
	}

	@Override
	protected void attachValidators() {
		// microServiceNameTxtFld.setRequired(true).setLabel(new
		// StringResourceModel(Constants.ERROR_MICRO_SERVICE_NAME_REQUIRED,
		// microServiceNameTxtFld, new
		// Model<String>(Constants.ERROR_MICRO_SERVICE_NAME_TAG)));
		// microServiceTxtArea.setRequired(true).setLabel(new
		// StringResourceModel(Constants.ERROR_MICRO_SERVICE_URL_REQUIRED,
		// microServiceNameTxtFld, new
		// Model<String>(Constants.ERROR_MICRO_SERVICE_URL_TAG)));
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
		AnalysisVo analysisVo = new AnalysisVo();
		containerForm.setModelObject(analysisVo);
	}

	@Override
	protected void onSave(Form<AnalysisVo> containerForm, AjaxRequestTarget target) {
		iGenomicService.saveOrUpdate(containerForm.getModelObject().getAnalysis());
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		// try {
		//
		// iGenomicService.delete(containerForm.getModelObject().getComputation());
		// ComputationVo computaionVo = new ComputationVo();
		// containerForm.setModelObject(computaionVo);
		// containerForm.info("Computation was deleted successfully.");
		// editCancelProcess(target);
		//
		// } catch (Exception e) {
		// containerForm.error("A System Error has occured please contact support.");
		// processErrors(target);
		// }

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
		arkCrudContainerVO.getDetailPanelFormContainer().add(analysisStatusTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(microServicesDDC);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dataSourcesDDC);
		arkCrudContainerVO.getDetailPanelFormContainer().add(computationsDDC);
		arkCrudContainerVO.getDetailPanelFormContainer().add(parametersTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(resultTxtFld);

		arkCrudContainerVO.getEditButtonContainer().add(executeButton);
		arkCrudContainerVO.getEditButtonContainer().add(resultButton);
	}

}

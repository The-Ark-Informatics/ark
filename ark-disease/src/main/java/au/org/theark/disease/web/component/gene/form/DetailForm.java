package au.org.theark.disease.web.component.gene.form;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.disease.entity.Gene;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkDiseaseService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.GeneVO;
import au.org.theark.core.web.form.AbstractDetailForm;

public class DetailForm extends AbstractDetailForm<GeneVO> {

	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(DetailForm.class);

	private WebMarkupContainer arkContextMarkupContainer;

	private TextField<String> name;
	
	@SpringBean(name = Constants.ARK_DISEASE_SERVICE)
	private IArkDiseaseService iArkDiseaseService;

	public DetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.arkContextMarkupContainer = arkContextContainer;
	}

	@Override
	public void onBeforeRender() {
		if(!isNew()) deleteButton.setVisible(true);
		super.onBeforeRender();
	}
	
	@SuppressWarnings("unchecked")
	public void initialiseDetailForm() {

		name = new TextField<String>("gene.name");
		
		attachValidators();
		addDetailFormComponents();

		deleteButton.setVisible(false);
	}
	
	@Override
	protected void attachValidators() {
		name.setRequired(true).setLabel(new StringResourceModel("gene.name.required", this, null));
	}
	
	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(name);
	}
	
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		GeneVO geneVO = new GeneVO();
		geneVO.getGene().setStudy(study);
		containerForm.setModelObject(geneVO);
	}
	
	@Override
	protected void onSave(Form<GeneVO> containerForm, AjaxRequestTarget target) {
		target.add(arkCrudContainerVO.getDetailPanelContainer());
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.STUDY_CONTEXT_ID);
		if(sessionStudyId == null) {
			// No study in context
			this.error("There is no study in Context. Please select a study to manage genes.");
			processErrors(target);
		} else {
			ContextHelper contextHelper = new ContextHelper();
			contextHelper.resetContextLabel(target, arkContextMarkupContainer);
	
//			arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
			
			Gene gene = containerForm.getModelObject().getGene();
			gene.setStudy(iArkCommonService.getStudy(sessionStudyId));
			
			log.info("name: " + gene.getName());
			
			if(isNew()) {
				iArkDiseaseService.save(gene);
			} else {
				iArkDiseaseService.update(gene);
			}
		}
		
	}
	
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		Gene gene = containerForm.getModelObject().getGene();
		if(gene != null) {
			iArkDiseaseService.delete(gene);
		}
		editCancelProcess(target);
		onCancel(target);
	}
	
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}
	
	@Override
	protected boolean isNew() {
		return containerForm.getModelObject().getGene().getId() == null;
	}
}

package au.org.theark.admin.web.component.audit.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.audit.entity.AuditEntity;
import au.org.theark.core.model.audit.entity.AuditField;
import au.org.theark.core.model.audit.entity.AuditPackage;
import au.org.theark.core.service.IAuditService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.AuditVO;
import au.org.theark.core.web.form.AbstractSearchForm;

public class SearchForm extends AbstractSearchForm<AuditVO> {
	
	static Logger log = LoggerFactory.getLogger(SearchForm.class);
	
	@SpringBean(name = Constants.ARK_AUDIT_SERVICE)
	private IAuditService iAuditService;
	
	private static final long serialVersionUID = 1L;
	
	private CompoundPropertyModel<AuditVO> cpModel;
	private ArkCrudContainerVO arkCrudContainerVO;
	private ContainerForm containerForm;
	private FeedbackPanel feedbackPanel;
	
	private DropDownChoice<AuditPackage> modulesSelection;
	private DropDownChoice<AuditEntity> entitiesSelection;
	private DropDownChoice<AuditField> fieldSelection;
	
	public SearchForm(String id, CompoundPropertyModel<AuditVO> cpModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedbackPanel, ContainerForm containerForm) {
		super(id, cpModel, feedbackPanel, arkCrudContainerVO);
		
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedbackPanel;
		setMultiPart(true);
		
		this.cpModel = cpModel;
		
		initialiseSearchForm();
	}
	
	public void initialiseSearchForm() {
		List<AuditPackage> arkModules = iAuditService.getAuditPackageList();
		ChoiceRenderer<AuditPackage> arkModuleChoiceRenderer = new ChoiceRenderer<AuditPackage>(Constants.NAME, Constants.ID);

		modulesSelection = new DropDownChoice<AuditPackage>("modules", new Model<AuditPackage>(), arkModules, arkModuleChoiceRenderer);
		modulesSelection.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				log.info("Onchange Package: " + modulesSelection.getModelObject());
				AuditPackage auditPackage = modulesSelection.getModelObject();
				if(auditPackage != null) {
					List<AuditEntity> auditEntities = new ArrayList<AuditEntity>();
					auditEntities.addAll(auditPackage.getAuditEntities());
					Collections.sort(auditEntities);
					entitiesSelection.setChoices(auditEntities);
					entitiesSelection.setEnabled(true);
				} else {
					entitiesSelection.setChoices(new ArrayList<AuditEntity>());
					entitiesSelection.setEnabled(false);
				}
				target.add(entitiesSelection);
			}
		});
		
		List<AuditEntity> auditEntities = new ArrayList<AuditEntity>();
		ChoiceRenderer<AuditEntity> auditEntityChoiceRenderer = new ChoiceRenderer<AuditEntity>(Constants.NAME, Constants.ID);
		
		entitiesSelection = new DropDownChoice<AuditEntity>("entities", new Model<AuditEntity>(), auditEntities, auditEntityChoiceRenderer);
		entitiesSelection.setOutputMarkupId(true);
		entitiesSelection.setEnabled(false);
		entitiesSelection.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				AuditEntity auditEntity = entitiesSelection.getModelObject();
				log.info("Onchange Entity: " + auditEntity);
				if(auditEntity != null) {
					List<AuditField> auditFields = new ArrayList<AuditField>();
					auditFields.addAll(auditEntity.getAuditFields());
					Collections.sort(auditFields);
					fieldSelection.setChoices(auditFields);
					fieldSelection.setEnabled(true);
				} else {
					fieldSelection.setChoices(new ArrayList<AuditField>());
					fieldSelection.setEnabled(false);
				}
				target.add(fieldSelection);
			}
		});
		
		List<AuditField> auditFields = new ArrayList<AuditField>();
		ChoiceRenderer<AuditField> auditFieldChoiceRenderer = new ChoiceRenderer<AuditField>(Constants.NAME, Constants.ID);
		
		fieldSelection = new DropDownChoice<AuditField>("fields", new Model<AuditField>(), auditFields, auditFieldChoiceRenderer);
		fieldSelection.setOutputMarkupId(true);
		fieldSelection.setEnabled(false);
		
		addComponents();
	}
	
	protected void addComponents() {		
		add(modulesSelection);
		add(entitiesSelection);
		add(fieldSelection);
	}
	
	@Override
	protected void onSearch(AjaxRequestTarget target) {
		containerForm.getModelObject().setAuditPackage(modulesSelection.getModelObject());
		containerForm.getModelObject().setAuditEntity(entitiesSelection.getModelObject());
		containerForm.getModelObject().setAuditField(fieldSelection.getModelObject());
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		
	}
}

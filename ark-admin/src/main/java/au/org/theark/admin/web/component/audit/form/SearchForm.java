package au.org.theark.admin.web.component.audit.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.envers.RevisionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.audit.entity.AuditEntity;
import au.org.theark.core.model.audit.entity.AuditField;
import au.org.theark.core.model.audit.entity.AuditPackage;
import au.org.theark.core.service.IAuditService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.AuditVO;
import au.org.theark.core.web.component.ArkDatePicker;
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
	
	private TextField<Integer> revisionNumberTxtFld;
	private DropDownChoice<RevisionType> revisionTypeSelection;
	private TextField<String> revisedByTxtFld;
	private DateTextField revisedDateTxtFld;
	private TextField<Long> idTxtFld;
	
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
		initAuditPackageDropdown();
		
		initAuditEntityDropdown();
		
		initAuditFieldDropdown();
		
		revisionNumberTxtFld = new TextField<Integer>("revisionNumber");
		
		revisionTypeSelection = new DropDownChoice<RevisionType>("revisionType", Arrays.asList(RevisionType.values()));
		
		revisedByTxtFld = new TextField<String>("revisedBy");
		
		revisedDateTxtFld = new DateTextField("revisedDate", Constants.DD_MM_YYYY);
		ArkDatePicker revisedDatePicker = new ArkDatePicker();
		revisedDatePicker.bind(revisedDateTxtFld);
		revisedDateTxtFld.add(revisedDatePicker);
		
		idTxtFld = new TextField<Long>("entityID");
		
		addComponents();
	}
	
	private void initAuditPackageDropdown() {
		List<AuditPackage> arkModules = iAuditService.getAuditPackageList();
		ChoiceRenderer<AuditPackage> arkModuleChoiceRenderer = new ChoiceRenderer<AuditPackage>(Constants.NAME, Constants.ID);

		modulesSelection = new DropDownChoice<AuditPackage>("auditPackage", arkModules, arkModuleChoiceRenderer);
		modulesSelection.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				AuditPackage auditPackage = modulesSelection.getModelObject();
				List<AuditEntity> auditEntities = new ArrayList<AuditEntity>();
				if(auditPackage != null) {
					auditEntities.addAll(auditPackage.getAuditEntities());
					Collections.sort(auditEntities);
					entitiesSelection.setEnabled(true);
					entitiesSelection.setModelObject(null);
				} else {
					entitiesSelection.setEnabled(false);
				}
				entitiesSelection.setChoices(auditEntities);
				target.add(entitiesSelection);
			}
		});
	}
	
	private void initAuditEntityDropdown() {
		List<AuditEntity> auditEntities = new ArrayList<AuditEntity>();
		ChoiceRenderer<AuditEntity> auditEntityChoiceRenderer = new ChoiceRenderer<AuditEntity>(Constants.NAME, Constants.ID);
		
		entitiesSelection = new DropDownChoice<AuditEntity>("auditEntity", auditEntities, auditEntityChoiceRenderer);
		entitiesSelection.setOutputMarkupId(true);
		entitiesSelection.setEnabled(false);
		entitiesSelection.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				AuditEntity auditEntity = entitiesSelection.getModelObject();
				List<AuditField> auditFields = new ArrayList<AuditField>();
				if(auditEntity != null) {
					auditFields.addAll(auditEntity.getAuditFields());
					Collections.sort(auditFields);
					fieldSelection.setEnabled(true);
					fieldSelection.setModelObject(null);
				} else {
					fieldSelection.setEnabled(false);
				}
				fieldSelection.setChoices(auditFields);
				target.add(fieldSelection);
			}
		});
	}
	
	private void initAuditFieldDropdown() {
		List<AuditField> auditFields = new ArrayList<AuditField>();
		ChoiceRenderer<AuditField> auditFieldChoiceRenderer = new ChoiceRenderer<AuditField>(Constants.NAME, Constants.ID);
		
		fieldSelection = new DropDownChoice<AuditField>("auditField", auditFields, auditFieldChoiceRenderer);
		fieldSelection.setOutputMarkupId(true);
		fieldSelection.setEnabled(false);
	}
	
	protected void addComponents() {		
		add(modulesSelection);
		add(entitiesSelection);
		add(fieldSelection);
		add(revisionNumberTxtFld);
		add(revisionTypeSelection);
		add(revisedByTxtFld);
		add(revisedDateTxtFld);
		add(idTxtFld);
	}
	
	@Override
	protected void onSearch(AjaxRequestTarget target) {
		log.info("" + containerForm.getModelObject());
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		
	}
}

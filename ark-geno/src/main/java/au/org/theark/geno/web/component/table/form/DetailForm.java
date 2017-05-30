package au.org.theark.geno.web.component.table.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.geno.entity.Beam;
import au.org.theark.core.model.geno.entity.Data;
import au.org.theark.core.model.geno.entity.Row;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.core.web.component.customfield.dataentry.DropDownChoiceDataEntryPanel;
import au.org.theark.core.web.component.customfield.dataentry.EncodedValueVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.geno.model.vo.RowListVO;
import au.org.theark.geno.model.vo.RowVO;
import au.org.theark.geno.service.IArkGenoService;
import au.org.theark.geno.web.component.tableeditor.TableEditorContainerPanel;
import au.org.theark.study.service.IStudyService;

public class DetailForm extends AbstractDetailForm<RowListVO>{

	private static final long serialVersionUID = 1L;

	static Logger log = LoggerFactory.getLogger(DetailForm.class);
	
	private WebMarkupContainer arkContextMarkupContainer;
	
	private ListView<Beam> headers;
	private ListView<Row> row_data;
	
	@SpringBean(name = "arkGenoService")
	private IArkGenoService											iArkGenoService;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService										iArkCommonService;
	
	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService											studyService;

	private AjaxButton editorButton;
	protected AbstractDetailModalWindow modalWindow;
	private WebMarkupContainer listContainer;
	
	private List<String> userPermissions;
	private Study study;
	private Person person;
	
	public DetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, ContainerForm containerForm, final ArkCrudContainerVO arkCrudContainerVO) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.arkContextMarkupContainer = arkContextContainer;
		this.setOutputMarkupId(true);
		arkCrudContainerVO.setDetailPanelFormContainer(this);
	}
	
	
	
	@Override
	public void initialiseForm() {
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		person = null;
		try {
			person = studyService.getPerson(sessionPersonId);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		} catch (ArkSystemException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			this.error("There is no subject selected. Please select a subject.");
		}
		
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();

		Long arkFunctionID = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.ARK_FUNCTION_KEY);
		ArkFunction arkFunction = iArkCommonService.getArkFunctionById(arkFunctionID);
		
		Long arkModuleID = (Long) SecurityUtils.getSubject().getSession().getAttribute(Constants.ARK_MODULE_KEY);
		ArkModule arkModule = iArkCommonService.getArkModuleById(arkModuleID);

		study = iArkCommonService.getStudy(sessionStudyId);
		
				
        listContainer = new WebMarkupContainer("theContainer", containerForm.getModel());
        listContainer.setOutputMarkupId(true);
        
        add(listContainer);
		modalWindow = new AbstractDetailModalWindow("detailModalWindow") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				populateListViews();
				target.add(listContainer);
			}
		};

		editorButton = new AjaxButton("editor"){
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				modalWindow.setTitle("Edit Table");
				modalWindow.setContent(new TableEditorContainerPanel("content",modalWindow, arkContextMarkupContainer, arkContextMarkupContainer, modalWindow));
				modalWindow.show(target);
			}				
			
		};
		
		editorButton.setEnabled(false);
		
		userPermissions = new ArrayList<String>();
		try {
			String userRole = iArkCommonService.getUserRole((String) currentUser.getPrincipal(), arkFunction, arkModule, study);
			userPermissions = (List<String>) iArkCommonService.getArkRolePermission(arkFunction, userRole, arkModule);
			
			if(userPermissions.contains("CREATE") || userPermissions.contains("DELETE")) { //user has create or delete permissions 
				editorButton.setEnabled(true);
			}
			
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		} 

		populateListViews();

		addDetailFormComponents();
	}
	
	private void populateListViews() {
		
		headers = new ListView<Beam>("headers", (List<Beam>) iArkGenoService.getGenoTableBeam(study)) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(org.apache.wicket.markup.html.list.ListItem<Beam> item) {
				item.add(new Label("header", item.getModelObject().getName()));
			}
		};
		headers.setOutputMarkupId(true);

		row_data = new ListView<Row>("listView", (List<Row>) iArkGenoService.getGenoTableRows(study)) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Row> item) {
				final Row row = item.getModelObject();
				item.add(new Label("row", row.getName()));
				final List<Beam> beamList = (List<Beam>) iArkGenoService.getGenoTableBeam(study);
				item.add(new ListView<Beam>("nested", beamList){

					private static final long serialVersionUID = 1L;
					
					@Override
					protected void populateItem(final ListItem<Beam> beam_item) {
						final Beam beam = beam_item.getModelObject();
						Data data = iArkGenoService.getDataGivenRowandColumn(person, row, beam_item.getModelObject());
						if(data == null) {
							data = new Data(study, person, row, beam);
							if(!(beam.getDefaultValue() == null || beam.getDefaultValue().isEmpty())) {
								data.setValue(beam_item.getModelObject().getDefaultValue());								
							}
						} else {
							if(data.getValue() == null || data.getValue().isEmpty()) {
								if(!(beam.getDefaultValue() == null || beam.getDefaultValue().isEmpty())) {
									data.setValue(beam.getDefaultValue());								
								} else {
									data.setValue("");
								}
							}
						}
												
						List<String> encodeKeyValueList = Arrays.asList(beam.getEncodedValues().split(";"));
						ArrayList<EncodedValueVO> choiceList = new ArrayList<EncodedValueVO>();
						for (String keyValue : encodeKeyValueList) {
							String[] keyValueArray = keyValue.split("=", 2);
							EncodedValueVO encodedValueVo = new EncodedValueVO();
							encodedValueVo.setKey(keyValueArray[0]);
							encodedValueVo.setValue(keyValueArray[1]);
							choiceList.add(encodedValueVo);
						}									
						
						if((userPermissions.contains("UPDATE") || userPermissions.contains("CREATE") || userPermissions.contains("DELETE")) && userPermissions.contains("READ")) {
							ChoiceRenderer<EncodedValueVO> choiceRenderer = new ChoiceRenderer<EncodedValueVO>("value", "key");
							DropDownChoiceDataEntryPanel ddcPanel = new DropDownChoiceDataEntryPanel("dataValueEntryPanel", new PropertyModel<String>(data, "value"), new Model<String>(row.getName()), choiceList, choiceRenderer);
							final DropDownChoice<EncodedValueVO> ddc = (DropDownChoice<EncodedValueVO>) ddcPanel.get("ddcDataValue");
							ddc.add(new OnChangeAjaxBehavior() {
								
								private static final long serialVersionUID = 1L;
	
								@Override
								protected void onUpdate(AjaxRequestTarget target) {
									Data data = iArkGenoService.getDataGivenRowandColumn(person, row, beam);
									if(ddc.getModelObject() != null) {
										if(data == null) {
											data = new Data(study, person, row, beam);
										}
										data.setValue(ddc.getModelObject().getKey());
										iArkGenoService.saveOrUpdate(data);										
									} else {
										iArkGenoService.delete(data);
									}
								}
							});
							beam_item.add(ddcPanel);
						} else {
							String label = new String();
							for(EncodedValueVO evo : choiceList) {
								if(evo.getKey().equals(data.getValue())) {
									label = evo.getValue();
									break;
								}
							}
							beam_item.add(new Label("dataValueEntryPanel", label));
						}
					}
				});	
				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel<Object>() {
					
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		listContainer.addOrReplace(headers);
		listContainer.addOrReplace(row_data);
	}
	
	@Override
	protected void attachValidators() {
	}

	@Override
	protected void onCancel(AjaxRequestTarget target) {
	}

	@Override
	protected void onSave(Form<RowListVO> containerForm, AjaxRequestTarget target) {
	}

	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
	}

	@Override
	protected void processErrors(AjaxRequestTarget target) {
	}

	@Override
	protected boolean isNew() {
		return false;
	}

	@Override
	protected void addDetailFormComponents() {
		addOrReplace(listContainer);
		add(modalWindow);
		add(editorButton);
	}
}

package au.org.theark.admin.web.component.audit;

import java.lang.reflect.Method;
import java.util.Iterator;

import jxl.write.DateFormat;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.envers.RevisionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.web.component.audit.form.ContainerForm;
import au.org.theark.core.Constants;
import au.org.theark.core.audit.UsernameRevisionEntity;
import au.org.theark.core.model.audit.entity.AuditEntity;
import au.org.theark.core.model.audit.entity.AuditField;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IAuditService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDataProvider;

public class SearchResultsPanel extends Panel {

	protected transient Logger log = LoggerFactory.getLogger(SearchResultsPanel.class);
	
	private static final long serialVersionUID = 1L;
	private ContainerForm containerForm;
	
	@SpringBean(name = Constants.ARK_AUDIT_SERVICE)
	private IAuditService iAuditService;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void> iArkCommonService;
	
	private ArkDataProvider<Object, IAuditService> auditProvider;
	private DataView<Object> auditDataView;
	private DataView<Object> fldAuditDataView;
	private WebMarkupContainer resultsWMC = new WebMarkupContainer("resultsWMC");
	private WebMarkupContainer fldResultsWMC = new WebMarkupContainer("fldResultsWMC");

	private AjaxPagingNavigator pageNavigator; 
	private AjaxPagingNavigator fldPageNavigator;
	
	private transient Method fieldReadMethod;
	private transient Class<?> fieldReturnType;
	private transient Method fieldDisplayMethod;
	
	public SearchResultsPanel(String id, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.containerForm = containerForm;
		initialisePanel();
		addComponents();
		resultsWMC.setOutputMarkupId(true);
		fldResultsWMC.setOutputMarkupId(true);
	}
	
	@Override
	protected void onBeforeRender() {
		initialisePanel();
		
		AuditEntity auditEntity = containerForm.getModelObject().getAuditEntity();
		AuditField auditField = containerForm.getModelObject().getAuditField();
		resultsWMC.setVisible(auditEntity != null && auditField == null);
		fldResultsWMC.setVisible(auditField != null);
		super.onBeforeRender();
	}
	
	public void initialisePanel() {
		final AuditEntity auditEntity = containerForm.getModelObject().getAuditEntity();
		final AuditField auditField = containerForm.getModelObject().getAuditField();
		
		if(auditField != null) {
			try {
				final Class<?> entityClass = Class.forName(auditField.getAuditEntity().getClassIdentifier());

				auditProvider = new ArkDataProvider<Object, IAuditService>(iAuditService) {
					private static final long serialVersionUID = 1L;

					@Override
					public Iterator<?> iterator(int first, int count) {
						return service.getAllEntitiesForClass(containerForm.getModelObject(), first, count).iterator();
					}

					@Override
					public int size() {
						return service.getAllEntitiesCountForClass(containerForm.getModelObject()).intValue();
					}
				};
				
				fieldReadMethod = iAuditService.getFieldReadMethod(auditField.getFieldName(), entityClass);
				fieldReturnType = fieldReadMethod.getReturnType();
				fieldDisplayMethod = iAuditService.getEntityDisplayMethod(fieldReturnType);
				
				fldAuditDataView = new DataView<Object>("fldAuditList", auditProvider) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final Item<Object> item) {
						Object[] result = (Object[]) item.getModelObject();
						UsernameRevisionEntity usernameRevisionEntity = (UsernameRevisionEntity) result[1];
						RevisionType revisionType = (RevisionType) result[2];
						Object entity = result[0];
						item.add(new Label("fldRevNumber", "" + usernameRevisionEntity.getId()));
						item.add(new Label("fldRevDate", new DateFormat(Constants.DD_MM_YYYY_HH_MM_SS).getDateFormat().format(usernameRevisionEntity.getRevisionDate())));
						item.add(new Label("fldRevBy", usernameRevisionEntity.getUsername()));
						item.add(new Label("fldEnType", auditEntity.getName()));
						item.add(new Label("fldPrimaryKey", iAuditService.getEntityPrimaryKey(entity).toString()));
						item.add(new Label("fldType", auditField.getName()));
						item.add(new Label("fldPriKey", iAuditService.getEntityValue(entity, fieldDisplayMethod, fieldReadMethod)));

						if(usernameRevisionEntity.getId() > 1) {
							Long id = 0L;
							
							id = iAuditService.getEntityPrimaryKey(entity);
							Number currentRevision = new Integer(usernameRevisionEntity.getId());
							Object previousEntity = iAuditService.getPreviousEntity(id, entity, currentRevision);
							if(previousEntity != null) {
								item.add(new Label("prevPriKey", iAuditService.getEntityValue(previousEntity, fieldDisplayMethod, fieldReadMethod)));
							} else {
								item.add(new Label("prevPriKey", "N/A"));
							}
						} else {
							item.add(new Label("prevPriKey", "N/A"));
						}
						
						item.add(new Label("fldRevType", revisionType.toString()));
						
						item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {
							@Override
							public String getObject() {
								return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
							}
						}));
					}
				};
				
				fldAuditDataView.setItemsPerPage(iArkCommonService.getRowsPerPage());
				
				fldPageNavigator = new AjaxPagingNavigator("fldNavigator", fldAuditDataView){

					private static final long	serialVersionUID	= 1L;

					@Override
					protected void onAjaxEvent(AjaxRequestTarget target) {
						target.add(fldResultsWMC);
					}
				};				
				fldResultsWMC.addOrReplace(fldPageNavigator);
				fldResultsWMC.addOrReplace(fldAuditDataView);				

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if(auditEntity != null) {
			try {
				final Class<?> entityClass = Class.forName(auditEntity.getClassIdentifier());

				auditProvider = new ArkDataProvider<Object, IAuditService>(iAuditService) {
					private static final long serialVersionUID = 1L;

					@Override
					public Iterator<?> iterator(int first, int count) {
						return service.getAllEntitiesForClass(containerForm.getModelObject(), first, count).iterator();
					}

					@Override
					public int size() {
						return service.getAllEntitiesCountForClass(containerForm.getModelObject()).intValue();
					}
				};
				
				auditDataView = new DataView<Object>("auditList", auditProvider) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final Item<Object> item) {
						Object[] result = (Object[]) item.getModelObject();
						UsernameRevisionEntity usernameRevisionEntity = (UsernameRevisionEntity) result[1];
						RevisionType revisionType = (RevisionType) result[2];
						Object entity = result[0];
						item.add(new Label("revNumber", "" + usernameRevisionEntity.getId()));
						item.add(new Label("revDate", new DateFormat(Constants.DD_MM_YYYY_HH_MM_SS).getDateFormat().format(usernameRevisionEntity.getRevisionDate())));
						item.add(new Label("revBy", usernameRevisionEntity.getUsername()));
						item.add(new Label("enType", auditEntity.getName()));
						try {
							item.add(new Label("priKey", iAuditService.getEntityPrimaryKey(entity).toString()));
						} catch (Exception e) {
							e.printStackTrace();
							item.add(new Label("priKey", ""));
						}
						item.add(new Label("revType", revisionType.toString()));
						
						item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {
							@Override
							public String getObject() {
								return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
							}
						}));
					}
				};
				
				auditDataView.setItemsPerPage(iArkCommonService.getRowsPerPage());
				
				pageNavigator = new AjaxPagingNavigator("navigator", auditDataView){

					private static final long	serialVersionUID	= 1L;

					@Override
					protected void onAjaxEvent(AjaxRequestTarget target) {
						target.add(resultsWMC);
					}
				};				
				resultsWMC.addOrReplace(pageNavigator);
				resultsWMC.addOrReplace(auditDataView);				

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addComponents() {
		add(resultsWMC);
		add(fldResultsWMC);
	}
}

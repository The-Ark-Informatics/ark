package au.org.theark.admin.web.component.audit;

import java.lang.reflect.InvocationTargetException;
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
	private WebMarkupContainer resultsWMC = new WebMarkupContainer("resultsWMC");
	private AjaxPagingNavigator pageNavigator;

	
	public SearchResultsPanel(String id, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.containerForm = containerForm;
		initialisePanel();
		addComponents();
		resultsWMC.setOutputMarkupId(true);
	}
	
	@Override
	protected void onBeforeRender() {
		log.info("SearchResultsPanel.onBeforeRender");
		initialisePanel();
		final AuditEntity auditEntity = containerForm.getModelObject().getAuditEntity();
		resultsWMC.setVisible(auditEntity != null);
		super.onBeforeRender();
	}
	
	public void initialisePanel() {
		final AuditEntity auditEntity = containerForm.getModelObject().getAuditEntity();
				
		if(auditEntity != null) {
			try {
				final Class<?> entityClass = Class.forName(auditEntity.getClassIdentifier());
				log.info("Size: " + iAuditService.getAllEntitiesCountForClass(entityClass));

				auditProvider = new ArkDataProvider<Object, IAuditService>(iAuditService) {
					private static final long serialVersionUID = 1L;

					@Override
					public Iterator<?> iterator(int first, int count) {
						return service.getAllEntitiesForClass(entityClass, first, count).iterator();
					}

					@Override
					public int size() {
						return service.getAllEntitiesCountForClass(entityClass).intValue();
					}
				};
				
				auditDataView = new DataView<Object>("auditList", auditProvider) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final Item<Object> item) {
						Object[] result = (Object[]) item.getModelObject();
						UsernameRevisionEntity usernameRevisionEntity = (UsernameRevisionEntity) result[1];
						RevisionType revisionType = (RevisionType) result[2];
						item.add(new Label("revNumber", "" + usernameRevisionEntity.getId()));
						item.add(new Label("revDate", new DateFormat(Constants.DD_MM_YYYY_HH_MM_SS).getDateFormat().format(usernameRevisionEntity.getRevisionDate())));
						item.add(new Label("revBy", usernameRevisionEntity.getUsername()));
						item.add(new Label("enType", auditEntity.getName()));
						Object entity = result[0];
						try {
							item.add(new Label("priKey", entity.getClass().getMethod("getId", null).invoke(entity, null).toString()));
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
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
	}
}

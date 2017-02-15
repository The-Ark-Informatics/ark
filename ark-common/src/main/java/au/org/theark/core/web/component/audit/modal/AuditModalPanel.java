package au.org.theark.core.web.component.audit.modal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import jxl.write.DateFormat;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.audit.UsernameRevisionEntity;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IAuditService;
import au.org.theark.core.web.component.ArkDataProvider;

public class AuditModalPanel extends Panel {

	class AuditRow implements Serializable {
		
		private UsernameRevisionEntity usernameRevisionEntity;
		private Object revisionProperty;
		private RevisionType revisionType;
		private String fieldName;
		
		public AuditRow(UsernameRevisionEntity usernameRevisionEntity, Object revisionProperty,
				RevisionType revisionType, String fieldName) {
			super();
			this.usernameRevisionEntity = usernameRevisionEntity;
			this.revisionProperty = revisionProperty;
			this.revisionType = revisionType;
			this.fieldName = fieldName;
		}
		
		public UsernameRevisionEntity getUsernameRevisionEntity() {
			return usernameRevisionEntity;
		}

		public void setUsernameRevisionEntity(UsernameRevisionEntity usernameRevisionEntity) {
			this.usernameRevisionEntity = usernameRevisionEntity;
		}

		public Object getRevisionProperty() {
			return revisionProperty;
		}

		public void setRevisionProperty(Object revisionProperty) {
			this.revisionProperty = revisionProperty;
		}

		public RevisionType getRevisionType() {
			return revisionType;
		}

		public void setRevisionType(RevisionType revisionType) {
			this.revisionType = revisionType;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
	}
		
	@SpringBean(name = Constants.ARK_AUDIT_SERVICE)
	private IAuditService iAuditService;
	
	@SpringBean(name = Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;
	
	private static final Logger log = LoggerFactory.getLogger(AuditModalPanel.class);
	
	private static final long serialVersionUID = 1L;

	private Object entity;
	private WebMarkupContainer masterContainer;	
	
	private WebMarkupContainer tableContainer;
	
	public AuditModalPanel(String id, Object entity, WebMarkupContainer masterContainer) {
		super(id);
		this.entity = entity;
		this.masterContainer = masterContainer;
		initialisePanel();
	}
	
	private void initialisePanel() {
		tableContainer = new WebMarkupContainer("tableContainer");
		tableContainer.setOutputMarkupId(true);
		log.info("history modal init panel");
		List<AuditRow> revisionEntities = new ArrayList<AuditRow>();
		AuditReader reader = iAuditService.getAuditReader();
		LinkedList<Component> list = new LinkedList<Component>();
		for(int i = 0; i < masterContainer.size(); i++) {
			Component component = masterContainer.get(i);				
			log.info(component.getId());
			list.add(component);
		}
		ListIterator<Component> componentIter = list.listIterator();
		while(componentIter.hasNext()) {
			Component component = componentIter.next();
			
			//If component is a WebMarkupContainer, and has children
			if(component instanceof WebMarkupContainer && ((WebMarkupContainer) component).size() > 0) {
				componentIter.remove();
				WebMarkupContainer container = (WebMarkupContainer) component;
				for(int i = 0; i < container.size(); i++) {
					componentIter.add(container.get(i));
				}
			}
		}
		for(Component component : list) {
			Object current = entity;
			for(String s : component.getId().split(Pattern.quote("."))) {
				try { 
					PropertyUtilsBean propertyBean = new PropertyUtilsBean();
					Object property = propertyBean.getNestedProperty(current, s);
					if(property != null && !reader.isEntityClassAudited(property.getClass())) {
						Object primaryKey = iAuditService.getEntityPrimaryKey(current);
						if(primaryKey != null && reader.isEntityClassAudited(current.getClass())) {
							List<Number> revisionNumbers = reader.getRevisions(current.getClass(), primaryKey);
							String fieldName = (s.equalsIgnoreCase("id") ? "ID" : iAuditService.getFieldName(current.getClass(), s));
							if(fieldName==null){
								log.info("Please add audit field "+s+" to table(Audit.audit_field) In Entity : "+current.getClass());
							}
							for(Number revision : revisionNumbers) {
								Object rev = reader.find(current.getClass(), primaryKey, revision);
								Object revProperty = propertyBean.getProperty(rev, s);
								Object[] result = (Object[]) reader.createQuery().forRevisionsOfEntity(current.getClass(), false, true)
													.add(AuditEntity.revisionNumber().eq(revision))
													.add(AuditEntity.id().eq(primaryKey))
													.getSingleResult();
								RevisionType type = (RevisionType) result[2];
								UsernameRevisionEntity ure = (UsernameRevisionEntity) result[1];
								revisionEntities.add(new AuditRow(ure, revProperty, type, fieldName));
							}
						}
				}
				 	current = property;
				} catch(NoSuchMethodException nsme) {
					//The current entity has no property named "s", move on to next "s"
					//TODO: find better way to catch this (i.e. use a if instead)
				} catch(Exception e) {
					//e.printStackTrace();
				}
			}
		}
		
		Collections.sort(revisionEntities, new Comparator<AuditRow>() {
			@Override
			public int compare(AuditRow o1, AuditRow o2) {
				return -o1.getUsernameRevisionEntity().getRevisionDate().compareTo(o2.getUsernameRevisionEntity().getRevisionDate());
			}
		});
		
		ArkDataProvider<AuditRow, IAuditService> dataProvider = new ArkDataProvider<AuditRow, IAuditService>(iAuditService) {

			@Override
			public Iterator<? extends AuditRow> iterator(
					int first, int count) {
				return revisionEntities.subList(first, first + count).listIterator();
			}

			@Override
			public int size() {
				return revisionEntities.size();
			}
			
		};
		
		DataView<AuditRow> dataView = new DataView<AuditRow>("table", dataProvider, iArkCommonService.getRowsPerPage()) {
			
			@Override
			protected void populateItem(Item<AuditRow> item) {
				UsernameRevisionEntity ure = item.getModelObject().getUsernameRevisionEntity();
				Object entity = item.getModelObject().getRevisionProperty();
				RevisionType type = item.getModelObject().getRevisionType();
				item.add(new Label("rev", new Integer(ure.getId()).toString()));
				item.add(new Label("revDate", new DateFormat(Constants.DD_MM_YYYY_HH_MM_SS).getDateFormat().format(ure.getRevisionDate())));
				item.add(new Label("revBy", ure.getUsername()));
				item.add(new Label("fieldName", item.getModelObject().getFieldName()));
				item.add(new Label("value", iAuditService.getEntityValue(entity)));
				item.add(new Label("revType", type.toString()));
				item.add(new AttributeModifier(Constants.CLASS, new AbstractReadOnlyModel() {

					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? Constants.EVEN : Constants.ODD;
					}
				}));
			}
		};
		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("pageNavigator", dataView) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(tableContainer);
			}
		};
		tableContainer.add(pageNavigator);
		
		tableContainer.add(dataView);
		add(tableContainer);
	}
}

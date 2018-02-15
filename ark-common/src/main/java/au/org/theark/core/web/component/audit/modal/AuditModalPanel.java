package au.org.theark.core.web.component.audit.modal;


import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.audit.UsernameRevisionEntity;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.lims.entity.BiospecimenCustomFieldData;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.model.pheno.entity.PhenoDataSetData;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.FamilyCustomFieldData;
import au.org.theark.core.model.study.entity.SubjectCustomFieldData;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.service.IAuditService;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.vo.FamilyCustomDataVO;
import au.org.theark.core.vo.LimsVO;
import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.core.vo.PhenoDataSetFieldVO;
import au.org.theark.core.vo.SubjectCustomDataVO;
import au.org.theark.core.web.component.ArkDataProvider;
import jxl.write.DateFormat;



public class AuditModalPanel extends Panel implements Serializable {

	class AuditRow implements Serializable {
		
		private UsernameRevisionEntity usernameRevisionEntity;
		private Object revisionProperty;
		private RevisionType revisionType;
		private String fieldName;
		
		public AuditRow(UsernameRevisionEntity usernameRevisionEntity, Object revisionProperty,RevisionType revisionType, String fieldName) {
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
	
	//private AjaxRequestTarget target;
	
	private FeedbackPanel feedbackPanel;
	
	
	
	public AuditModalPanel(String id, Object entity, WebMarkupContainer masterContainer) {
		super(id);
		//this.target=target;
		this.entity = entity;
		this.masterContainer = masterContainer;
		add(initialiseFeedBackPanel());
		initialisePanel();
	}
	private WebMarkupContainer initialiseFeedBackPanel() {
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}
	
	private void initialisePanel() {
		tableContainer = new WebMarkupContainer("tableContainer");
		tableContainer.setOutputMarkupId(true);
		log.info("history modal init panel");
		List<AuditRow> revisionEntities = new ArrayList<AuditRow>();
		AuditReader reader = iAuditService.getAuditReader();
		LinkedList<Component> list = new LinkedList<Component>();
		LinkedList<Component> dataList = new LinkedList<Component>();
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
				List<FormComponent<?>> formcomponents=new ArrayList<FormComponent<?>>();
				formcomponents=getListOfFormComponentInWebMarkupContainer(container,formcomponents);
				for (FormComponent<?> formComponent : formcomponents) {
					componentIter.add(formComponent);
				}
			}
		}
		

		for(Component component : list) {
			Object current = entity;
			//we can capture the PhenoDataSet Data and the Custom Field Data from the component name
			//which does not include the . for properties.
			//ARK-1791 We must ignore the nested properties like the category order number here.
			if(component.getId().contains(".") && StringUtils.countMatches(component.getId(), ".")==1){
				for(String s : component.getId().split(Pattern.quote("."))) {
					try { 
						PropertyUtilsBean propertyBean = new PropertyUtilsBean();
						
						Object property = propertyBean.getNestedProperty(current, s);
						//if(property != null && !reader.isEntityClassAudited(property.getClass())) {
						if(property != null ){
							Object primaryKey = iAuditService.getEntityPrimaryKey(current);
							if(primaryKey != null && reader.isEntityClassAudited(current.getClass())) {
								List<Number> revisionNumbers = reader.getRevisions(current.getClass(), primaryKey);
								String fieldName = (s.equalsIgnoreCase("id") ? "ID" : iAuditService.getFieldName(current.getClass(), s));
								if(fieldName==null){
									this.error("Please contact the system administrator; auditing for some fields("+s+") on this screen has not been properly configured.");
									log.error("Please contact system administrator need to add audit field "+s+" to table(Audit.audit_field) In Entity : "+current.getClass());
									setFeedbackPanel(feedbackPanel);
								}
								for(Number revision : revisionNumbers) {
									Object rev =reader.find(current.getClass(), primaryKey, revision);
									if(rev!=null){
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
						}
						//property becomes a current entity.
						current = property;
					} catch(NoSuchMethodException nsme) {
						//The current entity has no property named "s", move on to next "s"
						//TODO: find better way to catch this (i.e. use a if instead)
						//nsme.printStackTrace();
						log.info("Please check no property name called:"+s+"in "+current.toString());
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}else{
				log.info("Comp:"+component.getId());
			}
		}
		
		//Handling the history of pheno dataset.
		if(entity instanceof PhenoDataCollectionVO){
			PhenoDataCollectionVO phenoDataCollectionVO=((PhenoDataCollectionVO)entity);
			PhenoDataSetCategory phenoDataSetCategory=null;
			Set<Object> primaryKeyLst= new HashSet<Object>();
			if(phenoDataCollectionVO.getPhenoDataSetCollection()!=null){
				 //Set<PhenoDataSetData> phenoDataSetDatas=phenoDataSetCollection.getPhenoDataSetData();
				 List<PhenoDataSetData> phenoDataSetDatas=phenoDataCollectionVO.getPhenoFieldDataList();
					for (PhenoDataSetData phenoDataSetData : phenoDataSetDatas) {
							if(phenoDataSetData.getId()!=null){
								primaryKeyLst.add(phenoDataSetData.getId());
							}
					}
			}
			if(phenoDataCollectionVO.getPickedPhenoDataSetCategory()!=null && phenoDataCollectionVO.getPickedPhenoDataSetCategory().getPhenoDataSetCategory()!=null){
				phenoDataSetCategory=phenoDataCollectionVO.getPickedPhenoDataSetCategory().getPhenoDataSetCategory();
			}
			for (Object pKey : primaryKeyLst) {
				if(reader.isEntityClassAudited(PhenoDataSetData.class)) {
					List<Number> revisionNumbers = reader.getRevisions(PhenoDataSetData.class, pKey);
					for(Number revision : revisionNumbers) {
						PropertyUtilsBean propertyBean = new PropertyUtilsBean();
						Object rev =reader.find(PhenoDataSetData.class, pKey, revision);
						try {
							Object revProperty = pickDataValue(rev, propertyBean);
							Object fieldName = propertyBean.getProperty(rev, "phenoDataSetFieldDisplay");
							PhenoDataSetFieldDisplay phenoDataSetFieldDisplay=(PhenoDataSetFieldDisplay)fieldName;
							String fieldLabel=((phenoDataSetFieldDisplay.getPhenoDataSetField()!=null && phenoDataSetFieldDisplay.getPhenoDataSetField().getFieldLabel()!=null)?  phenoDataSetFieldDisplay.getPhenoDataSetField().getFieldLabel():phenoDataSetFieldDisplay.getPhenoDataSetField().getName());
							String unitTypeInText=((phenoDataSetFieldDisplay.getPhenoDataSetField()!=null)?phenoDataSetFieldDisplay.getPhenoDataSetField().getUnitTypeInText():"");;
							String dataWithUnit= revProperty.toString() +" "+(unitTypeInText!=null?unitTypeInText:"");
							Object[] result = (Object[]) reader.createQuery().forRevisionsOfEntity(PhenoDataSetData.class, false, true)
									.add(AuditEntity.revisionNumber().eq(revision))
									.add(AuditEntity.id().eq(pKey))
									.getSingleResult();
					RevisionType type = (RevisionType) result[2];
					UsernameRevisionEntity ure = (UsernameRevisionEntity) result[1];
					revisionEntities.add(new AuditRow(ure, dataWithUnit, type,fieldLabel+" ["+((phenoDataSetCategory!=null)?phenoDataSetCategory.getName():"All")+"]"));
						} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ObjectNotFoundException e) {
							this.error("There are some missing history during the data auditing. Please contact the administrator.");
							setFeedbackPanel(feedbackPanel);
						}
					}
				 }
			}
		//Handling the history of PhenoDataSetFiled values.		
		}else if(entity instanceof PhenoDataSetFieldVO){
			PhenoDataSetFieldVO phenoDataSetFieldVO=((PhenoDataSetFieldVO)entity);
			AuditQuery auditQuery = reader.createQuery().forRevisionsOfEntity(PhenoDataSetField.class, true, false)
					.add(AuditEntity.property("id").eq(phenoDataSetFieldVO.getPhenoDataSetField().getId()));
			List<Object> objects= auditQuery.getResultList();
			//Assigning to a set will automatically remove the duplicates.
			Set<Object> primaryKeyLst= new HashSet<Object>();
			for (Object object : objects) {
				primaryKeyLst.add(((PhenoDataSetField)object).getId());
			}
			for (Object pKey : primaryKeyLst) {
				if(reader.isEntityClassAudited(PhenoDataSetField.class)) {
					List<Number> revisionNumbers = reader.getRevisions(PhenoDataSetField.class, pKey);
					for(Number revision : revisionNumbers) {
						Object rev =reader.find(PhenoDataSetField.class, pKey, revision);
						Object[] result = (Object[]) reader.createQuery().forRevisionsOfEntity(PhenoDataSetField.class, false, true)
								.add(AuditEntity.revisionNumber().eq(revision))
								.add(AuditEntity.id().eq(pKey))
								.getSingleResult();
						pickAndSetFieldValue(revisionEntities,rev,result);
					}
				}
			}
		}else if(entity instanceof CustomFieldVO){
			CustomFieldVO customFieldVO=((CustomFieldVO)entity);
			AuditQuery auditQuery = reader.createQuery().forRevisionsOfEntity(CustomField.class, true, false)
					.add(AuditEntity.property("id").eq(customFieldVO.getCustomField().getId()));
			List<Object> objects= auditQuery.getResultList();
			//Assigning to a set will automatically remove the duplicates.
			Set<Object> primaryKeyLst= new HashSet<Object>();
			for (Object object : objects) {
				primaryKeyLst.add(((CustomField)object).getId());
			}for (Object pKey : primaryKeyLst) {
				if(reader.isEntityClassAudited(CustomField.class)) {
					List<Number> revisionNumbers = reader.getRevisions(CustomField.class, pKey);
					for(Number revision : revisionNumbers) {
						Object rev =reader.find(CustomField.class, pKey, revision);
						Object[] result = (Object[]) reader.createQuery().forRevisionsOfEntity(CustomField.class, false, true)
								.add(AuditEntity.revisionNumber().eq(revision))
								.add(AuditEntity.id().eq(pKey))
								.getSingleResult();
						pickAndSetFieldValue(revisionEntities,rev,result);
					}
				}
			}
		//Handling the History of subject custom field data.	
		}else if (entity instanceof SubjectCustomDataVO){
				SubjectCustomDataVO subjectCustomDataVO=((SubjectCustomDataVO)entity);
				CustomFieldCategory customFieldCategory=subjectCustomDataVO.getCustomFieldCategory();
				/*AuditQuery auditQuery = reader.createQuery().forRevisionsOfEntity(SubjectCustomFieldData.class, true, true)
						.add(AuditEntity.property("linkSubjectStudy").eq(subjectCustomDataVO.getLinkSubjectStudy()));
				List<Object> objects= auditQuery.getResultList();
				//Assigning to a set will automatically remove the duplicates.
				Set<Object> primaryKeyLst= new HashSet<Object>();
				for (Object object : objects) {
					primaryKeyLst.add(((SubjectCustomFieldData)object).getId());
				}*/
				List<SubjectCustomFieldData> subjectCustomFieldDatas =subjectCustomDataVO.getCustomFieldDataList();
				Set<Object> primaryKeyLst= new HashSet<Object>();
				for (SubjectCustomFieldData subjectCustomFieldData : subjectCustomFieldDatas) {
						if(subjectCustomFieldData.getId()!=null){
							primaryKeyLst.add(subjectCustomFieldData.getId());
						}
				}
				for (Object pKey : primaryKeyLst) {
					if(reader.isEntityClassAudited(SubjectCustomFieldData.class)) {
						List<Number> revisionNumbers = reader.getRevisions(SubjectCustomFieldData.class, pKey);
						for(Number revision : revisionNumbers) {
							PropertyUtilsBean propertyBean = new PropertyUtilsBean(); 
							Object rev =reader.find(SubjectCustomFieldData.class, pKey, revision);
							Object revProperty = pickDataValue(rev, propertyBean);
							try {
								Object fieldName = propertyBean.getProperty(rev, "customFieldDisplay");
							CustomFieldDisplay customFieldDisplay=(((CustomFieldDisplay)fieldName));
							String fieldLabel=((customFieldDisplay.getCustomField()!=null && customFieldDisplay.getCustomField().getFieldLabel()!=null)?  customFieldDisplay.getCustomField().getFieldLabel():customFieldDisplay.getCustomField().getName());
							String unitTypeInText=((customFieldDisplay.getCustomField()!=null)?customFieldDisplay.getCustomField().getUnitTypeInText():"");;
							String dataWithUnit= (revProperty!=null)?revProperty.toString():"" +" "+(unitTypeInText!=null?unitTypeInText:"");
							Object[] result = (Object[]) reader.createQuery().forRevisionsOfEntity(SubjectCustomFieldData.class, false, true)
									.add(AuditEntity.revisionNumber().eq(revision))
									.add(AuditEntity.id().eq(pKey))
									.getSingleResult();
							RevisionType type = (RevisionType) result[2];
							UsernameRevisionEntity ure = (UsernameRevisionEntity) result[1];
							revisionEntities.add(new AuditRow(ure, dataWithUnit, type,fieldLabel+" ["+((customFieldCategory!=null)?customFieldCategory.getName():"All")+"]"));
							} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ObjectNotFoundException e) {
								this.error("Audit information can not be displayed in some records.");
								setFeedbackPanel(feedbackPanel);
							}
						}
				}
			}
		//Do it for rest to capture category wise custom field data.
		//2017-02-03		
		}else if(entity instanceof FamilyCustomDataVO){
				FamilyCustomDataVO familyCustomDataVO=((FamilyCustomDataVO)entity);
				CustomFieldCategory customFieldCategory=familyCustomDataVO.getCustomFieldCategory();
				List<FamilyCustomFieldData> familyCustfieldDatalst =familyCustomDataVO.getCustomFieldDataList();
				Set<Object> primaryKeyLst= new HashSet<Object>();
				for (FamilyCustomFieldData familyCustomFieldData : familyCustfieldDatalst) {
						if(familyCustomFieldData.getId()!=null){
							primaryKeyLst.add(familyCustomFieldData.getId());
						}
				}
				for (Object pKey : primaryKeyLst) {
					if(reader.isEntityClassAudited(FamilyCustomFieldData.class)) {
						List<Number> revisionNumbers = reader.getRevisions(FamilyCustomFieldData.class, pKey);
						for(Number revision : revisionNumbers) {
							PropertyUtilsBean propertyBean = new PropertyUtilsBean(); 
							Object rev =reader.find(FamilyCustomFieldData.class, pKey, revision);
							Object revProperty = pickDataValue(rev, propertyBean);
							try {
								Object fieldName = propertyBean.getProperty(rev, "customFieldDisplay");
								CustomFieldDisplay customFieldDisplay=(((CustomFieldDisplay)fieldName));
									String fieldLabel=((customFieldDisplay.getCustomField()!=null && customFieldDisplay.getCustomField().getFieldLabel()!=null)?  customFieldDisplay.getCustomField().getFieldLabel():customFieldDisplay.getCustomField().getName());
									String unitTypeInText=((customFieldDisplay.getCustomField()!=null)?customFieldDisplay.getCustomField().getUnitTypeInText():"");;
									String dataWithUnit= (revProperty!=null)?revProperty.toString():"" +" "+(unitTypeInText!=null?unitTypeInText:"");
									Object[] result = (Object[]) reader.createQuery().forRevisionsOfEntity(FamilyCustomFieldData.class, false, true)
											.add(AuditEntity.revisionNumber().eq(revision))
											.add(AuditEntity.id().eq(pKey))
											.getSingleResult();
									RevisionType type = (RevisionType) result[2];
									UsernameRevisionEntity ure = (UsernameRevisionEntity) result[1];
									revisionEntities.add(new AuditRow(ure, dataWithUnit, type,fieldLabel+" ["+((customFieldCategory!=null)?customFieldCategory.getName():"All")+"]"));
							} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ObjectNotFoundException e) {
								this.error("Audit information can not be displayed in some records.");
								setFeedbackPanel(feedbackPanel);
							}
						}
				}
			}
		}
		//BioCollection custom field and Biospecimen custom field data.
		else if(entity instanceof LimsVO ){
			LimsVO limsVO=((LimsVO)entity);
			CustomFieldCategory customFieldCategory=limsVO.getCustomFieldCategory();
			List<BioCollectionCustomFieldData> bioCollectionCustomFieldDatas=limsVO.getBioCollectionCustomDataVO().getCustomFieldDataList();
			List<BiospecimenCustomFieldData> biospecimenCustomFieldDatas=limsVO.getBiospecimenCustomDataVO().getCustomFieldDataList();
			
			Set<Object> primaryKeyLst= new HashSet<Object>();
			//BioCollection part.
			if(bioCollectionCustomFieldDatas.size()>0){
				for (BioCollectionCustomFieldData bioCollectionCustomFieldData : bioCollectionCustomFieldDatas) {
						if(bioCollectionCustomFieldData.getId()!=null){
							primaryKeyLst.add(bioCollectionCustomFieldData.getId());
						}
				}
				for (Object pKey : primaryKeyLst) {
					if(reader.isEntityClassAudited(BioCollectionCustomFieldData.class)) {
						List<Number> revisionNumbers = reader.getRevisions(BioCollectionCustomFieldData.class, pKey);
						for(Number revision : revisionNumbers) {
							PropertyUtilsBean propertyBean = new PropertyUtilsBean(); 
							Object rev =reader.find(BioCollectionCustomFieldData.class, pKey, revision);
							if(rev!=null){
								Object revProperty = pickDataValue(rev, propertyBean);
								try {
									Object fieldName = propertyBean.getProperty(rev, "customFieldDisplay");
									CustomFieldDisplay customFieldDisplay=(((CustomFieldDisplay)fieldName));
										String fieldLabel=((customFieldDisplay.getCustomField()!=null && customFieldDisplay.getCustomField().getFieldLabel()!=null)?  customFieldDisplay.getCustomField().getFieldLabel():customFieldDisplay.getCustomField().getName());
										String unitTypeInText=((customFieldDisplay.getCustomField()!=null)?customFieldDisplay.getCustomField().getUnitTypeInText():"");;
										String dataWithUnit= (revProperty!=null)?revProperty.toString():"" +" "+(unitTypeInText!=null?unitTypeInText:"");
										Object[] result = (Object[]) reader.createQuery().forRevisionsOfEntity(BioCollectionCustomFieldData.class, false, true)
												.add(AuditEntity.revisionNumber().eq(revision))
												.add(AuditEntity.id().eq(pKey))
												.getSingleResult();
										RevisionType type = (RevisionType) result[2];
										UsernameRevisionEntity ure = (UsernameRevisionEntity) result[1];
										revisionEntities.add(new AuditRow(ure, dataWithUnit, type,fieldLabel+" ["+((customFieldCategory!=null)?customFieldCategory.getName():"All")+"]"));
								} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ObjectNotFoundException e) {
									this.error("Audit information can not be displayed in some records.");
									setFeedbackPanel(feedbackPanel);
								}
							}	
						}
				}
			}
		//BioSpeciment part.		
		}else if(biospecimenCustomFieldDatas.size() >0){
				for (BiospecimenCustomFieldData biospecimenCustomFieldData : biospecimenCustomFieldDatas) {
					if(biospecimenCustomFieldData.getId()!=null){
						primaryKeyLst.add(biospecimenCustomFieldData.getId());
					}
				}
				for (Object pKey : primaryKeyLst) {
					if(reader.isEntityClassAudited(BiospecimenCustomFieldData.class)) {
						List<Number> revisionNumbers = reader.getRevisions(BiospecimenCustomFieldData.class, pKey);
						for(Number revision : revisionNumbers) {
							PropertyUtilsBean propertyBean = new PropertyUtilsBean(); 
							Object rev =reader.find(BiospecimenCustomFieldData.class, pKey, revision);
							if(rev!=null){
								Object revProperty = pickDataValue(rev, propertyBean);
								try {
									Object fieldName = propertyBean.getProperty(rev, "customFieldDisplay");
									CustomFieldDisplay customFieldDisplay=(((CustomFieldDisplay)fieldName));
										String fieldLabel=((customFieldDisplay.getCustomField()!=null && customFieldDisplay.getCustomField().getFieldLabel()!=null)?  customFieldDisplay.getCustomField().getFieldLabel():customFieldDisplay.getCustomField().getName());
										String unitTypeInText=((customFieldDisplay.getCustomField()!=null)?customFieldDisplay.getCustomField().getUnitTypeInText():"");;
										String dataWithUnit= (revProperty!=null)?revProperty.toString():"" +" "+(unitTypeInText!=null?unitTypeInText:"");
										Object[] result = (Object[]) reader.createQuery().forRevisionsOfEntity(BiospecimenCustomFieldData.class, false, true)
												.add(AuditEntity.revisionNumber().eq(revision))
												.add(AuditEntity.id().eq(pKey))
												.getSingleResult();
										RevisionType type = (RevisionType) result[2];
										UsernameRevisionEntity ure = (UsernameRevisionEntity) result[1];
										revisionEntities.add(new AuditRow(ure, dataWithUnit, type,fieldLabel+" ["+((customFieldCategory!=null)?customFieldCategory.getName():"All")+"]"));
								} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ObjectNotFoundException e) {
									this.error("Audit information can not be displayed in some records.");
									setFeedbackPanel(feedbackPanel);
								}
						}
					}		
				}
			}
		}
	}else{
		for(Component component : list) {
			Object current = entity;
			
			String componentId="";
			
			String[] elements=component.getId().split(Pattern.quote("."));
			
			if(elements.length > 1){
				componentId= elements[elements.length - 2] +"."+elements[elements.length - 1];
			}
			if(componentId.contains(".") && StringUtils.countMatches(componentId, ".")==1){
				for(String s : componentId.split(Pattern.quote("."))) {
					try { 
						PropertyUtilsBean propertyBean = new PropertyUtilsBean();
						Object property = propertyBean.getNestedProperty(current, s);
						
						//TODO Should remove after ARK-1450 testing
						log.info("------------------------------------------- "+s);
						log.info("------------------------------------------- "+String.valueOf(current));
					
						//if(property != null && !reader.isEntityClassAudited(property.getClass())) {
						if(property != null ){
							Object primaryKey = iAuditService.getEntityPrimaryKey(current);
							if(primaryKey != null && reader.isEntityClassAudited(current.getClass())) {
								List<Number> revisionNumbers = reader.getRevisions(current.getClass(), primaryKey);
								String fieldName = (s.equalsIgnoreCase("id") ? "ID" : iAuditService.getFieldName(current.getClass(), s));
								if(fieldName==null){
									this.error("Please contact the system administrator; auditing for some fields("+s+") on this screen has not been properly configured.");
									log.error("Please contact system administrator need to add audit field "+s+" to table(Audit.audit_field) In Entity : "+current.getClass());
									setFeedbackPanel(feedbackPanel);
								}
								for(Number revision : revisionNumbers) {
									Object rev =reader.find(current.getClass(), primaryKey, revision);
									if(rev!=null){
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
						}else{
							log.error("----------------------------- Property value not specified ------------------------" + s);
						}
						//property becomes a current entity.
						current = property;
					} catch(NoSuchMethodException nsme) {
						//The current entity has no property named "s", move on to next "s"
						//TODO: find better way to catch this (i.e. use a if instead)
						nsme.printStackTrace();
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}else{
				log.info("Comp:"+component.getId());
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
				//Add the new modifier for the extra space when dataset available
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
	/**
	 * 
	 * @param webMarkupContainer
	 * @return
	 */
	private List<FormComponent<?>> getListOfFormComponentInWebMarkupContainer(WebMarkupContainer webMarkupContainer ,List<FormComponent<?>> formcomponents){
		
		for(int i = 0; i < webMarkupContainer.size(); i++) {
			Component thiscomponent=(Component)webMarkupContainer.get(i);
			if(thiscomponent instanceof FormComponent){
				formcomponents.add((FormComponent<?>)thiscomponent);
			}else if(thiscomponent instanceof WebMarkupContainer){
				getListOfFormComponentInWebMarkupContainer((WebMarkupContainer)thiscomponent,formcomponents);
			}	
		}
		return formcomponents;
	}
	/**
	 * 
	 * @param rev
	 * @return
	 */
	private Object pickDataValue(Object rev,PropertyUtilsBean propertyUtilsBean){
			try {
				if(propertyUtilsBean.getProperty(rev, "numberDataValue")!=null){
					return propertyUtilsBean.getProperty(rev, "numberDataValue");
				} else if(propertyUtilsBean.getProperty(rev, "textDataValue")!=null){
					return propertyUtilsBean.getProperty(rev, "textDataValue");
				}else if(propertyUtilsBean.getProperty(rev, "dateDataValue")!=null){
					return propertyUtilsBean.getProperty(rev, "dateDataValue");
				}else if(propertyUtilsBean.getProperty(rev, "errorDataValue")!=null){
					return propertyUtilsBean.getProperty(rev, "errorDataValue");
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				log.info("Problem at assigning one of max/min/missing and default value.");
				return null;
			}
			return null;
	}
	/**
	 * 
	 * @param rev
	 * @return
	 */
	private void pickAndSetFieldValue(List<AuditRow> auditRows,Object rev,Object[] result){
			RevisionType type = (RevisionType) result[2];
			UsernameRevisionEntity ure = (UsernameRevisionEntity) result[1];
			PropertyUtilsBean propertyBean = new PropertyUtilsBean();
			try {
				if(propertyBean.getProperty(rev, "minValue")!=null && !propertyBean.getProperty(rev, "minValue").toString().trim().isEmpty()){
					auditRows.add(new AuditRow(ure, propertyBean.getProperty(rev,"minValue").toString(), type, "Minimum Value"));
				} 
				if(propertyBean.getProperty(rev, "maxValue")!=null && !propertyBean.getProperty(rev, "maxValue").toString().trim().isEmpty()){
					auditRows.add(new AuditRow(ure, propertyBean.getProperty(rev,"maxValue").toString(), type, "Maximum Value"));
				}
				if(propertyBean.getProperty(rev, "missingValue")!=null && !propertyBean.getProperty(rev, "missingValue").toString().trim().isEmpty()){
					auditRows.add(new AuditRow(ure, propertyBean.getProperty(rev,"missingValue").toString(), type, "Missing Value"));
				}
				if(propertyBean.getProperty(rev, "defaultValue")!=null && !propertyBean.getProperty(rev, "defaultValue").toString().trim().isEmpty())  {
					auditRows.add(new AuditRow(ure, propertyBean.getProperty(rev,"defaultValue").toString(), type, "Default Value"));
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				log.info("Problem at assigning one of max/min/missing and default value.");
			}
	}
	public FeedbackPanel getFeedbackPanel() {
		return feedbackPanel;
	}
	public void setFeedbackPanel(FeedbackPanel feedbackPanel) {
		this.feedbackPanel = feedbackPanel;
	}
	
}

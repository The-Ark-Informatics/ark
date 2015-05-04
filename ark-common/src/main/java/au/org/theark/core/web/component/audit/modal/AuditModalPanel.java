package au.org.theark.core.web.component.audit.modal;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import jxl.write.DateFormat;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.audit.UsernameRevisionEntity;
import au.org.theark.core.service.IAuditService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDataProvider;

public class AuditModalPanel extends Panel {

	@SpringBean(name = Constants.ARK_AUDIT_SERVICE)
	private IAuditService iAuditService;
	
	private static final Logger log = LoggerFactory.getLogger(AuditModalPanel.class);
	
	private static final long serialVersionUID = 1L;

	private Object entity;
	private ArkCrudContainerVO arkCrudContainerVO;	
	
	public AuditModalPanel(String id, Object entity, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.entity = entity;
		this.arkCrudContainerVO = arkCrudContainerVO;
		initialisePanel();
	}
	
	private void initialisePanel() {
		log.info("history modal init panel");
		List<Entry<UsernameRevisionEntity, Object>> revisionEntities = new ArrayList<Entry<UsernameRevisionEntity,Object>>();
		AuditReader reader = iAuditService.getAuditReader();
		for(int i = 0; i < arkCrudContainerVO.getDetailPanelFormContainer().size(); i++) {
			Component component = arkCrudContainerVO.getDetailPanelFormContainer().get(i);
			Object current = entity;
			for(String s : component.getId().split(Pattern.quote("."))) {
				try { 
					PropertyUtilsBean propertyBean = new PropertyUtilsBean();
					Object property = propertyBean.getNestedProperty(current, s);
					if(!reader.isEntityClassAudited(property.getClass())) {
						List<Number> revisionNumbers = reader.getRevisions(current.getClass(), iAuditService.getEntityPrimaryKey(current));
						for(Number revision : revisionNumbers) {
//							UsernameRevisionEntity ure = reader.findRevision(UsernameRevisionEntity.class, revision);
							Object rev = reader.find(current.getClass(), iAuditService.getEntityPrimaryKey(current), revision);
							Object revProperty = propertyBean.getProperty(rev, s);
							Object[] result = (Object[])reader.createQuery().forRevisionsOfEntity(current.getClass(), false, true).add(AuditEntity.revisionNumber().eq(revision)).add(AuditEntity.id().eq(iAuditService.getEntityPrimaryKey(current))).getSingleResult();
							RevisionType type = (RevisionType) result[2];
							UsernameRevisionEntity ure = (UsernameRevisionEntity) result[1];
							log.info("Type: " + type);
							log.info("ure: " + ure);
							log.info("property: " + revProperty);
							revisionEntities.add(new AbstractMap.SimpleEntry(ure, revProperty)); 
						}
					}
					current = property;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		Collections.sort(revisionEntities, new Comparator<Entry<UsernameRevisionEntity, Object>>() {
			@Override
			public int compare(Entry<UsernameRevisionEntity, Object> o1,
					Entry<UsernameRevisionEntity, Object> o2) {
				return -o1.getKey().getRevisionDate().compareTo(o2.getKey().getRevisionDate());
			}
		});
		
		ArkDataProvider<Entry<UsernameRevisionEntity, Object>, IAuditService> dataProvider = new ArkDataProvider<Entry<UsernameRevisionEntity, Object>, IAuditService>(iAuditService) {

			@Override
			public Iterator<? extends Entry<UsernameRevisionEntity, Object>> iterator(
					int first, int count) {
				return revisionEntities.subList(first, first + count).listIterator();
			}

			@Override
			public int size() {
				return revisionEntities.size();
			}
			
		};
		
		DataView<Entry<UsernameRevisionEntity, Object>> dataView = new DataView<Entry<UsernameRevisionEntity,Object>>("table", dataProvider) {
			
			@Override
			protected void populateItem(Item<Entry<UsernameRevisionEntity, Object>> item) {
				UsernameRevisionEntity ure = item.getModelObject().getKey();
				Object entity = item.getModelObject().getValue();
				item.add(new Label("rev", new Integer(ure.getId()).toString()));
				item.add(new Label("revDate", new DateFormat(Constants.DD_MM_YYYY_HH_MM_SS).getDateFormat().format(ure.getRevisionDate())));
				item.add(new Label("revBy", ure.getUsername()));
				item.add(new Label("value", iAuditService.getEntityValue(entity)));
				item.add(new Label("revType", ""));
			}
		};
		add(dataView);
	}
}

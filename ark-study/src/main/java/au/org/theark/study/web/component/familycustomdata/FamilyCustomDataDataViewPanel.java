package au.org.theark.study.web.component.familycustomdata;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.FamilyCustomFieldData;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.customfield.dataentry.CustomDataEditorDataView;
import au.org.theark.study.model.vo.FamilyCustomDataVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subjectcustomdata.SubjectCustomDataDataViewPanel;

public class FamilyCustomDataDataViewPanel extends Panel {
	private static final long serialVersionUID = -1L;
	private static final Logger log = LoggerFactory.getLogger(SubjectCustomDataDataViewPanel.class);

	private CompoundPropertyModel<FamilyCustomDataVO> cpModel;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService<Void>							iArkCommonService;
	
	protected ArkDataProvider2<FamilyCustomDataVO, FamilyCustomFieldData> scdDataProvider;
	protected DataView<FamilyCustomFieldData> dataView;

	public FamilyCustomDataDataViewPanel(String id, CompoundPropertyModel<FamilyCustomDataVO> cpModel) {
		super(id);
		this.cpModel = cpModel;
		this.setOutputMarkupPlaceholderTag(true);
	}

	public FamilyCustomDataDataViewPanel initialisePanel(Integer numRowsPerPage, CustomFieldCategory customFieldCategory) {
		initialiseDataView(customFieldCategory);
		if (numRowsPerPage != null) {
			dataView.setItemsPerPage(numRowsPerPage); // iArkCommonService.getRowsPerPage());
		}

		this.add(dataView);
		return this;
	}

	private void initialiseDataView(CustomFieldCategory customFieldCategory) {
		// TODO fix for READ permission check
		if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
			// Data provider to get pageable results from backend
			scdDataProvider = new ArkDataProvider2<FamilyCustomDataVO, FamilyCustomFieldData>() {

				public int size() {
					LinkSubjectStudy lss = criteriaModel.getObject().getLinkSubjectStudy();
					ArkFunction arkFunction = criteriaModel.getObject().getArkFunction();
					return (int) studyService.getFamilyCustomFieldDataCount(lss, arkFunction);// TODO
																								// safeintconversion
				}

				public Iterator<FamilyCustomFieldData> iterator(int first, int count) {
					LinkSubjectStudy lss = criteriaModel.getObject().getLinkSubjectStudy();
					ArkFunction arkFunction = criteriaModel.getObject().getArkFunction();
					String familyUId=criteriaModel.getObject().getFamilyUId();
					CustomFieldType customFieldType=iArkCommonService.getCustomFieldTypeByName(au.org.theark.core.Constants.FAMILY);
					List<FamilyCustomFieldData> familyCustomDataList = studyService.getFamilyCustomFieldDataList(lss, arkFunction,customFieldCategory,customFieldType, first, count);
					//List<FamilyCustomFieldData> familyCustomDataList = studyService.getFamilyCustomFieldDataListByCategory(lss.getStudy(), familyId, arkFunction, customFieldCategory, customFieldType, first, count);
					cpModel.getObject().setCustomFieldDataList(familyCustomDataList);
					return cpModel.getObject().getCustomFieldDataList().iterator();
				}
			};
			// Set the criteria for the data provider
			scdDataProvider.setCriteriaModel(cpModel);
		} else {
			// Since module is not accessible, create a dummy dataProvider that
			// returns nothing
			scdDataProvider = new ArkDataProvider2<FamilyCustomDataVO, FamilyCustomFieldData>() {

				public Iterator<? extends FamilyCustomFieldData> iterator(int first, int count) {
					return null;
				}

				public int size() {
					return 0;
				}
			};
		}

		dataView = this.buildDataView(scdDataProvider);
	}

	public DataView<FamilyCustomFieldData> buildDataView(ArkDataProvider2<FamilyCustomDataVO, FamilyCustomFieldData> scdDataProvider2) {

		DataView<FamilyCustomFieldData> subjectCFDataDataView = new CustomDataEditorDataView<FamilyCustomFieldData>("customDataList", scdDataProvider2) {

			@Override
			protected void populateItem(final Item<FamilyCustomFieldData> item) {
				FamilyCustomFieldData subjectCustomData = item.getModelObject();
				// Ensure we tie Subject in context to the item if that link
				// isn't there already
				if (subjectCustomData.getFamilyUid() == null) {
					subjectCustomData.setFamilyUid(cpModel.getObject().getFamilyUId());
				}
				if(subjectCustomData.getStudy() == null){
					subjectCustomData.setStudy(cpModel.getObject().getLinkSubjectStudy().getStudy());
				}
				super.populateItem(item);
			}

			@Override
			protected WebMarkupContainer getParentContainer() {
				return this;
			}

			@Override
			protected Logger getLog() {
				return log;
			}
		};
		return subjectCFDataDataView;
	}

	public DataView<FamilyCustomFieldData> getDataView() {
		return dataView;
	}
}

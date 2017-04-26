/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.study.web.component.subjectcustomdata;

import au.org.theark.core.model.study.entity.*;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectCustomDataVO;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.customfield.dataentry.CustomDataEditorDataView;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * @author elam
 * 
 */
@SuppressWarnings({ "serial" })
public class SubjectCustomDataDataViewPanel extends Panel {

	private static final long serialVersionUID = -1L;
	private static final Logger log = LoggerFactory.getLogger(SubjectCustomDataDataViewPanel.class);

	private CompoundPropertyModel<SubjectCustomDataVO> cpModel;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService<Void>							iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

	protected ArkDataProvider2<SubjectCustomDataVO, SubjectCustomFieldData> scdDataProvider;
	protected DataView<SubjectCustomFieldData> dataView;

	public SubjectCustomDataDataViewPanel(String id, CompoundPropertyModel<SubjectCustomDataVO> cpModel) {
		super(id);
		this.cpModel = cpModel;
		this.setOutputMarkupPlaceholderTag(true);
	}

	public SubjectCustomDataDataViewPanel initialisePanel(Integer numRowsPerPage,CustomFieldCategory customFieldCategory) {
		initialiseDataView(customFieldCategory);
		if (numRowsPerPage != null) {
			dataView.setItemsPerPage(numRowsPerPage); // iArkCommonService.getRowsPerPage());
		}
		this.add(dataView);
		return this;
	}

	private void initialiseDataView(CustomFieldCategory customFieldCategory ) {
		// TODO fix for READ permission check
		if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
			// Data provider to get pageable results from backend
			scdDataProvider = new ArkDataProvider2<SubjectCustomDataVO, SubjectCustomFieldData>() {
			
				public long size() {
					LinkSubjectStudy lss = criteriaModel.getObject().getLinkSubjectStudy();
					ArkFunction arkFunction = criteriaModel.getObject().getArkFunction();
					return studyService.getSubjectCustomFieldDataCount(lss, arkFunction);
				}
				public Iterator<SubjectCustomFieldData> iterator(long first, long count) {
					LinkSubjectStudy lss = criteriaModel.getObject().getLinkSubjectStudy();
					ArkFunction arkFunction = criteriaModel.getObject().getArkFunction();
					CustomFieldType customFieldType=iArkCommonService.getCustomFieldTypeByName(au.org.theark.core.Constants.SUBJECT);
					//Changed the method with customFieldCategory inclusive.
					List<SubjectCustomFieldData> subjectCustomDataList = studyService.getSubjectCustomFieldDataList(lss, arkFunction, customFieldCategory,customFieldType, first, count);
					//List<SubjectCustomFieldData> subjectCustomDataList = studyService.getSubjectCustomFieldDataListByCategory(lss, arkFunction, customFieldCategory,customFieldType, first, count);
					cpModel.getObject().setCustomFieldDataList(subjectCustomDataList);
					return cpModel.getObject().getCustomFieldDataList().iterator();
				}
			};
			// Set the criteria for the data provider
			scdDataProvider.setCriteriaModel(cpModel);
		} else {
			// Since module is not accessible, create a dummy dataProvider that
			// returns nothing
			scdDataProvider = new ArkDataProvider2<SubjectCustomDataVO, SubjectCustomFieldData>() {

				public Iterator<? extends SubjectCustomFieldData> iterator(long first, long count) {
					return null;
				}

				public long size() {
					return 0;
				}
			};
		}

		dataView = this.buildDataView(scdDataProvider);
	}

	public DataView<SubjectCustomFieldData> buildDataView(ArkDataProvider2<SubjectCustomDataVO, SubjectCustomFieldData> scdDataProvider2) {

		DataView<SubjectCustomFieldData> subjectCFDataDataView = new CustomDataEditorDataView<SubjectCustomFieldData>("customDataList", scdDataProvider2) {

			@Override
			protected void populateItem(final Item<SubjectCustomFieldData> item) {
				SubjectCustomFieldData subjectCustomData = item.getModelObject();
				// Ensure we tie Subject in context to the item if that link
				// isn't there already
				if (subjectCustomData.getLinkSubjectStudy() == null) {
					subjectCustomData.setLinkSubjectStudy(cpModel.getObject().getLinkSubjectStudy());
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

	public DataView<SubjectCustomFieldData> getDataView() {
		return dataView;
	}

}

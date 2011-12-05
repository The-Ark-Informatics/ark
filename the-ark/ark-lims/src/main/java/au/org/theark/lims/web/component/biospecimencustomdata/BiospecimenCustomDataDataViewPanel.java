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
package au.org.theark.lims.web.component.biospecimencustomdata;

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

import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.BiospecimenCustomFieldData;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.customfield.dataentry.CustomDataEditorDataView;
import au.org.theark.lims.model.vo.BiospecimenCustomDataVO;
import au.org.theark.lims.service.ILimsService;


/**
 * @author elam
 * 
 */
@SuppressWarnings({ "serial" })
public class BiospecimenCustomDataDataViewPanel extends Panel {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -1L;
	private static final Logger	log					= LoggerFactory.getLogger(BiospecimenCustomDataDataViewPanel.class);

	private CompoundPropertyModel<BiospecimenCustomDataVO>			cpModel;

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_SERVICE)
	protected ILimsService					iLimsService;
	
	protected ArkDataProvider2<BiospecimenCustomDataVO, BiospecimenCustomFieldData> scdDataProvider;
	protected DataView<BiospecimenCustomFieldData> dataView;

	public BiospecimenCustomDataDataViewPanel(String id, CompoundPropertyModel<BiospecimenCustomDataVO> cpModel) {
		super(id);
		this.cpModel = cpModel;
		
		this.setOutputMarkupPlaceholderTag(true);
	}
	
	public BiospecimenCustomDataDataViewPanel initialisePanel(Integer numRowsPerPage) {	
		initialiseDataView();
		if (numRowsPerPage != null) {
			dataView.setItemsPerPage(numRowsPerPage);	// au.org.theark.core.Constants.ROWS_PER_PAGE);
		}
		
		this.add(dataView);
		return this;
	}

	private void initialiseDataView() {
		// TODO fix for READ permission check
		if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SEARCH)) {
			// Data provider to get pageable results from backend
			scdDataProvider = new ArkDataProvider2<BiospecimenCustomDataVO, BiospecimenCustomFieldData>() {
				
				public int size() {
					Biospecimen biospecimen = criteriaModel.getObject().getBiospecimen();
					ArkFunction arkFunction = criteriaModel.getObject().getArkFunction();
	
					return iLimsService.getBiospecimenCustomFieldDataCount(biospecimen, arkFunction);
				}
	
				public Iterator<BiospecimenCustomFieldData> iterator(int first, int count) {
					Biospecimen biospecimen = criteriaModel.getObject().getBiospecimen();
					ArkFunction arkFunction = criteriaModel.getObject().getArkFunction();
	
					List<BiospecimenCustomFieldData> biospecimenCustomDataList = iLimsService.getBiospecimenCustomFieldDataList(biospecimen, arkFunction, first, count);
					cpModel.getObject().setCustomFieldDataList(biospecimenCustomDataList);
					return cpModel.getObject().getCustomFieldDataList().iterator();
				}
			};
			// Set the criteria for the data provider
			scdDataProvider.setCriteriaModel(cpModel);
		}
		else {
			// Since module is not accessible, create a dummy dataProvider that returns nothing
			scdDataProvider = new ArkDataProvider2<BiospecimenCustomDataVO, BiospecimenCustomFieldData>() {
				
				public Iterator<? extends BiospecimenCustomFieldData> iterator(int first, int count) {
					return null;
				}

				public int size() {
					return 0;
				}
			};
		}
		
		dataView = this.buildDataView(scdDataProvider);
	}
	
	public DataView<BiospecimenCustomFieldData> buildDataView(ArkDataProvider2<BiospecimenCustomDataVO, BiospecimenCustomFieldData> scdDataProvider2) {

		DataView<BiospecimenCustomFieldData> biospecimenCFDataDataView = new CustomDataEditorDataView<BiospecimenCustomFieldData>("customDataList", scdDataProvider2) {

			@Override
			protected void populateItem(final Item<BiospecimenCustomFieldData> item) {
				BiospecimenCustomFieldData biospecimenCustomData = item.getModelObject();
				// Ensure we tie Subject in context to the item if that link isn't there already
				if (biospecimenCustomData.getBiospecimen() == null) {
					biospecimenCustomData.setBiospecimen(cpModel.getObject().getBiospecimen());
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
		return biospecimenCFDataDataView;
	}
	
	public DataView<BiospecimenCustomFieldData> getDataView() {
		return dataView;
	}
	
	public void saveCustomData() {
		if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SAVE)) {
			List<BiospecimenCustomFieldData> errorList = iLimsService.createOrUpdateBiospecimenCustomFieldData(cpModel.getObject().getCustomFieldDataList());
			if (errorList.size() > 0) {
				for (BiospecimenCustomFieldData biospecimenCustomFieldData : errorList) {
					CustomField cf = biospecimenCustomFieldData.getCustomFieldDisplay().getCustomField();
					String fieldType = cf.getFieldType().getName();
					if (fieldType.equals(au.org.theark.core.web.component.customfield.Constants.DATE_FIELD_TYPE_NAME)) {
						this.error("Unable to save this data: " + cf.getFieldLabel() + " = " + biospecimenCustomFieldData.getDateDataValue());
					}
					else {
						this.error("Unable to save this data: " + cf.getFieldLabel() + " = " + biospecimenCustomFieldData.getTextDataValue());					
					}
				}
			}
		}
	}
}

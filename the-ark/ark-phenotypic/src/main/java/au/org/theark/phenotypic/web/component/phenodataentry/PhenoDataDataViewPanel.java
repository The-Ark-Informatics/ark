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
package au.org.theark.phenotypic.web.component.phenodataentry;

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

import au.org.theark.core.model.pheno.entity.PhenoData;
import au.org.theark.core.model.pheno.entity.PhenotypicCollection;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.vo.PhenoDataCollectionVO;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.customfield.dataentry.CustomDataEditorDataView;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;

/**
 * @author elam
 * 
 */
@SuppressWarnings({ "serial" })
public class PhenoDataDataViewPanel extends Panel {

	private static final long		serialVersionUID	= -1L;
	private static final Logger	log					= LoggerFactory.getLogger(PhenoDataDataViewPanel.class);

	private CompoundPropertyModel<PhenoDataCollectionVO>			cpModel;

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService			iPhenotypicService;
	
	protected ArkDataProvider2<PhenoDataCollectionVO, PhenoData> scdDataProvider;
	protected DataView<PhenoData> dataView;

	public PhenoDataDataViewPanel(String id, CompoundPropertyModel<PhenoDataCollectionVO> cpModel) {
		super(id);
		this.cpModel = cpModel;
		
		this.setOutputMarkupPlaceholderTag(true);
	}
	
	public PhenoDataDataViewPanel initialisePanel(Integer numRowsPerPage) {	
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
			scdDataProvider = new ArkDataProvider2<PhenoDataCollectionVO, PhenoData>() {
				
				public int size() {
					PhenotypicCollection phenoCollection = criteriaModel.getObject().getPhenotypicCollection();
					ArkFunction arkFunction = criteriaModel.getObject().getArkFunction();
	
					return iPhenotypicService.getPhenoDataCount(phenoCollection);
				}
	
				public Iterator<PhenoData> iterator(int first, int count) {
					PhenotypicCollection phenoCollection = criteriaModel.getObject().getPhenotypicCollection();
					ArkFunction arkFunction = criteriaModel.getObject().getArkFunction();
	
					List<PhenoData> phenoDataList = iPhenotypicService.getPhenoDataList(phenoCollection, first, count);
					cpModel.getObject().setCustomFieldDataList(phenoDataList);
					return cpModel.getObject().getCustomFieldDataList().iterator();
				}
			};
			// Set the criteria for the data provider
			scdDataProvider.setCriteriaModel(cpModel);
		}
		else {
			// Since module is not accessible, create a dummy dataProvider that returns nothing
			scdDataProvider = new ArkDataProvider2<PhenoDataCollectionVO, PhenoData>() {
				
				public Iterator<? extends PhenoData> iterator(int first, int count) {
					return null;
				}

				public int size() {
					return 0;
				}
			};
		}
		
		dataView = this.buildDataView(scdDataProvider);
	}
	
	public DataView<PhenoData> buildDataView(ArkDataProvider2<PhenoDataCollectionVO, PhenoData> scdDataProvider2) {

		DataView<PhenoData> subjectCFDataDataView = new CustomDataEditorDataView<PhenoData>("customDataList", scdDataProvider2) {

			@Override
			protected void populateItem(final Item<PhenoData> item) {
				PhenoData phenoData = item.getModelObject();
				// Ensure we tie Subject in context to the item if that link isn't there already
				if (phenoData.getPhenotypicCollection() == null) {
					phenoData.setPhenotypicCollection(cpModel.getObject().getPhenotypicCollection());
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
	
	public DataView<PhenoData> getDataView() {
		return dataView;
	}
	
	public void saveCustomData() {
		if (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.SAVE)) {
			List<PhenoData> errorList = iPhenotypicService.createOrUpdatePhenoData(cpModel.getObject().getCustomFieldDataList());
			if (errorList.size() > 0) {
				for (PhenoData phenoData : errorList) {
					CustomField cf = phenoData.getCustomFieldDisplay().getCustomField();
					String fieldType = cf.getFieldType().getName();
					if (fieldType.equals(au.org.theark.core.web.component.customfield.Constants.DATE_FIELD_TYPE_NAME)) {
						this.error("Unable to save this data: " + cf.getFieldLabel() + " = " + phenoData.getDateDataValue());
					}
					else {
						this.error("Unable to save this data: " + cf.getFieldLabel() + " = " + phenoData.getTextDataValue());					
					}
				}
			}
		}
	}
}

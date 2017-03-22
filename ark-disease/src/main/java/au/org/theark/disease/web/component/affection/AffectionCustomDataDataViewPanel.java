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
package au.org.theark.disease.web.component.affection;

import au.org.theark.core.model.disease.entity.AffectionCustomFieldData;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.customfield.dataentry.CustomDataEditorDataView;
import au.org.theark.disease.service.IArkDiseaseService;
import au.org.theark.disease.vo.AffectionCustomDataVO;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;


/**
 * @author elam
 * 
 */
@SuppressWarnings({ "serial" })
public class AffectionCustomDataDataViewPanel extends Panel {


	private static final long		serialVersionUID	= -1L;
	private static final Logger	log					= LoggerFactory.getLogger(AffectionCustomDataDataViewPanel.class);

	private CompoundPropertyModel<AffectionCustomDataVO>			cpModel;

	@SpringBean(name = au.org.theark.core.Constants.ARK_DISEASE_SERVICE)
	protected IArkDiseaseService iArkDiseaseService;

	protected ArkDataProvider2<AffectionCustomDataVO, AffectionCustomFieldData> scdDataProvider;
	protected DataView<AffectionCustomFieldData> dataView;

	public AffectionCustomDataDataViewPanel(String id, CompoundPropertyModel<AffectionCustomDataVO> cpModel) {
		super(id);
		this.cpModel = cpModel;

		this.setOutputMarkupPlaceholderTag(true);
	}

	public AffectionCustomDataDataViewPanel initialisePanel(Integer numRowsPerPage) {	
		initialiseDataView();
		if (numRowsPerPage != null) {
			dataView.setItemsPerPage(numRowsPerPage);	
		}
		this.add(dataView);
		return this;
	}

	private void initialiseDataView() {
		scdDataProvider = new ArkDataProvider2<AffectionCustomDataVO, AffectionCustomFieldData>() {

			public long size() {
				return cpModel.getObject().getCustomFieldDataList().size();
			}

			public Iterator<AffectionCustomFieldData> iterator(long first, long count) {
				return cpModel.getObject().getCustomFieldDataList().iterator();
			}
		};
		// Set the criteria for the data provider
		scdDataProvider.setCriteriaModel(cpModel);
		dataView = this.buildDataView(scdDataProvider);
	}

	public DataView<AffectionCustomFieldData> buildDataView(ArkDataProvider2<AffectionCustomDataVO, AffectionCustomFieldData> scdDataProvider2) {

		DataView<AffectionCustomFieldData> bioCollectionCFDataDataView = new CustomDataEditorDataView<AffectionCustomFieldData>("customDataList", scdDataProvider2) {

			@Override
			protected void populateItem(final Item<AffectionCustomFieldData> item) {
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
		return bioCollectionCFDataDataView;
	}

	public DataView<AffectionCustomFieldData> getDataView() {
		return dataView;
	}
}

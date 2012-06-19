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
package au.org.theark.lims.web.component.barcodelabeldata;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.lims.entity.BarcodeLabel;
import au.org.theark.core.model.lims.entity.BarcodeLabelData;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;
import au.org.theark.core.web.component.listeditor.ListItem;
import au.org.theark.lims.service.ILimsAdminService;

/**
 * @author cellis
 * 
 */
public class BarcodeLabelDataPanel extends Panel {


	private static final long	serialVersionUID	= 1L;
	protected FeedbackPanel			feedBackPanel;
	private BarcodeLabel barcodeLabel= new BarcodeLabel();
	private TextArea<String>					labelPrefixTxtFld;
	private TextArea<String>					labelSuffixTxtFld;
	
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_ADMIN_SERVICE)
	private ILimsAdminService					iLimsAdminService;
	
	public BarcodeLabelDataPanel(String id, final BarcodeLabel barcodeLabel, FeedbackPanel feedBackPanel) {
		super(id);
		this.barcodeLabel = barcodeLabel;
		this.feedBackPanel = feedBackPanel;
		setOutputMarkupPlaceholderTag(true);
	}
	
	@Override
	protected void onBeforeRender() {
		barcodeLabel.setBarcodeLabelData(iLimsAdminService.getBarcodeLabelDataByBarcodeLabel(barcodeLabel));
		initialisePanel();
		super.onBeforeRender();
	}
		
	@SuppressWarnings("unchecked")
	public void initialisePanel() {
		
		labelPrefixTxtFld = new TextArea<String>("labelPrefix");
		labelSuffixTxtFld = new TextArea<String>("labelSuffix");
		labelPrefixTxtFld.setRequired(true).setLabel(new StringResourceModel("error.labelPrefix.required", this, new Model<String>("Prefix")));
		labelSuffixTxtFld.setRequired(true).setLabel(new StringResourceModel("error.labelSuffix.required", this, new Model<String>("Suffix")));
		
		final AbstractListEditor<BarcodeLabelData> listEditor = new AbstractListEditor<BarcodeLabelData>("barcodeLabelData", new PropertyModel(this, "barcodeLabel.barcodeLabelData")) {
			

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onPopulateItem(ListItem<BarcodeLabelData> item) {
				item.setModel(new CompoundPropertyModel<BarcodeLabelData>(item.getModel()));
            item.add(new TextField<String>("id").setEnabled(false));
            item.add(new TextField<String>("command"));
            item.add(new TextField<String>("xCoord"));
            item.add(new TextField<String>("yCoord"));
            item.add(new TextField<String>("p1"));
            item.add(new TextField<String>("p2"));
            item.add(new TextField<String>("p3"));
            item.add(new TextField<String>("p4"));
            item.add(new TextField<String>("p5"));
            item.add(new TextField<String>("p6"));
            item.add(new TextField<String>("p7"));
            item.add(new TextField<String>("p8"));
            item.add(new TextField<String>("data"));
			}
		};

		addOrReplace(labelPrefixTxtFld);
		addOrReplace(listEditor);
		addOrReplace(labelSuffixTxtFld);
	}
	
	/**
	 * @param barcodeLabel the barcodeLabel to set
	 */
	public void setBarcodeLabel(BarcodeLabel barcodeLabel) {
		this.barcodeLabel = barcodeLabel;
	}

	/**
	 * @return the barcodeLabel
	 */
	public BarcodeLabel getBarcodeLabel() {
		return barcodeLabel;
	}
}
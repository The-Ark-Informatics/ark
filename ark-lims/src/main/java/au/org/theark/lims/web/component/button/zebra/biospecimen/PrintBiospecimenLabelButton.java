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
package au.org.theark.lims.web.component.button.zebra.biospecimen;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.lims.entity.BarcodeLabel;
import au.org.theark.core.model.lims.entity.BarcodePrinter;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.lims.service.IBarcodeService;

/**
 * Class that represents a button to print a barcode label to a Zebra printer (generally the TLP2844 model) 
 * @author cellis
 */
public abstract class PrintBiospecimenLabelButton extends AjaxButton {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 5772993543283783679L;
	private static final Logger	log					= LoggerFactory.getLogger(PrintBiospecimenLabelButton.class);

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_BARCODE_SERVICE)
	private IBarcodeService			iBarcodeService;
	private final Biospecimen		biospecimen;
	private String						zplString;
	private BarcodePrinter			barcodePrinter;

	/**
	 * Construct an ajax button to send the specified barcodeString to a ZebraTLP2844 printer<br>
	 * <b>NOTE:</b> Assumes there is an applet on the page with the name "jZebra"
	 * 
	 * @param id
	 * @param biospecimen
	 */
	public PrintBiospecimenLabelButton(String id, final Biospecimen biospecimen) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.biospecimen = biospecimen;
		barcodePrinter = new BarcodePrinter();
		barcodePrinter.setStudy(biospecimen.getStudy());
		barcodePrinter.setName("zebra");
		barcodePrinter = iBarcodeService.searchBarcodePrinter(barcodePrinter);
	}
	
	@Override
	public boolean isEnabled() {
		boolean barcodePrinterAvailable = true;
		
		if(barcodePrinter.getId() == null) {
			PrintBiospecimenLabelButton.this.getParent().error("A Zebra barcode printer is currently not available. Please add the printer to the client machine and try again");
			barcodePrinterAvailable = false;
		}
		
		return (barcodePrinterAvailable);
	}

	@Override
	protected void onError(AjaxRequestTarget target, Form<?> form) {
		log.error("Error on PrintBarcodeToZebraButton click");
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		BarcodeLabel barcodeLabel = new BarcodeLabel();
		barcodeLabel.setBarcodePrinter(barcodePrinter);
		barcodeLabel.setStudy(biospecimen.getStudy());
		barcodeLabel.setName("zebra biospecimen");
		barcodeLabel = iBarcodeService.searchBarcodeLabel(barcodeLabel);

		this.zplString = iBarcodeService.createBiospecimenLabelTemplate(biospecimen, barcodeLabel);

		if (zplString == null || zplString.isEmpty()) {
			this.error("There was an error when attempting to print the barcode for: " + biospecimen.getBiospecimenUid());
			log.error("There was an error when attempting to print the barcode for: " + biospecimen.getBiospecimenUid());
		}
		else {
			log.debug(zplString);
			target.appendJavaScript("printZebraBarcode(\"" + zplString + "\");");
			onPostSubmit(target, form);
		}
	}

	/**
	 * Method used for any post-submit processing by the calling parent object/form
	 * 
	 * @param target
	 * @param form
	 */
	protected abstract void onPostSubmit(AjaxRequestTarget target, Form<?> form);
}
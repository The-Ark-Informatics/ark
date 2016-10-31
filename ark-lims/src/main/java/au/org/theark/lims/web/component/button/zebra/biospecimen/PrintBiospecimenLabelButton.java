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

import au.org.theark.lims.util.barcode.DigitalCertificateAjaxBehaviour;
import au.org.theark.lims.util.barcode.PrintZPL;
import au.org.theark.lims.util.barcode.PrivateKeySignerAjaxBehaviour;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.lims.entity.BarcodeLabel;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.lims.service.ILimsAdminService;

import java.util.List;

/**
 * Class that represents a button to print a barcode label to a Zebra printer (generally the TLP2844 model)
 * 
 * @author cellis
 */
public abstract class PrintBiospecimenLabelButton extends AjaxButton {

	private static final long		serialVersionUID	= 5772993543283783679L;
	private static final Logger	log					= LoggerFactory.getLogger(PrintBiospecimenLabelButton.class);

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_ADMIN_SERVICE)
	private ILimsAdminService		iLimsAdminService;
	private final Biospecimen		biospecimen;
	private IModel<?>					numberModel;
	private Number						barcodesToPrint;
	private String						zplString;
	private BarcodeLabel				barcodeLabel;

	/**
	 * Construct an ajax button to send the specified number of barcodes to a ZebraTLP2844 printer<br>
	 * <b>NOTE:</b> Assumes there is an applet on the page with the name "jZebra"
	 * 
	 * @param id
	 * @param biospecimenModel
	 *           the model definign the Biospecimen
	 * @param numberModel
	 *           the model defining the number of barcodes to print
	 */
	public PrintBiospecimenLabelButton(String id, IModel<?> biospecimenModel, IModel<?> numberModel) {
		super(id);
		this.biospecimen = (Biospecimen) biospecimenModel.getObject();
		this.numberModel = numberModel;
		this.barcodesToPrint = (Number) numberModel.getObject();

		barcodeLabel = new BarcodeLabel();
		barcodeLabel.setStudy(biospecimen.getStudy());
		barcodeLabel.setName("zebra biospecimen");
		barcodeLabel = iLimsAdminService.searchBarcodeLabel(barcodeLabel);


	}

	@Override
	public boolean isEnabled() {
		boolean barcodePrinterAvailable = true;
		log.info("barcodeLabel=" + ((barcodeLabel==null)?
				"barcodelabelnull":
				("barcodeLabel.getBarcodePrinterName() =" + barcodeLabel.getBarcodePrinterName())));
		if (barcodeLabel == null || barcodeLabel.getBarcodePrinterName() == null || barcodeLabel.getBarcodePrinterName().isEmpty()) {
			log.debug("A Zebra barcode label is currently not available. Please define the label and try again");
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
		if (barcodeLabel != null) {
			if(numberModel == null) {
				barcodesToPrint = 1;
				log.warn("number model was null - set to 1");
			}
			else {
				barcodesToPrint = (Number) numberModel.getObject();
				log.warn("number model aka barcodes to print = " + barcodesToPrint);
			}

			List<String> ZPLCommands = iLimsAdminService.createBiospecimenLabelTemplate(biospecimen, barcodeLabel);

			if (ZPLCommands == null || ZPLCommands.isEmpty()) {
				this.error("There was an error when attempting to print the barcode for: " + biospecimen.getBiospecimenUid());
				log.error("There was an error when attempting to print the barcode for: " + biospecimen.getBiospecimenUid());
			}
			else {
				log.warn("printer = " + barcodeLabel.getBarcodePrinterName());
				PrintZPL.printZPL(target, this, barcodeLabel.getBarcodePrinterName(), ZPLCommands, barcodesToPrint.intValue(), true);
				onPostSubmit(target, form);
			}

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
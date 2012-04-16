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
package au.org.theark.lims.web.component.button.zebra.biocollection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BarcodeLabel;
import au.org.theark.core.model.lims.entity.BarcodePrinter;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.lims.service.ILimsAdminService;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.web.Constants;

/**
 * Class that represents a button to print a barcode label to a Zebra printer (generally the TLP2844 model)
 * 
 * @author cellis
 */
public abstract class PrintBioCollectionLabelButton extends AjaxButton {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= 5772993543283783679L;
	private static final Logger		log					= LoggerFactory.getLogger(PrintBioCollectionLabelButton.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;
	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService					iLimsService;
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_ADMIN_SERVICE)
	private ILimsAdminService			iLimsAdminService;
	private BioCollection				bioCollection;
	private String							zplString;
	private BarcodePrinter				barcodePrinter;
	private BarcodeLabel					barcodeLabel;
	private IModel<?>						numberModel;
	private Number							barcodesToPrint;

	/**
	 * Construct an ajax button to send the specified barcodeString to a ZebraTLP2844 printer<br>
	 * <b>NOTE:</b> Assumes there is an applet on the page with the name "jZebra"
	 * 
	 * @param id the markup identifier
	 * @param bioCollection the bioCollection in context
	 * @param numberModel the number of labels to print
	 */
	public PrintBioCollectionLabelButton(String id, final BioCollection bioCollection, IModel<Number> numberModel) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.bioCollection = bioCollection;
		this.numberModel = numberModel;
		this.barcodesToPrint = (Number) numberModel.getObject();
		
		try {
			this.bioCollection = iLimsService.getBioCollection(bioCollection.getId());
			String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
			LinkSubjectStudy linkSubjectStudy = null;
			Study study = bioCollection.getStudy();
			linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID, study);
			this.bioCollection.setLinkSubjectStudy(linkSubjectStudy);
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		barcodePrinter = new BarcodePrinter();
		barcodePrinter.setStudy(bioCollection.getStudy());
		barcodePrinter.setName("zebra");
		barcodePrinter = iLimsAdminService.searchBarcodePrinter(barcodePrinter);

		barcodeLabel = new BarcodeLabel();
		barcodeLabel.setBarcodePrinter(barcodePrinter);
		barcodeLabel.setStudy(bioCollection.getStudy());
		barcodeLabel.setName("zebra bioCollection");
		barcodeLabel = iLimsAdminService.searchBarcodeLabel(barcodeLabel);
	}

	@Override
	public boolean isEnabled() {
		boolean barcodePrinterAvailable = true;

		if (barcodePrinter == null) {
			log.error("A Zebra barcode printer is currently not available. Please add the printer to the client machine and try again");
			barcodePrinterAvailable = false;
		}

		if (barcodeLabel == null) {
			log.error("A Zebra barcode label is currently not available. Please define the label and try again");
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
			StringBuffer sb = new StringBuffer();
			
			if(numberModel == null) {
				barcodesToPrint = 1;
			}
			else {
				barcodesToPrint = (Number) numberModel.getObject();
			}
			
			for (int i = 0; i < barcodesToPrint.intValue(); i++) {
				sb.append(iLimsAdminService.createBioCollectionLabelTemplate(bioCollection, barcodeLabel));
				sb.append("%0A");
			}
			
			this.zplString = sb.toString();

			if (zplString == null || zplString.isEmpty()) {
				this.error("There was an error when attempting to print the barcode for: " + bioCollection.getName());
				log.error("There was an error when attempting to print the barcode for: " + bioCollection.getName());
			}
			else {
				log.debug(zplString);
				target.appendJavaScript("printBarcode(\"" + barcodePrinter.getName() + "\",\"" + zplString + "\");");
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
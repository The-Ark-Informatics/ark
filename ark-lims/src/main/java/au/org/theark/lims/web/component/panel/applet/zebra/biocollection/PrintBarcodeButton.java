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
package au.org.theark.lims.web.component.panel.applet.zebra.biocollection;

import java.text.SimpleDateFormat;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.lims.entity.BarcodeLabel;
import au.org.theark.core.model.lims.entity.BarcodePrinter;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.lims.service.IBarcodeService;

/**
 * @author cellis
 * 
 */
public class PrintBarcodeButton extends AjaxButton {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 5772993543283783679L;
	private static final Logger	log					= LoggerFactory.getLogger(PrintBarcodeButton.class);
	private String						zplString;
	private SimpleDateFormat		simpleDateFormat	= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_BARCODE_SERVICE)
	private IBarcodeService			iBarcodeService;
	private final BioCollection	bioCollection;

	/**
	 * Construct an ajax button to send the specified barcodeString to a ZebraTLP2844 printer<br>
	 * <b>NOTE:</b> Assumes there is an applet on the page with the name "jZebra"
	 * 
	 * @param id
	 * @param bioCollection
	 */
	public PrintBarcodeButton(String id, final BioCollection bioCollection) {
		super(id);
		this.bioCollection = bioCollection;
	}

	@Override
	protected void onError(AjaxRequestTarget target, Form<?> form) {
		log.error("Error on PrintBarcodeToZebraButton click");
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		if (zplString == null || zplString.isEmpty()) {
			this.zplString = generateZpl();
		}
		else {
			BarcodePrinter barcodePrinter = new BarcodePrinter();
			barcodePrinter.setStudy(bioCollection.getStudy());
			barcodePrinter.setName("zebra");
			barcodePrinter = iBarcodeService.searchBarcodePrinter(barcodePrinter);
			
			BarcodeLabel barcodeLabel = new BarcodeLabel();
			barcodeLabel.setBarcodePrinter(barcodePrinter);
			barcodeLabel.setStudy(bioCollection.getStudy());
			barcodeLabel = iBarcodeService.searchBarcodeLabel(barcodeLabel);
			
			this.zplString = iBarcodeService.createBioCollectionLabelTemplate(bioCollection, barcodeLabel);
		}

		log.info("PrintBarcodeToZebraButton.onSubmit");
		log.info("Printing zplString:");
		log.info(zplString);
		target.appendJavaScript("printZebraBarcode(" + zplString + ");");
	}

	/**
	 * Safety method to create barcode if not defined
	 * 
	 * @return
	 */
	private String generateZpl() {
		// TODO: remove method
		String subjectUid = bioCollection.getLinkSubjectStudy().getSubjectUID();
		// TODO: need to get custom field data
		String familyId = "FAMILYID";
		// TODO: need to get custom field data
		String asrbno = "ASRBNO";
		String collectionDate = simpleDateFormat.format(bioCollection.getCollectionDate());
		String refDoctor = bioCollection.getRefDoctor();
		String dateOfBirth = simpleDateFormat.format(bioCollection.getLinkSubjectStudy().getPerson().getDateOfBirth());
		if(dateOfBirth == null || dateOfBirth.isEmpty()) {
			dateOfBirth = "31/12/0001";
		}
		String sex = bioCollection.getLinkSubjectStudy().getPerson().getGenderType().getName();
		StringBuffer sb = new StringBuffer();
		sb.append("D15\n");
		sb.append("N\n");
		sb.append("A240,10,1,2,1,1,N,\"ID: " + subjectUid + " Family ID:" + familyId + "\"" + "\n");
		sb.append("A220,10,1,2,1,1,N,\"ASRB No:" + asrbno + "\"" + "\n");
		sb.append("A200,10,1,2,1,1,N,\"Collection Date: " + collectionDate + "\"" + "\n");
		sb.append("A180,10,1,2,1,1,N,\"Researcher: " + refDoctor + "\"" + "\n");
		sb.append("A160,10,1,2,1,1,N,\"DOB: " + dateOfBirth + " " + "Sex: " + sex + "\"" + "\n");
		sb.append("P1\n");
		return sb.toString();
	}
}
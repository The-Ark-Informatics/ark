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
package au.org.theark.lims.web.component.panel.applet.zebra.biospecimen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	static private final String	REAL_NUMBER			= "^[-+]?\\d+(\\.\\d+)?$";
	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_BARCODE_SERVICE)
	private IBarcodeService			iBarcodeService;
	private final Biospecimen		biospecimen;

	/**
	 * Construct an ajax button to send the specified barcodeString to a ZebraTLP2844 printer<br>
	 * <b>NOTE:</b> Assumes there is an applet on the page with the name "jZebra"
	 * 
	 * @param id
	 * @param biospecimen
	 */
	public PrintBarcodeButton(String id, final Biospecimen biospecimen) {
		super(id);
		this.biospecimen = biospecimen;
	}

	@Override
	protected void onError(AjaxRequestTarget target, Form<?> form) {
		log.error("Error on PrintBarcodeToZebraButton click");
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		log.info("PrintBarcodeToZebraButton.onSubmit");
		
		log.info("getting barcode detail");
		BarcodePrinter barcodePrinter = new BarcodePrinter();
		barcodePrinter.setStudy(biospecimen.getStudy());
		barcodePrinter.setName("zebra");
		barcodePrinter = iBarcodeService.searchBarcodePrinter(barcodePrinter);
		
		BarcodeLabel barcodeLabel = new BarcodeLabel();
		barcodeLabel.setBarcodePrinter(barcodePrinter);
		barcodeLabel.setStudy(biospecimen.getStudy());
		barcodeLabel = iBarcodeService.searchBarcodeLabel(barcodeLabel);
		
		this.zplString = iBarcodeService.createBiospecimenLabelTemplate(biospecimen, barcodeLabel);
		
		if (zplString == null || zplString.isEmpty()) {
			this.zplString = generateZpl();
		}

		log.info("Printing zplString:");
		log.info(zplString);
		target.appendJavaScript("printZebraBarcode(\"" + zplString + "\");");
	}

	/**
	 * Safety method to create barcode if not defined
	 * 
	 * @return
	 */
	private String generateZpl() {
		// TODO: remove method
		String biospecimenUid = "00TST12345H";
		String dateOfBirth = "31/12/0001";
		String lastLineOfCircle = "";
		String secondLineOfCircle = "";

		// Ok first check and see if last character is a number.
		String lastChar = biospecimenUid.substring(biospecimenUid.length() - 1);
		// Need to find out where the study code ends
		// Trim off the year component
		String withoutYear = biospecimenUid.substring(2);
		Pattern pat = Pattern.compile("\\d");
		Matcher matcher = pat.matcher(withoutYear);
		boolean gotMatch = matcher.find();
		int indexOfnum = -1;
		if (gotMatch) {
			indexOfnum = matcher.start() + 2;
		}
		if (lastChar.matches(REAL_NUMBER)) {
			// We have a clone, therefore go one backwards.
			lastLineOfCircle = biospecimenUid.substring(biospecimenUid.length() - 2);
			secondLineOfCircle = biospecimenUid.substring(indexOfnum, biospecimenUid.length() - 2);
		}
		else {
			lastLineOfCircle = lastChar;
			secondLineOfCircle = biospecimenUid.substring(indexOfnum, biospecimenUid.length() - 1);
		}
		String secondLine = secondLineOfCircle;

		try {
			secondLine = new Integer(secondLineOfCircle).toString();
		}
		catch (NumberFormatException ne) {
		}

		StringBuffer sb = new StringBuffer();
		sb.append("D14\n");
		sb.append("q457\n");
		sb.append("N\n");
		sb.append("b200,15,D,h3,\"" + biospecimenUid + "\"\n");
		sb.append("A100,20,0,2,1,1,N,\"" + biospecimenUid.substring(0, indexOfnum) + "\"\n");
		sb.append("A100,40,0,2,1,1,N,\"" + secondLine + "\"\n");
		sb.append("A115,60,0,2,1,1,N,\"" + lastLineOfCircle + "\"\n");
		sb.append("A260,15,0,1,1,2,N,\"" + biospecimenUid + "\"\n");
		if (dateOfBirth != null)
			sb.append("A260,45,0,1,1,2,N,\"" + dateOfBirth + "\"\n");
		sb.append("P1\n");
		return sb.toString();
	}
}
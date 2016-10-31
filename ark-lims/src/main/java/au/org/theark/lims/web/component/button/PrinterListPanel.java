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
package au.org.theark.lims.web.component.button;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import au.org.theark.lims.util.barcode.DigitalCertificateAjaxBehaviour;
import au.org.theark.lims.util.barcode.PrivateKeySignerAjaxBehaviour;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.string.StringValue;

/**
 * Embedded PrintApplet and dynamic drop-down list that populates with the all_printers connected to the client machine
 * 
 * @author cellis
 * 
 */
public class PrinterListPanel extends Panel {
	private static final long		serialVersionUID	= -3613468944612447644L;
	protected String					selected				= new String();
	protected HiddenField<String>	selectedPrinter;
	protected static List<String>	PRINTERLIST			= Arrays.asList("zebra", "brady_bbp_11");
	protected DropDownChoice<String> printerListDdc;
	
	protected boolean isNew;

	protected DigitalCertificateAjaxBehaviour digitalCertificateAjaxBehaviour;
	protected PrivateKeySignerAjaxBehaviour privateKeySignerAjaxBehaviour;

	public PrinterListPanel(String id, String selected, boolean isNew) {
		super(id);
		setOutputMarkupId(true);
		this.isNew = isNew;
		setSelected(selected);
		setDefaultModel(new PropertyModel<String>(this, "selected"));
		initPanel();
	}

	private void initPanel() {
		selectedPrinter = new HiddenField<String>("selectedPrinter", new PropertyModel<String>(this, "selected"));
		selectedPrinter.setOutputMarkupPlaceholderTag(true);
		selectedPrinter.add(new AttributeModifier("name", "selectedPrinter"));
		selectedPrinter.add(new AbstractDefaultAjaxBehavior() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void respond(AjaxRequestTarget arg0) {
				StringValue selectedPrinter = RequestCycle.get().getRequest().getQueryParameters().getParameterValue("selectedPrinter");
				selected = selectedPrinter.toString();
				System.out.println("New selected printer is: " + selected);
			}
		});
		this.add(selectedPrinter);

		printerListDdc = new DropDownChoice<String>("printerList", new PropertyModel<String>(this, "selected"), PRINTERLIST);
		printerListDdc.add(new AttributeModifier("name", "printerList"));
		printerListDdc.setOutputMarkupPlaceholderTag(true);
		printerListDdc.setNullValid(true);
        printerListDdc.setEnabled(false);
		this.add(printerListDdc);

		digitalCertificateAjaxBehaviour = new DigitalCertificateAjaxBehaviour();
		this.add(digitalCertificateAjaxBehaviour);

		privateKeySignerAjaxBehaviour = new PrivateKeySignerAjaxBehaviour();
		this.add(privateKeySignerAjaxBehaviour);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		StringBuilder jsBuilder = new StringBuilder();
		jsBuilder.append("setupSigning(\"" + digitalCertificateAjaxBehaviour.getCallbackUrl() + "\", \"" + privateKeySignerAjaxBehaviour.getCallbackUrl() + "\");");
		jsBuilder.append("\n");
		jsBuilder.append("findPrinters(\"" + printerListDdc.getMarkupId() + "\", \"" + selectedPrinter.getMarkupId() + "\");");
        response.renderOnLoadJavaScript(jsBuilder.toString());

	}

	@Override
	protected void onBeforeRender() {
		printerListDdc.add(new AttributeModifier("onchange", "changeHiddenInput(\"" + printerListDdc.getMarkupId() + "\", \"" + selectedPrinter.getMarkupId() + "\" )"));
		super.onBeforeRender();
	}

	/**
	 * @return the selected
	 */
	public String getSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(String selected) {
		this.selected = selected;
	}

	/**
	 * @return the selectedPrinter
	 */
	public HiddenField<String> getSelectedPrinter() {
		return selectedPrinter;
	}

	/**
	 * @param selectedPrinter the selectedPrinter to set
	 */
	public void setSelectedPrinter(HiddenField<String> selectedPrinter) {
		this.selectedPrinter = selectedPrinter;
	}
}
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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.string.StringValue;

/**
 * Embedded PrintApplet and dynamic drop-down list that populatesd with the printers connected to the client machine
 * 
 * @author cellis
 * 
 */
public class PrinterListPanel extends Panel {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= -3613468944612447644L;
	protected String					selected				= new String();
	protected HiddenField<String>	selectedPrinter;
	protected static List<String>	PRINTERLIST			= Arrays.asList("zebra", "brady_bbp_11");
	
	public PrinterListPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		setDefaultModel(new PropertyModel<String>(this, "selected"));
		initPanel();
		add(new AttributeModifier("class", "floatLeft"));
	}

	private void initPanel() {
		Label printerLabel = new Label("printerLabel", "Printer:");
		this.add(printerLabel);

		DropDownChoice<String> printerListDdc = new DropDownChoice<String>("printerList", new PropertyModel<String>(this, "selected"), PRINTERLIST);
		printerListDdc.add(new AttributeModifier("name", "printerList"));
		printerListDdc.add(new AttributeModifier("onchange", "changeHiddenInput(this)"));
		printerListDdc.setOutputMarkupPlaceholderTag(true);
		this.add(printerListDdc);
		
		selectedPrinter = new HiddenField<String>("selectedPrinter", new PropertyModel<String>(this, "selected"));
		selectedPrinter.setOutputMarkupPlaceholderTag(true);
		selectedPrinter.add(new AttributeModifier("name", "selectedPrinter"));
		selectedPrinter.add(new AbstractDefaultAjaxBehavior() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void respond(AjaxRequestTarget arg0) {
				StringValue selectedPrinter = RequestCycle.get().getRequest().getQueryParameters().getParameterValue("selectedPrinter");
				selected = selectedPrinter.toString();
			}
		});
		this.add(selectedPrinter);
		
		StringBuilder javaScript = new StringBuilder();
		javaScript.append("function findPrinters() {");
		javaScript.append("\n");
		javaScript.append("	var applet = document.jZebra;");
		javaScript.append("\n");
		javaScript.append("	if (applet != null) {");
		javaScript.append("\n");
		javaScript.append("		document.getElementById('");
		javaScript.append(printerListDdc.getMarkupId());
		javaScript.append("').disabled = false;");
		javaScript.append("\n");
		javaScript.append("		var listing = applet.getPrinters();");
		javaScript.append("\n");
		javaScript.append("		var printers = listing.split(',');");
		javaScript.append("\n");
		javaScript.append("		for ( var i in printers) {");
		javaScript.append("\n");
		javaScript.append("			document.getElementById('");
		javaScript.append(printerListDdc.getMarkupId());
		javaScript.append("').options[i] = new Option(printers[i]);");
		javaScript.append("\n");
		javaScript.append("		}");
		javaScript.append("\n");
		javaScript.append("	} else {");
		javaScript.append("\n");
		javaScript.append("		document.getElementById('");
		javaScript.append(printerListDdc.getMarkupId());
		javaScript.append("').options[i] = new Option('N/A');");
		javaScript.append("\n");
		javaScript.append("		document.getElementById('");
		javaScript.append(printerListDdc.getMarkupId());
		javaScript.append("').disabled = true;");
		javaScript.append("\n");
		javaScript.append("	}");
		javaScript.append("\n");
		javaScript.append("}");
		javaScript.append("\n");
		javaScript.append("\n");
		javaScript.append("function changeHiddenInput (objDropDown) {");
		javaScript.append("\n");
		javaScript.append("	var objHidden = document.getElementById('");
		javaScript.append(selectedPrinter.getMarkupId());
		javaScript.append("');");
		javaScript.append("\n");
		javaScript.append("	objHidden.value = objDropDown.value;");
		javaScript.append("\n");
		javaScript.append("	callWicket(objDropDown.value);");
		javaScript.append("}");
		
		final Label script = new Label("script", javaScript.toString());
		script.setOutputMarkupPlaceholderTag(true);
		script.add(new AttributeModifier("onload", "findPrinters()"));
		script.setEscapeModelStrings(false); // do not HTML escape JavaScript code
		this.add(script);
		
		add(new AbstractDefaultAjaxBehavior() {
			private static final long	serialVersionUID	= 1L;

			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				response.renderOnLoadJavaScript("findPrinters()");
				String js = "function callWicket(selectedPrinter) { var wcall = wicketAjaxGet ('"
				    + getCallbackUrl() + "&selectedPrinter='+selectedPrinter, function() { }, function() { } ) }";
				response.renderJavaScript(js, "selectPrinter");
			}

			@Override
			protected void respond(AjaxRequestTarget arg0) {
				StringValue selectedPrinter = RequestCycle.get().getRequest().getQueryParameters().getParameterValue("selectedPrinter");
				selected = selectedPrinter.toString();
			}
		});
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
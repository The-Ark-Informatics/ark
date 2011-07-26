package au.org.theark.web.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.web.component.AbstractDetailModalWindow;

public class MyDetailModalWindow extends AbstractDetailModalWindow {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4739679880093551297L;
	private Panel					panel;
	private Form<?>				form;

	public MyDetailModalWindow(String id) {
		super(id);
	}

	protected void onCloseModalWindow(AjaxRequestTarget target) {
		target.addComponent(form);
		target.addComponent(panel);
	}

	/**
	 * @return the panel
	 */
	public Panel getPanel() {
		return panel;
	}

	/**
	 * @param panel the panel to set
	 */
	public void setPanel(Panel panel) {
		this.panel = panel;
	}

	/**
	 * @return the form
	 */
	public Form<?> getForm() {
		return form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(Form<?> form) {
		this.form = form;
	}
}
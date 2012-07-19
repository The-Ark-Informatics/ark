package au.org.theark.core.web.component.button;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public abstract class AjaxInvoiceButton extends IndicatingAjaxButton{
	private static final long	serialVersionUID	= 2845373897903023596L;
	private final IModel			confirm;

	public AjaxInvoiceButton(String id, IModel confirm, IModel label) {
		super(id);
		this.setModel(label);
		this.confirm = confirm;
	}
	
	public AjaxInvoiceButton(String id, IModel confirm) {
		super(id);
		this.confirm = confirm;
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return new AjaxPreprocessingCallDecorator(super.getAjaxCallDecorator()) {
			

			/**
			 * 
			 */
			private static final long serialVersionUID = -7982490494699252198L;

			@Override
			public CharSequence preDecorateScript(CharSequence script) {
				return "if(!confirm('" + confirm.getObject() + "'))" + "{ " + "	return false " + "} " + "else " + "{ " + "	this.disabled = true; " + "};" + script;
			}
			
			@Override
			public CharSequence decorateOnSuccessScript(Component c,
					CharSequence script) {
				return super.decorateOnSuccessScript(c, "	this.disabled = false; "+script );
			}
		};
	}

	@Override
	protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);

}

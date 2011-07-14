package au.org.theark.core.web.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

@SuppressWarnings({"unchecked"})
/**
 * @author cellis
 *
 */
public abstract class AjaxConfirmButton extends AjaxButton 
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8138160131842627124L;
	private final IModel confirm;

	public AjaxConfirmButton(String id, IModel confirm, IModel label) {
		super(id);
		this.setModel(label);
		this.confirm = confirm;
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return new AjaxPreprocessingCallDecorator(super.getAjaxCallDecorator()) {
			private static final long serialVersionUID = 7495281332320552876L;

			@Override
			public CharSequence preDecorateScript(CharSequence script) {
				return "if(!confirm('" + confirm.getObject() + "'))" +
						"{ " +
						"	return false " +
						"} " +
						"else " +
						"{ " +
						"	return true; " +
						"};" + script;
			}
		};
	}

	@Override
	protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);
}
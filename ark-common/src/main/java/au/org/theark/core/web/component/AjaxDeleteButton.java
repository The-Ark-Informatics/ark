package au.org.theark.core.web.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

import au.org.theark.core.security.ArkPermissionHelper;

@SuppressWarnings({"unchecked"})
/**
 * @author cellis
 *
 */
public abstract class AjaxDeleteButton extends IndicatingAjaxButton 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2845373897903023596L;
	private final IModel confirm;

	public AjaxDeleteButton(String id, IModel confirm, IModel label) {
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
						"	this.disabled = true; " +
						"};" + script;
			}
		};
	}

	@Override
	protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);
	
	@Override
	public boolean isVisible()
	{
		return ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.DELETE);
	}
}
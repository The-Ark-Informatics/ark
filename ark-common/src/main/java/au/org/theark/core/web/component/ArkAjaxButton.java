package au.org.theark.core.web.component;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPostprocessingCallDecorator;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;

public abstract class ArkAjaxButton extends IndicatingAjaxButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5799668393803636626L;

	public ArkAjaxButton(String id) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		setOutputMarkupId(true);
	}
	
	@Override
    protected IAjaxCallDecorator getAjaxCallDecorator() {
        return new AjaxPostprocessingCallDecorator(super.getAjaxCallDecorator()) {
            private static final long serialVersionUID = 1L;
 
            @Override
            public CharSequence postDecorateScript(CharSequence script) {
                return script + "this.disabled = true;";
            }
        };
    }
}
/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * @author nivedann
 * @param <T>
 *
 */
public abstract class AbstractContainerForm<T>  extends Form<T>{

	/**
	 * @param id
	 */
	public AbstractContainerForm(String id, CompoundPropertyModel<T> cpmModel) {
		super(id,cpmModel);
	}

}

/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.core.web.form;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.vo.ArkCrudContainerVO;

/**
 * An Abstract class for a top level Container Form, which will house the Search,SearchResults and
 * Detail Panel and their child objects.
 * @author nivedann
 * @param <T>
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractContainerForm<T>  extends Form<T>
{
	/**
	 * Constructor
	 * @param id
	 * @param cpmModel
	 */
	public AbstractContainerForm(String id, CompoundPropertyModel<T> cpmModel) {
		super(id,cpmModel);
		setOutputMarkupPlaceholderTag(true);
	}
}


package au.org.theark.study.web.component.subjectFile.form;

import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.form.AbstractContainerForm;

/**
 * @author cellis
 *
 */
public class ContainerForm extends AbstractContainerForm<SubjectVO> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5497609816761774384L;

	/**
	 * @param id
	 * @param cpmModel
	 */
	public ContainerForm(String id, CompoundPropertyModel<SubjectVO> cpmModel) {
		super(id, cpmModel);
	}

}

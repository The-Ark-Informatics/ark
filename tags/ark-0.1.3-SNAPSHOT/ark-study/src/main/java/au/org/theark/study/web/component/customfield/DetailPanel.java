/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.customfield;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.study.web.component.customfield.form.ContainerForm;

/**
 * @author nivedann
 *
 */
public class DetailPanel extends Panel {

	private FeedbackPanel feedbackPanel;
	private ArkCrudContainerVO arkCrudContainerVO;
	/**
	 * @param id
	 */
	public DetailPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public DetailPanel(String id, FeedbackPanel feedBackPanel,	ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm){
	
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		
	}
	

}

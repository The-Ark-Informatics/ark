
package au.org.theark.admin.web.component;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.core.service.IArkCommonService;

/**
 * @author cellis
 *
 */
public class AdminContainerPanel extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 442185554812824590L;
	protected CompoundPropertyModel<AdminVO> compoundPropertyModel;
	private FeedbackPanel feedbackPanel;
	
	@SpringBean( name =  au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void> iArkCommonService;
	
	/**
	 * @param id
	 */
	public AdminContainerPanel(String id) {
		super(id);
		compoundPropertyModel = new CompoundPropertyModel<AdminVO>(new AdminVO());
	}
	
	public void initialisePanel() {
		add(initialiseFeedBackPanel());
	}
	
	protected WebMarkupContainer initialiseFeedBackPanel(){
		/* Feedback Panel */
		setFeedbackPanel(new FeedbackPanel("feedbackMessage"));
		getFeedbackPanel().setOutputMarkupId(true);
		return getFeedbackPanel();
	}

	protected void prerenderContextCheck() 
	{		
	}

	public void setFeedbackPanel(FeedbackPanel feedbackPanel) {
		this.feedbackPanel = feedbackPanel;
	}

	public FeedbackPanel getFeedbackPanel() {
		return feedbackPanel;
	}

	public IArkCommonService<Void> getiArkCommonService() {
		return iArkCommonService;
	}

	public void setiArkCommonService(IArkCommonService<Void> iArkCommonService) {
		this.iArkCommonService = iArkCommonService;
	}
}

package au.org.theark.core.web.component;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.vo.ArkCrudContainerVO;

/**
 * <p>
 * Abstract class for the SubContainer panels that may contain any number of panels
 * </p>
 * 
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractSubContainerPanel<T> extends Panel
{

	@SpringBean(name = "arkLdapRealm")
	private ArkLdapRealm						realm;

	protected FeedbackPanel					feedbackPanel;

	protected ArkCrudContainerVO			arkCrudContainerVO;

	protected IModel<Object>				iModel;
	protected CompoundPropertyModel<T>	cpModel;

	protected PageableListView<T>			myListView;

	/**
	 * @param id
	 */
	public AbstractSubContainerPanel(String id)
	{
		super(id);
		setOutputMarkupPlaceholderTag(true);
		Subject currentUser = SecurityUtils.getSubject();
		realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());
		currentUser.hasRole("Administrator");
	}

	public AbstractSubContainerPanel(String id, boolean flag)
	{
		super(id);
		setOutputMarkupPlaceholderTag(true);
		Subject currentUser = SecurityUtils.getSubject();
		realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());// TODO(NN) Uncomment after the User management usecase is complete
	}

	public void initCrudContainerVO()
	{
		arkCrudContainerVO = new ArkCrudContainerVO();
	}

	protected WebMarkupContainer initialiseFeedBackPanel()
	{
		/* Feedback Panel */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}

	protected boolean isActionPermitted()
	{
		boolean flag = false;
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ))
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
		return flag;
	}
}

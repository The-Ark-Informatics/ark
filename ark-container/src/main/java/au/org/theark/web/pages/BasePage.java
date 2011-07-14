package au.org.theark.web.pages;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebResponse;

/**
 * <p>
 * The <code>BasePage</code> class that extends the {@link org.apache.wicket.markup.html.WebPage WebPage} class.
 * This page will be inherited by all wicket pages.
 * Contains basic functionality that all pages will require:
 * <ul>
 * 	<li>e.g. add the menu in here so all pages can inherit it by default.</li> 
 * </ul>
 * Access to each menu item will be determined by the subclass via annotations:
 * <ul>
 * 	<li>e.g if one of the menu item was Admin the class linked or invoked will place the constraints via annotations</li>
 * </ul>
 * Using IStrategyAuthorization,  the application will determine if the link is accessible to the current logged in user.
 * </p>
 * @author nivedann
 * @author cellis
 * 
 */
public abstract class BasePage extends WebPage
{
	private String						principal;
	private Label						userNameLbl;
	private Label						studyNameLbl;

	protected WebMarkupContainer	studyNameMarkup;
	protected WebMarkupContainer	studyLogoMarkup;

	/**
	 * Default constructor
	 */
	public BasePage()
	{
		ContextImage hostedByImage = new ContextImage("hostedByImage", new Model<String>("images/" + Constants.HOSTED_BY_IMAGE));
		ContextImage studyLogoImage = new ContextImage("studyLogoImage", new Model<String>("images/" + Constants.NO_STUDY_LOGO_IMAGE));
		ContextImage productImage = new ContextImage("productImage", new Model<String>("images/" + Constants.PRODUCT_IMAGE));

		Subject currentUser = SecurityUtils.getSubject();

		if (currentUser.getPrincipal() != null)
		{
			principal = (String) currentUser.getPrincipal();
			userNameLbl = new Label("loggedInUser", new Model<String>(principal));
			studyNameLbl = new Label("studyNameLabel", new Model<String>(" "));

			// Markup for Study name
			studyNameMarkup = new WebMarkupContainer("studyNameMarkupContainer");
			studyNameMarkup.add(studyNameLbl);
			studyNameMarkup.setOutputMarkupPlaceholderTag(true);

			// Markup for Study Logo
			studyLogoMarkup = new WebMarkupContainer("studyLogoMarkupContainer");
			studyLogoMarkup.add(studyLogoImage);
			studyLogoMarkup.setOutputMarkupPlaceholderTag(true);

			// Add images
			add(hostedByImage);
			add(studyNameMarkup);
			add(studyLogoMarkup);
			add(productImage);
			// add(bannerImage);

			add(userNameLbl);

			@SuppressWarnings("unchecked")
			AjaxLink link = new AjaxLink("ajaxLogoutLink")
			{
				/**
				 * 
				 */
				private static final long	serialVersionUID	= 422053857225833627L;

				@Override
				public void onClick(AjaxRequestTarget target)
				{
					Subject subject = SecurityUtils.getSubject();
					// Place the selected study in session context for the user
					SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
					SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
					SecurityUtils.getSubject().getSession().removeAttribute(au.org.theark.core.Constants.PERSON_TYPE);
					subject.logout();
					Session.get().invalidateNow(); // invalidate the wicket session
					setResponsePage(LoginPage.class);
				}
			};
			add(link);
		}
		else
		{
			setResponsePage(LoginPage.class);
		}
	}

	@Override
	protected void configureResponse()
	{
		super.configureResponse();
		WebResponse response = getWebRequestCycle().getWebResponse();
		response.setHeader("Cache-Control", "no-cache, max-age=0,must-revalidate, no-store");
		response.setHeader("Expires", "-1");
		response.setHeader("Pragma", "no-cache");
	}

	/**
	 * Build module tabs. All sub classes must implement this method
	 */
	protected abstract void buildModuleTabs();

	abstract String getTitle();
}

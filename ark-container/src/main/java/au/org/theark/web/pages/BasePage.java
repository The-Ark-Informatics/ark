package au.org.theark.web.pages;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
/**
 * This page will be inherited by all wicket pages. Contains basic functionality that all 
 * pages will require.e.g add the menu in here so all pages can inherit it by default.
 * Access to each menu item will be determined by the subclass via annotations. e.g if one of the
 * menu item was Admin the class linked or invoked will place the constraints via annotations.
 * Using IStrategyAuthorization the application will determine if the link is accessible to the
 * current logged in user. 
 * @author nivedann
 *
 */
public abstract class BasePage extends WebPage{
	
	//private Tabs tabs;
	//private Tabs etaTabs;
	private String principal;
	private Label userNameLbl;
	public BasePage(){
		
		ContextImage contextHostedByImage = new ContextImage("hostedByImage",new Model("images/"+Constants.HOSTED_BY_IMAGE));
		ContextImage productImage = new ContextImage("productImage", new Model("images/"+Constants.PRODUCT_IMAGE));
		//add(pageTitleLbl);
		Subject currentUser = SecurityUtils.getSubject();

		
		if(currentUser.getPrincipal() != null){
			principal = (String) currentUser.getPrincipal();
			userNameLbl = new Label("loggedInUser", new Model(principal));			
		}else{
			//user has not logged in as yet.
			principal="Guest";
			userNameLbl = new Label("loggedInUser", new Model("Guest"));
		}
		
		//return securityM
		
		add(contextHostedByImage);//hosted by image
		add(productImage);
		add(userNameLbl);
		add(new BookmarkablePageLink<Void>("logoutLink", LogoutPage.class));
	}
	
	/**
	 * Build module tabs
	 */
	protected abstract void buildModuleTabs();
	/**
	 * Implement method to add tab menus based on the module
	 */
	protected abstract void buildTabMenu();
	
	
	abstract String getTitle();

}

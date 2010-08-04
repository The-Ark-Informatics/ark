package au.org.theark.study.web.component.user;

import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkUserVO;
/**
 * A top level container that will have members like a Search control component and a 
 * User Details panel.
 * The container will have certain functions like handling the overall rendering of the child
 * components and the mode (create,edit,read etc). Based on the mode the rest of the components
 * will be controlled.
 * 
 * @author nivedann
 *
 */
public class UserContainer extends Panel{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Child components
	 */
	private Search searchUserPanel;
	
	/**
	 * 
	 * @param id
	 * @param userVO
	 */
	public UserContainer(String id, ArkUserVO userVO) {
		
		super(id);
		searchUserPanel = new Search("searchUserPanel");
		searchUserPanel.process("searchUserPanel");
		add(searchUserPanel);
	}

}

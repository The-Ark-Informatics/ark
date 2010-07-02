package au.org.theark.study.web.component.user;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.vo.EtaUserVO;
import au.org.theark.study.service.IUserService;
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
public class Container extends Panel{
	
	private static final long serialVersionUID = 1L;
	private transient Logger log = LoggerFactory.getLogger(Container.class);

	/**
	 * Child components
	 */
	private Search searchUserPanel;
	/**
	 * Method for toggling the components visible state.
	 * @param isVisible
	 * @param searchPanel
	 */
	private void setSearchVisible(boolean isVisible, Search searchPanel){
		searchPanel.setVisible(isVisible);
	}

	/**
	 * Set this mode to true or false based on user's action by clicking of a button 
	 * in a child object or on the container.
	 */
	private boolean isEditMode = false;
	
	
	/**
	 * The spring injected reference to a UserService implementation
	 */
	@SpringBean( name = "userService")
	private IUserService userService;
	
	
	/**
	 * Constructor
	 * @param id
	 */
	public Container(String id) {
		super(id);
		searchUserPanel = new Search("searchUserPanel");
		add(searchUserPanel);
	}
	
	/**
	 * 
	 * @param id
	 * @param userVO
	 */
	public Container(String id, EtaUserVO userVO) {
		
		super(id);
		searchUserPanel = new Search("searchUserPanel");
		add(searchUserPanel);
	}

	/**
	 * The main panel's form for users that will allow us to add child panels like the search/search results
	 * and the details panel.
	 * 
	 * @author nivedann
	 */
	public class UserPanelForm extends Form<EtaUserVO>{

		
		protected  void onSave(EtaUserVO userVO){}
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UserPanelForm(String id, EtaUserVO userVO) {
			
			super(id, new CompoundPropertyModel<EtaUserVO>(userVO));
			
			searchUserPanel = new Search("searchUserPanel");
			add(searchUserPanel);
			//Add the details panel for an ordinary user
			
		}
	}

}

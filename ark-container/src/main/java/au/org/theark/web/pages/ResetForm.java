package au.org.theark.web.pages;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkUserVO;

/**
 * <p>
 * The <code>ResetForm</code> class that extends the {@link org.apache.wicket.markup.html.form.StatelessForm StatelessForm} class. It provides the implementation of the
 * reset username form of The Ark application.
 * </p>
 * 
 * @author nivedann
 * @author cellis
 */
public class ResetForm extends StatelessForm<ArkUserVO> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5759125957232986063L;
	private Button					resetButton;
	private Button					cancelButton;
	private TextField<String>	emailAddressId		= new TextField<String>("email");

	/**
	 * Constructor
	 * @param id the component identifier
	 */
	public ResetForm(String id) {
		super(id, new CompoundPropertyModel<ArkUserVO>(new ArkUserVO()));

		resetButton = new Button("resetButton") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			public void onSubmit() {
				onReset();
			}
		};

		cancelButton = new Button("cancelButton") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			public void onSubmit() {
				onCancel();
			}
		};

		addFormComponents();
	}
	
	protected void addFormComponents() {
		this.add(emailAddressId);
		this.add(resetButton);
		this.add(cancelButton);
	}

	protected void onReset() {
		//TODO: Implement Reset of username's password
	};
	
	protected void onCancel() {
		setResponsePage(LoginPage.class);
	};
}

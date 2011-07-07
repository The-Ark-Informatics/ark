package au.org.theark.core.web.component;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public abstract class ArkDataProvider<T,U> implements IDataProvider<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected IModel<T> model;
	protected U service;
	
	public ArkDataProvider(U service) {
		super();
		this.service = service;
	}

	public IModel<T> getModel() {
		return model;
	}

	public void setModel(IModel<T> model) {
		this.model = model;
	}

	public IModel<T> model (final T object) {
		return new LoadableDetachableModel<T>() {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -4738032546393837333L;

			@Override
			protected T load() {
				return (T)object;
			}
		};
	}
	
	public void detach() {
		//TODO: Anything?...nope
	}
}


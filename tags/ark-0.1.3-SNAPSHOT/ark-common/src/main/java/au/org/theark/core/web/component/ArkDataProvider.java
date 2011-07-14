package au.org.theark.core.web.component;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/*
* The ArkDataProvider is designed for use with Hibernate as the underlying data source
* Due to Hibernate's ability to lazy-load, it is unnecessary to:
* - (re-)load() the object from the backend
* - do anything on detach()
*
* @author elam
*/
public abstract class ArkDataProvider<T,U> implements IDataProvider<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected IModel<T> model;
	protected transient U service;
	
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


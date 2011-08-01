package au.org.theark.core.web.component;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/*
 * The ArkDataProvider2 is designed for use with Hibernate as the underlying data source
 * with the flexibility of the criteria being a different type to the return object.
 * Due to Hibernate's ability to lazy-load, it is:
 * - unnecessary to (re-)load() the object from the backend for the "model(T object)"
 * - unnecessary to do anything on detach()
 *
 * @author elam
 */
public abstract class ArkDataProvider2<S, T, U> implements IDataProvider<T> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	protected IModel<S>			criteriaModel;
	protected transient U		service;

	public ArkDataProvider2(U service) {
		super();
		this.service = service;
	}

	public IModel<S> getCriteriaModel() {
		return criteriaModel;
	}

	public void setCriteriaModel(IModel<S> model) {
		this.criteriaModel = model;
	}

	// Implemented based on using Hibernate with Wicket - i.e. it just needs to return a 
	// LoadableDetachableModel of the object and relies on Hibernate's lazy loading
	public IModel<T> model(final T object) {
		return new LoadableDetachableModel<T>() {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -4738032546393837333L;

			@Override
			protected T load() {
				return (T) object;
			}
		};
	}

	public void detach() {
		// TODO: Anything?...nope
	}
}

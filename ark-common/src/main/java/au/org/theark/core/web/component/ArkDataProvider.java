package au.org.theark.core.web.component;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public abstract class ArkDataProvider<T,U> implements IDataProvider<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected T criteria;
	protected U service;
	
	public ArkDataProvider(U service) {
		super();
		this.service = service;
	}

	public T getCriteria() {
		return criteria;
	}

	public void setCriteria(T criteria) {
		this.criteria = criteria;
	}
	
	public IModel<T> model (final T object) {
		return new LoadableDetachableModel<T>() {
			@Override
			protected T load() {
				return (T)object;
			}
		};
	}
	
	public void detach() {
		//TODO: Anything?
	}
}


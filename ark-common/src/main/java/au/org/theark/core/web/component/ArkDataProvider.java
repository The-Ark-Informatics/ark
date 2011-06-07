package au.org.theark.core.web.component;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public abstract class ArkDataProvider<T,U> implements IDataProvider<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected CompoundPropertyModel<T> compoundPropertyModel;
	protected U service;
	
	public ArkDataProvider(U service) {
		super();
		this.service = service;
	}

	public CompoundPropertyModel<T> getCompoundPropertyModel() {
		return compoundPropertyModel;
	}

	public void setCompoundPropertyModel(CompoundPropertyModel<T> compoundPropertyModel) {
		this.compoundPropertyModel = compoundPropertyModel;
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
		//TODO: Anything?...nope
	}
}


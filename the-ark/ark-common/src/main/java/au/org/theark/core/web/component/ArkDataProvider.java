/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.core.web.component;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/*
 * The ArkDataProvider is designed for use with Hibernate as the underlying data source
 * Due to Hibernate's ability to lazy-load, it is:
 * - unnecessary to (re-)load() the object from the backend for the "model(T object)"
 * - unnecessary to do anything on detach()
 *
 * @author elam
 */
public abstract class ArkDataProvider<T, U> implements IDataProvider<T> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	protected IModel<T>			model;
	protected transient U		service;

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

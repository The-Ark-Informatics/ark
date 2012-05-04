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
package au.org.theark.lims.model;

import java.io.Serializable;

import au.org.theark.core.model.lims.entity.InvTreeNode;

/**
 * 
 * @author cellis
 *
 * @param <T>
 */
public class InventoryModel<T> implements Serializable {

	private static final long	serialVersionUID	= 485859504346024007L;
	protected T inventoryObject;
	protected String nodeType;
	
	public InventoryModel(T inventoryObject, String nodeType) {
		this.inventoryObject = inventoryObject;
		this.nodeType = nodeType;
	}
	
	public T getObject() {
		return inventoryObject;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		String name = null;
		InvTreeNode<T> invTreeNode = (InvTreeNode<T>) inventoryObject;
		name = invTreeNode.getNodeName();
		return name;	
	}
}

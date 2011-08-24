package au.org.theark.lims.model;

import java.io.Serializable;

import au.org.theark.core.model.lims.entity.InvTreeNode;

public class InventoryModel<T> implements Serializable {

	/**
	 * 
	 */
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
	
	@Override
	public String toString() {
		String name = null;
		InvTreeNode<T> invTreeNode = (InvTreeNode<T>) inventoryObject;
		name = invTreeNode.getNodeName();
		return name;	
	}
}

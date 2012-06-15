package au.org.theark.lims.model;

import java.io.Serializable;

/**
 * A default model that is used for a generic node on the tree
 * 
 * @author cellis
 */
public class TreeNodeModel implements Serializable {

	private static final long	serialVersionUID	= 7055619474572570930L;
	private String					propertyName;

	/**
	 * Creates the bean.
	 * 
	 * @param propertyName
	 *           String that will be suffix of each property.
	 */
	public TreeNodeModel(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Returns the propertyName.
	 * 
	 * @return propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getPropertyName();
	}
}

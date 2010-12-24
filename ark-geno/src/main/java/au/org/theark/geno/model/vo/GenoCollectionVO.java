/**
 * 
 */
package au.org.theark.geno.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import au.org.theark.geno.model.entity.GenoCollection;

/**
 * @author elam
 *
 */
public class GenoCollectionVO implements Serializable {

	private GenoCollection genoCollection;
	private java.util.Collection<GenoCollection> genoCollectionList;
	
	
	public GenoCollectionVO() {
		this.genoCollection = new GenoCollection();
		this.genoCollectionList = new ArrayList<GenoCollection>();
	}

	public GenoCollection getGenoCollection() {
		return genoCollection;
	}
	
	public void setGenoCollection(GenoCollection genoCollection) {
		this.genoCollection = genoCollection;
	}
	
	public java.util.Collection<GenoCollection> getGenoCollectionCollection() {
		return genoCollectionList;
	}
	
	public void setGenoCollectionCollection(
			java.util.Collection<GenoCollection> genoCollectionCollection) {
		this.genoCollectionList = genoCollectionCollection;
	}


}

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
	private java.util.Collection<GenoCollection> genoCollectionCollection;
	
	
	public GenoCollectionVO() {
		this.genoCollection = new GenoCollection();
		this.genoCollectionCollection = new ArrayList<GenoCollection>();
	}

	public GenoCollection getGenoCollection() {
		return genoCollection;
	}
	
	public void setGenoCollection(GenoCollection genoCollection) {
		this.genoCollection = genoCollection;
	}
	
	public java.util.Collection<GenoCollection> getGenoCollectionCollection() {
		return genoCollectionCollection;
	}
	
	public void setGenoCollectionCollection(
			java.util.Collection<GenoCollection> genoCollectionCollection) {
		this.genoCollectionCollection = genoCollectionCollection;
	}


}

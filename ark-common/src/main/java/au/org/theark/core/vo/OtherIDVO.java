package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import au.org.theark.core.model.study.entity.OtherID;

public class OtherIDVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected OtherID otherID;
	protected Collection<OtherID> otherIDs;
	
	public OtherIDVO() {
		otherID = new OtherID();
		otherIDs = new ArrayList<OtherID>();
	}
	
	public OtherID getOtherID() {
		return otherID;
	}
	
	public void setOtherID(OtherID otherID) {
		this.otherID = otherID;
	}
	
	public Collection<OtherID> getOtherIDs() {
		return otherIDs;
	}
	
	public void setOterIDs(Collection<OtherID> otherIDs) {
		this.otherIDs = otherIDs;
	}	
}
package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import au.org.theark.core.model.study.entity.Correspondences;

public class CorrespondenceVO implements Serializable {

	private Correspondences correspondence;
	private Collection<Correspondences> correspondenceList;
	
	public CorrespondenceVO() {
		correspondence = new Correspondences();
		correspondenceList = new ArrayList<Correspondences>();
	}
	
	public Correspondences getCorrespondence() {
		return correspondence;
	}
	
	public void setCorrespondence(Correspondences correspondence) {
		this.correspondence = correspondence;
	}
	
	public Collection<Correspondences> getCorrespondenceList() {
		return correspondenceList;
	}
	
	public void setCorrespondenceList(Collection<Correspondences> correspondenceList) {
		this.correspondenceList = correspondenceList;
	}
	
}

package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import au.org.theark.core.model.study.entity.CorrespondenceAttachment;
import au.org.theark.core.model.study.entity.Correspondences;

public class CorrespondenceVO implements Serializable {

	private Correspondences correspondence;
	private Collection<Correspondences> correspondenceList;
	// TODO: why is this single item needed if we have a collection?
	private CorrespondenceAttachment correspondenceAttachment;
	private Collection<CorrespondenceAttachment> correspondenceAttachmentList;

	public CorrespondenceVO() {
		correspondence = new Correspondences();
		correspondenceList = new ArrayList<Correspondences>();
		correspondenceAttachment = new CorrespondenceAttachment();
		correspondenceAttachmentList = new ArrayList<CorrespondenceAttachment>();
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

	public CorrespondenceAttachment getCorrespondenceAttachment() {
		return correspondenceAttachment;
	}

	public void setCorrespondenceAttachment(
			CorrespondenceAttachment correspondenceAttachment) {
		this.correspondenceAttachment = correspondenceAttachment;
	}

	public Collection<CorrespondenceAttachment> getCorrespondenceAttachmentList() {
		return correspondenceAttachmentList;
	}

	public void setCorrespondenceAttachmentList(
			Collection<CorrespondenceAttachment> correspondenceAttachmentList) {
		this.correspondenceAttachmentList = correspondenceAttachmentList;
	}
	
}

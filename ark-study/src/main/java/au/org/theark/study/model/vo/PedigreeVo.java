package au.org.theark.study.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PedigreeVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<RelationshipVo> relationshipList;
	


	public PedigreeVo() {
		relationshipList=new ArrayList<RelationshipVo>();
	}


	public List<RelationshipVo> getRelationshipList() {
		return relationshipList;
	}


	public void setRelationshipList(List<RelationshipVo> relationshipList) {
		this.relationshipList = relationshipList;
	}
}

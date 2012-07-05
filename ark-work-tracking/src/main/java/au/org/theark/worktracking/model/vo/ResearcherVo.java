package au.org.theark.worktracking.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import au.org.theark.core.model.worktracking.entity.Researcher;

public class ResearcherVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Researcher researcher;
	
	private List<Researcher> researcherList;
	
	private int						mode;
	
	public ResearcherVo(){
		researcher=new Researcher();
		researcherList=new ArrayList<Researcher>();
	}

	public Researcher getResearcher() {
		return researcher;
	}

	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}

	public List<Researcher> getResearcherList() {
		return researcherList;
	}

	public void setResearcherList(List<Researcher> researcherList) {
		this.researcherList = researcherList;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
	
}

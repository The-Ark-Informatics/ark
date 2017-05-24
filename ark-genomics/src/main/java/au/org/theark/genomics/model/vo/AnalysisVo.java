package au.org.theark.genomics.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.spark.entity.Analysis;
import au.org.theark.core.vo.ArkVo;

public class AnalysisVo implements ArkVo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Analysis analysis;
	
	private List<Analysis> analysisList;
	
	private String file;

	public AnalysisVo() {
		analysis = new Analysis();
		analysisList = new ArrayList<Analysis>();
	}

	public Analysis getAnalysis() {
		return analysis;
	}

	public void setAnalysis(Analysis analysis) {
		this.analysis = analysis;
	}

	public List<Analysis> getAnalysisList() {
		return analysisList;
	}

	public void setAnalysisList(List<Analysis> analysisList) {
		this.analysisList = analysisList;
	}
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	@Override
	public String getArkVoName(){
		return "Analysis";
	}

}

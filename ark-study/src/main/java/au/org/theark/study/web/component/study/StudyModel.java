package au.org.theark.study.web.component.study;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.IModel;

import au.org.theark.study.model.entity.Study;

@SuppressWarnings("serial")
public class StudyModel implements IModel{

	private Study study;
	private List<String> lmcAvailableApps;
	private List<String> lmcSelectedApps;

	public StudyModel(){
		study = new Study();
		lmcAvailableApps = new ArrayList<String>();
		lmcSelectedApps = new ArrayList<String>();
	}

	public List<String> getLmcAvailableApps() {
		return lmcAvailableApps;
	}

	public void setLmcAvailableApps(List<String> lmcAvailableApps) {
		this.lmcAvailableApps = lmcAvailableApps;
	}

	public List<String> getLmcSelectedApps() {
		return lmcSelectedApps;
	}

	public void setLmcSelectedApps(List<String> lmcSelectedApps) {
		this.lmcSelectedApps = lmcSelectedApps;
	}
	
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}


	public Object getObject() {
		return this;
	}

	public void setObject(Object object) {
		
	}

	public void detach() {
	}
	
	
	
}

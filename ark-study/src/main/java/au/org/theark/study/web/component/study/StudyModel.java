package au.org.theark.study.web.component.study;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.model.IModel;

import au.org.theark.study.model.entity.Study;

@SuppressWarnings("serial")
public class StudyModel implements IModel{

	private Study study;
	
	private Set<String> lmcAvailableApps = new HashSet<String>();
	private Set<String> lmcSelectedApps = new HashSet<String>();
	
	
	public StudyModel(){
		study = new Study();
	}
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Set<String> getLmcAvailableApps() {
		return lmcAvailableApps;
	}
	public void setLmcAvailableApps(Set<String> lmcAvailableApps) {
		this.lmcAvailableApps = lmcAvailableApps;
	}
	public Set<String> getLmcSelectedApps() {
		return lmcSelectedApps;
	}
	public void setLmcSelectedApps(Set<String> lmcSelectedApps) {
		this.lmcSelectedApps = lmcSelectedApps;
	}
	

	public Object getObject() {
		return this;
	}

	public void setObject(Object object) {
		
	}

	public void detach() {
	}
	
	
	
}

package au.org.theark.genomics.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.spark.entity.MicroService;
import au.org.theark.core.vo.ArkVo;


public class MicroServiceVo implements ArkVo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MicroService microService;
	
	private List<MicroService> microServiceList;
	
	private int						mode;
	
	public MicroServiceVo() {
		microService = new MicroService();
		microServiceList = new ArrayList<MicroService>();
	}

	public MicroService getMicroService() {
		return microService;
	}

	public void setMicroService(MicroService microService) {
		this.microService = microService;
	}

	public List<MicroService> getMicroServiceList() {
		return microServiceList;
	}

	public void setMicroServiceList(List<MicroService> microServiceList) {
		this.microServiceList = microServiceList;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}	
	
	@Override
	public String getArkVoName(){
		return "Microservice";
	}
}
package au.org.theark.genomics.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.spark.entity.Computation;

public class ComputationVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Computation computation;

	private List<Computation> computationList;

	private String file;

	public ComputationVo() {
		this.computation = new Computation();
		this.computation.setAvailable(Boolean.FALSE);
		this.computationList = new ArrayList<Computation>();
	}

	public Computation getComputation() {
		return computation;
	}

	public void setComputation(Computation computation) {
		this.computation = computation;
	}

	public List<Computation> getComputationList() {
		return computationList;
	}

	public void setComputationList(List<Computation> computationList) {
		this.computationList = computationList;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}

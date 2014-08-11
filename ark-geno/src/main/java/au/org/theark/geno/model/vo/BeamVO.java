package au.org.theark.geno.model.vo;

import au.org.theark.core.model.geno.entity.Beam;
import au.org.theark.core.vo.BaseVO;

public class BeamVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	private Beam beam;
	
	public BeamVO() {
		super();
		beam = new Beam();
	}

	public BeamVO(Beam beam) {
		this.beam = beam;
	}

	public Beam getBeam() {
		return beam;
	}

	public void setBeam(Beam beam) {
		this.beam = beam;
	}

	@Override
	public String toString() {
		return "BeamVO [beam=" + beam + "]";
	}	
}

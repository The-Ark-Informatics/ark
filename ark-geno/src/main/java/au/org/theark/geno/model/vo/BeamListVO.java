package au.org.theark.geno.model.vo;

import java.util.ArrayList;
import java.util.Collection;

import au.org.theark.core.vo.BaseVO;
import au.org.theark.core.vo.ArkVo;

public class BeamListVO extends BaseVO implements ArkVo {

	private static final long serialVersionUID = 1L;

	private Collection<BeamVO> beams = new ArrayList<BeamVO>();

	public BeamListVO() {
		super();
		beams = new ArrayList<BeamVO>();
	}

	public BeamListVO(Collection<BeamVO> beams) {
		super();
		this.beams = beams;
	}

	public Collection<BeamVO> getBeams() {
		return beams;
	}

	public void setBeams(Collection<BeamVO> beams) {
		this.beams = beams;
	}

	@Override
	public String toString() {
		return "BeamListVO [beams=" + beams + "]";
	}
	
	@Override
	public String getArkVoName(){
		return "Column";
	}
}

package au.org.theark.disease.vo;

import au.org.theark.core.model.disease.entity.Affection;
import au.org.theark.core.vo.BaseVO;
import au.org.theark.core.vo.ArkVo;

public class AffectionVO extends BaseVO implements ArkVo{
	
	private Affection affection;
	
	public AffectionVO() {
		this.affection = new Affection();
	}
	
	public AffectionVO(Affection affection) {
		this.affection = affection;
	}

	public Affection getAffection() {
		return affection;
	}

	public void setAffection(Affection affection) {
		this.affection = affection;
	}
	
	@Override
	public String getArkVoName(){
		return "Affection";
	}

}

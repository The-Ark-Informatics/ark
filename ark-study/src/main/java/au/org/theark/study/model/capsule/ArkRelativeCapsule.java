package au.org.theark.study.model.capsule;

import java.io.Serializable;

import com.x5.util.DataCapsule;

public class ArkRelativeCapsule implements DataCapsule, Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private String	 individualId;
	private String	 fatherId;
	private String	 motherId;
	private String	 twinStatus;
	private String	 twinId;
	
	private String line="";

	
	
	public String getIndividualId() {
		return individualId;
	}

	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}

	public String getFatherId() {
		return fatherId;
	}

	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}

	public String getMotherId() {
		return motherId;
	}

	public void setMotherId(String motherId) {
		this.motherId = motherId;
	}

	public String getTwinStatus() {
		return twinStatus;
	}

	public void setTwinStatus(String twinStatus) {
		this.twinStatus = twinStatus;
	}

	public String getTwinId() {
		return twinId;
	}

	public void setTwinId(String twinId) {
		this.twinId = twinId;
	}

	public String getLine() {
		return line;
	}

	public String[] getExports() {
		return new String[] { 
				"getIndividualId", 
				"getFatherId", 
				"getMotherId", 
				"getTwinStatus", 
				"getTwinId",
				"getLine"
				};
	}

	public String getExportPrefix() {
		// TODO Auto-generated method stub
		return "arkrelative";
	}

}

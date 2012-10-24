package au.org.theark.worktracking.model.vo;

import java.io.Serializable;

import au.org.theark.core.model.worktracking.entity.WorkRequest;

/**
 * Value object use to validate the {@link WorkRequest} GST properties.
 * @author thilina
 *
 */
public class WorkRequestBillableItemVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long billableItemCount;
	
	private Boolean gstAllow;
	
	private Double gst;

	public Long getBillableItemCount() {
		return billableItemCount;
	}

	public void setBillableItemCount(Long billableItemCount) {
		this.billableItemCount = billableItemCount;
	}

	public Boolean getGstAllow() {
		return gstAllow;
	}

	public void setGstAllow(Boolean gstAllow) {
		this.gstAllow = gstAllow;
	}

	public Double getGst() {
		return gst;
	}

	public void setGst(Double gst) {
		this.gst = gst;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((billableItemCount == null) ? 0 : billableItemCount
						.hashCode());
		result = prime * result + ((gst == null) ? 0 : gst.hashCode());
		result = prime * result
				+ ((gstAllow == null) ? 0 : gstAllow.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkRequestBillableItemVo other = (WorkRequestBillableItemVo) obj;
		if (billableItemCount == null) {
			if (other.billableItemCount != null)
				return false;
		} else if (!billableItemCount.equals(other.billableItemCount))
			return false;
		if (gst == null) {
			if (other.gst != null)
				return false;
		} else if (!gst.equals(other.gst))
			return false;
		if (gstAllow == null) {
			if (other.gstAllow != null)
				return false;
		} else if (!gstAllow.equals(other.gstAllow))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WorkRequestBillableItemVo [billableItemCount="
				+ billableItemCount + ", gstAllow=" + gstAllow + ", gst=" + gst
				+ "]";
	}
	
	

}

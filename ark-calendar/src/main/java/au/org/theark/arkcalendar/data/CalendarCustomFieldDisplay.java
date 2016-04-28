package au.org.theark.arkcalendar.data;

import java.io.Serializable;

public class CalendarCustomFieldDisplay implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long customFieldId;
	private Long customFieldGroupId;
	private Boolean required;
	private String requiredMessage;
	private Boolean allowMultiselect = Boolean.FALSE;
	private Long sequence;
	private String descriptiveNameIncludingCFGName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustomFieldId() {
		return customFieldId;
	}
	public void setCustomFieldId(Long customFieldId) {
		this.customFieldId = customFieldId;
	}
	public Long getCustomFieldGroupId() {
		return customFieldGroupId;
	}
	public void setCustomFieldGroupId(Long customFieldGroupId) {
		this.customFieldGroupId = customFieldGroupId;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public String getRequiredMessage() {
		return requiredMessage;
	}
	public void setRequiredMessage(String requiredMessage) {
		this.requiredMessage = requiredMessage;
	}
	public Boolean getAllowMultiselect() {
		return allowMultiselect;
	}
	public void setAllowMultiselect(Boolean allowMultiselect) {
		this.allowMultiselect = allowMultiselect;
	}
	public Long getSequence() {
		return sequence;
	}
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	public String getDescriptiveNameIncludingCFGName() {
		return descriptiveNameIncludingCFGName;
	}
	public void setDescriptiveNameIncludingCFGName(String descriptiveNameIncludingCFGName) {
		this.descriptiveNameIncludingCFGName = descriptiveNameIncludingCFGName;
	}
}

package au.org.theark.core.vo;

import org.apache.commons.lang.BooleanUtils;

import au.org.theark.core.model.study.entity.EmailAccount;
import au.org.theark.core.model.study.entity.EmailAccountType;
import au.org.theark.core.model.study.entity.EmailStatus;

public class EmailAccountSubjectVo {
	
	private EmailAccount emailAccount;
	private String subjectUID;
	
	public EmailAccountSubjectVo(EmailAccount emailAccount, String subjectUID) {
		this.emailAccount = emailAccount;
		this.subjectUID = subjectUID;
	}
	
	public Long getId() {
		return this.emailAccount.getId();
	}
	
	public String getSubjectUID() {
		return this.subjectUID;
	}
	
	public String getName() {
		return this.emailAccount.getName();
	}
	
	public String getEmailAccountType(){
		EmailAccountType type = this.emailAccount.getEmailAccountType(); 
		return type!=null ? type.getName() : "";
	}
	
	public String getEmailStatus(){
		EmailStatus status = this.emailAccount.getEmailStatus(); 
		return status!=null ? status.getName() : "";
	}
	
	public String getPrimaryEmail(){
		return BooleanUtils.isTrue(this.emailAccount.getPrimaryAccount())?"Yes":"No";
	}
}

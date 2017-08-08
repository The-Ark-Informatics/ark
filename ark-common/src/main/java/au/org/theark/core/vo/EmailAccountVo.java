package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.study.entity.EmailAccount;

public class EmailAccountVo implements ArkVo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EmailAccount emailAccount;

	private List<EmailAccount> emailAccountList;

	public EmailAccountVo() {
		this.emailAccount = new EmailAccount();
		this.emailAccountList = new ArrayList<EmailAccount>();
	}

	public EmailAccount getEmailAccount() {
		return emailAccount;
	}

	public void setEmailAccount(EmailAccount emailAccount) {
		this.emailAccount = emailAccount;
	}

	public List<EmailAccount> getEmailAccountList() {
		return emailAccountList;
	}

	public void setEmailAccountList(List<EmailAccount> emailAccountList) {
		this.emailAccountList = emailAccountList;
	}

	@Override
	public String getArkVoName() {
		// TODO Auto-generated method stub
		return "Email Account";
	}

}

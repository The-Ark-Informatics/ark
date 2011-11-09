package au.org.theark.core.dao;

import java.io.Serializable;

/**
 * reCAPTCHA source bean
 * 
 * @author cellis
 * 
 */
public class ReCaptchaContextSource implements Serializable{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4403517218839438932L;
	private String	reCaptchaPublicKey;
	private String	reCaptchaPrivateKey;

	/**
	 * @return the reCaptchaPublicKey
	 */
	public String getReCaptchaPublicKey() {
		return reCaptchaPublicKey;
	}

	/**
	 * @param reCaptchaPublicKey
	 *           the reCaptchaPublicKey to set
	 */
	public void setReCaptchaPublicKey(String reCaptchaPublicKey) {
		this.reCaptchaPublicKey = reCaptchaPublicKey;
	}

	/**
	 * @return the reCaptchaPrivateKey
	 */
	public String getReCaptchaPrivateKey() {
		return reCaptchaPrivateKey;
	}

	/**
	 * @param reCaptchaPrivateKey
	 *           the reCaptchaPrivateKey to set
	 */
	public void setReCaptchaPrivateKey(String reCaptchaPrivateKey) {
		this.reCaptchaPrivateKey = reCaptchaPrivateKey;
	}
}

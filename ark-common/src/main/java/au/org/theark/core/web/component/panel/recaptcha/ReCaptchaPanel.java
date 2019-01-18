package au.org.theark.core.web.component.panel.recaptcha;

import javax.servlet.http.HttpServletRequest;

/*import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;*/

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.Protocol;

/**
 * Displays recaptcha widget. It is configured using a pair of public/private keys which can be registered at the following location:
 * 
 * https://www.google.com/recaptcha/admin/create <br>
 * More details about recaptcha API: http://code.google.com/apis/recaptcha/intro.html
 * 
 * @author Alex Objelean
 * As referenced: http://alexo-web.blogspot.com.au/2011/02/integrating-recaptcha-in-wicket.html
 */
public class ReCaptchaPanel extends Panel {
	
	static Logger log = LoggerFactory.getLogger(ReCaptchaPanel.class);

	private static final long			serialVersionUID	= -6440830868624897070L;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;
	
	public ReCaptchaPanel(final String id, final Protocol protocol) {
		super(id);

		/*FormComponent<Void> formComponent = new FormComponent<Void>("captcha") {
		
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
//TODO: keep LEI and Chris code and do switch/if etc for whatever works.
// D'ark LEI George uses ReCaptcha recaptcha = ReCaptchaFactory.newSecureReCaptcha(iArkCommonService.getRecaptchaContextSource().getReCaptchaPublicKey(), iArkCommonService.getRecaptchaContextSource().getReCaptchaPrivateKey(), false);
// ReCaptcha recaptcha = ReCaptchaFactory.newReCaptcha(iArkCommonService.getRecaptchaContextSource().getReCaptchaPublicKey(), iArkCommonService.getRecaptchaContextSource().getReCaptchaPrivateKey(), false);
				ReCaptcha recaptcha = null;
				switch(protocol) {
					case HTTPS:
						recaptcha = ReCaptchaFactory.newSecureReCaptcha(iArkCommonService.getRecaptchaContextSource().getReCaptchaPublicKey(), iArkCommonService.getRecaptchaContextSource().getReCaptchaPrivateKey(), false);
						break;
					case HTTP:
						recaptcha = ReCaptchaFactory.newReCaptcha(iArkCommonService.getRecaptchaContextSource().getReCaptchaPublicKey(), iArkCommonService.getRecaptchaContextSource().getReCaptchaPrivateKey(), false);
						break;
					default:
						recaptcha = ReCaptchaFactory.newReCaptcha(iArkCommonService.getRecaptchaContextSource().getReCaptchaPublicKey(), iArkCommonService.getRecaptchaContextSource().getReCaptchaPrivateKey(), false);
						break;
				}
				replaceComponentTagBody(markupStream, openTag, recaptcha.createRecaptchaHtml(null, null));
			}
			
			@Override
			public void validate() {
				HttpServletRequest servletReq = (HttpServletRequest) getRequest().getContainerRequest();
				
				String remoteAddr = servletReq.getRemoteAddr();
				ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
				
				reCaptcha.setPrivateKey(iArkCommonService.getRecaptchaContextSource().getReCaptchaPrivateKey());

//George made change to give useful responses on not returning/coonnecting/null.				
				ReCaptchaResponse reCaptchaResponse = null;
				try {
					final String challenge = servletReq.getParameterValues("recaptcha_challenge_field")[0];
					final String uresponse = servletReq.getParameterValues("recaptcha_response_field")[0];
					reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
				} catch (NullPointerException ne) {
					log.info("Null pointer caught, recaptcha_challenge_field not found as a parameter");
					this.error("reCAPTCHA not entered. Try again"); //TODO  Test varios instances/issues
				}

				if (reCaptchaResponse != null && !reCaptchaResponse.isValid()) {
					this.error("Invalid reCAPTCHA value, please enter the reCAPTCHA words again!");
				}
			}
		};

		add(formComponent);*/
	}
}
package au.org.theark.core.web.component.panel.recaptcha;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.service.IArkCommonService;

/**
 * Displays recaptcha widget. It is configured using a pair of public/private keys which can be registered at the following location:
 * 
 * https://www.google.com/recaptcha/admin/create <br>
 * More details about recaptcha API: http://code.google.com/apis/recaptcha/intro.html
 * 
 * @author cellis
 */
public class ReCaptchaPanel extends Panel {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= -6440830868624897070L;
	private static final Logger		log					= LoggerFactory.getLogger(ReCaptchaPanel.class);
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;
	
	public ReCaptchaPanel(final String id) {
		super(id);
		final ReCaptcha recaptcha = ReCaptchaFactory.newReCaptcha(iArkCommonService.getRecaptchaContextSource().getReCaptchaPublicKey(), iArkCommonService.getRecaptchaContextSource().getReCaptchaPrivateKey(), false);

		FormComponent<Void> formComponent = new FormComponent<Void>("captcha") {
			/**
	 		 * 
	 		 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
				replaceComponentTagBody(markupStream, openTag, recaptcha.createRecaptchaHtml(null, null));
			}

			@Override
			public void validate() {
				HttpServletRequest servletReq = (HttpServletRequest) getRequest().getContainerRequest(); 
				String remoteAddr = servletReq.getRemoteAddr();
				ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
				
				reCaptcha.setPrivateKey(iArkCommonService.getRecaptchaContextSource().getReCaptchaPrivateKey());

				final String challenge = servletReq.getParameterValues("recaptcha_challenge_field")[0];
				log.info("challenge: " + challenge);
				
				final String uresponse = servletReq.getParameterValues("recaptcha_response_field")[0];
				log.info("uresponse: " + uresponse);
				
				final ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
				log.info("reCaptchaResponse: " + reCaptchaResponse.toString());

				if (!reCaptchaResponse.isValid()) {
					log.error("Wrong reCAPTCHA value");
					this.error("Invalid reCAPTCHA value, please enter the reCAPTCHA word again!");
				}
				else{
					log.info("reCAPTCHA words ok");
					this.info("reCAPTCHA words ok");
				}
			}
		};

		add(formComponent);
	}
}
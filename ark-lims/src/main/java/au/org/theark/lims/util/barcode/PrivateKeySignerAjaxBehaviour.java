package au.org.theark.lims.util.barcode;

import au.org.theark.core.Constants;
import au.org.theark.core.service.IArkCommonService;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.apache.wicket.ThreadContext.getRequestCycle;

public class PrivateKeySignerAjaxBehaviour extends AbstractDefaultAjaxBehavior {

    private transient static Logger log	= LoggerFactory.getLogger(PrivateKeySignerAjaxBehaviour.class);

    @SpringBean(name = Constants.ARK_COMMON_SERVICE)
    protected IArkCommonService<Void> iArkCommonService;

    @Autowired
    public void setiArkCommonService(IArkCommonService iArkCommonService) {
        this.iArkCommonService = iArkCommonService;
    }

    @Override
    protected void respond(AjaxRequestTarget target) {
        String user = (String) SecurityUtils.getSubject().getPrincipal();
        if(user != null) {
            String request = String.valueOf(getRequestCycle().getRequest().getRequestParameters().getParameterValue("request"));

            String signature = null;
            try {
                MessageSigner signer = new MessageSigner("/private-key.pem", iArkCommonService);
                signature = signer.sign(request).replace("\n", "");

                RequestCycle requestCycle = getRequestCycle();
                requestCycle.scheduleRequestHandlerAfterCurrent(null);

                WebResponse response = (WebResponse) requestCycle.getResponse();
                response.setContentLength(signature.length());
                response.setContentType("text/plain;charset=UTF8");
                response.write(signature);
            } catch (Exception e) {
                log.warn("Private Key not available, interruption free printing disabled.");
                e.printStackTrace();
            }
        }
    }
}

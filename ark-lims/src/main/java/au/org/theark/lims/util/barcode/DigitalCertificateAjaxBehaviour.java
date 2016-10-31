package au.org.theark.lims.util.barcode;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.apache.wicket.ThreadContext.getRequestCycle;

public class DigitalCertificateAjaxBehaviour extends AbstractDefaultAjaxBehavior {

    private transient static Logger log	= LoggerFactory.getLogger(DigitalCertificateAjaxBehaviour.class);

    @Override
    protected void respond(AjaxRequestTarget target) {
        String user = (String) SecurityUtils.getSubject().getPrincipal();
        if(user != null) {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(DigitalCertificateAjaxBehaviour.class.getResourceAsStream("/digital-certificate.txt")));
                StringBuilder builder = new StringBuilder();
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String digitalCert = builder.toString();

                RequestCycle requestCycle = getRequestCycle();
                requestCycle.scheduleRequestHandlerAfterCurrent(null);

                WebResponse response = (WebResponse) requestCycle.getResponse();
                response.setHeader("Content-Type", "text/plain;charset=UTF8");
                response.write(digitalCert);
            } catch (NullPointerException npe) {
                log.warn("Digital Certificate not available, interruption free printing disabled.");
                npe.printStackTrace();
            }
        }
    }
}

package au.org.theark.lims.util.barcode;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PrintZPL {

    private static final Logger log = LoggerFactory.getLogger(PrintZPL.class);

    private static PrivateKeySignerAjaxBehaviour privateKeyBehaviour;
    private static DigitalCertificateAjaxBehaviour digitalCertificateBehaviour;

    public static void printZPL(AjaxRequestTarget target, Component parentComponent, String printerName, List<String> ZPLCommands, int numberOfCopies) {
        printZPL(target, parentComponent, printerName, ZPLCommands, numberOfCopies, false);
    }

    public static void printZPL(AjaxRequestTarget target, Component parentComponent, String printerName, List<String> ZPLCommands, int numberOfCopies, boolean debug) {

        StringBuilder stringBuilder = new StringBuilder();

        privateKeyBehaviour = new PrivateKeySignerAjaxBehaviour();
        parentComponent.add(privateKeyBehaviour);

        digitalCertificateBehaviour = new DigitalCertificateAjaxBehaviour();
        parentComponent.add(digitalCertificateBehaviour);

        stringBuilder.append("setupSigning(\"")
                .append(digitalCertificateBehaviour.getCallbackUrl())
                .append("\",\"")
                .append(privateKeyBehaviour.getCallbackUrl())
                .append("\");")
                .append("\n");

        stringBuilder.append("printBarcode(\"")
                .append(printerName)
                .append("\", [");

        for(int i = 1; i <= numberOfCopies; i++) {
            for(int z = 0; z < ZPLCommands.size(); z++) {
                String ZPL = ZPLCommands.get(z).replace("\r\n", "\\n");
                stringBuilder.append("'")
                        .append(ZPL)
                        .append("\\n'");
                if (z != (ZPLCommands.size() - 1)) {
                    stringBuilder.append(",");
                }
            }
            if (i != numberOfCopies) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]);");

        String js = stringBuilder.toString();

        if(debug) {
            log.debug(js);
        }
        target.appendJavaScript(js);

    }

}

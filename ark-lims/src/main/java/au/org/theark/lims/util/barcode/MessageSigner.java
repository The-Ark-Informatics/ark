package au.org.theark.lims.util.barcode;

import au.org.theark.core.service.IArkCommonService;
import com.itextpdf.text.pdf.codec.Base64;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.*;
import java.security.*;

/**
 * Utility for creating an RSA SHA1 signature based on a supplied PEM formatted private key
 */
public class MessageSigner {
    private Signature sig;

    /**
         * Constructs an RSA SHA1 signature object for signing
         * @param keyPath
         * @throws Exception
         */
    public MessageSigner(String keyPath, IArkCommonService iArkCommonService) throws Exception {

        Reader reader = new BufferedReader(new InputStreamReader(MessageSigner.class.getResourceAsStream(keyPath)));

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        String privateKeyPassword = iArkCommonService.getBarcodePrivateKeyPassword();
        PEMReader pr = new PEMReader(reader, new PasswordFinder() {
            @Override
            public char[] getPassword() {
                return privateKeyPassword.toCharArray();
            }
        });

        KeyPair kp = (KeyPair) pr.readObject();

        PrivateKey key = kp.getPrivate();
        sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
    }

    /**
     * Signs the specified data with the provided private key, returning the
     * RSA SHA1 signature
     * @param data
     * @return
     * @throws Exception
     */
    public String sign(String data) throws Exception {
        sig.update(data.getBytes());
        return Base64.encodeBytes(sig.sign());
    }

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

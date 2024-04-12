package com.k11.automation.coreframework.util;
import org.apache.commons.codec.binary.Base64;
import com.k11.automation.coreframework.exceptions.AutomationError;

import java.io.UnsupportedEncodingException;
public class Base64PasswordDecryptor implements PasswordDecryptor {
        @Override
        public String getDecryptedPassword(String encriptedPassword) {
            byte[] decoded = Base64.decodeBase64(encriptedPassword);
            String decrypted;
            try {
                decrypted = new String(decoded, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new AutomationError("Unable to decrypt password", e);
            }
            return decrypted;
        }
}

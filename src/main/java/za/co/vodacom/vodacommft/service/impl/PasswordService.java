package za.co.vodacom.vodacommft.service.impl;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service.impl
 */

import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.service.IPasswordService;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

@Service
public class PasswordService implements IPasswordService {

    @Override
    public String encrypt(String value, String salt) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance("AES");
        c.init(1, key);
        String valueToEnc = null;
        String eValue = value;
        for (int i = 0; i < 2; i++) {
            valueToEnc = salt + eValue;
            byte[] encValue = c.doFinal(valueToEnc.getBytes());
            //eValue = new BASE64Encoder().encode(encValue);
            eValue = DatatypeConverter.printBase64Binary(encValue);
        }
        return eValue;
    }


    @Override
    public String decrypt(String value, String salt) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance("AES");
        c.init(2, key);
        String dValue = null;
        String valueToDecrypt = value;
        for (int i = 0; i < 2; i++) {
            //byte[] decordedValue = new BASE64Decoder().decodeBuffer(valueToDecrypt);
            byte[] decordedValue = DatatypeConverter.parseBase64Binary(valueToDecrypt);
            System.out.println(new String(decordedValue, "UTF-8"));
            byte[] decValue = c.doFinal(decordedValue);
            dValue = new String(decValue).substring(salt.length());
            valueToDecrypt = dValue;
        }
        return dValue;
    }

    private Key generateKey() throws Exception {
            final byte[] keyValue = { 84, 104, 105, 115, 73, 115, 78, 48, 116, 64, 83, 101, 99, 114, 101, 116 };
            Key key = new SecretKeySpec(keyValue, "AES");

            return key;
        }
}

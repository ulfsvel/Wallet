package ro.cheiafermecata.wallet.service;

import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AesServiceTest {

    @Test
    public void testEncryptionAndDecryption() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        String initialValue = "String to encrypt";
        String password = "password";

        AesService aesService = new AesService();
        String encryptedValue = aesService.encrypt(initialValue, password);
        String decryptedValue = aesService.decrypt(encryptedValue, password);

        assertEquals(decryptedValue, initialValue);
    }


}

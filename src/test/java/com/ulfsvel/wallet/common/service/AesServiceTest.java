package com.ulfsvel.wallet.common.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AesServiceTest {

    @Test
    public void testEncryptionAndDecryption() {
        String initialValue = "String to encrypt";
        String password = "password";
        String salt = AesService.createSalt();

        AesService aesService = new AesService(password, salt);
        String encryptedValue = aesService.encrypt(initialValue);
        String decryptedValue = aesService.decrypt(encryptedValue);

        assertEquals(decryptedValue, initialValue);
    }


}

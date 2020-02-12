package com.ulfsvel.wallet.common.service;


import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class AesService {

    private final BytesEncryptor encryptor;

    public AesService(String secret, String salt) {
        encryptor = Encryptors.stronger(secret, salt);
    }

    public static String createSalt() {
        return KeyGenerators.string().generateKey();
    }

    public String encrypt(String strToEncrypt) {
        return Base64.getEncoder().encodeToString(
                encryptor.encrypt(
                        strToEncrypt.getBytes(StandardCharsets.UTF_8)
                )
        );
    }

    public String decrypt(String strToDecrypt) {
        return new String(
                encryptor.decrypt(
                        Base64.getDecoder().decode(
                                strToDecrypt.getBytes(StandardCharsets.UTF_8)
                        )
                )
        );
    }
}

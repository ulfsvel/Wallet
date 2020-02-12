package com.ulfsvel.wallet.common.entiry.security;


import com.ulfsvel.wallet.common.entiry.Wallet;

import javax.persistence.*;

@Entity
public class ShamirBasicSecurity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String sharedEncryptionKey;
    private String encryptedEncryptionKey;
    private String encryptionSalt;

    @OneToOne
    @JoinColumn()
    private Wallet wallet;

    public ShamirBasicSecurity() {
    }

    public String getEncryptionSalt() {
        return encryptionSalt;
    }

    public ShamirBasicSecurity setEncryptionSalt(String encryptionSalt) {
        this.encryptionSalt = encryptionSalt;
        return this;
    }

    public Long getId() {
        return id;
    }

    public ShamirBasicSecurity setId(Long id) {
        this.id = id;
        return this;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public ShamirBasicSecurity setWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }

    public String getSharedEncryptionKey() {
        return sharedEncryptionKey;
    }

    public ShamirBasicSecurity setSharedEncryptionKey(String sharedEncryptionKey) {
        this.sharedEncryptionKey = sharedEncryptionKey;
        return this;
    }

    public String getEncryptedEncryptionKey() {
        return encryptedEncryptionKey;
    }

    public ShamirBasicSecurity setEncryptedEncryptionKey(String encryptedEncryptionKey) {
        this.encryptedEncryptionKey = encryptedEncryptionKey;
        return this;
    }
}

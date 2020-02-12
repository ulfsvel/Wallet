package com.ulfsvel.wallet.common.entiry;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Wallet {
    public static final int SHAMIR_BASIC_SECURITY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String encryptedPrivateKey;
    private String publicKey;
    private String publicAddress;
    private int walletSecurityType;

    public Wallet() {
    }

    public Long getId() {
        return id;
    }

    public String getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    public Wallet setEncryptedPrivateKey(String encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public Wallet setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public Wallet setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
        return this;
    }

    public int getWalletSecurityType() {
        return walletSecurityType;
    }

    public Wallet setWalletSecurityType(int walletSecurityType) {
        this.walletSecurityType = walletSecurityType;
        return this;
    }
}

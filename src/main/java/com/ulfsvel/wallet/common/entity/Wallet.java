package com.ulfsvel.wallet.common.entity;


import com.ulfsvel.wallet.common.enums.WalletSecurityType;
import com.ulfsvel.wallet.common.enums.WalletType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String encryptedPrivateKey;
    private String publicKey;
    private String publicAddress;
    private WalletSecurityType walletSecurityType;
    private WalletType walletType;

    public Wallet() {
    }

    public Wallet(UnencryptedWallet wallet) {
        id = wallet.getId();
        publicKey = wallet.getPublicKey();
        publicAddress = wallet.getPublicAddress();
        walletType = wallet.getWalletType();
    }

    public Long getId() {
        return id;
    }

    public Wallet setId(Long id) {
        this.id = id;
        return this;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public Wallet setWalletType(WalletType walletType) {
        this.walletType = walletType;
        return this;
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

    public WalletSecurityType getWalletSecurityType() {
        return walletSecurityType;
    }

    public Wallet setWalletSecurityType(WalletSecurityType walletSecurityType) {
        this.walletSecurityType = walletSecurityType;
        return this;
    }
}

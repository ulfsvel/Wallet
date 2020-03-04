package com.ulfsvel.wallet.common.entity.security;


import com.ulfsvel.wallet.common.entity.Wallet;

import javax.persistence.*;

@Entity
public class AesBasicSecurity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String encryptionSalt;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Wallet wallet;

    public AesBasicSecurity() {
    }

    public String getEncryptionSalt() {
        return encryptionSalt;
    }

    public AesBasicSecurity setEncryptionSalt(String encryptionSalt) {
        this.encryptionSalt = encryptionSalt;
        return this;
    }

    public Long getId() {
        return id;
    }

    public AesBasicSecurity setId(Long id) {
        this.id = id;
        return this;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public AesBasicSecurity setWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }
}

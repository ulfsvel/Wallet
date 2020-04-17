package com.ulfsvel.wallet.common.entity.security;


import com.ulfsvel.wallet.common.entity.Wallet;

import javax.persistence.*;

@Entity
public class ShamirAdvancedSecurity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String encryptionSalt;
    private Integer sharesToRebuild;
    private Integer totalShares;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Wallet wallet;

    public ShamirAdvancedSecurity() {
    }

    public String getEncryptionSalt() {
        return encryptionSalt;
    }

    public ShamirAdvancedSecurity setEncryptionSalt(String encryptionSalt) {
        this.encryptionSalt = encryptionSalt;
        return this;
    }

    public Long getId() {
        return id;
    }

    public ShamirAdvancedSecurity setId(Long id) {
        this.id = id;
        return this;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public ShamirAdvancedSecurity setWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }

    public Integer getSharesToRebuild() {
        return sharesToRebuild;
    }

    public ShamirAdvancedSecurity setSharesToRebuild(Integer sharesToRebuild) {
        this.sharesToRebuild = sharesToRebuild;
        return this;
    }

    public Integer getTotalShares() {
        return totalShares;
    }

    public ShamirAdvancedSecurity setTotalShares(Integer totalShares) {
        this.totalShares = totalShares;
        return this;
    }
}

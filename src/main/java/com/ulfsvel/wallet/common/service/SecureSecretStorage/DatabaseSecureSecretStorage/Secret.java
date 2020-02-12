package com.ulfsvel.wallet.common.service.SecureSecretStorage.DatabaseSecureSecretStorage;

import com.ulfsvel.wallet.common.entiry.Wallet;

import javax.persistence.*;

@Entity
public class Secret {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn()
    private Wallet wallet;
    private String secret;

    protected Secret() {
    }

    public Secret(Wallet wallet, String secret) {
        this.wallet = wallet;
        this.secret = secret;
    }

    public Long getId() {
        return id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public String getSecret() {
        return secret;
    }
}

package ro.cheiafermecata.wallet.service.SecureSecretStorage.DatabaseSecureSecretStorage;

import ro.cheiafermecata.wallet.entity.Wallet;

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

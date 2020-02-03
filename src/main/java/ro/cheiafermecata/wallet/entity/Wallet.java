package ro.cheiafermecata.wallet.entity;


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
    private String sharedEncryptionKey;
    private String encryptedEncryptionKey;
    private String encryptionSalt;

    public Wallet() {
    }

    public String getEncryptionSalt() {
        return encryptionSalt;
    }

    public Wallet setEncryptionSalt(String encryptionSalt) {
        this.encryptionSalt = encryptionSalt;
        return this;
    }

    public String getSharedEncryptionKey() {
        return sharedEncryptionKey;
    }

    public Wallet setSharedEncryptionKey(String sharedEncryptionKey) {
        this.sharedEncryptionKey = sharedEncryptionKey;
        return this;
    }

    public String getEncryptedEncryptionKey() {
        return encryptedEncryptionKey;
    }

    public Wallet setEncryptedEncryptionKey(String encryptedEncryptionKey) {
        this.encryptedEncryptionKey = encryptedEncryptionKey;
        return this;
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
}

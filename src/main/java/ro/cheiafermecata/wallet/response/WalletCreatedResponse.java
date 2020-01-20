package ro.cheiafermecata.wallet.response;

import java.math.BigInteger;

public class WalletCreatedResponse {

    public String address;

    public BigInteger publicKey;

    public BigInteger privateKey;

    public String getAddress() {
        return address;
    }

    public WalletCreatedResponse setAddress(String address) {
        this.address = address;
        return this;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public WalletCreatedResponse setPublicKey(BigInteger publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public WalletCreatedResponse setPrivateKey(BigInteger privateKey) {
        this.privateKey = privateKey;
        return this;
    }
}

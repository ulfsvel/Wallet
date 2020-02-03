package ro.cheiafermecata.wallet.request;

public class WalletCreationRequest {

    public String password;

    public String getPassword() {
        return password;
    }

    public WalletCreationRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}

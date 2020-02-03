package ro.cheiafermecata.wallet.request;

public class WalletUnlockRequest {

    private Long walletId;

    private String password;

    public Long getWalletId() {
        return walletId;
    }

    public WalletUnlockRequest setWalletId(Long walletId) {
        this.walletId = walletId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public WalletUnlockRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}

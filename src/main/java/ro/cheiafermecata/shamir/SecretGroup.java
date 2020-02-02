package ro.cheiafermecata.shamir;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

public class SecretGroup {

    private Integer sharesToRebuild;

    private List<Share> shares;

    private BigInteger actualSecret;

    SecretGroup(
            @NotNull Integer sharesToRebuild,
            @NotNull List<Share> shares,
            @NotNull BigInteger actualSecret
    ) {
        this.sharesToRebuild = sharesToRebuild;
        this.shares = shares;
        this.actualSecret = actualSecret;
    }

    @NotNull
    public Integer getTotalShares() {
        return shares.size();
    }

    @NotNull
    public Integer getSharesToRebuild() {
        return sharesToRebuild;
    }

    @NotNull
    public List<Share> getShares() {
        return shares;
    }

    @NotNull
    public BigInteger getActualSecret() {
        return actualSecret;
    }

}

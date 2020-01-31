package ro.cheiafermecata.shamir;

import java.math.BigInteger;
import java.util.List;

public class Secret {

    Integer minimumSharesToRebuild;

    List<BigInteger> shares;

    BigInteger actualSecret;

    Secret(Integer minimumSharesToRebuild, List<BigInteger> shares, BigInteger actualSecret) {
        this.minimumSharesToRebuild = minimumSharesToRebuild;
        this.shares = shares;
        this.actualSecret = actualSecret;
    }

    public Integer getTotalShares() {
        return shares.size();
    }

    public Integer getMinimumSharesToRebuild() {
        return minimumSharesToRebuild;
    }

    public List<BigInteger> getShares() {
        return shares;
    }

    public BigInteger getActualSecret() {
        return actualSecret;
    }

}

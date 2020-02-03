package ro.cheiafermecata.shamir;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class Share {

    private final BigInteger actualShare;

    private final BigInteger shareNumber;

    public Share(
            @NotNull String stringRepresentation
    ) {
        String[] parts = stringRepresentation.split("@");
        shareNumber = new BigInteger(parts[0]);
        actualShare = new BigInteger(parts[1]);
    }

    public Share(
            @NotNull BigInteger actualShare,
            @NotNull BigInteger shareNumber
    ) {
        this.actualShare = actualShare;
        this.shareNumber = shareNumber;
    }

    @NotNull
    public BigInteger getActualShare() {
        return actualShare;
    }

    @NotNull
    public BigInteger getShareNumber() {
        return shareNumber;
    }

    public String toString() {
        return shareNumber.toString() + "@" + actualShare.toString();
    }
}

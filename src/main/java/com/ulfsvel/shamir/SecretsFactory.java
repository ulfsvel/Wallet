package com.ulfsvel.shamir;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class SecretsFactory {

    private final BigNumbersGenerator bigNumbersGenerator;

    private final Integer numberOfBitsForSecret;

    private final BigInteger bigPrime;

    public SecretsFactory(
            @NotNull BigNumbersGenerator bigNumbersGenerator,
            @NotNull Integer numberOfBitsForSecret
    ) {
        this.bigNumbersGenerator = bigNumbersGenerator;
        this.numberOfBitsForSecret = numberOfBitsForSecret;
        this.bigPrime = this.bigNumbersGenerator.generateMersennePrimeNumber(numberOfBitsForSecret);
    }

    @NotNull
    public SecretGroup createSecret(
            @NotNull Integer sharesToRebuild,
            @NotNull Integer totalShares
    ) {
        if (sharesToRebuild > totalShares) {
            throw new IllegalArgumentException("Minimum shares to rebuild should be should not be greater than total shares");
        }
        List<Share> shares = new LinkedList<>();

        List<BigInteger> poly = bigNumbersGenerator.createListOfRandomNumbers(
                sharesToRebuild,
                numberOfBitsForSecret
        );

        for (int index = 1; index <= totalShares; index = index + 1) {
            shares.add(
                    new Share(
                            MathHelper.evaluatePoly(poly, index, this.bigPrime),
                            new BigInteger(String.valueOf(index))
                    )
            );
        }

        return new SecretGroup(sharesToRebuild, shares, poly.get(0));
    }

    @NotNull
    public BigInteger rebuildSecret(
            @NotNull List<Share> shares
    ) {
        List<BigInteger> xS = new LinkedList<>();
        List<BigInteger> yS = new LinkedList<>();

        for (Share share : shares) {
            xS.add(share.getShareNumber());
            yS.add(share.getActualShare());
        }

        return MathHelper.lagrangeInterpolate(BigInteger.ZERO, xS, yS, this.bigPrime);
    }
}

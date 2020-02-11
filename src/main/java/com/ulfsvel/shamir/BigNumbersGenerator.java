package com.ulfsvel.shamir;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

public class BigNumbersGenerator {

    private final SecureRandom secureRandom;

    public BigNumbersGenerator(
            @NotNull SecureRandom secureRandom
    ) {
        this.secureRandom = secureRandom;
    }

    @NotNull
    @Contract("_ -> new")
    private BigInteger generateRandomNumber(
            int numberOfBits
    ) {
        return new BigInteger(numberOfBits, secureRandom);
    }

    @NotNull
    BigInteger generateMersennePrimeNumber(
            int numberOfBits
    ) {
        BigInteger two = new BigInteger("2");
        return two.pow(numberOfBits).subtract(BigInteger.ONE);
    }

    @NotNull
    List<BigInteger> createListOfRandomNumbers(
            int listLength,
            int numberOfBits
    ) {
        List<BigInteger> list = new LinkedList<>();
        for (int index = 0; index < listLength; index = index + 1) {
            list.add(this.generateRandomNumber(numberOfBits));
        }

        return list;
    }

}

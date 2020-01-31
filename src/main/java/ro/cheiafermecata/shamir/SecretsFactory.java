package ro.cheiafermecata.shamir;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SecretsFactory {

    private final SecureRandom secureRandom;

    private final Integer numberOfBitsForSecretGeneration;

    public SecretsFactory(SecureRandom secureRandom, Integer numberOfBitsForSecretGeneration){
        this.secureRandom = secureRandom;
        this.numberOfBitsForSecretGeneration = numberOfBitsForSecretGeneration;
    }

    @NotNull
    @Contract(" -> new")
    private BigInteger generateRandomNumber() {
        return new BigInteger(numberOfBitsForSecretGeneration, secureRandom);
    }

    private BigInteger generatePrimeNumberBasedOnNumberOfBits() {
        BigInteger two = new BigInteger("2");
        return two.pow(numberOfBitsForSecretGeneration).subtract(BigInteger.ONE);
    }


    @NotNull
    private List<BigInteger> createPolyOfRandomNumbers(Integer minimumSharesToRebuild) {
        ArrayList<BigInteger> poly = new ArrayList<>();
        poly.ensureCapacity(minimumSharesToRebuild);
        for(int index = 0; index < minimumSharesToRebuild; index = index +1 ){
            poly.add(this.generateRandomNumber());
        }

        return  poly;
    }

    private BigInteger evaluatePoly(@NotNull List<BigInteger> poly, @NotNull Integer position, BigInteger primeNumber){
        BigInteger value = new BigInteger("0");
        BigInteger polyPosition = new BigInteger(position.toString());

        ListIterator<BigInteger> listIterator = poly.listIterator(poly.size());
        while (listIterator.hasPrevious()) {
            BigInteger currentPolyValue = listIterator.previous();
            value = value.multiply(polyPosition);
            value = value.add(currentPolyValue);
            value = value.mod(primeNumber);
        }

        return value;
    }


    public Secret createSecret(Integer minimumSharesToRebuild, Integer totalShares) {
        ArrayList<BigInteger> shares = new ArrayList<>();
        shares.ensureCapacity(totalShares);

        List<BigInteger> poly = this.createPolyOfRandomNumbers(minimumSharesToRebuild);
        BigInteger bigPrime = this.generatePrimeNumberBasedOnNumberOfBits();

        for(int index = 1; index <= totalShares; index = index + 1){
            shares.add(evaluatePoly(poly, index, bigPrime));
        }

        return new Secret(minimumSharesToRebuild, shares, poly.get(0));
    }

}

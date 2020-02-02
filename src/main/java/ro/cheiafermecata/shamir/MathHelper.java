package ro.cheiafermecata.shamir;


import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("SameParameterValue")
public class MathHelper {

    @NotNull
    private static List<BigInteger> extendedEuclid(
            @NotNull BigInteger a,
            @NotNull BigInteger b
    ) {
        BigInteger aux;
        BigInteger x = BigInteger.ZERO;
        BigInteger lastX = BigInteger.ONE;
        BigInteger y = BigInteger.ONE;
        BigInteger lastY = BigInteger.ZERO;

        while (!b.equals(BigInteger.ZERO)) {
            BigInteger quot = a.divide(b);

            aux = b;
            b = a.mod(b);
            a = aux;

            aux = x;
            x = lastX.subtract(quot.multiply(x));
            lastX = aux;

            aux = y;
            y = lastY.subtract(quot.multiply(y));
            lastY = aux;
        }

        return Arrays.asList(lastX, lastY);
    }

    @NotNull
    private static BigInteger divideInModulus(
            @NotNull BigInteger number,
            @NotNull BigInteger den,
            @NotNull BigInteger bigPrime
    ) {
        List<BigInteger> euclidResult = extendedEuclid(den, bigPrime);
        return number.multiply(euclidResult.get(0));
    }

    @NotNull
    private static BigInteger sumList(
            @NotNull List<BigInteger> numbers
    ) {
        BigInteger sum = BigInteger.ZERO;
        for (BigInteger value : numbers) {
            sum = sum.add(value);
        }

        return sum;
    }

    @NotNull
    private static BigInteger multiplyList(
            @NotNull List<BigInteger> numbers
    ) {
        BigInteger prod = BigInteger.ONE;
        for (BigInteger value : numbers) {
            prod = prod.multiply(value);
        }

        return prod;
    }

    @NotNull
    private static List<BigInteger> subtractEachFrom(
            @NotNull List<BigInteger> numbers,
            @NotNull BigInteger numberToSubtractFrom
    ) {
        List<BigInteger> result = new LinkedList<>();
        for (BigInteger number : numbers) {
            result.add(numberToSubtractFrom.subtract(number));
        }

        return result;
    }

    @NotNull
    static BigInteger evaluatePoly(
            @NotNull List<BigInteger> poly,
            @NotNull Integer position,
            @NotNull BigInteger primeNumber
    ) {
        BigInteger value = BigInteger.ZERO;
        BigInteger polyPosition = new BigInteger(position.toString());

        ListIterator<BigInteger> listIterator = poly.listIterator(poly.size());
        while (listIterator.hasPrevious()) {
            BigInteger currentPolyValue = listIterator.previous();
            value = value.multiply(polyPosition).add(currentPolyValue).mod(primeNumber);
        }

        return value;
    }

    @NotNull
    static BigInteger lagrangeInterpolate(
            @NotNull BigInteger x,
            @NotNull List<BigInteger> xS,
            @NotNull List<BigInteger> yS,
            @NotNull BigInteger bigPrime
    ) {
        List<BigInteger> nums = new LinkedList<>();
        List<BigInteger> dens = new LinkedList<>();

        for (int index = 0; index < xS.size(); index = index + 1) {
            List<BigInteger> other = new LinkedList<>(xS);
            BigInteger current = other.get(index);
            //noinspection SuspiciousListRemoveInLoop
            other.remove(index);

            nums.add(multiplyList(subtractEachFrom(other, x)));
            dens.add(multiplyList(subtractEachFrom(other, current)));
        }

        BigInteger den = multiplyList(dens);

        List<BigInteger> something = new LinkedList<>();
        for (int index = 0; index < xS.size(); index = index + 1) {
            something.add(divideInModulus(nums.get(index).multiply(den).multiply(yS.get(index)).mod(bigPrime), dens.get(index), bigPrime));
        }
        BigInteger num = sumList(something);

        return divideInModulus(num, den, bigPrime).add(bigPrime).mod(bigPrime);
    }

}

package ro.cheiafermecata.shamir;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecretsFactoryTest {

    private static final int numberOfBits = 127;
    private static final int sharesToRebuild = 2;
    private static final int totalShares = 3;
    private static final String primeNumber = "170141183460469231731687303715884105727";
    private static final String firstRandomNumber = "95329630071695736338439622911656571285";
    private static final String secondRandomNumber = "38964625830938379449623976642761305306";
    private static final List<Share> shares = new LinkedList<>();
    private static final String firstShare = "134294255902634115788063599554417876591";
    private static final String secondShare = "3117698273103263506000272481295076170";
    private static final String thirdShare = "42082324104041642955624249124056381476";
    private static SecretsFactory secretsFactory;

    @BeforeAll
    public static void runOnceBeforeClass() {
        BigNumbersGenerator bigNumbersGenerator = mock(BigNumbersGenerator.class);

        when(bigNumbersGenerator.generateMersennePrimeNumber(numberOfBits)).thenReturn(new BigInteger(primeNumber));
        when(bigNumbersGenerator.createListOfRandomNumbers(sharesToRebuild, numberOfBits)).thenReturn(Arrays.asList(new BigInteger(firstRandomNumber), new BigInteger(secondRandomNumber)));

        secretsFactory = new SecretsFactory(bigNumbersGenerator, numberOfBits);

        shares.add(new Share(new BigInteger(firstShare), new BigInteger("1")));
        shares.add(new Share(new BigInteger(secondShare), new BigInteger("2")));
        shares.add(new Share(new BigInteger(thirdShare), new BigInteger("3")));
    }


    @Test
    void testSecretAndSharesAreBuildProperly() {
        SecretGroup secret = secretsFactory.createSecret(sharesToRebuild, totalShares);

        assertEquals(secret.getSharesToRebuild(), sharesToRebuild);
        assertEquals(secret.getTotalShares(), totalShares);
        assertEquals(secret.getActualSecret().toString(), new BigInteger(firstRandomNumber).toString());
        List<Share> shares = secret.getShares();
        for (int index = 0; index < totalShares; index = index + 1) {
            Share share = shares.get(index);
            Share expectedShare = SecretsFactoryTest.shares.get(index);

            assertEquals(share.getActualShare().toString(), expectedShare.getActualShare().toString());
            assertEquals(share.getShareNumber(), expectedShare.getShareNumber());
        }
    }

    @Test
    void testSharesRebuildProperly() {
        List<Share> shares;

        shares = new LinkedList<>();
        shares.add(SecretsFactoryTest.shares.get(0));
        shares.add(SecretsFactoryTest.shares.get(1));
        assertEquals(secretsFactory.rebuildSecret(shares).toString(), firstRandomNumber);

        shares = new LinkedList<>();
        shares.add(SecretsFactoryTest.shares.get(1));
        shares.add(SecretsFactoryTest.shares.get(0));
        assertEquals(secretsFactory.rebuildSecret(shares).toString(), firstRandomNumber);

        shares = new LinkedList<>();
        shares.add(SecretsFactoryTest.shares.get(0));
        shares.add(SecretsFactoryTest.shares.get(2));
        assertEquals(secretsFactory.rebuildSecret(shares).toString(), firstRandomNumber);

        shares = new LinkedList<>();
        shares.add(SecretsFactoryTest.shares.get(2));
        shares.add(SecretsFactoryTest.shares.get(0));
        assertEquals(secretsFactory.rebuildSecret(shares).toString(), firstRandomNumber);

        shares = new LinkedList<>();
        shares.add(SecretsFactoryTest.shares.get(1));
        shares.add(SecretsFactoryTest.shares.get(2));
        assertEquals(secretsFactory.rebuildSecret(shares).toString(), firstRandomNumber);

        shares = new LinkedList<>();
        shares.add(SecretsFactoryTest.shares.get(2));
        shares.add(SecretsFactoryTest.shares.get(1));
        assertEquals(secretsFactory.rebuildSecret(shares).toString(), firstRandomNumber);
    }

    @Test
    void testShareConstructionAndRebuildingWithTrueRandomGenerator() {
        SecretsFactory secretsFactory = new SecretsFactory(new BigNumbersGenerator(new SecureRandom()), numberOfBits);
        SecretGroup secretGroup = secretsFactory.createSecret(sharesToRebuild, totalShares);
        List<Share> shares;

        shares = new LinkedList<>();
        shares.add(secretGroup.getShares().get(0));
        shares.add(secretGroup.getShares().get(1));
        assertEquals(secretsFactory.rebuildSecret(shares).toString(), secretGroup.getActualSecret().toString());

        shares = new LinkedList<>();
        shares.add(secretGroup.getShares().get(1));
        shares.add(secretGroup.getShares().get(0));
        assertEquals(secretsFactory.rebuildSecret(shares).toString(), secretGroup.getActualSecret().toString());

        shares = new LinkedList<>();
        shares.add(secretGroup.getShares().get(0));
        shares.add(secretGroup.getShares().get(2));
        assertEquals(secretsFactory.rebuildSecret(shares).toString(), secretGroup.getActualSecret().toString());

        shares = new LinkedList<>();
        shares.add(secretGroup.getShares().get(2));
        shares.add(secretGroup.getShares().get(0));
        assertEquals(secretsFactory.rebuildSecret(shares).toString(), secretGroup.getActualSecret().toString());

        shares = new LinkedList<>();
        shares.add(secretGroup.getShares().get(1));
        shares.add(secretGroup.getShares().get(2));
        assertEquals(secretsFactory.rebuildSecret(shares).toString(), secretGroup.getActualSecret().toString());

        shares = new LinkedList<>();
        shares.add(secretGroup.getShares().get(2));
        shares.add(secretGroup.getShares().get(1));
        assertEquals(secretsFactory.rebuildSecret(shares).toString(), secretGroup.getActualSecret().toString());
    }

}

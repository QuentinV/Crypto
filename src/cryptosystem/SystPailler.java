package cryptosystem;

import java.math.BigInteger;
import java.util.Random;

public class SystPailler extends CryptoSystem {
    public static int MAX_SIZE_R = 8;

    @Override
    public Keys generateKeys(int length) {
        Random rand = new Random();

        BigInteger one = new BigInteger("1");

        //Generation p & q
        BigInteger p = BigInteger.probablePrime(length, rand);
        BigInteger q;
        do {
            q = BigInteger.probablePrime(length, rand);
        } while (p.compareTo(q) == 0);

        //Keys
        Keys keys = new Keys();
        keys.pk = p.multiply(q);
        keys.sk = p.subtract(one).multiply(q.subtract(one));

        return keys;
    }

    @Override
    public BigInteger chiffrer(BigInteger m, BigInteger pk) {
        if (pk == null || m == null) return null;

        BigInteger zero = new BigInteger("0");
        BigInteger one = new BigInteger("1");

        if (m.compareTo(pk) >= 0) //|| m.compareTo(zero) <= 0)
            return null;

        //generate random number R between 0
        BigInteger r;
        do {
            r = new BigInteger(MAX_SIZE_R, new Random());
        } while (r.compareTo(pk) >= 0 && r.compareTo(zero) < 0);

        BigInteger pkPk = pk.multiply(pk);

        BigInteger c; //c = ((g^m) * r^n) mod n²
        BigInteger g = pk.add(one);

        BigInteger part1 = g.modPow(m, pkPk);
        BigInteger part2 = r.modPow(pk, pkPk);

        c = part1.multiply(part2);

        return c;
    }

    @Override
    public BigInteger dechiffrer(BigInteger c, Keys keys) {
        if (keys == null || c == null)
            return null;
        if (keys.pk == null || keys.sk == null)
            return null;

        BigInteger one = new BigInteger("1");
        BigInteger zero = new BigInteger("0");

        BigInteger pkPk = keys.pk.multiply(keys.pk);

        BigInteger m; //L((c^sk) mod pk²) * mu mod pk where L(u) = (u-1) / pk and mu = sk^-1  mod pk

        //u = (c ^ sk) mod pk²
        BigInteger u = c.modPow(keys.sk, pkPk);
        BigInteger lu = u.subtract(one).divide(keys.pk);//L(u) = (u-1) / pk

        //mu = sk^-1  mod pk
        BigInteger mu = keys.sk.modPow(new BigInteger("-1"), keys.pk);

        m = lu.multiply(mu).mod(keys.pk);

        return m;
    }
}

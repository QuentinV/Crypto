package cryptosystem;

import java.math.BigInteger;

public abstract class CryptoSystem {
    public static class Keys {
        public BigInteger pk;
        public BigInteger sk;

        public Keys() {
        }

        public Keys(BigInteger pk, BigInteger sk) {
            this.pk = pk;
            this.sk = sk;
        }

        @Override
        public String toString() {
            return "Pk = "+pk+"\nSk = "+sk;
        }
    };

    public abstract Keys generateKeys(int length);
    public abstract BigInteger chiffrer(BigInteger m, BigInteger pk);
    public abstract BigInteger dechiffrer(BigInteger c, Keys keys);
}

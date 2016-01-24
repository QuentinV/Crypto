package env;

import cryptosystem.CryptoSystem;
import env.Question;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Random;

public abstract class Bob extends Thread {
    protected CryptoSystem cs;

    public Bob(CryptoSystem cs) {
        this.cs = cs;
    }

    @Override
    public abstract void run();
}

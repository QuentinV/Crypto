package solutionOne.main;

import cryptosystem.CryptoSystem;
import cryptosystem.SystPailler;
import env.Question;
import utils.Utils;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class Bob extends env.Bob {
    //512 bits keys
    protected static CryptoSystem.Keys myKeys = new CryptoSystem.Keys(
            new BigInteger("105592678282549073866124653400919551893397082528952515018052268994510731576183067599910254230904300748219511813642115308059813580443727087080777211516521792173157419110874851380210565312640819741912869856790974320954162071843523853072018988953904686527048296688753895555343375503513524691867690218009558437601"),
            new BigInteger("105592678282549073866124653400919551893397082528952515018052268994510731576183067599910254230904300748219511813642115308059813580443727087080777211516521771615806662731885180234896706995516898382506391962872847118927036808852145355530958873179354896238842811529855593733982675835285636583110326983497563269312")
    );

    public static BigInteger getPublicKey() {
        return myKeys.pk;
    }

    public Bob(CryptoSystem cs) {
        super(cs);
    }

    protected Socket s;

    protected void connect() throws IOException {
        s = new Socket();
        s.connect(new InetSocketAddress(InetAddress.getByName("localhost"), 10000));
    }

    protected LinkedList<String> getEncryptRep(BufferedReader br) throws IOException
    {
        // On récupère toutes les questions encryptées d'Alice

        LinkedList<String> repEncryptee = new LinkedList<>();

        for(int cpt = 1; ; cpt++)
        {
            String rep = br.readLine();
            if (rep.isEmpty())
                break;
            else {
                System.out.println("Reponse " + cpt + " : " + rep);
                repEncryptee.add(rep);
            }
        }

        return repEncryptee;
    }
}

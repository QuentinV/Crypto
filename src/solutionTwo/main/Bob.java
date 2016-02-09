package solutionTwo.main;

import cryptosystem.CryptoSystem;
import cryptosystem.SystPailler;
import env.Question;
import utils.Utils;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;

public class Bob extends env.Bob {
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

    @Override
    public void run() {
        try {
            Random random = new Random();
            Question secretQ = Question.listQuestions.get(random.nextInt(Question.listQuestions.size()));
            System.out.println("Secret question : "+secretQ);

            BigInteger i = new BigInteger(String.valueOf(secretQ.getNum()));
            BigInteger I = cs.chiffrer(i, Bob.getPublicKey());

            Socket s = new Socket();
            s.connect(new InetSocketAddress(InetAddress.getByName("localhost"), 10000));

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            System.out.println("Envoi de I = "+I.toString());
            bw.write(I.toString() + '\n');

            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            String repOk = "";
            for(int cpt = 1; ; cpt++)
            {
                String rep = br.readLine();

                if (rep.isEmpty())
                    break;

                {
                    BigInteger repBi = new BigInteger(rep);
                    System.out.println("Reception num  "+cpt+" = "+repBi);

                    BigInteger decrypt = cs.dechiffrer(repBi, myKeys);
                    String myRep = Utils.convertToString(decrypt);
                    if (cpt == secretQ.getNum())
                        repOk = myRep;
                    System.out.println("REPONSE"+(cpt == secretQ.getNum() ? " OK" : "")+" = "+myRep);
                }
            }

            System.out.println("\nLa reponse a la question "+secretQ.getNum()+" est : "+repOk);

            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Question.initList();

        Bob b = new Bob(new SystPailler());

        b.start();

        try {
            b.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

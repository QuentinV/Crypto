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
                    System.out.println("REPONSE"+(cpt == secretQ.getNum() ? " OK" : "")+" = "+myRep);

                    //break;
                }
            }

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

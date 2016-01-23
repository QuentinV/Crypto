package solutionTwo.main;

import cryptosystem.CryptoSystem;
import cryptosystem.SystPailler;
import env.Question;
import env.Response;
import utils.Utils;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;

public class Alice extends env.Alice {
    public Alice(CryptoSystem cs) {
        super(cs);
    }

    @Override
    public void run() {
        System.out.println("Hello my name is Alice. How are you ?");
        System.out.println("Voici les reponses aux questions : ");

        for (int i = 0; i < responses.size(); ++i)
            System.out.println(responses.get(i));

        // Server socket
        try {
            ServerSocket ss = new ServerSocket(10000);

            BigInteger zero = new BigInteger("0");
            BigInteger pkPk = Bob.getPublicKey().multiply(Bob.getPublicKey());

            Random random = new Random();

            for (;;)
            {
                Socket s = ss.accept();

                // Receive mess
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String value = br.readLine(); //BigInteger

                try {
                    BigInteger I = new BigInteger(value);
                    System.out.println("I = "+I);

                    // Chiffre l'ensemble des reps avec I
                    LinkedList<BigInteger> listRepEncrypt = new LinkedList<>();
                    for (Response r : responses)
                    {
                        BigInteger rand = new BigInteger(128, random);

                        BigInteger rep = cs.chiffrer(Utils.convertString(r.getContent()), Bob.getPublicKey());
                        BigInteger ei = cs.chiffrer(zero.subtract(new BigInteger(String.valueOf(r.getQ().getNum()))), Bob.getPublicKey());
                        BigInteger e = rep.multiply(ei.multiply(I).modPow(rand, pkPk)).mod(pkPk); // (I * E) ^ Rand

                        listRepEncrypt.add(e);
                    }

                    System.out.println("Liste de reponses encryptes : ");

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                    for (BigInteger b : listRepEncrypt) {
                        bw.write(b.toString() + '\n');
                        System.out.println(b.toString());
                    }
                    bw.write('\n');

                    bw.flush();
                } catch(Exception ex)
                {
                    System.out.println("Error invalid input");
                    ex.printStackTrace();
                }

                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Question.initList();
        Alice a = new Alice(new SystPailler());

        a.start();

        try {
            a.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

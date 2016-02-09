package solutionOne.main;

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

    //512 bits keys
    protected static CryptoSystem.Keys myKeys = new CryptoSystem.Keys(
            new BigInteger("130131071253097583747630659815310849918484884379289909856006370109970784534724825495191236462512378783183677819484358734488185023262931726371541157000582697678635072950262054111662819016748688091105817993027870610569899398194660590199929537141471532683874374881902914109920879611980119471552862103424780809051"),
            new BigInteger("130131071253097583747630659815310849918484884379289909856006370109970784534724825495191236462512378783183677819484358734488185023262931726371541157000582674832385056297904239774454062772804023883965755324497040739286275501890568406332095860008753111205727375777376935205740256397971370659728114822056440290400")
    );
    public static BigInteger getPublicKey() {
        return myKeys.pk;
    }

    public Alice(CryptoSystem cs) {
        super(cs);
    }

    @Override
    public void run() {
        System.out.println("Hello my name is Alice. How are you ?");
        System.out.println("Voici les reponses aux questions : ");

        for (int i = 0; i < responses.size(); ++i) {
            System.out.println(responses.get(i));
            System.out.println(Utils.convertString(responses.get(i).getContent()));
        }

        // Server socket
        try {
            ServerSocket ss = new ServerSocket(10000);

            for (;;)
            {
                Socket s = ss.accept();


                // Chiffre l'ensemble des réponses
                LinkedList<BigInteger> listRepEncrypt = new LinkedList<>();
                for (Response r : responses)
                {
                    BigInteger rep = cs.chiffrer(Utils.convertString(r.getContent()), Alice.getPublicKey());
                    listRepEncrypt.add(rep);
                }


                // On envoie l'ensemble des réponses
                System.out.println("Liste de reponses encryptes : ");

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                for (BigInteger b : listRepEncrypt) {
                    bw.write(b.toString() + '\n');
                    System.out.println(b.toString());
                }
                bw.write('\n');
                bw.flush();

                // On recoit la réponse encryptée par Bob, que l'on décrypte
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String value = br.readLine(); //BigInteger
                BigInteger repBob = cs.dechiffrer(new BigInteger(value), myKeys);
                System.out.println("Message reçu décrypté : " + repBob.toString());

                // On encrypte maintenant avec la clé de Bob
                BigInteger repFinale = cs.chiffrer(repBob, Bob.getPublicKey());
                bw.write(repFinale.toString());
                bw.flush();
                System.out.println("Réponse finale : " + repFinale.toString());


                // Fin de la communication
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

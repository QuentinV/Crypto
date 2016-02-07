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
import java.util.ArrayList;
import java.util.List;
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

            Socket s = new Socket();
            s.connect(new InetSocketAddress(InetAddress.getByName("localhost"), 10000));


            // On récupère toutes les questions encryptées d'Alice
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            List<String> repEncryptee = new ArrayList<>();

            for(int cpt = 1; ; cpt++)
            {
                String rep = br.readLine();
                if (rep.isEmpty())
                    break;
                else {
                    System.out.println("Question " + cpt + " : " + rep);
                    repEncryptee.add(rep);
                }
            }


            // On choisi une réponse parmi la liste
            Random random = new Random();
            int idxRepChoisie = random.nextInt(repEncryptee.size());
            String repChoisie = repEncryptee.get(idxRepChoisie);
            System.out.println("Nous choisissons la question " + (idxRepChoisie + 1));


            // On envoie cette réponse multipliée avec un nombre aléatoire
            BigInteger nbAleatoire = BigInteger.valueOf(random.nextInt(100));
            BigInteger encAleatoire = cs.chiffrer(nbAleatoire, Alice.getPublicKey());
            BigInteger repBob = encAleatoire.multiply(new BigInteger(repChoisie));

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            System.out.println("Envoi de M = " + repBob.toString());
            bw.write(repBob.toString() + '\n');
            bw.flush();


            // Nous décryptons maintenant la réponse d'Alice et nous la décryptons
            String repFinale = br.readLine();
            BigInteger decrypt = cs.dechiffrer(new BigInteger(repFinale), myKeys).subtract(nbAleatoire);
            String myRep = Utils.convertToString(decrypt);
            System.out.println("Réponse finale : " + myRep);


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

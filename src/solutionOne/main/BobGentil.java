package solutionOne.main;

import cryptosystem.CryptoSystem;
import cryptosystem.SystPailler;
import env.Question;
import utils.Utils;

import java.io.*;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Random;

public class BobGentil extends Bob {
    public BobGentil(CryptoSystem cs) {
        super(cs);
    }

    @Override
    public void run()
    {
        try {
            this.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            LinkedList<String> repEncryptee = this.getEncryptRep(br);

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

            // Nous décryptons maintenant la réponse d'Alice
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

        Bob b = new BobGentil(new SystPailler());

        b.start();

        try {
            b.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package solutionOne.main;

import cryptosystem.CryptoSystem;
import cryptosystem.SystPailler;
import env.Question;
import utils.Utils;

import java.io.*;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Random;

public class BobProfiteur extends Bob {
    private static int offset = 60;

    public BobProfiteur(CryptoSystem cs) {
        super(cs);
    }

    @Override
    public void run()
    {
        System.out.println("Bonjour je suis bob profiteur qui veut avoir PLUSIEURS reponses !\n");
        try {
            this.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            LinkedList<String> repEncryptee = this.getEncryptRep(br);

            // On choisi une réponse parmi la liste
            Random random = new Random();
            BigInteger repBob = new BigInteger("1");
            BigInteger dix = new BigInteger("10");
            BigInteger pkPk = Alice.getPublicKey().multiply(Alice.getPublicKey());

            int max = repEncryptee.size() > 5 ? 5 : repEncryptee.size();
            int nbRep = random.nextInt(max) + 1;

            System.out.println("\nNombre de rep voulu : "+nbRep+"\n");
            System.out.println("ENCRYPTION : ");
            for (int i = 0; i < nbRep; ++i)
            {
                BigInteger nb = new BigInteger(repEncryptee.get(i));
                System.out.println("rep : "+nb);

                Integer decalage = offset * i;
                System.out.println("decalage : "+decalage);

                nb = nb.modPow(dix.pow(decalage), pkPk);
                repBob = repBob.multiply(nb);
            }

            repBob = repBob.mod(pkPk);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            System.out.println("\nEnvoi de M = " + repBob.toString());
            bw.write(repBob.toString() + '\n');
            bw.flush();

            // Nous décryptons maintenant la réponse d'Alice
            String repFinale = br.readLine();
            BigInteger decrypt = cs.dechiffrer(new BigInteger(repFinale), myKeys);


            System.out.println("\nDECRYPTION : ");
            LinkedList<BigInteger> reps = new LinkedList<>();
            for (int i = 0; i < nbRep-1; ++i)
            {
                int decalageTotal = offset;
                BigInteger d = dix.pow(decalageTotal);

                System.out.println("decrypt : "+decrypt);

                // Division et obtentien reste qui correspond à la rep
                BigInteger nb = decrypt.divide(d);
                BigInteger rep = decrypt.subtract(nb.multiply(d));
                System.out.println("Division par "+d+" = "+nb);
                System.out.println("rep = "+rep);

                reps.add(rep); //ajoute la rep
                decrypt = nb;
            }

            reps.add(decrypt);

            System.out.println();
            for (int i = 0; i < reps.size(); ++i)
            {
                String myRep = Utils.convertToString(reps.get(i));
                System.out.println("Reponse a la question " + (i+1) + " : " + myRep);
            }

            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Question.initList();

        Bob b = new BobProfiteur(new SystPailler());

        b.start();

        try {
            b.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

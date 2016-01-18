package solutionOne.main;

import solutionOne.agents.Alice;
import solutionOne.agents.Bob;
import cryptosystem.SystPailler;
import env.Question;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        //Public Questions
        Question.listQuestions.add(new Question(1, "Ma premiere question"));
        Question.listQuestions.add(new Question(2, "Ma seconde question"));
        Question.listQuestions.add(new Question(3, "Ma troisième question"));

        SystPailler sp = new SystPailler();

        //Alice
        Alice a = new Alice(sp);

        //Bob
        Bob b = new Bob(sp);
        b.setAlicePublicKey(a.getPublicKey());

        //Recuperation des reponses encrypté par bob
        b.setListEncryptReps(a.getListEncryptReps());

        for(BigInteger bi : a.getListEncryptReps())
            System.out.println("r : "+bi);

        //Bob : R = R1^a1 * R2^a2
        //Alice : r1 * a1 + r2 * a2

    }
}

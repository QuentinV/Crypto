package solutionOne.agents;

import cryptosystem.CryptoSystem;
import env.Question;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Random;

public class Bob {
    private CryptoSystem cs;
    private LinkedList<BigInteger> listEncryptReps;
    private Question choosedQuestion;
    private BigInteger alicePublicKey;

    private BigInteger aleatNumber;

    public Bob(CryptoSystem cs) {
        this.cs = cs;

        Random rand  = new Random();
        this.choosedQuestion = Question.listQuestions.get(rand.nextInt(Question.listQuestions.size()));
        System.out.println("Question de bob (non connu d'alice) = "+choosedQuestion);
    }

    public void setAlicePublicKey(BigInteger alicePublicKey) {
        this.alicePublicKey = alicePublicKey;
    }

    public LinkedList<BigInteger> getListEncryptReps() {
        return listEncryptReps;
    }

    public void setListEncryptReps(LinkedList<BigInteger> listEncryptReps) {
        this.listEncryptReps = listEncryptReps;
    }

    public BigInteger getReturnResponse()
    {
        BigInteger br = listEncryptReps.get(choosedQuestion.getNum() - 1);
        aleatNumber = new BigInteger(16, new Random());

        BigInteger c = cs.chiffrer(aleatNumber, alicePublicKey);

        return c;
    }
}

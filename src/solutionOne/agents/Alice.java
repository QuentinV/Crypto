package solutionOne.agents;

import cryptosystem.CryptoSystem;
import env.Question;
import env.Response;
import utils.Utils;

import java.math.BigInteger;
import java.util.LinkedList;

public class Alice {
    private CryptoSystem cs;
    private CryptoSystem.Keys keys;
    private LinkedList<Response> responses;
    private LinkedList<BigInteger> listEncryptReps;

    public Alice(CryptoSystem cs) {
        this.cs = cs;

        //keys
        this.keys = cs.generateKeys(512);
        System.out.println("ALICE >> keys : "+this.keys);

        //responses
        responses = new LinkedList<>();
        listEncryptReps = new LinkedList<>();
        for (Question q : Question.listQuestions) {
            Response r = new Response(q, "Réponse à la question "+String.valueOf(q.getNum()));
            responses.add(r);
            listEncryptReps.add(cs.chiffrer(Utils.convertString(r.getContent()), this.keys.pk));
        }
    }

    public LinkedList<BigInteger> getListEncryptReps() {
        return listEncryptReps;
    }

    public BigInteger getPublicKey()
    {
        return keys.pk;
    }
}

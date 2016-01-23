package env;

import cryptosystem.CryptoSystem;

import java.util.LinkedList;

public abstract class Alice extends Thread {
    protected CryptoSystem cs;
    protected LinkedList<Response> responses;

    public Alice(CryptoSystem cs) {
        this.cs = cs;

        //responses
        responses = new LinkedList<>();

        for (Question q : Question.listQuestions) {
            Response r = new Response(q, "Reponse a la question "+String.valueOf(q.getNum()));
            responses.add(r);
        }
    }

    @Override
    public abstract void run();
}

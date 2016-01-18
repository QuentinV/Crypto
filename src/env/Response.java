package env;

public class Response {
    private Question q;
    private String content;

    public Response(Question q, String content) {
        this.q = q;
        this.content = content;
    }

    public Question getQ() {
        return q;
    }

    public String getContent() {
        return content;
    }
}

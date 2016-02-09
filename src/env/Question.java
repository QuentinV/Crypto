package env;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Question {
    public static LinkedList<Question> listQuestions = new LinkedList<>();

    public static LinkedList<Question> initList() {
        listQuestions.clear();

        listQuestions.add(new Question(1, "Ma premiere question"));
        listQuestions.add(new Question(2, "Ma seconde question"));
        listQuestions.add(new Question(3, "Ma troisieme question"));
        listQuestions.add(new Question(4, "Ma quatrieme question"));
        listQuestions.add(new Question(5, "Je suis la question que tu ne sais pas : la cinquieme"));
        listQuestions.add(new Question(6, "Please save me from bob"));
        listQuestions.add(new Question(7, "Am i a question ?"));
        listQuestions.add(new Question(8, "Hello a question i am"));
        listQuestions.add(new Question(9, "Et mince encore une question"));
        listQuestions.add(new Question(10, "LA derniere youpiiiiiiiiiiiii"));

        return listQuestions;
    }

    private int num;
    private String content;

    public Question(int num, String content) {
        this.num = num;
        this.content = content;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.valueOf(num)+" / "+content;
    }
}

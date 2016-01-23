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
        listQuestions.add(new Question(5, "Et la derniere question la cinquieme"));

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

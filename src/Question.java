import java.util.ArrayList;
import java.util.List;

public class Question {
    private String questionId;
    private String questionBody;
    private List<String> choices;
    private char answer;

    public Question(String questionId, String questionBody, List<String> choices, char answer) {
        this.questionId = questionId;
        this.questionBody = questionBody;
        this.choices = (choices != null) ? choices : new ArrayList<>();
        this.answer = answer;
    }
    public Question() {}
    public Question(String questionBody, List<String> choices, char answer)
    {
        this.questionBody = questionBody;
        this.choices = (choices != null) ? choices : new ArrayList<>();
        this.answer = answer;
    }

    public String getQuestionId() { return questionId; }
    public String getQuestionBody() { return questionBody; }
    public List<String> getChoices() { return choices; }
    public char getAnswer() { return answer; }
}


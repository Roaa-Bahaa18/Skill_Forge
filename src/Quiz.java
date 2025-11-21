import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String quizId;
    private List<Question> questions;
    private Double score;
    public Quiz() {
        this.questions = new ArrayList<>();
        this.score = null;
    }
    public Quiz(String quizId, List<Question> questions) {
        this.quizId = quizId;
        this.questions = (questions != null) ? questions : new ArrayList<>();
        this.score = null;
    }
    public String getQuizId() { return quizId; }
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions){this.questions=questions;}
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public void calculateScore(List<Character> answers) {
        if (questions == null || questions.isEmpty()) {
            this.score = 0.0;
            return;
        }
        int correct = 0;
        for(int i = 0; i < questions.size(); i++) {
            if (i < answers.size() && questions.get(i).getAnswer() == answers.get(i)) {
                correct++;
            }
        }
        this.score = (double) correct / questions.size();
    }

}

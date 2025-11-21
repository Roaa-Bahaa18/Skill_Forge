import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String quizId;
    private List<Question> questions;
    private double score;

    public Quiz(String quizId, List<Question> questions) {
        this.quizId = quizId;
        this.questions = (questions != null) ? questions : new ArrayList<>();
    }

    public String getQuizId() { return quizId; }
    public List<Question> getQuestions() { return questions; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public void calculateScore(List<Character> answers) {
        int correct = 0;
        for(int i = 0; i < questions.size(); i++) {
            if(questions.get(i).getAnswer() == answers.get(i)) {
                correct++;
            }
        }
        this.score = (double) correct / questions.size();
    }

}

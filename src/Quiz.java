import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String quizId;
    private List<Question> questions;
    private Double score = 0.0;
    private double passingScore = 50;// default value can be changed
    private int maxAttempts = 5;
    private List<Character> userAnswers;
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
    public double getPassingScore() { return passingScore; }
    public void setPassingScore(double passingScore) { this.passingScore = passingScore; }
    public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
    public int getMaxAttempts() { return maxAttempts; }
    public List<Character> getUserAnswers() { return userAnswers; }
    public void setUserAnswers(List<Character> userAnswers) { this.userAnswers = userAnswers; }
    public void calculateScore(List<Character> answers) {
        if (questions == null || questions.isEmpty()) {
            this.score = 0.0;
            return;
        }
        int correct = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (i < answers.size()) {
                char userAnswer = Character.toUpperCase(answers.get(i));
                char correctAnswer = Character.toUpperCase(questions.get(i).getAnswer());
                if (userAnswer == correctAnswer) {
                    correct++;
                }

            }
        }
        this.score = (correct * 100.0) / questions.size();
    }

    public boolean isPassed() {
        return score != null && score >= passingScore;
    }

}



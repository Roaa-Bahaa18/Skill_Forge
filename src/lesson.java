import java.util.ArrayList;

public class lesson {
    private String lessonId;
    private String lessonTitle;
    private String content;
    private ArrayList<String> resources = new ArrayList<>();
    private boolean status;
    private Quiz quiz;
    private boolean quizstate;
    private boolean quizPassed = false;

    public lesson(String lessonId, String lessonTitle, String content, ArrayList<String> resources,boolean QuizState,Quiz quiz){
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.content = content;
        this.resources = resources;
        this.quizstate=QuizState;
        this.quiz=quiz;
    }
    public boolean getQuizState() { return quizstate; }
    public void setQuizState(boolean quizstate) { this.quizstate = quizstate; }
    public void setStatus(boolean status){this.status =status;}
    public void setQuiz(Quiz quiz) {this.quiz = quiz;}
    public void setLessonTitle(String lessonTitle) { this.lessonTitle = lessonTitle; }
    public void setContent(String content) { this.content = content; }
    public void setResources(ArrayList<String> resources) { this.resources = resources; }
    public Quiz getQuiz() {return quiz;}
    public boolean getStatus() {
        return this.status;
    }
    public String getLessonId() {
        return lessonId;
    }
    public String getLessonTitle() {
        return lessonTitle;
    }
    public String getContent() {
        return content;
    }
    public ArrayList<String> getResources() {
        return resources;
    }
    public boolean isQuizPassed() { return quizPassed; }
    public void setQuizPassed(boolean quizPassed) { this.quizPassed = quizPassed; }

    @Override
    public String toString(){
        return "(" + this.lessonId + ") " + this.lessonTitle;
    }
}

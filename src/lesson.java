import java.util.ArrayList;

public class lesson {
    private String lessonId;
    private String lessonTitle;
    private String content;
    private ArrayList<String> resources = new ArrayList<>();
    private boolean status;
    private Quiz quiz=null;

    public lesson(String lessonId, String lessonTitle, String content, ArrayList<String> resources,boolean status){
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.content = content;
        this.resources = resources;
        this.status =status;
    }

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

    @Override
    public String toString(){
        return "(" + this.lessonId + ") " + this.lessonTitle;
    }
}

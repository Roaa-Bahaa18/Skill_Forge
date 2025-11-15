import java.util.ArrayList;

public class lesson {
    private int lessonId;
    private String lessonTitle;
    private String content;
    private ArrayList<String> resources = new ArrayList<>();
    private boolean completed;

    public lesson(String title, String content){
        this.lessonId = (int)(Math.random() * 9000) + 1000;
        this.lessonTitle = title;
        this.content = content;
        this.completed = false;
    }
    public lesson(int lessonId, String lessonTitle, String content, ArrayList<String> resources){
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.content = content;
        this.resources = resources;

    }

    public boolean getCompleted() {
        return this.completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getLessonId() {
        return lessonId;
    }
    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }
    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getResources() {
        return resources;
    }
    public void setResources(ArrayList<String> resources) {
        this.resources = resources;
    }

    public String lineRepresentation(){
        return this.lessonId + " " + this.lessonTitle + " " + this.content;
    }




}

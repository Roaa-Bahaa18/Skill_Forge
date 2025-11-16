import java.util.ArrayList;

public class lesson {
    private String lessonId;
    private String lessonTitle;
    private String content;
    private ArrayList<String> resources = new ArrayList<>();
    private boolean status;

    public lesson(String lessonId, String lessonTitle, String content, ArrayList<String> resources,boolean status){
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.content = content;
        this.resources = resources;
        this.status =status;
    }

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

    public void setStatus(boolean status){this.status =status;}

    @Override
    public String toString(){
        return "(" + this.lessonId + ") " + this.lessonTitle;
    }
}

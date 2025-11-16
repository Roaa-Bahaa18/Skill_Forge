import java.util.ArrayList;
import java.util.HashMap;

public class Student extends User{
    private ArrayList<String> enrolledCourseIds = new ArrayList<>();
    private HashMap<String, ArrayList<Boolean>> lessonProgress = new HashMap<>();

    public Student(String username, String password, String userID, String email,ArrayList<String> enroll, HashMap<String, ArrayList<Boolean>> lessonProgress) {
        super(userID,"Student",username,email,password);
        if (enroll != null) {this.enrolledCourseIds.addAll(enroll);}
        if (lessonProgress != null) {this.lessonProgress.putAll(lessonProgress);}
    }
    public ArrayList<String> getEnrolledCourseIds() {return enrolledCourseIds;}
    public void addCourse(String courseId) {enrolledCourseIds.add(courseId);}
    public HashMap<String, ArrayList<Boolean>> getProgress() {return lessonProgress;}
    public void addCourseProgress(String courseId, ArrayList<Boolean> statuses) {
        if (this.lessonProgress == null) {this.lessonProgress = new HashMap<>();}
        this.lessonProgress.put(courseId, statuses);
    }
}

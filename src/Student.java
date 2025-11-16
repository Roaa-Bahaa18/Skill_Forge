import java.util.ArrayList;
import java.util.HashMap;

public class Student extends User{
    private ArrayList<String> enrolledCourseIds = new ArrayList<>();
    private HashMap<String, ArrayList<Boolean>> lessonProgress = new HashMap<>();

    public Student(String username, String password, String userID, String email,ArrayList<String> enroll, HashMap<String, ArrayList<Boolean>> lessonProgress) {
        super(userID,"Student",username,email,password);
        this.enrolledCourseIds = enroll;
        this.lessonProgress=lessonProgress;
    }
    public void setEnrolledCourseIds(ArrayList<String> enrolledCourseIds){this.enrolledCourseIds=enrolledCourseIds;}
    public ArrayList<String> getEnrolledCourseIds() {return enrolledCourseIds;}
    public void addCourse(String courseId) {enrolledCourseIds.add(courseId);}
    public HashMap<String, ArrayList<Boolean>> getProgress() {return lessonProgress;}

    public void setProgress(HashMap<String, ArrayList<Boolean>> lessonProgress) {this.lessonProgress = lessonProgress;}
}

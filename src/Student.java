import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Student extends User{
    private ArrayList<String> enrolledCourseIds = new ArrayList<>();
    private HashMap<String, ArrayList<Boolean>> lessonProgress = new HashMap<>();
    private HashMap<String, Integer> quizAttempts = new HashMap<>();
    private HashMap<String, List<Double>> quizScores = new HashMap<>();
    private ArrayList<String> completedCoursesIDs = new ArrayList<>();


    public Student(String username, String password, String userID, String email,ArrayList<String> enroll, HashMap<String, ArrayList<Boolean>> lessonProgress) {
        super(userID,"Student",username,email,password);
        if (enroll != null) {this.enrolledCourseIds.addAll(enroll);}
        if (lessonProgress != null) {this.lessonProgress.putAll(lessonProgress);
        }
        this.quizAttempts = new HashMap<>();
        this.quizScores = new HashMap<>();
        this.completedCoursesIDs = new ArrayList<>();
    }
    public ArrayList<String> getEnrolledCourseIds() {return enrolledCourseIds;}
    public void addCourse(String courseId) {enrolledCourseIds.add(courseId);}
    public HashMap<String, ArrayList<Boolean>> getProgress() {return lessonProgress;}
    public void addCourseProgress(String courseId, ArrayList<Boolean> statuses) {
        if (this.lessonProgress == null) {this.lessonProgress = new HashMap<>();}
        this.lessonProgress.put(courseId, statuses);
    }
    public ArrayList<String> getCompletedCoursesIDs() { return completedCoursesIDs; }
    public void setCompletedCoursesIDs(ArrayList<String> completedCoursesIDs) { this.completedCoursesIDs = completedCoursesIDs;}

    public void recordQuizAttempt(String quizId, double score) {
        if (quizAttempts == null) {
            quizAttempts = new HashMap<>();
        }
        if (quizScores == null) {
            quizScores = new HashMap<>();
        }
        quizAttempts.put(quizId, quizAttempts.getOrDefault(quizId, 0) + 1);
        quizScores.computeIfAbsent(quizId, k -> new ArrayList<>()).add(score);
    }

    public int getQuizAttempts(String quizId) {
        if(quizAttempts == null) quizAttempts = new HashMap<>();
        return quizAttempts.getOrDefault(quizId, 0);
    }
    public List<Double> getQuizScore(String quizId) {
        return quizScores.getOrDefault(quizId, new ArrayList<>());
    }

}

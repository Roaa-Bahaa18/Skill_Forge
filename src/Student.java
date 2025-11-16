import java.util.ArrayList;

public class Student extends User{
    private ArrayList<course> enrolledCourses;
    private ArrayList<Float> progress;

    public Student(String username, String password, String userID, String email,ArrayList<course> enroll, ArrayList<Float> progress) {
        super(userID,"Student",username,email,password);
        this.enrolledCourses = enroll;
        this.progress=progress;
    }
    public void setEnrolledCourses(ArrayList<course> courses) {this.enrolledCourses = courses;}
    public ArrayList<course> getEnrolledCourses(){return enrolledCourses;}
    public ArrayList<Float> getProgress() {return progress;}

    public void setProgress(ArrayList<Float> progress) {this.progress = progress;}
}

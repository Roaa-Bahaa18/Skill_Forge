import java.util.ArrayList;

public class Student extends User{
    private ArrayList<course> enrolledCourses;

    public Student(String username, String password, String userID, String email,ArrayList<course> enroll) {
        super(userID,"student",username,email,password);
        this.enrolledCourses = enroll;
    }
    public void setEnrolledCourses(ArrayList<course> courses) {this.enrolledCourses = courses;}
    public ArrayList<course> getEnrolledCourses(){return enrolledCourses;}

}

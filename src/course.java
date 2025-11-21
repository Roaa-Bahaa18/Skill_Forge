import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;


public class course {
    private String courseId;
    private String courseTitle;
    private String courseDescription;
    private String instructorId;
    private ArrayList<lesson> lessons = new ArrayList<>();
    private ArrayList<String> studentIds = new ArrayList<>();
    private String status;

    public course(String courseId, String courseTitle, String courseDescription, String instructorId, ArrayList<String> students, ArrayList<lesson> lessons, String status) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.instructorId = instructorId;
        this.studentIds = students;
        this.lessons = lessons;
        this.status="Pending";
    }

    public String getCourseId() { return courseId;}
    public String getCourseTitle() { return courseTitle;}
    public String getCourseDescription() { return courseDescription;}
    public String getInstructorId() {return instructorId;}
    public ArrayList<lesson> getLessons() {return lessons;}
    public void setStatus(String status){this.status = status;}
    public String getStatus(){return status;}

    public ArrayList<String> getStudentIds() {
        if (this.studentIds == null) {this.studentIds = new ArrayList<>();}
        return studentIds;}
    public void addStudent(String studentId) {studentIds.add(studentId);}
    @Override
    public String toString(){return "(" + this.getCourseId() + ") " + this.getCourseTitle();}
}



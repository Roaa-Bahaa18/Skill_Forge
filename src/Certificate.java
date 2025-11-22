import java.time.LocalDate;
import java.util.Random;

public class Certificate {
    private String studentID;
        private String courseID;
        private String certificateID;
        private String issueDate;

        public Certificate(String studentID, String courseID, String issueDate) {
            this.studentID = studentID;
            this.courseID = courseID;
            this.issueDate = issueDate;
            Random rand = new Random();
            this.certificateID = Integer.toString(rand.nextInt(100000));
        }
    public String getCourseID() {return courseID;}

    public void setCourseID(String courseID) {this.courseID = courseID;}

    public String getStudentID() {return studentID;}

    public void setStudentID(String studentID) {this.studentID = studentID;}
    public String getCertificateID() {return certificateID;}

    public String getIssueDate() {return issueDate;}

    public void setIssueDate(String issueDate) {this.issueDate = issueDate;}
}

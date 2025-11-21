import java.util.ArrayList;

public class Instructor extends User{
    private ArrayList<course> createdCoursesIDS;

    public Instructor(String userId,String username,String email,String passwordHash, ArrayList<course> createdCourses){
        super(userId,"Instructor",username,email,passwordHash);
        this.createdCoursesIDS = createdCourses;
    }

    public void setCreatedCoursesIDS(ArrayList<course> createdCoursesIDS) {
        this.createdCoursesIDS = createdCoursesIDS;
    }
    ArrayList<course> getCreatedCoursesIDS(){
        return createdCoursesIDS;
    }
}

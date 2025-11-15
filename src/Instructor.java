import java.util.ArrayList;

public class Instructor extends User{
    private ArrayList<course> createdCourses;

    public Instructor(String userId,String username,String email,String passwordHash, ArrayList<course> createdCourses){
        super(userId,"Instructor",username,email,passwordHash);
        this.createdCourses = createdCourses;
    }

    public void setCreatedCourses(ArrayList<course> createdCourses) {
        this.createdCourses = createdCourses;
    }

    ArrayList<course> getCreatedCourses(){
        return createdCourses;
    }
}

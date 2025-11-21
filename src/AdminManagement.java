import java.util.ArrayList;

public class AdminManagement {
    private Admin admin;
    public AdminManagement(Admin admin) {this.admin = admin;}

    public ArrayList<course> getPendingCourses(){
        ArrayList<course> pendingCourses = new ArrayList<>();
        ArrayList<course> all = courseManagement.loadCourses();
        for(course c : all){
            if(c.getStatus() == "Pending"){
                pendingCourses.add(c);
            }
        }
        return pendingCourses;
    }

    public void manageCourse(course course, boolean option){
        if(option){
            course.setStatus("Accepted");
        }
        else{
            course.setStatus("Rejected");
        }
    }



}

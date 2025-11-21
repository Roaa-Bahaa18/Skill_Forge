import java.util.ArrayList;

public class AdminManagement {
    private Admin admin;
    public AdminManagement(Admin admin) {this.admin = admin;}

    public ArrayList<course> getPendingCourses(){
        ArrayList<course> pendingCourses = new ArrayList<>();
        ArrayList<course> all = courseManagement.loadCourses();
        for(course c : all){
            if(c.getStatus().equals("Pending")){
                pendingCourses.add(c);
            }
        }
        return pendingCourses;
    }

    public void manageCourse(course course, boolean option){
        ArrayList<course> courses = courseManagement.loadCourses();
        for(course c:courses)
        {
            if(c.getCourseId().equals(course.getCourseId()))
            {
                if(option) c.setStatus("Approved");
                else c.setStatus("Rejected");
                break;
            }
        }
        courseManagement.saveCourses(courses);
    }
    public course findCourseById(String courseID)
    {
        ArrayList<course> courses = courseManagement.loadCourses();
        for(course c:courses){
            if(c.getCourseId().equals(courseID))
                return c;
        }
        return null;
    }
}

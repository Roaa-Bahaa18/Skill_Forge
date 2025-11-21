import java.util.ArrayList;
import java.util.List;

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
        List<User> users= userService.loadUsers();
        boolean flag=false;
        for(User u:users)
        {
            if(course.getInstructorId().equals(u.getUserId()))
            {
                Instructor i=(Instructor) u;
                List<course> InstructorCourses = i.getCreatedCourses();
                for(int j=0;j<InstructorCourses.size();j++)
                {
                    if(course.getCourseId().equals(InstructorCourses.get(j).getCourseId()))
                    {
                        course newcourse= new course(course.getCourseId(), course.getCourseTitle(),
                                course.getCourseDescription(), course.getInstructorId(),
                                course.getStudentIds(),course.getLessons(),
                                (option)?"Approved":"Rejected");
                        InstructorCourses.set(j,newcourse);
                        flag=true;
                        break;
                    }

                }
            }
            if(flag) break;
        }
        userService.saveUsers(users);
    }

    public course findCourseById(String courseID) {
        ArrayList<course> courses = courseManagement.loadCourses();
        for(course c:courses){
            if(c.getCourseId().equals(courseID))
                return c;
        }
        return null;
    }
}

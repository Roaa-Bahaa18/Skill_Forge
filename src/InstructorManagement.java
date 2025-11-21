import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InstructorManagement {
    private Instructor instructor;
    public InstructorManagement(Instructor instructor) {
        this.instructor = instructor;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public boolean createCourse(String title, String courseDescription){
        Random ran = new Random();
        String courseId = Integer.toString(ran.nextInt(99) + 100);
        while(!Validations.isValidCourseID(courseId)){
            courseId = Integer.toString(ran.nextInt(99) + 100);
        }
        if (!Validations.isValidCourseTitle(title)) {
            return false;
        }
        ArrayList<String> studentsIDs = new ArrayList<>();
        ArrayList<lesson> lessons = new ArrayList<>();
        course c = new course(courseId,title, courseDescription, this.instructor.getUserId(), studentsIDs, lessons,"Pending");
        courseManagement.addCourse(c);
        List<User> users = userService.loadUsers();
        int i=0;
        for(User user : users){
            if(user.getUserId().equals(this.instructor.getUserId())){
                ArrayList<course> courses = instructor.getCreatedCoursesIDS();
                courses.add(c);
                instructor.setCreatedCoursesIDS(courses);
                users.set(i,instructor);
                break;
            }
            i++;
        }
        userService.saveUsers(users);
        return true;
    }

    public boolean editCourse(String courseId, String newTitle, String newContentDescription, String newStatus){
        ArrayList<course> courses = courseManagement.loadCourses();
        course newCourse= null;
        boolean flag = false;
        if (!Validations.isValidCourseTitle(newTitle)) {return false;}
        for(course c : courses){
            if(c.getCourseId().matches(courseId)){
                String finalStatus = c.getStatus();
                if (c.getStatus().equals("Approved") || c.getStatus().equals("Rejected")) {
                    if (!newTitle.equals(c.getCourseTitle()) || !newContentDescription.equals(c.getCourseDescription())) {
                        finalStatus = "Pending";
                    } else {
                        finalStatus = newStatus;
                    }
                } else {
                    finalStatus = newStatus;
                }
                newCourse = new course(courseId, newTitle, newContentDescription, this.instructor.getUserId(),c.getStudentIds(), c.getLessons(), finalStatus);
                flag = courseManagement.editCourse(c, newCourse);
                break;
            }
        }
        List<User> users = userService.loadUsers();
        int i=0;
        for(User user : users){
            if(user.getUserId().equals(this.instructor.getUserId())){
                ArrayList<course> InstructorCourses = instructor.getCreatedCoursesIDS();
                for(int j=0;j<InstructorCourses.size();j++){
                    if(InstructorCourses.get(j).getCourseId().matches(courseId)){
                        InstructorCourses.set(j,newCourse);
                        instructor.setCreatedCoursesIDS(InstructorCourses);
                        break;
                    }
                }
                users.set(i,instructor);
            }
            i++;
        }
        userService.saveUsers(users);
        return flag;
    }

    public boolean deleteCourse(String courseId){
        boolean flag = courseManagement.deleteCourse(courseId);
        List<User> users = userService.loadUsers();
        int i=0;
        for(User user : users){
            if(user.getUserId().equals(this.instructor.getUserId())){
                ArrayList<course> icourses = instructor.getCreatedCoursesIDS();
                for(int j=0;j<icourses.size();j++){
                    if(icourses.get(j).getCourseId().matches(courseId)){
                        icourses.remove(j);
                        instructor.setCreatedCoursesIDS(icourses);
                        break;
                    }
                }
                users.set(i,instructor);
            }
            i++;
        }
        userService.saveUsers(users);
        return flag;

    }

    public boolean addLesson(String courseId, String title, String content, ArrayList<String> resources){
        Random ran = new Random();
        String lessonId = Integer.toString(ran.nextInt(99) + 100);
        while(!Validations.isValidLessonID(lessonId)){
            lessonId = Integer.toString(ran.nextInt(99) + 100);
        }
        if(!Validations.isValidLessonTitle(courseId,title)) { return false;}
        lesson l = new lesson(lessonId, title, content, resources, false);
        return courseManagement.addLesson(courseId, l);
    }

    public boolean editLesson(String courseId, String lessonId, String newTitle, String newContent, ArrayList<String> newResources){
        if(!Validations.isValidLessonTitle(courseId,newTitle)) { return false;}
        ArrayList<course> list = courseManagement.loadCourses();
        boolean flag = false;
        for(course c : list){
            if(c.getCourseId().matches(courseId)){
                for(lesson l : c.getLessons()){
                    lesson newLesson = new lesson(lessonId, newTitle, newContent, newResources, l.getStatus());
                    flag = courseManagement.editLesson(courseId, lessonId, newLesson);
                    break;
                }
            }
        }
        return flag;
    }

    public boolean deleteLesson(String courseId, String lessonId){
        return courseManagement.removeLesson(courseId, lessonId);
    }

    public boolean addQuizToLesson(String courseId, String lessonId, Quiz quiz){
        boolean flag = courseManagement.addQuizToLesson(courseId, lessonId, quiz);
        if (flag) {
            List<User> users = userService.loadUsers();
            int i=0;
            for(User user : users){
                if(user.getUserId().equals(this.instructor.getUserId())){
                    ArrayList<course> icourses = instructor.getCreatedCoursesIDS();
                    for(course c : icourses){
                        if(c.getCourseId().matches(courseId)){
                            lesson l = c.getLesson(lessonId);
                            if (l != null) {
                                l.setQuiz(quiz);
                                break;
                            }
                        }
                    }
                    users.set(i,instructor);
                }
                i++;
            }
            userService.saveUsers(users);
        }
        return flag;
    }

    public ArrayList<lesson> getLessonsWithoutQuiz(String courseId) {
        ArrayList<lesson> lessons= new ArrayList<>();
        ArrayList<course> courses=courseManagement.loadCourses();
        for(course c:courses)
        {
            if(c.getCourseId().equals(courseId))
            {
                for(lesson l : c.getLessons())
                {
                    if(l.getQuiz()==null) lessons.add(l);
                }
            }
        }
        return lessons;
    }


}

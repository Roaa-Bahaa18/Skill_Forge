import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class InstructorManagement {
    private Instructor instructor;
    public InstructorManagement(Instructor instructor) {
        this.instructor = instructor;
    }

    public void createCourse(String title, String courseDescription){
        Random ran = new Random();
        String courseId = Integer.toString(ran.nextInt(99) + 100);
        while(!Validations.isValidCourseID(courseId)){
            courseId = Integer.toString(ran.nextInt(99) + 100);
        }
        ArrayList<String> studentsIDs = new ArrayList<>();
        ArrayList<lesson> lessons = new ArrayList<>();
        course c = new course(courseId,title, courseDescription, this.instructor.getUserId(), studentsIDs, lessons);
        courseManagement.addCourse(c);
    }

    public boolean editCourse(String courseId, String newTitle, String newContentDescription){
        ArrayList<course> courses = courseManagement.loadCourses();
        boolean flag = false;
        for(course c : courses){
            if(c.getCourseId().matches(courseId)){
                course newCourse = new course(courseId, newTitle, newContentDescription, this.instructor.getUserId(),c.getStudentIds(), c.getLessons());
                flag = courseManagement.editCourse(c, newCourse);;
                break;
            }
        }
        return flag;
    }

    public boolean deleteCourse(String courseId){
        return courseManagement.deleteCourse(courseId);
    }

    public boolean addLesson(String courseId, String title, String content, ArrayList<String> resources){
        Random ran = new Random();
        String lessonId = Integer.toString(ran.nextInt(99) + 100);
        while(!Validations.isValidLessonID(lessonId)){
            lessonId = Integer.toString(ran.nextInt(99) + 100);
        }
        lesson l = new lesson(lessonId, title, content, resources, false);
        return courseManagement.addLesson(courseId, l);
    }

    public boolean editLesson(String courseId, String lessonId, String newTitle, String newContent, ArrayList<String> newResources){
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
}

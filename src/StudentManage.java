import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class StudentManage {
    private Student student;
    public StudentManage(Student student) {
        this.student = student;
    }

    //Course browsing --> aka available courses
    public course[] viewAvailableCourse() {
        ArrayList<course> available = courseManagement.loadCourses();
        available.removeAll(student.getEnrolledCourses());
        return available.toArray(new course[0]);
    }
    //view enrolled courses
    public course[] viewEnrolledCourse() {
        return student.getEnrolledCourses().toArray(new course[0]);
    }
    //Course enrollment --> this function won't call unless it's available
    public boolean enrollCourse(course c) {
        ArrayList<course> enrolled = student.getEnrolledCourses();
        ArrayList<Student> students= c.getStudents();
        if(enrolled.contains(c)) {
            return false;
        }
        else {
            List<User> list= userService.loadUsers();
            enrolled.add(c);
            student.getProgress().add(0f);
            student.setEnrolledCourses(enrolled);
            for(int i=0;i<list.size();i++) {
                if(list.get(i).getUserId().equals(student.getUserId()))
                {
                    list.set(i,student);
                    userService.saveUsers(list);
                }
            }

            ArrayList<course> courses= courseManagement.loadCourses();
            students.add(student);
            c.setStudents(students);
            for(int i=0;i<courses.size();i++) {
                if (courses.get(i).getCourseId().equals(c.getCourseId())) {
                    courses.set(i, c);
                    courseManagement.saveCourses(courses);
                }
            }
        }
        return true;
    }
    //Lesson Access w progress tracking w a mark lessons as complete kman
    public float[] progressTrack() {
        ArrayList<course> courses = student.getEnrolledCourses();
        if(courses.isEmpty()) return null;
        float[] progress = new float[courses.size()];
        int i=0;
        for(course c : courses)
        {
            int percent=0;
            ArrayList<lesson> lessons = c.getLessons();
            for(lesson l: lessons)
            {
                if(l.getStatus()) percent++;
            }
            progress[i] = (float) (percent*100)/lessons.size();
            i++;
        }
        return progress;
    }
    public boolean completeLesson(course c, lesson l) {

        List<User> students = userService.loadUsers();
        for (User u : students) {
            if (u.getUserId().equals(student.getUserId())) {
                ArrayList<course> enrolled = student.getEnrolledCourses();
                for (course sc : enrolled) {
                    if (sc.getCourseId().equals(c.getCourseId())) {
                        ArrayList<lesson> lessons = sc.getLessons();
                        for (lesson studentLesson : lessons) {
                            if (studentLesson.getLessonId().equals(l.getLessonId())) {
                                if (studentLesson.getStatus()) return false;
                                studentLesson.setStatus(true);
                                updateCourseProgress(student, c);
                                userService.saveUsers(students);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
    public void updateCourseProgress(Student student, course c) {
        ArrayList<course> courses = student.getEnrolledCourses();
        ArrayList<Float> progress = student.getProgress();

        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseId().equals(c.getCourseId())) {
                ArrayList<lesson> lessons = c.getLessons();
                int completed = 0;
                for (lesson l : lessons) {
                    if (l.getStatus()) completed++;
                }
                float percent = (completed * 100f) / lessons.size();
                progress.set(i, percent);
                student.setProgress(progress);
                return;
            }
        }
    }

}

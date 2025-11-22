import javax.swing.*;
import java.lang.classfile.constantpool.FloatEntry;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.stream.Collectors;

public class StudentManage {
    private Student student;
    public StudentManage(Student student) {
        this.student = student;
    }

    public List<course> viewAvailableCourses() {
        List<course> allCourses = courseManagement.loadCourses();
        List<String> enrolledIds = student.getEnrolledCourseIds();
        List<course> availableCourses = allCourses.stream()
                .filter(c -> "Approved".equals(c.getStatus()))
                .filter(c -> !enrolledIds.contains(c.getCourseId()))
                .collect(Collectors.toList());
        return availableCourses;
    }

    public List<course> viewEnrolledCourses() {
        ArrayList<String> enrolledIds = student.getEnrolledCourseIds();
        ArrayList<course> allCourses = courseManagement.loadCourses();
        ArrayList<course> enrolled = new ArrayList<>();
        for (course c : allCourses) {
            if (enrolledIds.contains(c.getCourseId())) {
                enrolled.add(c);
            }
        }
        return enrolled;
    }

    public boolean enrollCourse(course c) {
        String courseId = c.getCourseId();
        String studentId = student.getUserId();
        if (student.getEnrolledCourseIds().contains(courseId)) return false;
        student.addCourse(courseId);
        ArrayList<Boolean> lessonStatuses = new ArrayList<>();
        for (int i = 0; i < c.getLessons().size(); i++) {
            lessonStatuses.add(false);
        }
        student.addCourseProgress(courseId, lessonStatuses);
        List<User> users = userService.loadUsers();

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(studentId)) {
                users.set(i, student);
            }
        }
        userService.saveUsers(users);

        ArrayList<course> courses = courseManagement.loadCourses();
        for (course cc : courses) {
            if (cc.getCourseId().equals(courseId)) {
                cc.getStudentIds().add(studentId);
            }
        }
        courseManagement.saveCourses(courses);
        return true;
    }

    public float progressTrack(course c) {
        ArrayList<String> enrolledIds = student.getEnrolledCourseIds();
        float progress=0;
        for (int i = 0; i < enrolledIds.size(); i++) {
            String courseId = enrolledIds.get(i);
            if(courseId.equals(c.getCourseId()))
            {
                ArrayList<Boolean> lessonStatuses = student.getProgress().get(courseId);
                if (lessonStatuses == null || lessonStatuses.isEmpty()) {break;}
                int completed = 0;
                for (Boolean status : lessonStatuses) {if (status) completed++;}
                progress = (completed * 100f) / lessonStatuses.size();
                break;
            }
        }
        return progress;
    }

    public boolean completeLesson(course c, lesson l) {
        if(l.getQuiz() != null && !l.getQuizState()){
            return false;
        }
        String courseId = c.getCourseId();
        ArrayList<Boolean> lessonStatuses = student.getProgress().get(courseId);
        if (lessonStatuses == null) return false;

        int lessonIndex = -1;
        ArrayList<lesson> lessons = c.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId().equals(l.getLessonId())) {
                lessonIndex = i;
                break;
            }
        }
        if (lessonIndex == -1) return false;

        if (lessonStatuses.get(lessonIndex)) return false;

        lessonStatuses.set(lessonIndex, true);
        student.getProgress().put(courseId, lessonStatuses);
        List<User> users = userService.loadUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(student.getUserId())) {
                users.set(i, student);
            }
        }
        userService.saveUsers(users);

        float progress = progressTrack(c);
        if (progress == 100f) {
            markCourseCompleted(c);
        }
        userService.saveUsers(users);
        return true;
    }

    public void markCourseCompleted(course c) {
        if (student.getCompletedCoursesIDs() == null) {
            student.setCompletedCoursesIDs(new ArrayList<>());
        }

        if (!student.getCompletedCoursesIDs().contains(c.getCourseId())) {
            student.getCompletedCoursesIDs().add(c.getCourseId());

            List<User> users = userService.loadUsers();
            if (users == null) {
                users = new ArrayList<>();
            }

            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserId().equals(student.getUserId())) {
                    users.set(i, student);
                }
            }
            userService.saveUsers(users);
        }

    }

    public boolean takeQuiz(lesson lesson, List<Character> answers){
        Quiz quiz = lesson.getQuiz();
        if(quiz == null) return false;
        String quizID = quiz.getQuizId();
        int attempts = student.getQuizAttempts(quizID);
        if(attempts >= quiz.getMaxAttempts()){
            JOptionPane.showMessageDialog(null, "Max attempts reached for quiz: " + quizID);
            return false;
        }
        quiz.calculateScore(answers);
        double score = quiz.getScore();
        student.recordQuizAttempt(quizID, score);

        List<User> users = userService.loadUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(student.getUserId())) {
                users.set(i, student);
            }
        }
        userService.saveUsers(users);

        if(quiz.isPassed()){
            lesson.setQuizState(true);
            List<course> courses = courseManagement.loadCourses();
            for(course c : courses){
                for(lesson l : c.getLessons()){
                    if(l.getLessonId().equals(lesson.getLessonId())){
                        if (c != null){
                            completeLesson(c, lesson);
                        }
                    }
                }
            }
        }
        return quiz.isPassed();
    }


}

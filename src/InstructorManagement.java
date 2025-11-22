import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InstructorManagement {
    private Instructor instructor;
    public InstructorManagement(Instructor instructor) {
        this.instructor = instructor;
    }
    public void refreshInstructor() {
        List<User> users = userService.loadUsers();
        for (User user : users) {
            if (user instanceof Instructor && user.getUsername().equals(this.instructor.getUsername())) {
                this.instructor = (Instructor) user;
                break;
            }
        }
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public boolean createCourse(String title, String courseDescription){
        Random ran = new Random();
        String courseId = "C" + Integer.toString(ran.nextInt(99) + 100);
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
                ArrayList<course> courses = instructor.getCreatedCourses();
                courses.add(c);
                instructor.setCreatedCourses(courses);
                users.set(i,instructor);
                break;
            }
            i++;
        }
        userService.saveUsers(users);
        return true;
    }

    public boolean editCourse(String courseId, String newTitle, String newContentDescription) {
        ArrayList<course> courses = courseManagement.loadCourses();
        course oldCourse = null;

        for(course c : courses){if(c.getCourseId().equals(courseId)){oldCourse = c;break;}}
        if (oldCourse == null) {return false;}
        if (oldCourse.getStatus().equals("Pending")) {return false;}
        if (!Validations.isValidCourseTitle(newTitle)) {return false;}

        course newCourse = new course(courseId, newTitle, newContentDescription, this.instructor.getUserId(), oldCourse.getStudentIds(), oldCourse.getLessons(), "Pending");
        boolean flag = courseManagement.editCourse(oldCourse, newCourse);
        if(flag) {
            List<User> users = userService.loadUsers();
            boolean flag2=false;
            for(User u:users) {
                if (oldCourse.getInstructorId().equals(u.getUserId())) {
                    Instructor i = (Instructor) u;
                    List<course> InstructorCourses = i.getCreatedCourses();
                    for (int j = 0; j < InstructorCourses.size(); j++) {
                        if (courseId.equals(InstructorCourses.get(j).getCourseId())) {
                            InstructorCourses.set(j, newCourse);
                            flag2=true;
                            break;
                        }
                        if(flag2) break;
                    }
                }
            }
            userService.saveUsers(users);
            refreshInstructor();
        }
        //Lazem ttzbt 3nd Student bs wait 7abba
        return flag;
    }

    public boolean deleteCourse(String courseId){
        boolean flag = courseManagement.deleteCourse(courseId);
        List<User> users = userService.loadUsers();
        int i=0;
        for(User user : users){
            if(user.getUserId().equals(this.instructor.getUserId())){
                ArrayList<course> icourses = instructor.getCreatedCourses();
                for(int j=0;j<icourses.size();j++){
                    if(icourses.get(j).getCourseId().matches(courseId)){
                        icourses.remove(j);
                        instructor.setCreatedCourses(icourses);
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
        String lessonId = "L" + Integer.toString(ran.nextInt(99) + 100);
        while(!Validations.isValidLessonID(lessonId)){
            lessonId = Integer.toString(ran.nextInt(99) + 100);
        }
        if(!Validations.isValidLessonTitle(courseId,title)) { return false;}
        String quizId = "QZ" + (ran.nextInt(9000) + 1000);
        while(!Validations.isValidQuizID(quizId, courseId)){
            quizId = "QZ" + (ran.nextInt(9000) + 1000);
        }
        Quiz initialQuiz = new Quiz(quizId, new ArrayList<Question>());
        lesson l = new lesson(lessonId, title, content, resources, false,initialQuiz);
        boolean flag= courseManagement.addLesson(courseId, l);
        if(flag)
        {
            course c= courseManagement.getCourseByID(courseId);
            List<User> users = userService.loadUsers();
            boolean flag2=false;
            for(User u:users)
            {
                if(c.getInstructorId().equals(u.getUserId()))
                {
                    Instructor instructor=(Instructor) u;
                    ArrayList<course> courses= instructor.getCreatedCourses();
                    for(int i=0;i<courses.size();i++)
                    {
                        if(courses.get(i).getCourseId().equals(courseId))
                        {
                            courses.get(i).addLesson(l);
                            flag2=true;
                            break;
                        }
                    }
                }
                if(flag2) break;
            }
            userService.saveUsers(users);
            refreshInstructor();
        }
        return flag;
    }

    public boolean editLesson(String courseId, String lessonId, String newTitle, String newContent, ArrayList<String> newResources,Boolean state){
        if(!Validations.isValidLessonTitle(courseId,newTitle)) { return false;}
        ArrayList<course> list = courseManagement.loadCourses();
        boolean flag = false;
        for(course c : list){
            if(c.getCourseId().matches(courseId)){
                lesson existingLesson = courseManagement.getLessonByID(courseId, lessonId);
                Quiz existingQuiz = (existingLesson != null) ? existingLesson.getQuiz() : null;
                lesson newLesson = new lesson(lessonId, newTitle, newContent, newResources,state,existingQuiz);
                flag = courseManagement.editLesson(courseId, lessonId, newLesson);
                break;
            }
        }
        if(flag) {
            List<User> users = userService.loadUsers();
            boolean instructorFound = false;
            for(User u : users) {
                if (u instanceof Instructor) {
                    Instructor instructor = (Instructor) u;
                    ArrayList<course> createdCourses = instructor.getCreatedCourses();
                    for(int i = 0; i < createdCourses.size(); i++) {
                        course targetCourse = createdCourses.get(i);
                        if (targetCourse.getCourseId().equals(courseId)) {
                            for(int j = 0; j < targetCourse.getLessons().size(); j++) {
                                lesson targetLesson = targetCourse.getLessons().get(j);
                                if (targetLesson.getLessonId().equals(lessonId)) {
                                    Quiz existingQuiz = targetLesson.getQuiz();
                                    lesson newlesson = new lesson(lessonId, newTitle, newContent, newResources, state, existingQuiz);
                                    targetCourse.getLessons().set(j, newlesson);
                                    instructorFound = true;
                                    break;
                                }
                            }
                        }
                        if(instructorFound) break;
                    }
                }
                if(instructorFound) break;
            }
            userService.saveUsers(users);
            refreshInstructor();
        }
        return flag;
    }

    public boolean deleteLesson(String courseId, String lessonId){
        boolean success = courseManagement.removeLesson(courseId, lessonId);
        if (success) {
            List<User> users = userService.loadUsers();
            boolean instructorFound = false;
            for(User u : users) {
                if (u instanceof Instructor) {
                    Instructor instructor = (Instructor) u;
                    ArrayList<course> createdCourses = instructor.getCreatedCourses();
                    for(int i = 0; i < createdCourses.size(); i++) {
                        course targetCourse = createdCourses.get(i);
                        if (targetCourse.getCourseId().equals(courseId)) {
                            boolean removed = targetCourse.getLessons()
                                    .removeIf(lesson -> lesson.getLessonId().equals(lessonId));
                            if (removed) {instructorFound = true;}
                            break;
                        }
                    }
                }
                if(instructorFound) break;
            }
            userService.saveUsers(users);
            refreshInstructor();
        }
        return success;
    }

    public boolean addQuizToLesson(String courseId, String lessonId, Quiz quiz) {
        boolean success = courseManagement.addQuizToLesson(courseId, lessonId, quiz);
        quiz.setScore(0.0);
        if (success) {
            List<User> users = userService.loadUsers();
            boolean instructorFound = false;

            for (User u : users) {
                if (u instanceof Instructor) {
                    Instructor instructor = (Instructor) u;
                    for (course targetCourse : instructor.getCreatedCourses()) {
                        if (targetCourse.getCourseId().equals(courseId)) {
                            for (lesson targetLesson : targetCourse.getLessons()) {
                                if (targetLesson.getLessonId().equals(lessonId)) {
                                    targetLesson.setQuiz(quiz);
                                    targetLesson.setQuizState(true);
                                    instructorFound = true;
                                    break;
                                }
                            }
                        }
                        if (instructorFound) break;
                    }
                }
                if (instructorFound) break;
            }
            userService.saveUsers(users);
            refreshInstructor();
        }

        return success;
    }

    public ArrayList<lesson> getLessonsWithoutQuiz(String courseId) {
        ArrayList<lesson> result= new ArrayList<>();
        course targetCourse = null;
        for (course c : this.instructor.getCreatedCourses()) {
            if (c.getCourseId().equals(courseId)) {
                targetCourse = c;
                break;
            }
        }
        if (targetCourse != null) {
            for (lesson l : targetCourse.getLessons()) {
                if (!l.getQuizState()) {result.add(l);}
            }
        }
        return result;
    }


}

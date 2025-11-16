import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class courseManagement {
    private static final String COURSES_FILE = "courses.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static ArrayList<course> loadCourses() {
        ArrayList<course> list = new ArrayList<>();
        try {
            File f = new File(COURSES_FILE);
            if (!f.exists() || f.length() == 0) {
                f.createNewFile();
                FileWriter fw = new FileWriter(f);
                fw.write("{\"courses\": []}");
                fw.close();
            }
            JsonObject root = gson.fromJson(new FileReader(f), JsonObject.class);
            JsonArray arr = root.getAsJsonArray("courses");
            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                list.add(gson.fromJson(obj,course.class));
            }
            return list;
        } catch (Exception e) {e.printStackTrace();}
        return list;
    }

    public static void saveCourses(ArrayList<course> courses) {
        try {
            JsonObject root = new JsonObject();
            JsonArray arr = new JsonArray();
            for (course c : courses) {arr.add(gson.toJsonTree(c, c.getClass()));}
            root.add("courses", arr);
            FileWriter fw = new FileWriter(COURSES_FILE);
            gson.toJson(root, fw);
            fw.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }

    public static void addCourse(course course) {
        ArrayList<course> list = loadCourses();
        list.add(course);
        saveCourses(list);
    }

    public static boolean editCourse(course course, course editedcourse) {
        ArrayList<course> list = loadCourses();
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getCourseId().equals(course.getCourseId()))
            {
                list.set(i,editedcourse);
                saveCourses(list);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteCourse(String courseId) {
        ArrayList<course> list = loadCourses();
        course courseToRemove = null;
        for(course c:list){if(c.getCourseId().matches(courseId)) {courseToRemove = c;break;}}
        if (courseToRemove == null) {return false;}

        ArrayList<String> studentIds = courseToRemove.getStudentIds();
        if (studentIds != null && !studentIds.isEmpty()) {
            List<User> users = userService.loadUsers();
            for (User u : users) {
                if (u instanceof Student) {
                    Student s = (Student) u;
                    if (s.getEnrolledCourseIds().contains(courseId)) {
                        s.getEnrolledCourseIds().remove(courseId);
                        s.getProgress().remove(courseId);
                    }
                }
            }
            userService.saveUsers(users);
        }
        list.remove(courseToRemove);
        saveCourses(list);
        return true;
    }

    public static boolean addLesson(String courseId,lesson lesson) {
        ArrayList<course> list = loadCourses();
        course reqCourse = null;
        for (course c : list) {if (c.getCourseId().matches(courseId)) {reqCourse = c;break;}}
        if (reqCourse == null) {return false;}

        ArrayList<String> studentIds = reqCourse.getStudentIds();
        if (studentIds != null && !studentIds.isEmpty()) {
            List<User> users = userService.loadUsers();
            for (User u : users) {
                if (u instanceof Student) {
                    Student s = (Student) u;
                    ArrayList<Boolean> progress = s.getProgress().get(courseId);
                    if (progress != null) {
                        progress.add(false);
                    }
                }
            }
            userService.saveUsers(users);
        }
        reqCourse.getLessons().add(lesson);
        saveCourses(list);
        return true;
    }

    public static boolean editLesson(String courseId,String lessonId, lesson newLesson) {
        ArrayList<course> list = loadCourses();
        for(course c:list) {
            if(c.getCourseId().matches(courseId))
            {
                int i = 0;
                for(lesson l : c.getLessons()){
                    if(l.getLessonId().matches(lessonId)){
                        ArrayList<lesson> lessons = c.getLessons();
                        lessons.set(i, newLesson);
                        saveCourses(list);
                        return true;
                    }
                    i++;
                }
            }
        }
        return false;

    }

    public static boolean removeLesson(String courseId,String lessonId) {
        ArrayList<course> list = loadCourses();
        course targetCourse = null;
        int lessonIndex = -1;
        for(course c:list) {
            if(c.getCourseId().matches(courseId))
            {
                targetCourse = c;
                int i = 0;
                for (lesson l : c.getLessons()) {
                    if (l.getLessonId().matches(lessonId)) {
                        lessonIndex = i;
                        break;
                    }
                }
                i++;
            }
            if (lessonIndex != -1) break;
        }
        if (targetCourse == null || lessonIndex == -1) {return false;}

        ArrayList<String> studentIds = targetCourse.getStudentIds();
        if (studentIds != null && !studentIds.isEmpty()) {
            List<User> users = userService.loadUsers();
            for (User u : users) {
                if (u instanceof Student) {
                    Student s = (Student) u;
                    ArrayList<Boolean> progress = s.getProgress().get(courseId);
                    if (progress != null && progress.size() > lessonIndex) {
                        progress.remove(lessonIndex);
                    }
                }
            }
            userService.saveUsers(users);
        }
        targetCourse.getLessons().remove(lessonIndex);
        saveCourses(list);
        return true;
    }
}

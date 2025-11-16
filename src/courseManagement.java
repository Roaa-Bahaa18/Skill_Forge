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

    public static void saveCourses(List<course> courses) {
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

    //7agat el Course l Habiba
    public static boolean addCourse(course course) {
        ArrayList<course> list = loadCourses();
        for(course c:list) {
            if(c.getCourseId().matches(course.getCourseId())) {return false;}
        }
        list.add(course);
        saveCourses(list);
        return true;
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

    public static boolean deleteCourse(course course) {
        ArrayList<course> list = loadCourses();
        for(course c:list){
            if(c.getCourseId().matches(course.getCourseId()))
            {
                list.remove(course);
                saveCourses(list);
                return true;
            }
        }
        return false;
    }

    //7agat el lesson l Habiba
    public static boolean addLesson(course course,lesson lesson) {
        ArrayList<course> list = loadCourses();
        for(course c:list) {
            if(c.getCourseId().matches(course.getCourseId()))
            {
               c.getLessons().add(lesson);
               saveCourses(list);
               return true;
            }
        }
        return false;
    }

    public static boolean removeLesson(course course,lesson lesson) {
        ArrayList<course> list = loadCourses();
        for(course c:list) {
            if(c.getCourseId().matches(course.getCourseId()))
            {
                c.getLessons().remove(lesson);
                saveCourses(list);
                return true;
            }
        }
        return false;
    }

    //View Enrolled Students l Habiba

}

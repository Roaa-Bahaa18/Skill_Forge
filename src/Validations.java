import java.util.ArrayList;

public abstract class Validations {
    public static boolean isEmpty(String input) {return input == null || input.trim().isEmpty();}
    public static boolean isValidName(String text)
    {
        if(isEmpty(text)) return false;
        if(text.matches("[A-Za-z]+([ '-][A-Za-z]+)+")){
            String[] names = text.split("\\s+");
            return names.length >= 2 && names.length <= 4;
        }
        return false;
    }
    public static boolean isValidEmail(String text)
    {
        if(isEmpty(text)) return false;
        return text.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidID(String text)
    {
        if(isEmpty(text)) return false;
        return text.matches("\\d+");
    }
    public static boolean isValidCourseID(String text){
        ArrayList<course> data = courseManagement.loadCourses();
        for(course c : data){
            if(c.getCourseId().matches(text)){
                return false;
            }
        }
        return true;
    }
    public static boolean isValidCourseTitle(String text)
    {
        ArrayList<course> data = courseManagement.loadCourses();
        for(course c : data){
           if(c.getCourseTitle().equalsIgnoreCase(text)) return false;
        }
        return true;
    }

    public static boolean isValidLessonID(String text){
        ArrayList<course> data = courseManagement.loadCourses();
        for(course c : data){
            for(lesson l : c.getLessons()){
                if(l.getLessonId().matches(text)){
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean isValidLessonTitle(String courseID, String text)
    {
        ArrayList<course> data = courseManagement.loadCourses();
        for(course c : data){
            if(c.getCourseId().equals(courseID))
            {
                for(lesson l: c.getLessons()){
                    if(l.getLessonTitle().equalsIgnoreCase(text)) return false;
                }
            }
        }
        return true;
    }
}


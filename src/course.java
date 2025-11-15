import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;


public class course {
    private int courseId;
    private String courseTitle;
    private String courseDescription;
    private int instructorId;
    private ArrayList<lesson> lessons = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();

    //constructor for creating a new course
    public course(String courseTitle, String courseDescription, int instructorId) {
        this.courseId = (int) (Math.random() * 90000) + 10000;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.instructorId = instructorId;
    }

    //constructor for reading from json file
    public course(int courseId, String courseTitle, String courseDescription, int instructorId, ArrayList<Student> students, ArrayList<lesson> lessons) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.instructorId = instructorId;
        this.students = students;
        this.lessons = lessons;
    }


    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public ArrayList<lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<lesson> lessons) {
        this.lessons = lessons;
    }

    public void addLesson(lesson lesson) {
        if (!lessons.contains(lesson)) {
            lessons.add(lesson);
        } else {
            JOptionPane.showMessageDialog(null, "Lesson already in course");
        }
    }

    public void removeLesson(lesson lesson) {
        if (lessons.contains(lesson)) {
            lessons.remove(lesson);
        } else {
            JOptionPane.showMessageDialog(null, "Lesson not found");
        }
    }

    public void addStudent(Student s) {
        if (!students.contains(s)) {
            students.add(s);
        } else {
            JOptionPane.showMessageDialog(null, "Student already in course");
        }
    }

    public void removeStudent(Student s) {
        if (students.contains(s)) {
            students.remove(s);
        } else {
            JOptionPane.showMessageDialog(null, "Student not found");
        }
    }

    public void viewStudents() {
        for (Student s : students) {
            System.out.println(s.lineRepresentation);
        }
    }

    public void getLessonsByCourse() {
        for (lesson lesson : lessons) {
            System.out.println(lesson.lineRepresentation());
        }
    }

    public static ArrayList<course> readFromFile(String filename) {
        //create array list of courses and read the file and store the information
        //into a string
        ArrayList<course> courses = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            br.close();

            //create jsonarray from the string and extract information
            JSONArray coursesArray = new JSONArray(sb.toString());

            for (int i = 0; i < coursesArray.length(); i++) {
                JSONObject coursesObj = coursesArray.getJSONObject(i);

                int courseId = coursesObj.getInt("courseId");
                String title = coursesObj.getString("title");
                String description = coursesObj.getString("description");
                int instructorId = coursesObj.getInt("instructorId");

                ArrayList<Student> students = new ArrayList<>();
                Student s;
                JSONArray studentsArray = coursesObj.getJSONArray("students");
                for (int j = 0; j < studentsArray.length(); j++) {
                    s = Student.getStudent(studentsArray.getInt(j));
                    students.add(s);
                }

                ArrayList<lesson> lessons = new ArrayList<>();
                JSONArray lessonsArray = coursesObj.getJSONArray("lessons");
                for (int j = 0; j < lessonsArray.length(); j++) {
                    JSONObject lessonsObj = lessonsArray.getJSONObject(j);
                    int lessonId = lessonsObj.getInt("lessonId");
                    String lessonTitle = lessonsObj.getString("title");
                    String content = lessonsObj.getString("content");

                    ArrayList<String> resources = new ArrayList<>();
                    JSONArray resourcesArray = lessonsObj.getJSONArray("resources");
                    for (int k = 0; k < resourcesArray.length(); k++) {
                        resources.add(resourcesArray.getString(k));
                    }
                    lessons.add(new lesson(lessonId, lessonTitle, content, resources));
                }
                courses.add(new course(courseId, title, description, instructorId, students, lessons));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    public static void saveToFile(ArrayList<course> courses, String filename) {
        JSONArray coursesArray = new JSONArray();

        for (course c : courses) {
            JSONObject courseObj = new JSONObject();
            courseObj.put("courseId", c.getCourseId());
            courseObj.put("title", c.getCourseTitle());
            courseObj.put("description", c.getCourseDescription());
            courseObj.put("instructorId", c.getInstructorId());

            JSONArray studentsArray = new JSONArray();
            for (Student s : course.getStudents()) {
                JSONObject studentObj = new JSONObject();
                studentObj.put("studentId", s.getUserId());
                studentObj.put("studentName", s.getUsername());
                studentObj.put("email", s.getUserEmail());

                JSONArray lessonsArray = new JSONArray();
                for (lesson l : c.getLessons()) {
                    JSONObject lessonObj = new JSONObject();
                    lessonObj.put("lessonId", l.getLessonId());
                    lessonObj.put("title", l.getLessonTitle());
                    lessonObj.put("content", l.getContent());
                    lessonObj.put("resources", l.getResources());
                    lessonsArray.put(lessonObj);
                }
                courseObj.put("lessons", lessonsArray);

                coursesArray.put(courseObj);
            }
        }
        try (FileWriter file = new FileWriter(filename)) {
            file.write(coursesArray.toString(4));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString(){
        return "(" + this.getCourseId() + ") " + this.getCourseTitle();
    }

}








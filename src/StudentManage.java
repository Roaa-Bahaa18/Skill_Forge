import java.util.ArrayList;

public class StudentManage {
    private Student student;
    public StudentManage(Student student) {
        this.student = student;
    }

    //Course browsing --> aka available courses
    public course[] viewAvailableCourse()
    {
        //ArrayList<course> available = course.readFromFile();
        available.removeAll(student.getEnrolledCourses());
        return available.toArray(new course[0]);
    }

    //Course enrollment --> this function won't call unless it's available
    public boolean enrollCourse(course c)
    {
        ArrayList<course> enrolled = student.getEnrolledCourses();
        if(enrolled.contains(c))
        {
            return false;
        }
        else
        {
            enrolled.add(c);
            student.setEnrolledCourses(enrolled);
            //savetofile(); -->users
            c.addStudent(student);
           // course.saveToFile(); ???
            return true;
        }
    }
    //Lesson Access w progress tracking w a mark lessons as complete kman
    public float[] progressTrack()
    {
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
                if(l.getCompleted()) percent++;
            }
            progress[i] = (float) (percent*100)/lessons.size();
            i++;
        }
        return progress;
    }

    public boolean completeLesson(course c,lesson l)
    {
       ArrayList<lesson> lessons = c.getLessons();
        for(lesson l2: lessons)
        {
            if((l2.getLessonId())==(l.getLessonId()))
            {
                if(!l2.getCompleted()) {
                    l2.setCompleted(true);
                     //savetofile();
                    return true;
                }
                else return false;
            }
        }
        return false;
    }

    //view enrolled courses
    public course[] viewEnrolledCourse()
    {
        return student.getEnrolledCourses().toArray(new course[0]);
    }



}

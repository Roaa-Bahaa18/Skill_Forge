import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CoursePanel extends JFrame {
    private JPanel courseview;
    private JButton enrollCourseButton;
    private JTextArea coursedetails;
    private JButton viewLessonsButton;

    public CoursePanel(StudentManage s,course c) {
        setTitle("Course Details");
        setSize(400, 400);
        setVisible(true);
        setContentPane(courseview);

        String[] details = new String[4];
        details[0] = "Course ID:" + c.getCourseId();
        details[1] = "Course Title:" + c.getCourseTitle();
        details[2] = "Course Description:" + c.getCourseDescription();
        details[3] = "Course Instructor:" + c.getInstructorId();
        for(int i = 0; i < details.length; i++)
        {
            coursedetails.append(details[i] + "\n");
        }
        coursedetails.setEditable(false);


        enrollCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(s.enrollCourse(c))
                {
                    JOptionPane.showMessageDialog(CoursePanel.this,"Course Enrolled Successfully");
                }
                else
                {
                    JOptionPane.showMessageDialog(CoursePanel.this,"Course Already Enrolled");
                }
            }
        });
        viewLessonsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lesson[] lessons = c.getLessons().toArray(new lesson[0]);
                String lessontitles = "1- " + lessons[0].getLessonTitle() +"\n";
                for(int i = 1; i < lessons.length; i++)
                {
                    lessontitles += (i+1) + "- " + lessons[i].getLessonTitle() +"\n";
                }
                JOptionPane.showMessageDialog(CoursePanel.this,lessontitles);
            }
        });
    }
}

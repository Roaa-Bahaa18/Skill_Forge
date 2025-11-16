import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class InstructorPanel extends JFrame{
    private JPanel instructorpage;
    private JButton createCourseButton;
    private JButton editCourseButton;
    private JButton deleteCourseButton;
    private JButton manageLessonsButton;
    private JButton logoutButton;

    public InstructorPanel(Instructor instructor){
        setTitle("InstructorDashBoard");
        setSize(400, 400);
        setVisible(true);
        setContentPane(instructorpage);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        createCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageCoursePanel(instructor, false);
            }
        });
        editCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = JOptionPane.showInputDialog("Enter Course ID:");
                ArrayList<course> courses = courseManagement.loadCourses();
                boolean found = false;
                for(course c : courses){
                    if(c.getCourseId().matches(idText)) {
                        new ManageCoursePanel(instructor, true, c);
                        found = true;
                        break;
                    }
                }
                if(!found){
                    Object[] options = { "OK" };

                    JOptionPane.showOptionDialog(null, "Course not Found!!", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                }
                new InstructorPanel(instructor);
            }
        });
        deleteCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InstructorManagement manage = new InstructorManagement(instructor);
                String idText = JOptionPane.showInputDialog("Enter Course ID:");
                boolean check = manage.deleteCourse(idText);
                if(check){
                    Object[] options = { "OK" };
                    JOptionPane.showOptionDialog(null, "Course Deleted Successfully!", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    new InstructorPanel(instructor);
                }
                else{
                    Object[] options = { "OK" };
                    JOptionPane.showOptionDialog(null, "Error Deleting Course", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    new InstructorPanel(instructor);
                }
            }
        });
        manageLessonsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<course> courses = courseManagement.loadCourses();
                InstructorManagement manage = new InstructorManagement(instructor);
                String idText = JOptionPane.showInputDialog("Enter Course ID:");
                boolean check = manage.deleteCourse(idText);
                boolean found = false;
                for(course c : courses){
                    if(c.getCourseId().matches(idText)) {
                        new ManageLessonPanel(instructor, c);
                        found = true;
                        break;
                    }
                }
                if(!found){
                    Object[] options = { "OK" };
                    JOptionPane.showOptionDialog(null, "Course not Found!", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                }
                new InstructorPanel(instructor);
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login();
            }
        });

    }


}


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InstructorPanel extends JFrame{
    private JPanel instructorpage;
    private JButton createCourseButton;
    private JButton editCourseButton;
    private JButton deleteCourseButton;
    private JButton manageLessonsButton;
    private JButton logoutButton;
    private JButton viewButton;

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
                dispose();
            }
        });
        editCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = JOptionPane.showInputDialog("Enter Course ID:");
                if(idText == null || idText.isEmpty()){ return; }
                ArrayList<course> courses = courseManagement.loadCourses();
                boolean found = false;
                for(course c : courses){
                    if(c.getCourseId().matches(idText)) {
                        new ManageCoursePanel(instructor, true, c);
                        dispose();
                        found = true;
                        break;
                    }
                }
                if(!found){
                    Object[] options = { "OK" };
                    JOptionPane.showOptionDialog(null, "Course not Found!!", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    new InstructorPanel(instructor);
                    dispose();
                }
            }
        });
        deleteCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InstructorManagement manage = new InstructorManagement(instructor);
                String idText = JOptionPane.showInputDialog("Enter Course ID:");
                if(idText == null || idText.isEmpty()){ return; }
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
                    dispose();
                }
            }
        });
        manageLessonsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<course> courses = courseManagement.loadCourses();
                InstructorManagement manage = new InstructorManagement(instructor);
                String idText = JOptionPane.showInputDialog("Enter Course ID:");
                if(idText == null || idText.isEmpty()){ return; }
                boolean found = false;
                for(course c : courses){
                    if(c.getCourseId().matches(idText)) {
                        new ManageLessonPanel(instructor, c);
                        dispose();
                        found = true;
                        break;
                    }
                }
                if(!found){
                    Object[] options = { "OK" };
                    JOptionPane.showOptionDialog(null, "Course not Found!", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    new InstructorPanel(instructor);
                    dispose();
                }
            }
        });
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseID = JOptionPane.showInputDialog("Enter Course ID:");
                if(courseID == null || courseID.isEmpty()){ return; }
                ArrayList<course> courses = courseManagement.loadCourses();
                boolean found = false;
                for(course c : courses){
                    if(c.getCourseId().matches(courseID)) {
                        viewStudents(instructor, c);
                        dispose();
                        found = true;
                        break;
                    }
                }
                if(!found){
                    Object[] options = { "OK" };
                    JOptionPane.showOptionDialog(null, "Course not Found!!", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    new InstructorPanel(instructor);
                    dispose();
                }
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FirstPage().setVisible(true);
                dispose();
            }
        });
    }

    private void viewStudents(Instructor instructor, course course){
        ArrayList<String> studentIDs = course.getStudentIds();
        if (studentIDs == null || studentIDs.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No students enrolled in this course.", "Course Students",JOptionPane.INFORMATION_MESSAGE);
            new InstructorPanel(instructor);
            dispose();
        }
        else{
            List<User> users = userService.loadUsers();
            StringBuilder sb = new StringBuilder("Enrolled Students:\n\n");
            for (User u : users) {
                if (u instanceof Student) {
                    Student s = (Student) u;
                    if (studentIDs.contains(s.getUserId())) {
                        sb.append("- ").append(s.getUsername()).append(" (ID: ").append(s.getUserId()).append(")\n");
                    }
                }
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Course Students", JOptionPane.INFORMATION_MESSAGE);
            new InstructorPanel(instructor);
            dispose();
        }


    }

}


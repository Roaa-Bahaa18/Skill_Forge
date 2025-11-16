import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class StudentPanel extends JFrame {
    private JLabel Welcome;
    private JPanel studentpage;
    private JComboBox options;
    private JPanel viewpanel;
    private JButton logoutButton;
    private JList<course> courselist;
    public StudentPanel(Student student) {
        setTitle("StudentDashBoard");
        setSize(400, 400);
        setVisible(true);
        setContentPane(studentpage);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String option = (String) options.getSelectedItem();
                View(option,student);
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(StudentPanel.this, "Logout Successful!\nBack to Login Page");
                new Login().setVisible(true);
                dispose();
            }
        });
    }

    void View(String option,Student student) {
        StudentManage s = new StudentManage(student);
        for (javax.swing.event.ListSelectionListener lsl : courselist.getListSelectionListeners()) {
            courselist.removeListSelectionListener(lsl);
        }
        switch (option)
        {
            case "Browse Courses": {
                course[] courses = s.viewAvailableCourse();
                courselist.setListData(courses);
                courselist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                courselist.addListSelectionListener(e ->
                {
                    course c = courselist.getSelectedValue();
                    if (c != null) {
                    new CoursePanel(s,c);
                }
                });
                break;
            }
            case "Show Enrolled Courses": {
                course[] courses = s.viewEnrolledCourse();
                courselist.setListData(courses);
                courselist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                courselist.addListSelectionListener(e ->
                {
                    course c = courselist.getSelectedValue();
                    if (c != null) {
                        new LessonPanel(s, c);
                    }
                });
                break;

            }
            case "Track Progress": {
                course[] courses = s.viewEnrolledCourse();
                float[] progress = s.progressTrack();
                courselist.setListData(courses);
                courselist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                courselist.addListSelectionListener(e ->
                {
                    course c = courselist.getSelectedValue();
                    if (c != null) {
                        JOptionPane.showMessageDialog(StudentPanel.this, "This course is" + progress[courselist.getSelectedIndex()] + "completed");
                    }
                });
                break;
            }

        }
    }

}



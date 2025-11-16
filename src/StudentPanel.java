import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
                    if(!e.getValueIsAdjusting())
                    {
                    course c = courselist.getSelectedValue();
                    if (c != null) {
                    new CoursePanel(s,c);
                    courselist.clearSelection();
                }}
                });
                break;
            }
            case "Show Enrolled Courses": {
                course[] courses = s.viewEnrolledCourse();
                courselist.setListData(courses);
                courselist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                courselist.addListSelectionListener(e ->
                {
                    if(!e.getValueIsAdjusting())
                    {
                    course c = courselist.getSelectedValue();
                    if (c != null) {
                        new LessonPanel(s, c);
                        courselist.clearSelection();
                    }}
                });
                break;

            }
            case "Track Progress": {
                course[] courses = s.viewEnrolledCourse();
                courselist.setListData(courses);
                courselist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                courselist.addListSelectionListener(e ->
                {
                    if(!e.getValueIsAdjusting())
                    {
                    course c = courselist.getSelectedValue();
                    if (c != null) {
                        float progress=s.progressTrack(c);
                        JOptionPane.showMessageDialog(StudentPanel.this, "This course is " + progress + "% completed");
                        courselist.clearSelection();
                    }}
                });
                break;
            }
            default:
            {
                DefaultListModel<course> d = new DefaultListModel<>();
                courselist.setModel(d);
                break;
            }

        }
    }

}



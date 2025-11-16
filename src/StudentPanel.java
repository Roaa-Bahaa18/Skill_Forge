import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentPanel extends JFrame {
    private JLabel Welcome;
    private JPanel studentpage;
    private JComboBox options;
    private JPanel viewpanel;
    private JButton logoutButton;

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
        switch (option)
        {
            case "Browse Courses": {
                course[] courses = s.viewAvailableCourse();
                JList<course> courseList = new JList<>(courses);
                courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                courseList.addListSelectionListener(e ->
                {
                    course c = courseList.getSelectedValue();
                    if (c != null) {
                        new CoursePanel(s,c);
                    }
                });
            }
            case "Show Enrolled Courses": {
                course[] courses = s.viewEnrolledCourse();
                JList<course> courseList = new JList<>(courses);
                courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                courseList.addListSelectionListener(e ->
                {
                    course c = courseList.getSelectedValue();
                    if (c != null) {
                        new LessonPanel(s, c);
                    }
                });

            }
            case "Track Progress":
            {
                course[] courses = s.viewEnrolledCourse();
                float[] progress = s.progressTrack();
                JList<course> courseList = new JList<>(courses);
                courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                courseList.addListSelectionListener(e ->
                {
                    course c = courseList.getSelectedValue();
                    if (c != null) {
                        JOptionPane.showMessageDialog(StudentPanel.this, "This course is" + progress[courseList.getSelectedIndex()] + "completed");
                    }
                });


        }

    }
}

}

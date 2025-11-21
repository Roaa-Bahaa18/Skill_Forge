import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AdminPanel extends JFrame{
    private JPanel adminpage;
    private JTextArea courseDetails;
    private JPanel panel1;
    private JButton acceptButton;
    private JButton rejectButton;

    public AdminPanel(Admin admin){
        setTitle("AdminDashBoard");
        setSize(400, 400);
        setVisible(true);
        setContentPane(adminpage);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        String courses [] = new String[100];
        AdminManagement a = new AdminManagement(admin);

        ArrayList<course> pendingCourses = a.getPendingCourses();
        for(int i = 0; i < pendingCourses.size(); i++){
            courses[i] = pendingCourses.get(i).toString();
            courseDetails.append(courses[i]+"\n");
        }
        courseDetails.setEditable(false);

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //admin.manageCourse(c, true);
                JOptionPane.showMessageDialog(AdminPanel.this, "Course has been accepted");

            }
        });
        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //a.manageCourse(c, false);
                JOptionPane.showMessageDialog(AdminPanel.this, "Course has been rejected");

            }
        });
    }



}

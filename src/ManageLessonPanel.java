import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageLessonPanel extends JFrame{
    private JPanel manageLesson;
    private JButton addLessonButton;
    private JButton editLessonButton;
    private JButton deleteLessonButton;
    private course course;

    public ManageLessonPanel(Instructor instructor, course course){
        setTitle("InstructorDashBoard");
        setSize(400, 400);
        setVisible(true);
        setContentPane(manageLesson);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.course = course;


        addLessonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}


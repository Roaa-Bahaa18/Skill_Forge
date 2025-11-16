import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManageLessonPanel extends JFrame{
    private JPanel manageLesson;
    private JButton addLessonButton;
    private JButton editLessonButton;
    private JButton deleteLessonButton;
    private JButton backButton;
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
                new UpdateLesson(instructor, false, course.getCourseId());
                dispose();
            }
        });
        editLessonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = JOptionPane.showInputDialog("Enter Lesson ID:");
                ArrayList<lesson> lessons = course.getLessons();
                boolean found = false;
                for(lesson l : lessons){
                    if(l.getLessonId().matches(idText)){
                        new UpdateLesson(instructor, true, course.getCourseId(), l);
                        dispose();
                        found = true;
                        break;
                    }
                }
                if(!found){
                    Object[] options = { "OK" };
                    JOptionPane.showOptionDialog(null, "Lesson not Found!!", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    new InstructorPanel(instructor);
                    dispose();
                }
            }
        });
        deleteLessonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InstructorManagement manage = new InstructorManagement(instructor);
                String idText = JOptionPane.showInputDialog("Enter Lesson ID:");
                boolean check = manage.deleteLesson(course.getCourseId(), idText);
                if(check){
                    Object[] options = { "OK" };
                    JOptionPane.showOptionDialog(null, "Lesson Deleted Successfully!", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    new InstructorPanel(instructor);
                }
                else{
                    Object[] options = { "OK" };
                    JOptionPane.showOptionDialog(null, "Error Deleting Lesson", "Message", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    new InstructorPanel(instructor);
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InstructorPanel(instructor);
                dispose();
            }
        });
    }
}


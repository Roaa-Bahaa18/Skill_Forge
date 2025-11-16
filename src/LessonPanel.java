import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LessonPanel extends JFrame {
    private JList<lesson> lessonlist;
    private JPanel lessonview;

    public LessonPanel(StudentManage s, course c) {
        setTitle("Lesson Panel");
        setSize(400, 400);
        setVisible(true);
        setContentPane(lessonview);

        lesson[] lessons = c.getLessons().toArray(new lesson[0]);
         lessonlist.setListData(lessons);
        lessonlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lessonlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            lesson l = lessonlist.getSelectedValue();
            if (l != null) {
                String lessondetails = "Lesson ID:" + l.getLessonId() +"\nLesson Title:" + l.getLessonTitle() + "\nContent:" +l.getContent()+"\n";
                int choice = JOptionPane.showConfirmDialog(null, lessondetails + "\nDo you want to complete this lesson?");
                if (choice == JOptionPane.YES_OPTION) {
                    if(s.completeLesson(c, l))
                        JOptionPane.showMessageDialog(null, "Lesson completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(null, "Lesson Already Completed!", "Error", JOptionPane.ERROR_MESSAGE);
                }}
            }
        });






    }

}

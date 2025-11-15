import javax.swing.*;

public class LessonPanel extends JFrame {
    private JList lessonlist;
    private JPanel lessonview;

    public LessonPanel(StudentManage s, course c) {
        setTitle("Lesson Panel");
        setSize(400, 400);
        setVisible(true);
        setContentPane(lessonview);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        lesson[] lessons = c.getLessons().toArray(new lesson[0]);
        JList<lesson> lessonlist = new JList<>(lessons);
        lessonlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lessonlist.addListSelectionListener(e ->
        {
            lesson l = lessonlist.getSelectedValue();
            if (l != null) {
                String lessondetails = "Lesson ID:" + l.getLessonId() +"\nLesson Title:" + l.getLessonTitle() + "\nContent:" +l.getContent()+"\n";
                if(l.getCompleted())
                {
                    JOptionPane.showMessageDialog(null, lessondetails + "Status: Completed");
                }
                else
                {
                int choice = JOptionPane.showConfirmDialog(null, lessondetails + "\nDo you want to complete this lesson?");
                if (choice == JOptionPane.YES_OPTION) {
                    if(s.completeLesson(c, lessonlist.getSelectedValue()))
                        JOptionPane.showMessageDialog(null, "Lesson completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(null, "Lesson not completed!", "Error", JOptionPane.ERROR_MESSAGE);
                }}
            }
        });






    }

}

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UpdateLesson extends JFrame{
    private JPanel Lesson;
    private JTextField idField;
    private JTextField titleField;
    private JTextField contentField;
    private JTextField resourcesField;
    private JButton saveButton;
    private JButton backButton;
    boolean isEditMode;
    String courseId;
    lesson lesson;

    public UpdateLesson(Instructor instructor, boolean mode, String courseId){
        this(instructor, mode, courseId, null);
    }

    public UpdateLesson(Instructor instructor, boolean mode, String courseId, lesson lesson){
        setTitle("InstructorDashBoard");
        setSize(400, 400);
        setVisible(true);
        setContentPane(Lesson);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.isEditMode = mode;
        this.courseId = courseId;
        this.lesson= lesson;
        idField.setEnabled(false);

        setForm();

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isEditMode){
                    editLesson(instructor);
                }
                else{
                    addLesson(instructor);
                }
            }
        });
    }

    private void setForm() {
        if (isEditMode) {
            idField.setText(String.valueOf(lesson.getLessonId()));
            titleField.setText(lesson.getLessonTitle());
            contentField.setText(lesson.getContent());
            resourcesField.setText(String.valueOf(lesson.getResources()));
            saveButton.setText("Save Changes");
        } else {
            saveButton.setText("Add Lesson");
        }
    }

    private void addLesson(Instructor instructor){
        String lessonTitle = titleField.getText();
        String lessonContent = contentField.getText();
        ArrayList<String> resources = new ArrayList<>();
        resources.add(resourcesField.getText());

        InstructorManagement manage = new InstructorManagement(instructor);
        manage.addLesson(courseId, lessonTitle, lessonContent, resources);

        JOptionPane.showMessageDialog(null, "Lesson Added Successfully...");
        new InstructorPanel(instructor);
        dispose();
    }

    private void editLesson(Instructor instructor){
        InstructorManagement manage = new InstructorManagement(instructor);
        ArrayList<String> resources = new ArrayList<>();
        resources.add(resourcesField.getText());
        manage.editLesson(courseId, lesson.getLessonId(), titleField.getText(), contentField.getText(), resources);

        JOptionPane.showMessageDialog(this, "Lesson Edited Successfully");
        new InstructorPanel(instructor);
        dispose();
    }
}

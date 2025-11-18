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
    private course c;
    public UpdateLesson(Instructor instructor, boolean mode, String courseId){
        this(instructor, mode, courseId, null);
    }

    public UpdateLesson(Instructor instructor, boolean mode, String courseId, lesson lesson){
        setTitle("Lesson");
        setSize(400, 400);
        setVisible(true);
        setContentPane(Lesson);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.isEditMode = mode;
        this.courseId = courseId;
        this.lesson= lesson;
        c = courseManagement.getCourseByID(courseId);
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

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageLessonPanel(instructor, c);
                dispose();
            }
        });
    }

    private void setForm() {
        if (isEditMode) {
            idField.setText(String.valueOf(lesson.getLessonId()));
            titleField.setText(lesson.getLessonTitle());
            contentField.setText(lesson.getContent());
            String joinedResources = String.join(", ", lesson.getResources());
            resourcesField.setText(joinedResources);
            saveButton.setText("Save Changes");
        } else {
            saveButton.setText("Add Lesson");
        }
    }

    private void addLesson(Instructor instructor){
        String lessonTitle = titleField.getText();
        String lessonContent = contentField.getText();
        String resourcesText = resourcesField.getText();
        if(titleField.getText().isEmpty()||contentField.getText().isEmpty()|| resourcesField.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Please fill all the fields");
            return;
        }
        if(!Validations.isValidLessonTitle(courseId,lessonTitle))
        {
            JOptionPane.showMessageDialog(null, "This lesson is already added before");
            return;
        }

        ArrayList<String> resources = new ArrayList<>();
        String[] resourceArray = resourcesText.split("\\s*,\\s*");
        for (String resource : resourceArray) {
            String Resource = resource.trim();
            if (!Resource.isEmpty()) {
                resources.add(Resource);
            }
        }
        InstructorManagement manage = new InstructorManagement(instructor);
        manage.addLesson(courseId, lessonTitle, lessonContent, resources);

        JOptionPane.showMessageDialog(null, "Lesson Added Successfully...");
        c = courseManagement.getCourseByID(courseId);
        new ManageLessonPanel(instructor,c);
        dispose();
    }

    private void editLesson(Instructor instructor){
        InstructorManagement manage = new InstructorManagement(instructor);
        String resourcesText = resourcesField.getText();
        ArrayList<String> resources = new ArrayList<>();
        String[] resourceArray = resourcesText.split("\\s*,\\s*");
        for (String resource : resourceArray) {
            String Resource = resource.trim();
            if (!Resource.isEmpty()) {
                resources.add(Resource);
            }
        }
        manage.editLesson(courseId, lesson.getLessonId(), titleField.getText(), contentField.getText(), resources);
        JOptionPane.showMessageDialog(this, "Lesson Edited Successfully");
        c = courseManagement.getCourseByID(courseId);
        new ManageLessonPanel(instructor,c);
        dispose();
    }
}

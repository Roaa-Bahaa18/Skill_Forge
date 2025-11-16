import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageCoursePanel extends JFrame{
    private JPanel manageCourse;
    private JTextField titleField;
    private JTextField idField;
    private JTextField descriptionField;
    private JButton saveButton;
    private JButton backButton;
    private boolean isEditMode = false;
    private course course;

    public ManageCoursePanel(Instructor instructor, boolean mode){
        this(instructor, mode, null);
    }
    public ManageCoursePanel(Instructor instructor, boolean mode, course courseToEdit){
        setTitle("InstructorDashBoard");
        setSize(400, 400);
        setVisible(true);
        setContentPane(manageCourse);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.isEditMode = mode;
        this.course = courseToEdit;
        idField.setEnabled(false);

        setForm();

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isEditMode){
                    editCourse(instructor);
                }
                else{
                    addCourse(instructor);
                }
            }
        });
    }

    private void setForm() {
        if (isEditMode) {
            idField.setText(String.valueOf(course.getCourseId()));
            titleField.setText(course.getCourseTitle());
            descriptionField.setText(course.getCourseDescription());
            saveButton.setText("Save Changes");
        } else {
            saveButton.setText("Add Course");
        }
    }

    private void addCourse(Instructor instructor){
        String courseTitle = titleField.getText();
        String description = descriptionField.getText();

        InstructorManagement manage = new InstructorManagement(instructor);
        manage.createCourse(courseTitle, description);

        JOptionPane.showMessageDialog(null, "Course Created Successfully...");
        new InstructorPanel(instructor);
        dispose();
    }

    private void editCourse(Instructor instructor) {
        InstructorManagement manage = new InstructorManagement(instructor);
        manage.editCourse(course.getCourseId(), titleField.getText(), descriptionField.getText());

        JOptionPane.showMessageDialog(this, "Course Updated Successfully");
        new InstructorPanel(instructor);
        dispose();
    }
}


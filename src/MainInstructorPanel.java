import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MainInstructorPanel extends JFrame{
    private JLabel welcome;
    private JTabbedPane tabbedPane1;
    private JPanel CreateCourse;
    private JTabbedPane tabbedPane2;
    private JTabbedPane tabbedPane3;
    private JPanel coursedetails;
    private JPanel lessondetails;
    private JPanel addLesson;
    private JPanel coursedata;
    private JPanel insights;
    private JPanel sperformance;
    private JPanel quiz;
    private JTextField coursetitle;
    private JTextField description;
    private JButton createCourseButton;
    private JTable coursestable;
    private JScrollPane scrollcourses;
    private JTable Lessontable;
    private JScrollPane scrolllesson;
    private JComboBox addlessoncombo;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton saveLessonButton;
    private JPanel mainpanel;
    private JButton logoutButton;
    private JComboBox coursecombo;
    private Instructor instructor = null;
    private final InstructorManagement im;
    private final String InstructorName;
    private ArrayList<course> createdCourses= new ArrayList<>();

    public MainInstructorPanel(Instructor instructor) {
        setTitle("Skill-Forge Student Main Panel");
        setContentPane(mainpanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
        this.instructor = instructor;
        im = new InstructorManagement(instructor);

        InstructorName= instructor.getUsername();
        createdCourses= instructor.getCreatedCourses();
        setUpcoursestable();
        welcome.setText("Welcome " + InstructorName);


        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Logging out Instructor " + InstructorName);
                new FirstPage().setVisible(true);
                dispose();
            }
        });
        createCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseTitle = coursetitle.getText();
                String courseDescription = description.getText();
                if(courseTitle.isEmpty() || courseDescription.isEmpty()){
                    JOptionPane.showMessageDialog(null,"Fields cannot be empty!");
                }
                else{
                    im.createCourse(courseTitle,courseDescription);
                    JOptionPane.showMessageDialog(null,"Course Created Successfully!");
                    createdCourses = im.getInstructor().getCreatedCourses();
                    updateCourseTable();
                    tabbedPane1.setSelectedIndex(1);
                    tabbedPane2.setSelectedIndex(0);
                    coursetitle.setText("");
                    description.setText("");
            }}
        });
    }
    private void setUpcoursestable()
    {
        String[] columnNames= {"Course ID","Course Title","Course Description","Lessons Count","Number of Enrolled Students","Edit","Delete"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return columnIndex == 1 || columnIndex == 2 ;
            }
        };
        coursestable.setModel(model);
        coursestable.getTableHeader().setReorderingAllowed(false);
        updateCourseTable();

        coursestable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = coursestable.getSelectedRow();
                    int column = coursestable.getSelectedColumn();
                    if (row >= 0 && column>=6) {
                        if(column==5){
                            String courseId = (String) coursestable.getValueAt(row,0);
                            String courseTitle = (String) coursestable.getValueAt(row,1);
                            String courseDescription = (String) coursestable.getValueAt(row,2);
                            int option = JOptionPane.showConfirmDialog(null,"Do you want to Edit "+courseTitle+" ?","Edit Confirmation",JOptionPane.YES_NO_OPTION);
                            if(option==JOptionPane.YES_OPTION){
                                im.editCourse(courseId,courseTitle,courseDescription);
                                createdCourses = im.getInstructor().getCreatedCourses();
                                updateCourseTable();
                            }
                        }
                        if(column==6){
                            String courseId= (String) coursestable.getValueAt(row,0);
                            String courseTitle = (String) coursestable.getValueAt(row,1);
                          int option = JOptionPane.showConfirmDialog(null,"Do you want to delete "+courseTitle+" ?","Deletion Confirmation",JOptionPane.YES_NO_OPTION);
                          if(option==JOptionPane.YES_OPTION){
                              im.deleteCourse(courseId);
                              createdCourses = im.getInstructor().getCreatedCourses();
                              model.removeRow(row);
                              coursestable.setModel(model);
                              updateCourseTable();
                          }
                        }
                    }
                }
            }
        });

    }
    private void updateCourseTable()
    {
        DefaultTableModel model = (DefaultTableModel) coursestable.getModel();
        model.setRowCount(0);
        for (course c : createdCourses) {
            model.addRow(new Object[]{c.getCourseId(),c.getCourseTitle(),c.getCourseDescription(),c.getLessons().size(),c.getStudentIds().size(),"Edit","Delete"});
        }
        coursestable.setModel(model);
    }


}

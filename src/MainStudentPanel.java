import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainStudentPanel extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JButton logoutButton;
    private JTabbedPane tabbedPane;
    private JTable availableCoursesTable;
    private JTable enrolledCoursesTable;
    private JScrollPane availableCoursesScrollPane;
    private JScrollPane enrolledCoursesScrollPane;
    private final String StudentName;
    private final StudentManage sm;
    private final List<course> availableCourses;
    private final List<course> enrolledCourses;
    private Student student = null;
    public MainStudentPanel(Student student)
    {
        setTitle("Skill-Forge Student Main Panel");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
        this.student=student;
        sm= new StudentManage(student);
        StudentName= student.getUsername();
        availableCourses= sm.viewAvailableCourses();
        enrolledCourses=sm.viewEnrolledCourses();

        welcomeLabel.setText("Welcome, " + StudentName);
        setupAvailableCoursesTab();
        setupEnrolledCoursesTab();

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainStudentPanel.this,"Logging out student " + StudentName);
                new FirstPage().setVisible(true);
                dispose();
            }
        });
    }
    private void setupAvailableCoursesTab() {
        String[] columnNames = {"Course Name", "Course ID", "Lessons Number"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        availableCoursesTable.setModel(model);
        availableCoursesTable.getTableHeader().setReorderingAllowed(false);
        updateAvailableCoursesTable();
        availableCoursesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = availableCoursesTable.getSelectedRow();
                    if (row >= 0) {
                        String courseId = (String) model.getValueAt(row, 1);
                        handleAvailableCourseClick(courseId);
                    }
                }
            }
        });
    }
    private void updateAvailableCoursesTable() {
        DefaultTableModel model = (DefaultTableModel) availableCoursesTable.getModel();
        model.setRowCount(0);

        for (course course : availableCourses) {
            model.addRow(new Object[]{course.getCourseTitle(), course.getCourseId(), course.getLessons().size()});
        }
    }
    private void handleAvailableCourseClick(String courseId) {
        course course = availableCourses.stream()
                .filter(c -> c.getCourseId().equals(courseId))
                .findFirst()
                .orElse(null);

        if (course == null) return;
        String[] options = {"Enroll in Course", "View Lesson Details", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Select an action for course: " + course.getCourseTitle(),
                "Course Action",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION) {
            Enrollment(courseId);
        } else if (choice == JOptionPane.NO_OPTION) {
            showSimpleLessonDetail(course);
        }
    }

    private void Enrollment(String courseId) {
        course courseToEnroll = availableCourses.stream()
                .filter(c -> c.getCourseId().equals(courseId))
                .findFirst()
                .orElse(null);

        if (courseToEnroll != null) {
            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "Do you want to enroll in " + courseToEnroll.getCourseTitle() + "?",
                    "Confirm Enrollment", JOptionPane.YES_NO_OPTION);

            if (dialogResult == JOptionPane.YES_OPTION) {
                sm.enrollCourse(courseToEnroll);
                enrolledCourses.add(courseToEnroll);
                availableCourses.remove(courseToEnroll);
                updateAvailableCoursesTable();
                updateEnrolledCoursesTable();
                JOptionPane.showMessageDialog(this, courseToEnroll.getCourseTitle() + " successfully enrolled! Check the 'Enrolled Courses' tab.", "Success", JOptionPane.INFORMATION_MESSAGE);
                tabbedPane.setSelectedIndex(1);
            }
        }
    }
    private void showSimpleLessonDetail(course course) {
        String[] lessonColumnNames = {"Lesson Name", "Lesson ID"};
        DefaultTableModel lessonModel = new DefaultTableModel(lessonColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (lesson lesson : course.getLessons()) {
            lessonModel.addRow(new Object[]{lesson.getLessonTitle(), lesson.getLessonId()});
        }
        JTable lessonTable = new JTable(lessonModel);
        lessonTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane lessonScrollPane = new JScrollPane(lessonTable);
        JDialog dialog = new JDialog(this, "Details for: " + course.getCourseTitle(), true);
        dialog.setLayout(new BorderLayout());
        dialog.add(lessonScrollPane, BorderLayout.CENTER);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void setupEnrolledCoursesTab() {
        String[] columnNames = {"Course Name", "Course ID", "Progress"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        enrolledCoursesTable.setModel(model);
        enrolledCoursesTable.getTableHeader().setReorderingAllowed(false);
        updateEnrolledCoursesTable();
        enrolledCoursesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = enrolledCoursesTable.getSelectedRow();
                    if (row >= 0) {
                        String courseId = (String) model.getValueAt(row, 1);
                        showLessonDetail(courseId);
                    }
                }
            }
        });
    }

    private void updateEnrolledCoursesTable() {
        DefaultTableModel model = (DefaultTableModel) enrolledCoursesTable.getModel();
        model.setRowCount(0);

        for (course course : enrolledCourses) {
            model.addRow(new Object[]{course.getCourseTitle(), course.getCourseId(), sm.progressTrack(course)});
        }
    }

    private void showLessonDetail(String courseId) {
        course course = enrolledCourses.stream()
                .filter(c -> c.getCourseId().equals(courseId))
                .findFirst()
                .orElse(null);

        if (course == null) return;
        String[] lessonColumnNames = {"Lesson Name", "Lesson ID", "Status"};
        DefaultTableModel lessonModel = new DefaultTableModel(lessonColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        List<lesson> lessons= course.getLessons();
        ArrayList<Boolean> lessonStatus = student.getProgress().get(courseId);
        int i=0;
        for (lesson lesson : lessons) {
            String status = (lessonStatus.get(i)) ? "Completed" : "Uncompleted";
            lessonModel.addRow(new Object[]{lesson.getLessonTitle(), lesson.getLessonId(), status});
            i++;
        }
        JTable lessonTable = new JTable(lessonModel);
        lessonTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane lessonScrollPane = new JScrollPane(lessonTable);
        lessonTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lessonTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = lessonTable.getSelectedRow();
                    if (row >= 0) {
                        String lessonId = (String) lessonModel.getValueAt(row, 1);
                        String status = (String) lessonModel.getValueAt(row, 2);

                        if (Objects.equals(status, "Uncompleted")) {
                            LessonCompletion(course, lessonId, lessonModel);
                        } else {
                            JOptionPane.showMessageDialog(lessonTable, "This lesson is already completed.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });

        JDialog dialog = new JDialog(this, "Lessons for " + course.getCourseTitle(), true);
        dialog.setLayout(new BorderLayout());
        dialog.add(lessonScrollPane, BorderLayout.CENTER);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void LessonCompletion(course course, String lessonId, DefaultTableModel lessonModel) {
        int dialogResult = JOptionPane.showConfirmDialog(this,
                "Do you want to complete this?",
                "Confirm Lesson Completion", JOptionPane.YES_NO_OPTION);

        if (dialogResult == JOptionPane.YES_OPTION) {
            lesson lessonToComplete = course.getLessons().stream()
                    .filter(l -> l.getLessonId().equals(lessonId))
                    .findFirst()
                    .orElse(null);

            if (lessonToComplete != null && !lessonToComplete.getStatus()) {
                lessonToComplete.setStatus(true);
                sm.completeLesson(course,lessonToComplete);

                for (int i = 0; i < lessonModel.getRowCount(); i++) {
                    if (lessonModel.getValueAt(i, 1).equals(lessonId)) {
                        lessonModel.setValueAt("Completed", i, 2);
                        break;
                    }
                }
                updateEnrolledCoursesTable();
                JOptionPane.showMessageDialog(this, lessonToComplete.getLessonTitle() + " completed", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

}

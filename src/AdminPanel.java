import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AdminPanel extends JFrame{
    private JPanel adminpage;
    private JScrollPane coursesScrollPane;
    private JTable pendingCoursesTable;
    private JButton acceptButton;
    private JButton rejectButton;
    AdminManagement ad= null;
    private course selectedCourse = null;
    private DefaultTableModel tableModel;

    public AdminPanel(Admin admin) {
        setTitle("Skill Forge Admin Dashboard");
        setSize(800, 600);
        setContentPane(adminpage);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ad = new AdminManagement(admin);

        String[] columnNames = {"Course ID", "Title", "Instructor", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if (pendingCoursesTable == null) {
            pendingCoursesTable = new JTable(tableModel);
            coursesScrollPane.setViewportView(pendingCoursesTable);
        } else {
            pendingCoursesTable.setModel(tableModel);
        }
        acceptButton.setEnabled(false);
        rejectButton.setEnabled(false);
        loadPendingCourses();

        pendingCoursesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean isRowSelected = pendingCoursesTable.getSelectedRow() != -1;
                acceptButton.setEnabled(isRowSelected);
                rejectButton.setEnabled(isRowSelected);
            }
        });
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = pendingCoursesTable.getSelectedRow();

                if (selectedRow != -1) {
                    String courseId = (String) tableModel.getValueAt(selectedRow, 0);
                    course currentCourse = ad.findCourseById(courseId);
                    if (currentCourse != null) {
                        String courseTitle = currentCourse.getCourseTitle();
                        ad.manageCourse(currentCourse, true);
                        removeCourseFromTable(currentCourse);
                        JOptionPane.showMessageDialog(AdminPanel.this,
                                courseTitle + " has been accepted.");
                    } else {
                        JOptionPane.showMessageDialog(AdminPanel.this,
                                "Error", "Data Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = pendingCoursesTable.getSelectedRow();
                if (selectedRow != -1) {
                    String courseId = (String) tableModel.getValueAt(selectedRow, 0);
                    course currentCourse = ad.findCourseById(courseId);
                    if (currentCourse != null) {
                        String courseTitle = currentCourse.getCourseTitle();
                        ad.manageCourse(currentCourse, false);
                        removeCourseFromTable(currentCourse);
                        JOptionPane.showMessageDialog(AdminPanel.this,
                                courseTitle + " has been rejected.");
                    } else {
                        JOptionPane.showMessageDialog(AdminPanel.this,
                                "Error.", "Data Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        setVisible(true);
    }
    private void loadPendingCourses() {
        tableModel.setRowCount(0);
        ArrayList<course> pendingCourses = ad.getPendingCourses();
        for (course c : pendingCourses) {
            Object[] row = {
                    c.getCourseId(),
                    c.getCourseTitle(),
                    c.getInstructorId(),
                    c.getCourseDescription()
            };
            tableModel.addRow(row);
        }
    }
    private void removeCourseFromTable(course c) {
        if (tableModel == null) return;

        int rowCount = tableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            if (c.getCourseId().equals(tableModel.getValueAt(i, 0))) {
                tableModel.removeRow(i);
                if (pendingCoursesTable != null) {
                    pendingCoursesTable.clearSelection();
                }
                acceptButton.setEnabled(false);
                rejectButton.setEnabled(false);
                return;
            }
        }
    }
}

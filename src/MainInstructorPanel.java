import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainInstructorPanel extends JFrame{
    // Instructor Panel Components
    private JLabel welcome;
    private JTabbedPane tabbedPane1;
    private JPanel CreateCourse;
    private JTabbedPane tabbedPane2;
    private JTabbedPane tabbedPane3;
    private JPanel coursedetails;
    private JPanel lessondetails;
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
    private JButton saveLessonButton;
    private JPanel mainPanel;
    private JButton logoutButton;
    private JComboBox coursecombo;
    private JPanel addQuiz;
    private JComboBox coursebox;
    private JComboBox lessonbox;
    private JButton addLessonRowButton;
    private JScrollPane scrollquestions;
    private JButton addQuestionButton;
    private JButton saveQuizButton;
    private JButton uploadQuizButton;
    private JTable Questiontbale;
    private JComboBox choosecourse;
    private JComboBox choosestudent;
    private JPanel progresspanel;
    private JComboBox courseQcombo;
    private JComboBox lessonQcombo;
    private JPanel quizprogress;
    private JPanel courseperform;
    private JComboBox coursepcombo;
    private JPanel courseprogress;
    private JProgressBar progressBar;
    private JLabel completion;
    private JPanel sprogress;

    private Instructor instructor = null;
    private final InstructorManagement im;
    private final String InstructorName;
    private ArrayList<course> createdCourses= new ArrayList<>();
    private Quiz currentQuiz = null;

    public MainInstructorPanel(Instructor instructor) {
        setTitle("Skill-Forge Instructor Main Panel");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        this.instructor = instructor;
        im = new InstructorManagement(instructor);
        InstructorName= instructor.getUsername();
        this.createdCourses = (instructor.getCreatedCourses() != null) ? instructor.getCreatedCourses() : new ArrayList<>();
        welcome.setText("Welcome " + InstructorName);
        saveLessonButton.setEnabled(false);
        addLessonRowButton.setEnabled(false);
        setUpcoursestable();
        setUpLessonTable();
        setUpQuestionTable();
        updateCourseCombo(coursecombo,"Approved");
        updateCourseCombo(coursebox,null);
        updateCourseCombo(choosecourse,null);
        updateCourseCombo(courseQcombo,null);
        updateCourseCombo(coursepcombo,null);

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
                    JOptionPane.showMessageDialog(null,"Fields cannot be empty");
                }
                else{
                    boolean ok = im.createCourse(courseTitle,courseDescription);
                    if (!ok) {
                        JOptionPane.showMessageDialog(null,"Failed to create course. Check title validity.");
                        return;
                    }
                    JOptionPane.showMessageDialog(null,"Course Created Successfully!");
                    createdCourses = im.getInstructor().getCreatedCourses();
                    updateCourseTable();
                    updateCourseCombo(coursecombo,"Approved");
                    updateCourseCombo(coursebox,null);
                    tabbedPane1.setSelectedIndex(1);
                    tabbedPane2.setSelectedIndex(0);
                    coursetitle.setText("");
                    description.setText("");
                }}
        });

        coursecombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCourse = (String) coursecombo.getSelectedItem();

                boolean courseSelected = (selectedCourse != null && !selectedCourse.isEmpty() && !selectedCourse.equals("Choose A Course"));
                saveLessonButton.setEnabled(courseSelected);
                addLessonRowButton.setEnabled(courseSelected);
                if (courseSelected) {
                    String courseId = getCourseIdFromCombo(selectedCourse);
                    updateLessonTable(courseId);
                } else {
                    ((DefaultTableModel) Lessontable.getModel()).setRowCount(0);
                }
            }
        });

        addLessonRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) Lessontable.getModel();
                model.addRow(new Object[]{"NEW", "Enter Title", "Enter Content", "resource1,resource2", "No", "Edit", "Delete"});
                Lessontable.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
            }
        });

        saveLessonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCourse = (String) coursecombo.getSelectedItem();
                if (selectedCourse == null || selectedCourse.isEmpty() || selectedCourse.equals("Choose A Course")) {
                    JOptionPane.showMessageDialog(null, "Please select a course first.");
                    return;
                }
                String courseId = getCourseIdFromCombo(selectedCourse);

                DefaultTableModel model = (DefaultTableModel) Lessontable.getModel();
                int row = Lessontable.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(null, "Please select a lesson row to save.");
                    return;
                }

                String lessonId = (String) model.getValueAt(row, 0);
                String title = (String) model.getValueAt(row, 1);
                String content = (String) model.getValueAt(row, 2);
                String resourcesString = (String) model.getValueAt(row, 3);
                String quizStatusString = (String) model.getValueAt(row, 4);
                boolean state=false;
                if (quizStatusString.equalsIgnoreCase("Yes")) {
                    state = true;
                }
                ArrayList<String> resources = new ArrayList<>(Arrays.asList(resourcesString.split(",")));
                if (title.equals("Enter Title") || content.equals("Enter Content") || resourcesString.equals("resource1,resource2") ) {
                    JOptionPane.showMessageDialog(null, "Title, Content and resources must be filled with values.");
                    return;
                }

                boolean success = false;
                if (lessonId.equals("NEW")) {
                    success = im.addLesson(courseId, title, content, resources);
                    if (success) JOptionPane.showMessageDialog(null, "Lesson Added Successfully!");
                    else JOptionPane.showMessageDialog(null, "Failed to Add Lesson.");
                } else {
                    success = im.editLesson(courseId, lessonId, title, content, resources,state);
                    if (success) JOptionPane.showMessageDialog(null, "Lesson Edited Successfully!");
                    else JOptionPane.showMessageDialog(null, "Failed to Edit Lesson.");
                }
                if (success) {
                    createdCourses = im.getInstructor().getCreatedCourses();
                    updateLessonTable(courseId);
                    updateCourseTable();
                }
            }
        });

        coursebox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("comboBoxEdited") || e.getActionCommand().equals("comboBoxChanged")) {
                    String selectedCourse = (String) coursebox.getSelectedItem();
                    lessonbox.removeAllItems();
                    lessonbox.addItem("Choose a Lesson");
                    currentQuiz = null;

                    if (selectedCourse != null && !selectedCourse.isEmpty() && !selectedCourse.equals("Choose a Course")) {
                        String courseId = getCourseIdFromCombo(selectedCourse);
                        ArrayList<lesson> lessonsWithoutQuiz = im.getLessonsWithoutQuiz(courseId);
                        for (lesson l : lessonsWithoutQuiz) {
                            lessonbox.addItem(l);
                        }
                    }
                    ((DefaultTableModel) Questiontbale.getModel()).setRowCount(0);
                }
            }
        });

        lessonbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selectedItem = lessonbox.getSelectedItem();
                ((DefaultTableModel) Questiontbale.getModel()).setRowCount(0);
                if (selectedItem instanceof lesson) {
                    lesson selectedLesson = (lesson) selectedItem;
                    String lessonId = selectedLesson.getLessonId();
                    String selectedCourse = (String) coursebox.getSelectedItem();
                    assert selectedCourse != null;
                    String courseId = getCourseIdFromCombo(selectedCourse);
                    lesson targetLesson = courseManagement.getLessonByID(courseId, lessonId);
                    if (targetLesson != null && targetLesson.getQuiz() != null) {
                        currentQuiz = targetLesson.getQuiz();
                    } else {
                        currentQuiz = null;
                    }
                } else {
                    currentQuiz = null;
                }
            }
        });

        addQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuiz == null) {
                    JOptionPane.showMessageDialog(null, "Please select a Lesson first to start a new Quiz.");
                    return;
                }
                DefaultTableModel model = (DefaultTableModel) Questiontbale.getModel();
                model.addRow(new Object[]{"Enter Question Text", "Option A", "Option B", "Option C", "Option D", "Correct Answer"});
                Questiontbale.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
            }
        });

        saveQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuiz == null) {
                    JOptionPane.showMessageDialog(null, "No quiz initialized. Select a Lesson.");
                    return;
                }

                DefaultTableModel model = (DefaultTableModel) Questiontbale.getModel();
                ArrayList<Question> questions = new ArrayList<>();
                boolean valid = true;
                for (int i = 0; i < model.getRowCount(); i++) {
                    String text = (String) model.getValueAt(i, 0);
                    List<String> options = new ArrayList<>();
                    for (int j = 1; j <= 4; j++) {
                        options.add((String) model.getValueAt(i, j));
                    }
                    String correctOptionStr = (String)model.getValueAt(i, 5);
                    if (!correctOptionStr.matches("[A-D]")) {
                        JOptionPane.showMessageDialog(null,
                                "Correct answer must be A, B, C, or D");
                        valid = false;
                        break;
                    }

                    if (text.isEmpty() || options.stream().anyMatch(String::isEmpty)) {
                        JOptionPane.showMessageDialog(null,
                                "Question text or options cannot be empty.");
                        valid = false;
                        break;
                    }

                    char ans = correctOptionStr.charAt(0);
                    questions.add(new Question(text, options, ans));
                }

                if (valid) {
                    currentQuiz.setQuestions(questions);
                    JOptionPane.showMessageDialog(null, questions.size() + " Questions Saved to Temporary Quiz ID: " + currentQuiz.getQuizId());
                }
            }
        });

        uploadQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuiz == null || currentQuiz.getQuestions().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No questions saved to the current quiz. Click 'Save Quiz' first.");
                    return;
                }
                String selectedCourse = (String) coursebox.getSelectedItem();
                Object selectedLessonItem = lessonbox.getSelectedItem();
                if (selectedCourse == null || selectedCourse.equals("Choose a Course") ||
                        selectedLessonItem == null || selectedLessonItem.equals("Choose a Lesson")) {
                    JOptionPane.showMessageDialog(null, "Please select both a Course and a Lesson.");
                    return;
                }
                String courseId = getCourseIdFromCombo(selectedCourse);
                String lessonId;
                if (selectedLessonItem instanceof lesson) {
                    lessonId = ((lesson) selectedLessonItem).getLessonId();
                } else {
                    lessonId = getLessonIdFromCombo(selectedLessonItem.toString());
                }
                boolean success = im.addQuizToLesson(courseId, lessonId, currentQuiz);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Quiz " + currentQuiz.getQuizId() + " successfully uploaded and saved to lesson " + lessonId + "!");
                    coursebox.setSelectedIndex(0);
                    lessonbox.removeAllItems();
                    ((DefaultTableModel) Questiontbale.getModel()).setRowCount(0);
                    currentQuiz = null;
                    createdCourses = im.getInstructor().getCreatedCourses();
                    updateLessonTable(courseId);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to upload quiz.");
                }
            }
        });

        choosecourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCourse = (String) choosecourse.getSelectedItem();
                choosestudent.addItem("Choose A Student");

                if (selectedCourse != null && !selectedCourse.isEmpty() && !selectedCourse.equals("Choose a Course")) {
                    String courseId = getCourseIdFromCombo(selectedCourse);
                    updateStudentCombo(choosestudent, courseId);
                }
            }
        });

        choosestudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedStudent = (String) choosestudent.getSelectedItem();
                String selectedCourse= (String) choosecourse.getSelectedItem();
                if (selectedCourse != null && !selectedCourse.isEmpty() && !selectedCourse.equals("Choose a Course")) {
                    String courseId = getCourseIdFromCombo(selectedCourse);

                if (selectedStudent != null && !selectedStudent.isEmpty() && !selectedStudent.equals("Choose A Student")) {
                   //track progress data hna
                    Student student = null;
                    List<User> users = userService.loadUsers();
                    for(User u :users) {if(u.getUserId().equals(selectedStudent)) {student= (Student)u;}}
                    if(student!=null)
                    {
                        //hena completion bar
                    StudentManage s = new StudentManage(student);
                    float progresscompletion = s.progressTrack(getCourse(courseId));
                    progressBar.setVisible(true);
                    progressBar.setStringPainted(true);
                    progressBar.setValue((int)progresscompletion);

                    //el mford a get quiz scores hna -> ma3aya sm w student w courseId
                        HashMap<String,List<Double>> allquizscores = student.getAllQuizScores();
                        ArrayList<String> quizzes = getCourse(courseId).getAllQuizzes();

                        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                        //add data hna in a loop
                        // bl tre2a de --> dataset.addValue(40.0,"Quiz1","Score");
                        if(quizzes!=null)
                        {
                            for (String quizid : quizzes) {
                                dataset.addValue(allquizscores.get(quizid).get(0),quizid,"Score");
                            }
                        }

                        JFreeChart chart = ChartFactory.createBarChart("Student Perfromance", "Quizzes", "Score", dataset);
                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new java.awt.Dimension(450, 200));
                    progresspanel.setLayout(new BorderLayout());
                    progresspanel.removeAll();
                    progresspanel.add(chartPanel,BorderLayout.CENTER);
                    progresspanel.revalidate();
                    progresspanel.repaint();
                }}}
            }
        });

        courseQcombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCourse = (String) choosecourse.getSelectedItem();
                if (selectedCourse != null && !selectedCourse.isEmpty() && !selectedCourse.equals("Choose a Course")) {
                    String courseId = getCourseIdFromCombo(selectedCourse);
                    updateLessonCombo(lessonQcombo, courseId);
                }
            }
        });

        coursepcombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCourse = (String) choosecourse.getSelectedItem();
                if (selectedCourse != null && !selectedCourse.isEmpty() && !selectedCourse.equals("Choose A Course")) {
                    String courseId = getCourseIdFromCombo(selectedCourse);
                    updateStudentCombo(choosestudent, courseId);
                }
            }
        });


        setVisible(true);
    }

    private void updateCourseTable() {
        DefaultTableModel model = (DefaultTableModel) coursestable.getModel();
        model.setRowCount(0);
        for (course c : createdCourses) {
            model.addRow(new Object[]{
                    c.getCourseId(),
                    c.getCourseTitle(),
                    c.getCourseDescription(),
                    c.getStatus(),
                    c.getLessons().size(),
                    c.getStudentIds().size(),
                    "Edit",
                    "Delete"
            });
        }
        coursestable.setModel(model);
    }

    private void updateCourseCombo(JComboBox comboBox, String filterStatus) {
        comboBox.removeAllItems();
        if (filterStatus == null || filterStatus.isEmpty() || filterStatus.equals("Approved")) {
            comboBox.addItem("Choose A Course");
        }
        ArrayList<course> allCourses = im.getInstructor().getCreatedCourses();
        for (course c : allCourses) {
            String courseStatus = c.getStatus();
            if (courseStatus != null) {
                if (filterStatus == null || courseStatus.equals(filterStatus)) {
                    comboBox.addItem("(" + c.getCourseId() + ") " + c.getCourseTitle());
                }
            }
        }
    }
        private void updateStudentCombo(JComboBox comboBox,String courseId) {
        comboBox.removeAllItems();
        comboBox.addItem("Choose A Student");
        ArrayList<course> allCourses = im.getInstructor().getCreatedCourses();
        for (course c : allCourses) {
            if(c.getCourseId().equals(courseId)){
                ArrayList<String> allStudentsids = c.getStudentIds();
                for (String sid : allStudentsids) {
                    comboBox.addItem(sid);
                }
            }
            }
        }
        private void updateLessonCombo(JComboBox comboBox,String courseId) {
        comboBox.removeAllItems();
        comboBox.addItem("Choose A Lesson");
        ArrayList<course> allCourses = im.getInstructor().getCreatedCourses();
        for (course c : allCourses) {
            if(c.getCourseId().equals(courseId)){
                ArrayList<lesson> alllessons = c.getLessons();
                for (lesson l : alllessons) {
                    comboBox.addItem(l);
                }
            }
        }
    }



    private void setUpcoursestable() {
        String[] columnNames= {"Course ID","Course Title","Course Description","Status","Lessons Count","Number of Enrolled Students","Edit","Delete"};
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
                    if (row >= 0) {
                        String courseId = (String) coursestable.getValueAt(row,0);
                        String courseTitle = (String) coursestable.getValueAt(row,1);
                        String courseStatus = (String) coursestable.getValueAt(row,3);
                        if(column==6){
                            if (courseStatus.equals("Pending")) {
                                JOptionPane.showMessageDialog(null, "Editing is not allowed, pls wait till the course state to change ");
                                updateCourseTable();
                                return;
                            }
                            String editedTitle = (String) coursestable.getValueAt(row,1);
                            String editedDescription = (String) coursestable.getValueAt(row,2);

                            int option = JOptionPane.showConfirmDialog(null,"Do you want to Save Edits for "+courseTitle+" ?","Edit Confirmation",JOptionPane.YES_NO_OPTION);
                            if(option==JOptionPane.YES_OPTION){
                                boolean success = im.editCourse(courseId, editedTitle, editedDescription);
                                if (success) {
                                    createdCourses = im.getInstructor().getCreatedCourses();
                                    updateCourseTable();
                                    JOptionPane.showMessageDialog(null,"Course Edited Successfully!");
                                } else {
                                    JOptionPane.showMessageDialog(null,"Failed to edit course.");
                                }
                            }
                        }
                        if(column==7){
                            int option = JOptionPane.showConfirmDialog(null,"Do you want to delete "+courseTitle+" ?","Deletion Confirmation",JOptionPane.YES_NO_OPTION);
                            if(option==JOptionPane.YES_OPTION){
                                im.deleteCourse(courseId);
                                createdCourses = im.getInstructor().getCreatedCourses();
                                updateCourseTable();
                                updateCourseCombo(coursecombo,"Approved");
                                updateCourseCombo(coursebox,null);
                                JOptionPane.showMessageDialog(null,"Course Deleted Successfully!");
                            }
                        }
                    }
                }
            }
        });
    }

    private void setUpLessonTable() {
        String[] columnNames= {"Lesson ID", "Title", "Content", "Resources (Comma-separated)", "Quiz Added", "Edit", "Delete"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0)
        {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return columnIndex >= 1 && columnIndex <= 3;
            }
        };
        Lessontable.setModel(model);
        Lessontable.getTableHeader().setReorderingAllowed(false);

        Lessontable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = Lessontable.getSelectedRow();
                int column = Lessontable.getSelectedColumn();
                if (row >= 0) {
                    String selectedCourse = (String) coursecombo.getSelectedItem();
                    if (selectedCourse == null || selectedCourse.equals("Choose A Course")) return;

                    String courseId = getCourseIdFromCombo(selectedCourse);
                    String lessonId = (String) Lessontable.getValueAt(row, 0);
                    String lessonTitle = (String) Lessontable.getValueAt(row, 1);

                    if (column == 5) {
                        Lessontable.setRowSelectionInterval(row, row);
                        if (lessonId.equals("NEW")) {
                            JOptionPane.showMessageDialog(null, "Editing NEW Lesson. Modify cells and press 'Save Lesson Changes' below to add it to the course.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Editing existing lesson. Modify cells and press 'Save Lesson Changes' below to update it.");
                        }
                    }

                    if (column == 6) {
                        int option = JOptionPane.showConfirmDialog(null, "Do you want to delete lesson: " + lessonTitle + " ?", "Deletion Confirmation", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            if (im.deleteLesson(courseId, lessonId)) {
                                JOptionPane.showMessageDialog(null, "Lesson Deleted Successfully!");
                                createdCourses = im.getInstructor().getCreatedCourses(); // Refresh local data
                                updateLessonTable(courseId); // Refresh table
                                updateCourseTable(); // Update course table lesson count
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to delete lesson.");
                            }
                        }
                    }
                }
            }
        });
    }

    private void updateLessonTable(String courseId) {
        DefaultTableModel model = (DefaultTableModel) Lessontable.getModel();
        model.setRowCount(0);

        course targetCourse = null;
        for (course c : createdCourses) {
            if (c.getCourseId().equals(courseId)) {
                targetCourse = c;
                break;
            }
        }

        if (targetCourse != null) {
            for (lesson l : targetCourse.getLessons()) {
                String quizStatus = l.getQuizState() ? "Yes" : "No";
                String resourcesString = String.join(",", l.getResources());

                model.addRow(new Object[]{
                        l.getLessonId(),
                        l.getLessonTitle(),
                        l.getContent(),
                        resourcesString,
                        quizStatus,
                        "Edit",
                        "Delete"
                });
            }
        }
        Lessontable.setModel(model);
    }

    private void setUpQuestionTable() {
        String[] columnNames = {"Question Text", "Option A", "Option B", "Option C", "Option D", "Correct Option (A-D)"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        Questiontbale.setModel(model);
        Questiontbale.getTableHeader().setReorderingAllowed(false);
    }

    private String getCourseIdFromCombo(String comboItem) {
        if (comboItem.startsWith("(") && comboItem.contains(")")) {
            return comboItem.substring(1, comboItem.indexOf(')'));
        }
        return comboItem;
    }

    private String getLessonIdFromCombo(String comboItem) {
        if (comboItem.startsWith("(") && comboItem.contains(")")) {
            return comboItem.substring(1, comboItem.indexOf(')'));
        }
        return comboItem;
    }
    private course getCourse(String courseId) {
        ArrayList<course> courses = courseManagement.loadCourses();
        for (course c : courses) {
            if(c.getCourseId().equals(courseId)) {
                return c;
            }
        }
        return null;
    }

}
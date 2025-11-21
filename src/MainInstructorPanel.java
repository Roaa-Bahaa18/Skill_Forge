import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random; // Required for generating new Lesson/Quiz IDs

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
    private JTextField quizIdField;

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
        setVisible(true);
        this.instructor = instructor;
        im = new InstructorManagement(instructor);
        InstructorName= instructor.getUsername();
        createdCourses= instructor.getCreatedCoursesIDS();
        welcome.setText("Welcome " + InstructorName);

        setUpcoursestable();
        setUpLessonTable();
        setUpQuestionTable();
        updateCourseCombo(coursecombo);
        updateCourseCombo(coursebox);

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
                    boolean ok = im.createCourse(courseTitle,courseDescription);
                    if (!ok) {
                        JOptionPane.showMessageDialog(null,"Failed to create course. Check title validity.");
                        return;
                    }
                    JOptionPane.showMessageDialog(null,"Course Created Successfully!");
                    createdCourses = im.getInstructor().getCreatedCoursesIDS();
                    updateCourseTable();
                    updateCourseCombo(coursecombo);
                    updateCourseCombo(coursebox);
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
                if (selectedCourse != null && !selectedCourse.isEmpty() && !selectedCourse.equals("Choose A Course")) {
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
                ArrayList<String> resources = new ArrayList<>(Arrays.asList(resourcesString.split(",")));

                if (title.isEmpty() || content.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Title and Content cannot be empty.");
                    return;
                }

                boolean success = false;
                if (lessonId.equals("NEW")) {
                    success = im.addLesson(courseId, title, content, resources);
                    if (success) JOptionPane.showMessageDialog(null, "Lesson Added Successfully!");
                    else JOptionPane.showMessageDialog(null, "Failed to Add Lesson.");
                } else {
                    success = im.editLesson(courseId, lessonId, title, content, resources);
                    if (success) JOptionPane.showMessageDialog(null, "Lesson Edited Successfully!");
                    else JOptionPane.showMessageDialog(null, "Failed to Edit Lesson.");
                }
                if (success) {
                    createdCourses = im.getInstructor().getCreatedCoursesIDS();
                    updateLessonTable(courseId);
                    updateCourseTable();
                }
            }
        });

        coursebox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCourse = (String) coursebox.getSelectedItem();
                lessonbox.removeAllItems();
                lessonbox.addItem("Choose a Lesson");
                quizIdField.setText("");
                currentQuiz = null;

                if (selectedCourse != null && !selectedCourse.isEmpty() && !selectedCourse.equals("Choose a Course")) {
                    String courseId = getCourseIdFromCombo(selectedCourse);
                    ArrayList<lesson> lessonsWithoutQuiz = im.getLessonsWithoutQuiz(courseId);
                    for (lesson l : lessonsWithoutQuiz) {
                        lessonbox.addItem("(" + l.getLessonId() + ") " + l.getLessonTitle());
                    }
                }
                ((DefaultTableModel) Questiontbale.getModel()).setRowCount(0);
            }
        });

        lessonbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLesson = (String) lessonbox.getSelectedItem();
                currentQuiz = null;
                ((DefaultTableModel) Questiontbale.getModel()).setRowCount(0);

                if (selectedLesson != null && !selectedLesson.isEmpty() && !selectedLesson.equals("Choose a Lesson")) {
                    Random ran = new Random();
                    String quizId = "QZ" + (ran.nextInt(9000) + 1000);
                    quizIdField.setText(quizId);
                    currentQuiz = new Quiz(quizId, new ArrayList<Question>());
                } else {
                    quizIdField.setText("");
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
                model.addRow(new Object[]{"NEW", "Enter Question Text", "Option 1", "Option 2", "Option 3", "Option 4", "1"});
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
                    String text = (String) model.getValueAt(i, 1);
                    List<String> options = new ArrayList<>();
                    for (int j = 2; j <= 5; j++) {
                        options.add((String) model.getValueAt(i, j));
                    }
                    String correctOptionStr = (String) model.getValueAt(i, 6);
                    int correctOption;

                    try {
                        correctOption = Integer.parseInt(correctOptionStr);
                        if (correctOption < 1 || correctOption > 4) throw new NumberFormatException();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid correct option (must be 1-4) in row " + (i + 1));
                        valid = false;
                        break;
                    }

                    if (text.isEmpty() || options.stream().anyMatch(String::isEmpty)) {
                        JOptionPane.showMessageDialog(null, "Question text or options cannot be empty in row " + (i + 1));
                        valid = false;
                        break;
                    }
                    Character ans=new Character('a');
                    switch (correctOption)
                    {
                        case 1: {ans='A'; break;}
                        case 2: {ans='B'; break;}
                        case 3: {ans='C'; break;}
                        case 4: {ans='D'; break;}

                    }
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
                String selectedLesson = (String) lessonbox.getSelectedItem();

                if (selectedCourse == null || selectedCourse.equals("Choose a Course") ||
                        selectedLesson == null || selectedLesson.equals("Choose a Lesson")) {
                    JOptionPane.showMessageDialog(null, "Please select both a Course and a Lesson.");
                    return;
                }

                String courseId = getCourseIdFromCombo(selectedCourse);
                String lessonId = getLessonIdFromCombo(selectedLesson);
                boolean success = im.addQuizToLesson(courseId, lessonId, currentQuiz);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Quiz " + currentQuiz.getQuizId() + " successfully uploaded and saved to lesson " + lessonId + "!");
                    coursebox.setSelectedIndex(0);
                    lessonbox.removeAllItems();
                    lessonbox.addItem("Choose a Lesson");
                    quizIdField.setText("");
                    ((DefaultTableModel) Questiontbale.getModel()).setRowCount(0);
                    currentQuiz = null;
                    createdCourses = im.getInstructor().getCreatedCoursesIDS();
                    updateLessonTable(courseId);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to upload quiz. Check console/logs.");
                }
            }
        });
    }

    private void updateCourseTable() {
        DefaultTableModel model = (DefaultTableModel) coursestable.getModel();
        model.setRowCount(0);
        for (course c : createdCourses) {
            model.addRow(new Object[]{c.getCourseId(),c.getCourseTitle(),c.getCourseDescription(),c.getLessons().size(),c.getStudentIds().size(),"Edit","Delete"});
        }
        coursestable.setModel(model);
    }

    private void updateCourseCombo(JComboBox comboBox) {
        comboBox.removeAllItems();
        if (comboBox == coursecombo) {
            comboBox.addItem("Choose A Course");
        } else if (comboBox == coursebox) {
            comboBox.addItem("Choose a Course");
        }

        for (course c : createdCourses) {
            comboBox.addItem("(" + c.getCourseId() + ") " + c.getCourseTitle());
        }
    }

    private void setUpcoursestable() {
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
                    if (row >= 0) {
                        String courseId = (String) coursestable.getValueAt(row,0);
                        String courseTitle = (String) coursestable.getValueAt(row,1);
                        if(column==5){

                            String editedTitle = (String) coursestable.getValueAt(row,1);
                            String editedDescription = (String) coursestable.getValueAt(row,2);

                            int option = JOptionPane.showConfirmDialog(null,"Do you want to Save Edits for "+courseTitle+" ?","Edit Confirmation",JOptionPane.YES_NO_OPTION);
                            if(option==JOptionPane.YES_OPTION){
                                im.editCourse(courseId, editedTitle, editedDescription, "Pending");
                                createdCourses = im.getInstructor().getCreatedCoursesIDS();
                                updateCourseTable();
                                JOptionPane.showMessageDialog(null,"Course Edited Successfully!");
                            }
                        }
                        if(column==6){
                            int option = JOptionPane.showConfirmDialog(null,"Do you want to delete "+courseTitle+" ?","Deletion Confirmation",JOptionPane.YES_NO_OPTION);
                            if(option==JOptionPane.YES_OPTION){
                                im.deleteCourse(courseId);
                                createdCourses = im.getInstructor().getCreatedCoursesIDS();
                                updateCourseTable();
                                updateCourseCombo(coursecombo);
                                updateCourseCombo(coursebox);
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
                        if (lessonId.equals("NEW")) {
                            JOptionPane.showMessageDialog(null, "Editing enabled. Modify cells and press 'Save Lesson Changes' below.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Editing enabled. Modify cells and press 'Save Lesson Changes' below.");
                        }
                    }

                    if (column == 6) {
                        int option = JOptionPane.showConfirmDialog(null, "Do you want to delete lesson: " + lessonTitle + " ?", "Deletion Confirmation", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            if (im.deleteLesson(courseId, lessonId)) {
                                JOptionPane.showMessageDialog(null, "Lesson Deleted Successfully!");
                                createdCourses = im.getInstructor().getCreatedCoursesIDS(); // Refresh local data
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
                String quizStatus = (l.getQuiz() != null) ? "Yes" : "No";
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
        String[] columnNames = {"ID (NEW)", "Question Text", "Option 1", "Option 2", "Option 3", "Option 4", "Correct Option (1-4)"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex >= 1;
            }
        };
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

}
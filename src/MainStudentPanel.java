import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainStudentPanel extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JButton logoutButton;
    private JTabbedPane tabbedPane;
    private JTable availableCoursesTable;
    private JTable enrolledCoursesTable;
    private JScrollPane availableCoursesScrollPane;
    private JScrollPane enrolledCoursesScrollPane;
    private JTabbedPane tabbedPane1;
    private JComboBox<String> courseCombo;
    private JTable Lessontable;
    private JPanel lessonScrollPane;
    private JTable certificatetable;
    private JScrollPane certificatescrolller;
    private final String StudentName;
    private final StudentManage sm;
    private final List<course> availableCourses;
    private final List<course> enrolledCourses;
    private final List<Certificate> certificates;
    private Student student = null;
    private static final int MAX_QUIZ_ATTEMPTS = 5;
    public MainStudentPanel(Student student) {
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
        certificates=student.getEarnedCertificates();

        welcomeLabel.setText("Welcome, " + StudentName);
        setupAvailableCoursesTab();
        setupEnrolledCoursesTab();
        setupCertificateTable();
        updateCertificateTable();
        setupLessonTab();

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainStudentPanel.this,"Logging out student " + StudentName);
                new FirstPage().setVisible(true);
                dispose();
            }
        });
    }

    private void setupCertificateTable(){
        String[] columnNames = {"Course ID", "Certificate ID", "Issue Date","View PDF","Download PDF","Download JSON"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount()-1 || column == getColumnCount()-2||column == getColumnCount()-3;
            }
        };
        certificatetable.setModel(model);
        certificatetable.getTableHeader().setReorderingAllowed(false);
        updateCertificateTable();
        certificatetable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = certificatetable.getSelectedRow();
                    int column = certificatetable.getSelectedColumn();
                    String courseId = (String) certificatetable.getValueAt(row, 0);
                    String certificateId = (String) certificatetable.getValueAt(row, 1);
                    if (row >= 0) {
                        if(column==model.getColumnCount()-1)
                        {
                            sm.saveCertificateJSON(courseId);
                        }
                        if(column==model.getColumnCount()-2)
                        {
                            sm.createCertificatePDF(courseId,"download");
                        }
                        if(column==model.getColumnCount()-3)
                        {
                            sm.createCertificatePDF(courseId,"view");
                        }
                    }
                }
            }
        });

    }

    private void updateCertificateTable(){
        DefaultTableModel model = (DefaultTableModel) certificatetable.getModel();
        model.setRowCount(0);
        for(Certificate c:certificates){
            model.addRow(new Object[]{c.getCourseID(), c.getCertificateID(), c.getIssueDate(),"View","Download PDF","Download JSON"});
        }
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
                refreshCourseCombo();
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

    private void setupLessonTab() {
        refreshCourseCombo();
        String[] colNames = {"Lesson ID", "Lesson Name", "Quiz", "Quiz Paper", "Mark","Passed"};
        DefaultTableModel lessonTableModel = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        Lessontable.setModel(lessonTableModel);
        Lessontable.getTableHeader().setReorderingAllowed(false);

        courseCombo.addActionListener(e -> {
            String selected = (String) courseCombo.getSelectedItem();
            if (selected == null || selected.equals("Choose a Course")) return;

            String courseId = selected.substring(0, selected.indexOf(" - "));
            course selectedCourse = courseManagement.getCourseByID(courseId);
            displayLessonsForCourse(selectedCourse, lessonTableModel);
        });

        installLessonTableMouseHandler();
    }

    private void displayLessonsForCourse(course selectedCourse, DefaultTableModel model) {
        model.setRowCount(0);
        if (selectedCourse == null) return;

        List<lesson> lessons = selectedCourse.getLessons();
        for (lesson L : lessons) {
            Quiz quiz = L.getQuiz();
            int attemptsCount = student.getQuizAttempts(quiz.getQuizId());
            Double lastScore = student.getStudentLastQuizScore(quiz.getQuizId());
            if(lastScore==null) lastScore=0.0;
            boolean quizExists=false;
            if(quiz!=null) quizExists=true;
            String quizPaper;
            String mark;
            boolean passed=(lastScore>=50.0)?true:false;
            String state=passed?"Yes":"No";
            if (quiz == null) {quizPaper = ""; mark = "";
            } else if (attemptsCount == 0) {quizPaper = "Take Quiz";mark = "";
            } else {quizPaper = "View Paper";mark = (lastScore != null) ? String.format("%.2f%%", lastScore) : "N/A";}

            model.addRow(new Object[]{
                    L.getLessonId(),
                    L.getLessonTitle(),
                    quizExists,
                    quizPaper,
                    mark,
                    state
            });
        }
    }

    private void refreshCourseCombo() {
        if (courseCombo == null) return;
        courseCombo.removeAllItems();
        courseCombo.addItem("Choose a Course");

        for (course c : enrolledCourses) {
            courseCombo.addItem(c.getCourseId() + " - " + c.getCourseTitle());
        }

        courseCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof course) {
                    setText(((course) value).getCourseId() + " - " + ((course) value).getCourseTitle());
                }
                return this;
            }
        });
    }

    private void installLessonTableMouseHandler() {

        Lessontable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = Lessontable.rowAtPoint(e.getPoint());
                int col = Lessontable.columnAtPoint(e.getPoint());
                if (row < 0 || col < 0) return;

                String selected = (String) courseCombo.getSelectedItem();
                if (selected == null || selected.equals("Choose a Course")) return;

                String courseId = selected.substring(0, selected.indexOf(" - "));
                course selectedCourse = courseManagement.getCourseByID(courseId);
                lesson L = selectedCourse.getLessons().get(row);

                if (col == 1) {
                    openLessonContent(L);
                }
                else if (col == 3) {
                    handleQuizPaperClick(L, selectedCourse.getCourseId(), row, (DefaultTableModel) Lessontable.getModel());
                }
            }
        });
    }

    private void showLessonDetail(String courseId) {
        course course = enrolledCourses.stream()
                .filter(c -> c.getCourseId().equals(courseId))
                .findFirst()
                .orElse(null);

        if (course == null) return;

        JPanel mainPanel = new JPanel(new BorderLayout());

        String[] lessonColumnNames = {"Lesson Name", "Lesson ID", "Status"};

        DefaultTableModel lessonModel = new DefaultTableModel(lessonColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells uneditable
            }
        };

        List<lesson> lessons = course.getLessons();
        ArrayList<Boolean> lessonStatus = student.getProgress().get(courseId);
        if (lessonStatus == null) {
            lessonStatus = new ArrayList<>();
            for (int i = 0; i < lessons.size(); i++) {
                lessonStatus.add(false);
            }
            student.getProgress().put(courseId, lessonStatus);
        }
        for (int i = 0; i < lessons.size(); i++) {
            String status = (lessonStatus.get(i)) ? "Completed" : "Uncompleted";
            lessonModel.addRow(new Object[]{lessons.get(i).getLessonTitle(), lessons.get(i).getLessonId(), status});
        }
        JTable lessonTable = new JTable(lessonModel);
        lessonTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane lessonScrollPane = new JScrollPane(lessonTable);
        mainPanel.add(lessonScrollPane, BorderLayout.NORTH);
        JPanel quizPanel = new JPanel(new BorderLayout());

        lessonTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = lessonTable.getSelectedRow();
                int col = lessonTable.getSelectedColumn();
                if (row < 0 || col < 0) return;

                lesson selectedLesson = lessons.get(row);

                if (col == 0) {
                    openLessonContent(selectedLesson);
                }
            }
        });

        JDialog dialog = new JDialog(this, "Lessons for " + course.getCourseTitle(), true);
        dialog.setLayout(new BorderLayout());
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openLessonContent(lesson selectedLesson) {
        if (selectedLesson == null) return;

        String content = selectedLesson.getContent();
        SwingUtilities.invokeLater(() -> {
            JFrame contentFrame = new JFrame("Lesson Content");
            contentFrame.setSize(500, 400);
            contentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTextArea textArea = new JTextArea(content);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            contentFrame.add(scrollPane);
            contentFrame.setLocationRelativeTo(this);
            contentFrame.setVisible(true);
        });
    }

    private void handleQuizPaperClick(lesson selectedLesson, String courseId, int row, DefaultTableModel model) {
        if (selectedLesson == null) return;
        Quiz quiz = selectedLesson.getQuiz();

        if (quiz == null) {
            JOptionPane.showMessageDialog(this, "No quiz for this lesson.");
            return;
        }
        int attemptsCount = student.getQuizAttempts(quiz.getQuizId());
        if (attemptsCount >= MAX_QUIZ_ATTEMPTS) {
            JOptionPane.showMessageDialog(this,
                    "You have exceeded the limit of " + MAX_QUIZ_ATTEMPTS + " attempts for this quiz.",
                    "Limit Reached", JOptionPane.WARNING_MESSAGE);
            if (attemptsCount > 0) {
                showQuizPaper(selectedLesson);
            }
            return;
        }
        if (attemptsCount == 0) {
            takeMCQQuiz(selectedLesson, courseId, row, model);
            return;
        }
        if (attemptsCount > 0 && attemptsCount < MAX_QUIZ_ATTEMPTS) {
            String message = "You have attempted this quiz " + attemptsCount + " time(s).\n\n" +
                    "Do you want to retake the quiz or view your last attempt?";
            Object[] options = {"Retake Quiz", "View Last Paper", "Cancel"};
            int choice = JOptionPane.showOptionDialog(this, message,
                    "Quiz Options",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                takeMCQQuiz(selectedLesson, courseId, row, model);
            } else if (choice == 1) {
                showQuizPaper(selectedLesson);
            }
        }
    }

    private void takeMCQQuiz(lesson selectedLesson, String courseId, int row, DefaultTableModel model) {
        Quiz quiz = selectedLesson.getQuiz();
        List<Question> questions = quiz.getQuestions();
        if (questions == null || questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions in this quiz.");
            return;
        }
        JDialog dialog = new JDialog(this, "Take Quiz - " + selectedLesson.getLessonTitle(), true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());

        JPanel quizPanel = new JPanel();
        quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));
        List<ButtonGroup> buttonGroups = new ArrayList<>();
        for (Question q : questions) {
            JPanel qPanel = new JPanel();
            BoxLayout box =new BoxLayout(qPanel, BoxLayout.Y_AXIS);
            qPanel.setLayout(box);
            qPanel.setBorder(BorderFactory.createTitledBorder(q.getQuestionBody()));
            ButtonGroup bg = new ButtonGroup();
            for (int i = 0; i < q.getChoices().size(); i++) {
                char optionChar = (char) ('A' + i);
                JRadioButton rb = new JRadioButton(optionChar + ": " + q.getChoices().get(i));
                rb.setActionCommand(String.valueOf(optionChar));
                bg.add(rb);
                qPanel.add(rb);
            }
            //We need to add the paper after we submit quiz, we need to adjust size of the question
            buttonGroups.add(bg);
            quizPanel.add(qPanel);
        }

        JScrollPane sp = new JScrollPane(quizPanel);
        dialog.add(sp, BorderLayout.CENTER);
        JButton submit = new JButton("Submit Quiz");
        dialog.add(submit, BorderLayout.SOUTH);
        submit.addActionListener(e -> {
            List<Character> answers = new ArrayList<>();
            for (ButtonGroup bg : buttonGroups) {
                if (bg.getSelection() == null) {
                    JOptionPane.showMessageDialog(dialog, "Please answer all questions.");
                    return;
                }
                answers.add(bg.getSelection().getActionCommand().charAt(0));
            }
            quiz.setUserAnswers(answers);
            boolean passed = sm.takeQuiz(selectedLesson, answers);
            double score = (selectedLesson.getQuiz().getScore() == null ) ? 0.0 : selectedLesson.getQuiz().getScore();
            selectedLesson.setQuizState(true);
            selectedLesson.setQuizState(passed);
            String state=passed?"Yes":"No";
            model.setValueAt(score + "%", row, 4);
            model.setValueAt("View Paper", row, 3);
            model.setValueAt(state,row,5);
            model.fireTableRowsUpdated(row, row);
            updateEnrolledCoursesTable();
            JOptionPane.showMessageDialog(dialog, (passed ? "Quiz Passed!" : "Quiz Failed!") + " Score: " + score + "%");
            dialog.dispose();
            if(sm.progressTrack(getCourse(courseId))==100f)
            {
                Certificate c = sm.getCertificate(courseId);
                JOptionPane.showMessageDialog(dialog, "Course is Completed!\nCertificate Earned check certificate tab!");
                updateCertificateTable();
                tabbedPane.setSelectedIndex(2);
            }
        });
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showQuizPaper(lesson selectedLesson) {
        if (selectedLesson == null) return;

        Quiz quiz = selectedLesson.getQuiz();
        if (quiz == null || quiz.getQuestions() == null || quiz.getUserAnswers() == null) {
            JOptionPane.showMessageDialog(this, "Quiz data is incomplete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Question> questions = quiz.getQuestions();
        List<Character> studentAnswers = quiz.getUserAnswers();
        if (questions.size() != studentAnswers.size()) {
            JOptionPane.showMessageDialog(this, "Mismatch between questions and answers.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            String[] cols = {"Question", "Correct Answer", "Your Answer"};
            DefaultTableModel paperModel = new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                paperModel.addRow(new Object[]{
                        q.getQuestionBody(),
                        q.getAnswer(),
                        studentAnswers.get(i)
                });
            }

            JTable paperTable = new JTable(paperModel);
            paperTable.getTableHeader().setReorderingAllowed(false);
            JScrollPane sp = new JScrollPane(paperTable);

            JLabel scoreLabel = new JLabel("Score: " + quiz.getScore() + "%");
            scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JDialog dialog = new JDialog(this, "Quiz Paper - " + selectedLesson.getLessonTitle(), true);
            dialog.setSize(600, 400);
            dialog.setLayout(new BorderLayout());
            dialog.add(scoreLabel, BorderLayout.NORTH);
            dialog.add(sp, BorderLayout.CENTER);

            JButton close = new JButton("Close");
            close.addActionListener(e -> dialog.dispose());
            dialog.add(close, BorderLayout.SOUTH);

            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });
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

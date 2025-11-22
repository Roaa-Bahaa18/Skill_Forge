import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

public class StudentManage {
    private Student student;
    public StudentManage(Student student) {
        this.student = student;
    }
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public List<course> viewAvailableCourses() {
        List<course> allCourses = courseManagement.loadCourses();
        List<String> enrolledIds = student.getEnrolledCourseIds();
        List<course> availableCourses = allCourses.stream()
                .filter(c -> "Approved".equals(c.getStatus()))
                .filter(c -> !enrolledIds.contains(c.getCourseId()))
                .collect(Collectors.toList());
        return availableCourses;
    }

    public List<course> viewEnrolledCourses() {
        ArrayList<String> enrolledIds = student.getEnrolledCourseIds();
        ArrayList<course> allCourses = courseManagement.loadCourses();
        ArrayList<course> enrolled = new ArrayList<>();
        for (course c : allCourses) {
            if (enrolledIds.contains(c.getCourseId())) {
                enrolled.add(c);
            }
        }
        return enrolled;
    }

    public boolean enrollCourse(course c) {
        String courseId = c.getCourseId();
        String studentId = student.getUserId();
        String instructorId = c.getInstructorId();
        if (student.getEnrolledCourseIds().contains(courseId)) return false;
        student.addCourse(courseId);
        ArrayList<Boolean> lessonStatuses = new ArrayList<>();
        for (int i = 0; i < c.getLessons().size(); i++) {
            lessonStatuses.add(false);
        }
        student.addCourseProgress(courseId, lessonStatuses);
        List<User> users = userService.loadUsers();

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(studentId)) {
                users.set(i, student);
            }
            if(users.get(i).getUserId().equals(instructorId)) {
                InstructorManagement im = new InstructorManagement((Instructor) users.get(i));
                im.addStudentToCourse(studentId,courseId);
                users.set(i,im.getInstructor());
            }
        }
        userService.saveUsers(users);

        ArrayList<course> courses = courseManagement.loadCourses();
        for (course cc : courses) {
            if (cc.getCourseId().equals(courseId)) {
                cc.getStudentIds().add(studentId);
            }
        }
        courseManagement.saveCourses(courses);
        return true;
    }

    public float progressTrack(course c) {
        ArrayList<String> enrolledIds = student.getEnrolledCourseIds();
        float progress=0;
        for (int i = 0; i < enrolledIds.size(); i++) {
            String courseId = enrolledIds.get(i);
            if(courseId.equals(c.getCourseId()))
            {
                ArrayList<Boolean> lessonStatuses = student.getProgress().get(courseId);
                if (lessonStatuses == null || lessonStatuses.isEmpty()) {break;}
                int completed = 0;
                for (Boolean status : lessonStatuses) {if (status) completed++;}
                progress = (completed * 100f) / lessonStatuses.size();
                break;
            }
        }
        return progress;
    }

    public boolean completeLesson(course c, lesson l) {
        if(l.getQuiz() != null && !l.getQuizState()){
            return false;
        }
        String courseId = c.getCourseId();
        ArrayList<Boolean> lessonStatuses = student.getProgress().get(courseId);
        if (lessonStatuses == null) return false;

        int lessonIndex = -1;
        ArrayList<lesson> lessons = c.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getLessonId().equals(l.getLessonId())) {
                lessonIndex = i;
                break;
            }
        }
        if (lessonIndex == -1) return false;

        if (lessonStatuses.get(lessonIndex)) return false;

        lessonStatuses.set(lessonIndex, true);
        student.getProgress().put(courseId, lessonStatuses);
        List<User> users = userService.loadUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(student.getUserId())) {
                users.set(i, student);
            }
        }
        userService.saveUsers(users);

        float progress = progressTrack(c);
        if (progress == 100f) {
            markCourseCompleted(c);
        }
        userService.saveUsers(users);
        return true;
    }

    public void markCourseCompleted(course c) {
        if (student.getCompletedCoursesIDs() == null) {
            student.setCompletedCoursesIDs(new ArrayList<>());
        }

        if (!student.getCompletedCoursesIDs().contains(c.getCourseId())) {
            student.getCompletedCoursesIDs().add(c.getCourseId());

            List<User> users = userService.loadUsers();
            if (users == null) {
                users = new ArrayList<>();
            }

            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserId().equals(student.getUserId())) {
                    users.set(i, student);
                }
            }
            userService.saveUsers(users);
        }

    }

    public boolean takeQuiz(lesson lesson, List<Character> answers){
        Quiz quiz = lesson.getQuiz();
        if(quiz == null) return false;
        String quizID = quiz.getQuizId();
        int attempts = student.getQuizAttempts(quizID);
        if(attempts >= quiz.getMaxAttempts()){
            JOptionPane.showMessageDialog(null, "Max attempts reached for quiz: " + quizID);
            return false;
        }
        quiz.calculateScore(answers);
        double score = quiz.getScore();
        student.recordQuizAttempt(quizID, score);

        List<User> users = userService.loadUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(student.getUserId())) {
                users.set(i, student);
            }
        }
        userService.saveUsers(users);

        if(quiz.isPassed()){
            lesson.setQuizState(true);
            List<course> courses = courseManagement.loadCourses();
            for(course c : courses){
                for(lesson l : c.getLessons()){
                    if(l.getLessonId().equals(lesson.getLessonId())){
                        if (c != null){
                            completeLesson(c, lesson);
                        }
                    }
                }
            }
        }
        return quiz.isPassed();
    }

    public Certificate getCertificate(String courseId) {
        ArrayList<course> courses = courseManagement.loadCourses();
        ArrayList<Certificate> certificates = student.getEarnedCertificates();
        if(certificates != null){
        for(Certificate c : certificates){
            if(c.getCourseID().equals(courseId)){
                return c;
            }
        }}
        course Course = null;
        for (course c : courses) {
            if(c.getCourseId().equals(courseId))
            {
                Course = c;
                break;
            }
        }
        float progress = progressTrack(Course);
        if(progress == 100f)
        {
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = date.format(formatter);
            Certificate certificate = new Certificate(student.getUserId(), courseId, formattedDate);
            //add the new certificate to student arraylist of certificates
            student.addEarnedCertificate(certificate);
            //Update student in users
            List<User> users = userService.loadUsers();
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserId().equals(student.getUserId())) {
                    users.set(i, student);
                }
            }
            userService.saveUsers(users);

            //add to data base
            return certificate;
        }
        else return null;
    }

    public void createCertificatePDF(String courseId,String mode)
    {
        ArrayList<course> courses = courseManagement.loadCourses();
        course Course = null;
        for (course c : courses) {
            if(c.getCourseId().equals(courseId))
            {
                Course = c;
                break;
            }
        }
        ArrayList<Certificate> certificates = this.student.getEarnedCertificates();
        Certificate certificate = null;
        for(Certificate c : certificates)
        {
            if(c.getCourseID().equals(courseId))
            {
                certificate = c;
            }
        }
        if(Course == null || certificate == null) return;

        try(PDDocument document = new PDDocument()) {

            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(),PDRectangle.A4.getWidth()));
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document,page)){
                float width = page.getMediaBox().getWidth();
                float currentY = (page.getMediaBox().getHeight()/2)+100;

                content.setNonStrokingColor(210f/255f, 199f/255f, 184f/255f);
                content.addRect(0, 0, width, page.getMediaBox().getHeight());
                content.fill();

                content.setStrokingColor(0.133f,0.192f,0.282f);
                content.setLineWidth(2);
                content.addRect(20,20,width-40,page.getMediaBox().getHeight()-40);
                content.stroke();

                content.beginText();
                content.setNonStrokingColor(0.133f,0.192f,0.282f);
                content.setFont(new PDType1Font(FontName.TIMES_BOLD_ITALIC),40);
                String text = "CERTIFICATE OF COMPLETION";
                float w =new PDType1Font(FontName.TIMES_BOLD_ITALIC).getStringWidth(text)/1000*40;
                content.newLineAtOffset((width-w)/2, currentY);
                currentY-=80;
                content.showText(text);
                content.endText();

                content.beginText();
                content.setFont(new PDType1Font(FontName.TIMES_BOLD), 30);
                 text = "Proudly Presented to: "+student.getUsername();
                 w =new PDType1Font(FontName.TIMES_BOLD).getStringWidth(text)/1000*30;
                content.newLineAtOffset((width-w)/2, currentY);
                currentY -= 40;
                content.showText(text);
                content.endText();

                content.beginText();
                content.setFont(new PDType1Font(FontName.TIMES_BOLD), 20);
                 text = "For outstanding completion of "+Course.getCourseTitle()+" course.";
                 w =new PDType1Font(FontName.TIMES_BOLD).getStringWidth(text)/1000*20;
                content.newLineAtOffset((width-w)/2, currentY);
                currentY -= 60;
                content.showText(text);
                content.endText();

                content.beginText();
                content.setNonStrokingColor(47f/255f, 72f/255f, 109f/255f);
                content.setFont(new PDType1Font(FontName.TIMES_BOLD), 14);
                text = "Issue Date: " + certificate.getIssueDate();
                 w = new PDType1Font(FontName.TIMES_BOLD).getStringWidth(text)/1000*14;
                content.newLineAtOffset((width-w)/2, currentY);
                content.showText(text);
                content.endText();

                currentY-=20;
                content.beginText();
                content.setFont(new PDType1Font(FontName.TIMES_BOLD), 14);
                text = "Certificate Id: "+certificate.getCertificateID();
                w = new PDType1Font(FontName.TIMES_BOLD).getStringWidth(text)/1000*14;
                content.newLineAtOffset((width-w)/2, currentY);
                content.showText(text);
                content.endText();
                currentY-=20;

                PDImageXObject square = PDImageXObject.createFromFile("Square.png", document);
                float swidth = 100f;
                float sheight = 100f;
                content.drawImage(square,width-swidth-130,130,swidth,sheight);

            }

            if(mode.equalsIgnoreCase("Download")) {
                document.save("Certificate#" + certificate.getCertificateID() + ".pdf");
            }
            else if(mode.equalsIgnoreCase("View")) {
                //-> logic will be: create a file"temp" -> save the certificate -> view it -> delete the file
                File viewfile =File.createTempFile("ViewCertificate#" ,".pdf");
                viewfile.deleteOnExit();
                document.save(viewfile);
                //now view it
                if(Desktop.isDesktopSupported()&&Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    Desktop.getDesktop().open(viewfile);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCertificateJSON(String courseID) {
        ArrayList<Certificate> certificates = student.getEarnedCertificates();
        Certificate targetcertificate = null;
        if(certificates==null) return;
        for(Certificate c:certificates) {
            if(c.getCourseID().equals(courseID)) {
                targetcertificate = c;
            }
        }
        if(targetcertificate==null) return;
        JsonObject root = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(gson.toJsonTree(targetcertificate,targetcertificate.getClass()));
        root.add("Certificate", jsonArray);
        try(FileWriter fw = new FileWriter("Certificate#"+targetcertificate.getCertificateID()+".json")) {
            gson.toJson(jsonArray,fw);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FirstPage extends JFrame {
    private JPanel firstpage;
    private JTextField emailfield;
    private JButton loginButton;
    private JButton signUpButton;
    private JPasswordField passwordField;

    public FirstPage() {
        setTitle("First Page");
        setSize(450, 400);
        setContentPane(firstpage);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {checkLogin();}
        });


        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new Signup().setVisible(true);
              dispose();
            }
        });
    }

    private void checkLogin()
    {
        String email= emailfield.getText();
        String password = new String(passwordField.getPassword());

        if(Validations.isEmpty(email)||Validations.isEmpty(password))
        {
            JOptionPane.showMessageDialog(this, "Fields Can't be Empty");
        }
        else
        {
            User user= null;
            try
            {
                user= userService.confirmLogin(email,password);
                if(user!=null)
                {
                    JOptionPane.showMessageDialog(this, "Logged in Successfully");
                    if(user.getRole().equals("Student"))
                    {
                        Student s= (Student)user;
                        new MainStudentPanel(s);
                        dispose();
                    }
                    else
                    {
                        Instructor s= (Instructor) user;
                        new MainInstructorPanel(s).setVisible(true);
                        dispose();

                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "Invalid Name or Password");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}

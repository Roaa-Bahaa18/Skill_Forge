import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Login extends JFrame {
    private JPanel Login;
    private JTextField nametext;
    private JPasswordField passwordField;
    private JButton loginbutton;
    private JLabel pass;
    private JLabel name;
    private JButton back;

    public Login()
    {
        setTitle("Log In");
        setSize(400, 400);
        setVisible(true);
        setContentPane(Login);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        loginbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { checkLogin();}
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new firstPage().setVisible(true);
                dispose();
            }
        });
    }
    private void checkLogin()
    {
        String email= nametext.getText();
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
                        new StudentPanel(s);
                        dispose();
                    }
                    else
                    {
                         Instructor s= (Instructor) user;
                         new InstructorPanel(s);
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


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Signup extends JFrame{
    private JPanel Signup;
    private JTextField namefield;
    private JLabel name;
    private JLabel email;
    private JLabel password;
    private JPasswordField passwordField1;
    private JTextField emailfield;
    private JLabel confirmpassword;
    private JPasswordField passwordField2;
    private JComboBox rolebox;
    private JButton signup;
    private JLabel id;
    private JLabel role;
    private JTextField idfield;
    private JButton back;

    public Signup()
    {
        setTitle("Sign Up");
        setSize(400, 400);
        setVisible(true);
        setContentPane(Signup);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { checkSignUp();}
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new firstPage().setVisible(true);
                dispose();
            }
        });
    }
    private void checkSignUp()
    {
        String name = namefield.getText();
        String password = new String(passwordField1.getPassword());
        String confirmedPassword = new String(passwordField2.getPassword());
        String email = emailfield.getText();
        String id=idfield.getText();
        String role = (String)rolebox.getSelectedItem();
        if(!Validations.isValidName(name))
        {
            JOptionPane.showMessageDialog(this, "Kindly add Valid Name");
        }
        else if(!Validations.isValidEmail(email))
        {
            JOptionPane.showMessageDialog(this, "Kindly add Valid email");
        }
        else if(!Validations.isValidID(id))
        {
            JOptionPane.showMessageDialog(this, "Kindly add Valid ID");
        }
        else if("Select Role".equals(role))
        {
            JOptionPane.showMessageDialog(this, "Kindly select your role");
        }
        else if(!password.equals(confirmedPassword))
        {
            JOptionPane.showMessageDialog(this, "Kindly check password fields, they must be identical");
        }
        else
        {
            User user;
            if("Student".equals(role))
            {
                ArrayList<String> enrolledCourses= new ArrayList<>();
                HashMap<String, ArrayList<Boolean>> progress= new HashMap<>();
                user= new Student(name,password,id,email,enrolledCourses,progress);
            }
            else
            {
                ArrayList<course> createdCourses = new ArrayList<>();
                user= new Instructor(id,name,email,password,createdCourses);
            }

            try{
                int x= userService.confirmSignup(user);
                switch(x)
                {
                    case 0: {
                        JOptionPane.showMessageDialog(this, "Account created successfully!");
                        new Login().setVisible(true);
                        dispose();
                        break;
                    }
                    case 1:{
                        JOptionPane.showMessageDialog(this, "This email already exists, kindly use another one");
                        break;
                    }
                    case 2:{
                        JOptionPane.showMessageDialog(this, "This ID already exists, kindly use another one ");
                        break;
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}


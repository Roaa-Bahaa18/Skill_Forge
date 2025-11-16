import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class firstPage extends JFrame{
    private JPanel firstPage;
    private JButton signupbutton;
    private JButton loginbutton;
    private JLabel welcome;
    private JLabel dont;
    private JLabel already;

    public firstPage()
    {
        setTitle("Skill Forge");
        setSize(400, 200);
        setVisible(true);
        setContentPane(firstPage);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        signupbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Signup().setVisible(true);
                dispose();
            }
        });


        loginbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login().setVisible(true);
                dispose();
            }
        });
    }
    public static void main(String[] args) {firstPage firstPage1=new firstPage();}
}


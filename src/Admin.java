import java.util.ArrayList;

public class Admin extends User{
    public Admin(String username, String password, String userId, String email){
        super(userId, "Admin", username, email ,password);
    }



}

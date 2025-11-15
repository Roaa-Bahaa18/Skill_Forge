public class User {
    protected String userId;
    protected String role;
    protected String username;
    protected String email;
    protected String passwordHash;
    public User() {
    }
    public String getUserId(){return userId;}
    public String getUsername(){return username;}
    public String getUserEmail(){return email;}
    public String getUserPassword(){return passwordHash;}
    public String getRole(){return role;}


    public void setUserPassword(String passwordHash){this.passwordHash=passwordHash;}
    public void setUserId(String id){this.userId=id;}
    public void setUserName(String name){this.username=name;}
    public void setUserRole(String role){this.role=role;}
    public void setUserEmail(String email){this.email=email;}

}

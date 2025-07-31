package bean;
 
public class Users {
    private String userid;
    private String password;
    private String role;
 
    public Users(String userid, String password, String role) {
        this.userid = userid;
        this.password = password;
        this.role = role;
    }
    public String getUserid() { return userid; }
    public void setUserid(String userid) { this.userid = userid; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
 
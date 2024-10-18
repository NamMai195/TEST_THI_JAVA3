package poly.entity;

import java.util.Date;

public class USERS {
    private String Id;
    private String Password;
    private String Fullname;
    private String Email;
    private Boolean Admin;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getFullname() {
		return Fullname;
	}
	public void setFullname(String fullname) {
		Fullname = fullname;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public Boolean getAdmin() {
		return Admin;
	}
	public void setAdmin(Boolean admin) {
		Admin = admin;
	}
	public USERS(String id, String password, String fullname, String email, Boolean admin) {
		
		Id = id;
		Password = password;
		Fullname = fullname;
		Email = email;
		Admin = admin;
	}
	public USERS() {
		
	}
   
}

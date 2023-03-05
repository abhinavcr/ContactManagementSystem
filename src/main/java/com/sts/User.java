package com.sts;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.persistence.FetchType;
import org.hibernate.annotations.Cascade;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.MappedSuperclass;

@Entity
@Table(name="USER")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@NotBlank(message="Name field is required!!")
	@Size(min=2,max=20,message="atleast 2 charaters are writen")
	private String name;
	@NotBlank(message="Term And Condition must be agreed")
	private boolean enabled;
	@Column(unique=true)
	private String email;
	private String image;
	@NotBlank(message="Password field is required")
	@Size(max=8,message="8 character are required")
	private String password;
	private String role;
	@Column(length=500)
	private String about;
	
	//if we want to drop the user_contact table in database smartcontact then use mappedBy.
	@OneToMany(cascade=CascadeType.ALL,mappedBy="user",orphanRemoval=true)
	private List<Contact> contact=new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPassword() {
		return "password";
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public List<Contact> getContact() {
		return contact;
	}

	public void setContact(List<Contact> i) {
		this.contact = i;
	}

	public User(int id, String name, boolean enabled, String email, String image, String password, String role,
			String about, List<Contact> contact) {
		super();
		this.id = id;
		this.name = name;
		this.enabled = enabled;
		this.email = email;
		this.image = image;
		this.password = password;
		this.role = role;
		this.about = about;
		this.contact = contact;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", enabled=" + enabled + ", email=" + email + ", image=" + image
				+ ", password=" + password + ", role=" + role + ", about=" + about + ", contact=" + contact + "]";
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
}

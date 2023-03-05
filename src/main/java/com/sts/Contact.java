package com.sts;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int C_id;
	private String name;
	private String nickname;
	private String work;
	private String image;
	@Column(unique=true)
	private String email;
	private int phonenumber;
	@Column(length=500)
	private String description;
	
	@ManyToOne
	@JsonIgnore
	private User use;

	public int getC_id() {
		return C_id;
	}

	public void setC_id(int c_id) {
		C_id = c_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(int phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUse() {
		return use;
	}

	public void setUse(User use) {
		this.use = use;
	}
	
	public Contact(int c_id, String name, String nickname, String work, String image, String email, int phonenumber,
			String description, User use) {
		super();
		C_id = c_id;
		this.name = name;
		this.nickname = nickname;
		this.work = work;
		this.image = image;
		this.email = email;
		this.phonenumber = phonenumber;
		this.description = description;
		this.use = use;
	}

	@Override
	public String toString() {
		return "Contact [C_id=" + C_id + ", name=" + name + ", nickname=" + nickname + ", work=" + work + ", image="
				+ image + ", email=" + email + ", phonenumber=" + phonenumber + ", description=" + description
				+ ", use=" + use + "]";
	}

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public boolean equals(Object obj)
	{
		return this.C_id==((Contact)obj).getC_id();
	}
}

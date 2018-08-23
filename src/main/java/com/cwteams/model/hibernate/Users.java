package com.cwteams.model.hibernate;
// Generated 22/10/2017 06:54:37 PM by Hibernate Tools 5.2.3.Final

/**
 * Users generated by hbm2java
 */
public class Users implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idUser;
	private String user;
	private String pass;
	private String name;
	private String surname;
	private String email;
	private String phone;
	private UsersType userType;	
	private Boolean first=true;

	public Users() {
	}

	public Users(String user, String pass, UsersType userType) {
		this.user = user;
		this.pass = pass;
		this.userType = userType;
	}

	public Users(String user, String pass, String name, String surname, String email, String phone, UsersType userType,
			Boolean first) {
		this.user = user;
		this.pass = pass;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone = phone;
		this.userType = userType;
		this.first = first;
	}

	public Integer getIdUser() {
		return this.idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return this.pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public UsersType getUserType() {
		return this.userType;
	}

	public void setUserType(UsersType userType) {
		this.userType = userType;
	}

	public Boolean getFirst() {
		return this.first;
	}

	public void setFirst(Boolean first) {
		this.first = first;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idUser;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Users other = (Users) obj;
		if (idUser != other.idUser)
			return false;
		return true;
	}

}

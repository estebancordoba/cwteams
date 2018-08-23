package com.cwteams.model.dao;

import java.util.List;

import com.cwteams.model.hibernate.Users;
import com.cwteams.model.hibernate.UsersType;

public interface UsersDAO {

	// Usuario

	public List<Users> getUsers();
		
	public void saveUser(Users user);
	
	public void updateUser(Users user);
	
	public void removeUser(Users user);
	
	public Users login(Users user);
	
	public Users getUserXId(int id);
	
	public Users getUserXUser(String user);
	
	public Users firstUser();
	
	// Rol
	
	public List<UsersType> getUserTypes();
	
	public UsersType getUserTypeXId(int id);

}

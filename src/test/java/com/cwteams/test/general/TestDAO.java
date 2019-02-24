package com.cwteams.test.general;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cwteams.model.dao.UsersDAO;
import com.cwteams.model.hibernate.Users;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/test-context.xml" })
public class TestDAO {

	@Resource
	private UsersDAO userDAO;

	List<Users> users;

	private static final int USERSBD = 2;

	@Before
	public void setUp() {		
	}

	@Test
	public void testBBDD() {				
		users = userDAO.getUsers();		
		Assert.assertTrue("Comprobar que en la base de datos hay 2 users",
				users.size() == USERSBD);
	}
}
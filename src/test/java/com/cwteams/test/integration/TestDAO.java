package com.cwteams.test.integration;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.cwteams.model.dao.CollaborativeDAO;
import com.cwteams.model.dao.RatingsDAO;
import com.cwteams.model.dao.UsersDAO;
import com.cwteams.model.hibernate.CollaborativeGa;
import com.cwteams.model.hibernate.GroupsGa;
import com.cwteams.model.hibernate.Ratings;
import com.cwteams.model.hibernate.Users;
import com.cwteams.model.hibernate.UsersType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/test-context.xml" })
public class TestDAO {

	@Autowired
	private UsersDAO userDAO;
	@Autowired
	private RatingsDAO ratingsDAO;
	@Autowired
	private CollaborativeDAO collaborativeDAO;

	@Before
	public void setUp() {
	}

	@Test
	@Transactional
    @Rollback(true)
	public void testUsersDAO() {
		List<Users> users;
		
		users = userDAO.getUsers();		
		Assert.assertNotNull(users);
		
		Users user= userDAO.firstUser();
		Assert.assertNotNull(user);
		
		if(user != null){		
			Users user_aux= userDAO.getUserXUser(user.getUser());
			Assert.assertEquals(user, user_aux);
			
			user_aux= userDAO.getUserXId(user.getIdUser());
			Assert.assertEquals(user, user_aux);
			
			user_aux= userDAO.getUserXId(user.getIdUser());
			Assert.assertEquals(user, user_aux);
		}	
		
		List<UsersType> user_type = userDAO.getUserTypes();
		Assert.assertNotNull(user_type);
	}
	
	@Test
	@Transactional
    @Rollback(true)
	public void testRatingsDAO() {
		List<Ratings> ratings;
		
		ratings = ratingsDAO.getRatings();		
		Assert.assertNotNull(ratings);
	}
	
	@Test
	@Transactional
    @Rollback(true)
	public void testCollaborativeDAO() {
		List<CollaborativeGa> collaborative_ga;		
		
		collaborative_ga = collaborativeDAO.getCollaborativeGa(false);		
		Assert.assertNotNull(collaborative_ga);

		CollaborativeGa c_ga= collaborativeDAO.firstCollaborative();
		Assert.assertNotNull(c_ga);
		
		if(c_ga != null){		
			CollaborativeGa c_ga_aux= collaborativeDAO.searchCollaborativeGaxId(c_ga.getIdCollaborativeGa());			
			Assert.assertEquals(c_ga.getIdCollaborativeGa(), c_ga_aux.getIdCollaborativeGa());

			List<GroupsGa> groups_ga;
			groups_ga = collaborativeDAO.getGroupsGaxIdCollaborativeGa(c_ga.getIdCollaborativeGa());
			Assert.assertNotNull(groups_ga);
		}	
	}
}
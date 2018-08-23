package com.cwteams.model.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.cwteams.model.hibernate.Users;
import com.cwteams.model.hibernate.UsersType;

public class UsersDAOImpl implements UsersDAO, Serializable {

	private static final long serialVersionUID = 1L;
	private SessionFactory sessionFactory;

	// Usuario

	@SuppressWarnings("unchecked")
	@Override
	public List<Users> getUsers() {

		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (List<Users>) session.createQuery("FROM Users").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}

	}
	
	@Override
	public void saveUser(Users user) {

		Session session = null;
		Transaction tx = null;

		try {

			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.persist(user);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}

	}

	@Override
	public void updateUser(Users user) {

		Session session = null;
		Transaction tx = null;

		try {

			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.update(user);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}

	}

	@Override
	public void removeUser(Users user) {

		Session session = null;
		Transaction tx = null;

		try {

			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			Users u = session.load(Users.class, user.getIdUser());
			session.delete(u);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}

	}

	@Override
	public Users login(Users user) {
		Session session = null;
				
		try {
			session = sessionFactory.openSession();
			/*return (Users) session.createQuery("FROM Users WHERE user = :user AND pass = :pass")
					.setParameter("user", user.getUser()).setParameter("pass", user.getPass())
					.getSingleResult();*/
			return (Users) session.createQuery("FROM Users WHERE user = :user AND pass = :pass")
					.setParameter("user", user.getUser()).setParameter("pass", DigestUtils.md5Hex(user.getPass()))
					.getSingleResult();			
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}

	// Rol

	@SuppressWarnings("unchecked")
	@Override
	public List<UsersType> getUserTypes() {

		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (List<UsersType>) session.createQuery("FROM UsersType").getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}

	public UsersType getUserTypeXId(int id) {
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (UsersType) session.createQuery("FROM UsersType WHERE id_user_type = :id").setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}
	
	@Override
	public Users getUserXId(int id) {
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (Users) session.createQuery("FROM Users WHERE id_user = :id").setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}
	
	@Override
	public Users getUserXUser(String user) {
		Session session = null;
				
		try {
			session = sessionFactory.openSession();	
			return (Users) session.createQuery("FROM Users WHERE user = :user")
					.setParameter("user", user)
					.getSingleResult();			
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}
	
	@Override
	public Users firstUser() {
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (Users) session.createQuery("FROM Users").setMaxResults(1).getSingleResult();			
		} catch (Exception e) {			
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}

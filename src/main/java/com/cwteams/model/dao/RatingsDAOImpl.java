package com.cwteams.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.cwteams.model.hibernate.Ratings;

public class RatingsDAOImpl implements RatingsDAO,Serializable{

	private static final long serialVersionUID = 1L;
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Ratings> getRatings() {
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (List<Ratings>) session.createQuery("FROM Ratings").getResultList();
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
	public void saveRating(Ratings raiting) {
		Session session = null;
		Transaction tx = null;

		try {

			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.persist(raiting);
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
	public void updateRating(Ratings raiting) {

		Session session = null;
		Transaction tx = null;

		try {

			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.update(raiting);
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
	public void removeRating(Ratings raiting) {
		Session session = null;
		Transaction tx = null;

		try {

			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			Ratings r = session.load(Ratings.class, raiting.getIdRating());
			session.delete(r);
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
	public Ratings getRatingXId(Long id) {
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (Ratings) session.createQuery("FROM Ratings WHERE id_rating = :id").setParameter("id", id).getSingleResult();
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Ratings> getRatingNoView() {
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (List<Ratings>) session.createQuery("FROM Ratings WHERE view=0").getResultList();
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
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}

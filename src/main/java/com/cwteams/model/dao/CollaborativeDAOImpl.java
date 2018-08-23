package com.cwteams.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.cwteams.model.hibernate.CharacteristicsGa;
import com.cwteams.model.hibernate.CollaborativeGa;
import com.cwteams.model.hibernate.GroupsGa;
import com.cwteams.model.hibernate.MembersGa;

public class CollaborativeDAOImpl  implements CollaborativeDAO, Serializable {

	private static final long serialVersionUID = 1L;
	private SessionFactory sessionFactory;
	
	@Override
	public void saveCollaborativeGa(CollaborativeGa collaborative_ga) {
		Session session = null;
		Transaction tx = null;

		try {

			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.persist(collaborative_ga);
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
	public void removeCollaborativeGa(CollaborativeGa collaborative_ga) {
		Session session = null;
		Transaction tx = null;

		try {

			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			CollaborativeGa rmv = session.load(CollaborativeGa.class, collaborative_ga.getIdCollaborativeGa());
			session.delete(rmv);
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
	public CollaborativeGa searchCollaborativeGaxId(Integer id_collaborative){
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (CollaborativeGa) session.createQuery("FROM CollaborativeGa WHERE id_collaborative_ga = :id_collaborative_ga")
					.setParameter("id_collaborative_ga", id_collaborative)					
					.getSingleResult();
		} catch (Exception e) {			
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}
	
	@Override
	public void updateCollaborativeGa(CollaborativeGa collaborative_ga) {
		Session session = null;
		Transaction tx = null;

		try {

			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.update(collaborative_ga);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CollaborativeGa> getCollaborativeGa(boolean libre) {
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (List<CollaborativeGa>) session.createQuery("FROM CollaborativeGa"+((libre)?" WHERE free=1":""))						
						.getResultList();			
			
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
	public List<CollaborativeGa> getCollaborativeGaxOwner(Integer id_user){
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (List<CollaborativeGa>) session.createQuery("FROM CollaborativeGa WHERE user_owner=:user_owner")
					.setParameter("user_owner", id_user)
					.getResultList();			
			
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
	public CollaborativeGa firstCollaborative() {
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (CollaborativeGa) session.createQuery("FROM CollaborativeGa").setMaxResults(1).getSingleResult();			
		} catch (Exception e) {			
			return null;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}
	
	@Override
	public void saveGroupGa(GroupsGa groupsGa) {
		Session session = null;
		Transaction tx = null;

		try {

			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.persist(groupsGa);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<GroupsGa> getGroupsGaxIdCollaborativeGa(Integer id_collaborative_ga) {
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (List<GroupsGa>) session.createQuery("FROM GroupsGa WHERE id_collaborative_ga=:id_collaborative_ga")
					.setParameter("id_collaborative_ga", id_collaborative_ga)
					.getResultList();
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
	public void saveMemberGa(MembersGa memberGa) {
		Session session = null;
		Transaction tx = null;

		try {

			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.persist(memberGa);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<MembersGa> getMembersGaxIdGroupGa(Integer id_group_ga) {
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (List<MembersGa>) session.createQuery("FROM MembersGa WHERE id_group_ga=:id_group_ga")
					.setParameter("id_group_ga", id_group_ga)
					.getResultList();
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
	public void saveCharacteristicGa(CharacteristicsGa characteristicGa) {
		Session session = null;
		Transaction tx = null;

		try {

			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.persist(characteristicGa);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<CharacteristicsGa> getCharacteristicsGaxIdMemberGa(Integer id_members_ga) {
		Session session = null;

		try {
			session = sessionFactory.openSession();
			return (List<CharacteristicsGa>) session.createQuery("FROM CharacteristicsGa WHERE id_members_ga=:id_members_ga")
					.setParameter("id_members_ga", id_members_ga)
					.getResultList();
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
	
	//Otros
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}

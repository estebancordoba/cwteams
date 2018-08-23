package com.cwteams.service;

import java.io.Serializable;
import java.util.List;

import com.cwteams.model.dao.CollaborativeDAO;
import com.cwteams.model.hibernate.CharacteristicsGa;
import com.cwteams.model.hibernate.CollaborativeGa;
import com.cwteams.model.hibernate.GroupsGa;
import com.cwteams.model.hibernate.MembersGa;

public class CollaborativeServiceImpl implements CollaborativeService, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private CollaborativeDAO collaborativeDAO;
	
	public CollaborativeDAO getCollaborativeDAO() {
		return collaborativeDAO;
	}

	public void setCollaborativeDAO(CollaborativeDAO collaborativeDAO) {
		this.collaborativeDAO = collaborativeDAO;
	}

	@Override
	public void saveCollaborativeGa(CollaborativeGa collaborative_ga) {
		collaborativeDAO.saveCollaborativeGa(collaborative_ga);
	}

	@Override
	public void removeCollaborativeGa(CollaborativeGa collaborative_ga) {
		collaborativeDAO.removeCollaborativeGa(collaborative_ga);
	}

	@Override
	public CollaborativeGa searchCollaborativeGaxId(Integer id_collaborative) {
		return collaborativeDAO.searchCollaborativeGaxId(id_collaborative);
	}
	
	public void updateCollaborativeGa(CollaborativeGa collaborative_ga) {
		collaborativeDAO.updateCollaborativeGa(collaborative_ga);
	}
	
	@Override
	public List<CollaborativeGa> getCollaborativeGa(boolean libre) {
		return collaborativeDAO.getCollaborativeGa(libre);
	}
	
	@Override
	public List<CollaborativeGa> getCollaborativeGaxOwner(Integer id_user){
		return collaborativeDAO.getCollaborativeGaxOwner(id_user);
	}
	
	@Override
	public CollaborativeGa firstCollaborative(){		
		return collaborativeDAO.firstCollaborative();
	}
	
	@Override
	public void saveGroupGa(GroupsGa groupsGa) {
		collaborativeDAO.saveGroupGa(groupsGa);
	}

	@Override
	public List<GroupsGa> getGroupsGaxIdCollaborativeGa(Integer id_collaborative_ga) {
		return collaborativeDAO.getGroupsGaxIdCollaborativeGa(id_collaborative_ga);
	}

	@Override
	public void saveMemberGa(MembersGa memberGa) {
		collaborativeDAO.saveMemberGa(memberGa);
	}

	@Override
	public List<MembersGa> getMembersGaxIdGroupGa(Integer id_group_ga) {
		return collaborativeDAO.getMembersGaxIdGroupGa(id_group_ga);
	}

	@Override
	public void saveCharacteristicGa(CharacteristicsGa characteristicGa) {
		collaborativeDAO.saveCharacteristicGa(characteristicGa);
	}

	@Override
	public List<CharacteristicsGa> getCharacteristicsGaxIdMemberGa(Integer id_member_ga) {
		return collaborativeDAO.getCharacteristicsGaxIdMemberGa(id_member_ga);
	}

}

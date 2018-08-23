package com.cwteams.model.dao;

import java.util.List;

import com.cwteams.model.hibernate.CharacteristicsGa;
import com.cwteams.model.hibernate.CollaborativeGa;
import com.cwteams.model.hibernate.GroupsGa;
import com.cwteams.model.hibernate.MembersGa;

public interface CollaborativeDAO {
	//CollaborativeGA
	public void saveCollaborativeGa(CollaborativeGa collaborative_ga);
	
	public void removeCollaborativeGa(CollaborativeGa collaborative_ga);
	
	public CollaborativeGa searchCollaborativeGaxId(Integer id_collaborative);
	
	public void updateCollaborativeGa(CollaborativeGa collaborative_ga);
		
	public List<CollaborativeGa> getCollaborativeGa(boolean libre);
	
	public List<CollaborativeGa> getCollaborativeGaxOwner(Integer id_user);
	
	public CollaborativeGa firstCollaborative();
	
	//GroupsGA	
	public void saveGroupGa(GroupsGa groupsGa);
	
	public List<GroupsGa> getGroupsGaxIdCollaborativeGa(Integer id_collaborative_ga);
	
	//MembersGA	
	public void saveMemberGa(MembersGa memberGa);
	
	public List<MembersGa> getMembersGaxIdGroupGa(Integer id_group_ga);
	
	//CharacteristicsGA
	public void saveCharacteristicGa(CharacteristicsGa characteristic_ga);
	
	public List<CharacteristicsGa> getCharacteristicsGaxIdMemberGa(Integer id_member_ga);
}

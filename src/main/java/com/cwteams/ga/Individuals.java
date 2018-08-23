package com.cwteams.ga;
import java.util.ArrayList;

public class Individuals {
	private ArrayList<GroupsFormed> groups;
	
	public Individuals(ArrayList<GroupsFormed> groups){
		this.groups=groups;
	}

	public ArrayList<GroupsFormed> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<GroupsFormed> groups) {
		this.groups = groups;
	}
}

package com.cwteams.ga;
import java.util.ArrayList;

public class GroupsFormed {
	private String name;
	private ArrayList<MemberData> members;
	
	public GroupsFormed(ArrayList<MemberData> members){
		this.members=members;
	}
	
	public GroupsFormed(String name, ArrayList<MemberData> members){
		this.name=name;
		this.members=members;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<MemberData> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<MemberData> members) {
		this.members = members;
	}
}

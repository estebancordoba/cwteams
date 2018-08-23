package com.cwteams.ga;
import java.util.ArrayList;

public class MemberData {
	private int id;
	private String name;	
	private ArrayList<Characteristics> character;
	
	public MemberData(){		
	}
	
	public MemberData(int id, ArrayList<Characteristics> character){
		this.id=id;
		this.character=character;
	}
	
	public MemberData(int id, String name, ArrayList<Characteristics> character){
		this.id=id;
		this.name=name;
		this.character=character;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Characteristics> getCharacter() {
		return character;
	}
	public void setCharacter(ArrayList<Characteristics> character) {
		this.character = character;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberData other = (MemberData) obj;		
		if (this.id != other.id)
			return false;
		return true;
	}
}
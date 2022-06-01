package com.ssk;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Team {

	public Team() {
		
	}
	@Id @GeneratedValue
	@Column(name = "TEAM_ID")
	private long id;
	
	@Column(name = "NAME")
	private String name;
	
	@OneToMany(mappedBy="team")
	private List<Member> members = new ArrayList<Member>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}
	
	
}

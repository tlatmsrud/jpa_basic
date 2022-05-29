package com.ssk;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SequenceGenerator(
		name = "member_seq_generator",
		sequenceName= "member_seq",
		initialValue = 1, allocationSize = 50)
public class Member {

	public Member() {
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String username;

	@Column
	private Integer age;
	
	@Enumerated(EnumType.STRING)
	private RoleType roleType;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;
	
	//varchar를 넘어선 큰 자료형 타입
	@Lob
	private String description;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}

package jpabook.jpashop.domain;

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
		sequenceName = "member_seq",
		initialValue = 1, 
		allocationSize = 50)
public class Member {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "member_seq_generator")
	private Long id;
	
	// nullable = not null
	@Column(name = "name", nullable = false, length = 10)
	private String username;
	
	private Integer age;
	
	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;
	
	@Lob // 큰 자료형 (문자형 데이터 타입일 형우 clob으로 생성)
	private String description;
	
	public Member() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
	
	
}

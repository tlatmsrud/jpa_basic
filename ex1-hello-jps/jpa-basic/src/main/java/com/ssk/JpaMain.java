package com.ssk;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class JpaMain {

	public static void main(String[] args) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		
		
		EntityManager em = emf.createEntityManager();
		
	
		EntityTransaction tx = em.getTransaction();
		
		
		tx.begin();
		
		try{
			
			Team team = new Team();
			team.setName("TeamA");
			em.persist(team);
			
			Member member = new Member();
			member.setUsername("member1");
			em.persist(member);
	
			
			team.addMember(member);
			Team findTeam = em.find(Team.class, team.getId());

			List<Member> members = findTeam.getMembers();
			
			for(Member m : members) {
				System.out.println("m = "+m.getUsername());
			}
			tx.commit();
		}catch(Exception e) {
			tx.rollback();
		}finally {
			em.close();
		}
		
		//EntityManagerFactory close
		emf.close();
	}
}

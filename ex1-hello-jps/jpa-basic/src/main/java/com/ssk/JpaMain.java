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
			
			Member member1 = new Member();
			member1.setUsername("member1");
			em.persist(member1);
			
			Team team = new Team();
			team.setName("TEamA");
			em.persist(team);
			
			member1.setTeam(team);
			
			em.flush();
			em.clear();
			
			Member refMember = em.find(Member.class, member1.getId());
			System.out.println(refMember.getTeam().getClass());
			
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

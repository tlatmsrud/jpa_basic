package jpabook.jpashop;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.RoleType;

public class JpaMain {
	
	public static void main(String[] args) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpashop");
		
		EntityManager em = emf.createEntityManager();
		
		EntityTransaction tx = em.getTransaction();
		
		tx.begin();
		
		try {
			Member member1 = new Member();
			member1.setUsername("A");
			member1.setRoleType(RoleType.USER);
			
			Member member2 = new Member();
			member2.setUsername("B");
			member2.setRoleType(RoleType.USER);
			
			Member member3 = new Member();
			member3.setUsername("C");
			member3.setRoleType(RoleType.USER);
			
			System.out.println("==============");
			em.persist(member1);
			em.persist(member2);
			em.persist(member3);
			System.out.println("==============");
			tx.commit();
		}catch(Exception e) {
			tx.rollback();
		}finally {
			em.close();
			emf.close();
		}

	}
}

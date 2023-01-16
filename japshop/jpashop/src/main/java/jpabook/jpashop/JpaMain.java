package jpabook.jpashop;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import jpabook.jpashop.domain.Member;

public class JpaMain {
	
	public static void main(String[] args) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpashop");
		
		EntityManager em = emf.createEntityManager();
		
		EntityTransaction tx = em.getTransaction();
		
		tx.begin();
		
		Member member = new Member();
		member.setId(1L);
		member.setName("Creater");
			
		// 영속성 컨텍스트의 1차 캐시에 저장 (영속상태)
		em.persist(member); 
	
		// 1차 캐시에 저장 된 Member 조회하기에 Select 쿼리가 나가지 않음.
		Member findMember = em.find(Member.class, 1L); 
		System.out.println(findMember.getName());
		System.out.println(findMember.getId());
		tx.commit();
		em.close();

		EntityManager em2 = emf.createEntityManager();
		Member findMember1 = em2.find(Member.class, 1L);
		Member findMember2 = em2.find(Member.class, 1L);
		
		System.out.println("동일성 보장 : "+(findMember1 == findMember2));
		emf.close();

	}
}

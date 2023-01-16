package jpabook.jpashop;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import jpabook.jpashop.domain.Member;

public class PersistenceContextMain {

	public static void main(String[] args) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpashop");
		
		EntityManager em = emf.createEntityManager();
		
		EntityTransaction tx = em.getTransaction();
		
		tx.begin();
		// 1. 비영속상태 - 객체를 생성한 상태
		Member member = new Member();
		member.setId(3L);
		member.setName("Tester3");
		
		// 2. 영속상태 - 영속성 컨텍스트에 저장한 상태
		em.persist(member);
		
		// 3. 준영속상태 - 회원 엔티티를 영속성 컨텍스트에서 분리한 상태
		em.detach(member);

		tx.commit();
	}
}

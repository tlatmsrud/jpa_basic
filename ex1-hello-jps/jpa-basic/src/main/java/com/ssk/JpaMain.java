package com.ssk;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class JpaMain {

	public static void main(String[] args) {
		//Persistance 객체로 persistance.xml 파일 로드 후 EntityManagerFactory 생성
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		
		//EntityManagerFactory.에서 EntityManager 생성
		EntityManager entityManager = emf.createEntityManager();
		
		//EntityManager에서 Transaction 생성
		EntityTransaction tx = entityManager.getTransaction();
		
		//Transaction 시작
		tx.begin();
		
		try{
			// 등록
			//Member member = new Member();
			//member.setId(1L);
			//member.setName("tester");
			//entityManager.persist(member);
			
			// 조회
			Member findMember = entityManager.find(Member.class, 1L);
			
			List<Member> result = entityManager.createQuery("select m from Member as m", Member.class).getResultList();
			
			for(Member member : result) {
				System.out.println(member.getName());
			}
			// 삭제
			//entityManager.remove(findMember);
			
			// 수정
			//findMember.setName("Updater");
			
		}catch(Exception e) {
			tx.rollback();
		}finally {
			entityManager.close();
		}
		
		
		//Transaction 커밋
		tx.commit();
		
		//EntityManager close
		entityManager.close();
		
		//EntityManagerFactory close
		emf.close();
	}
}

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
			Member member = entityManager.find(Member.class, 200L); // 영속상태
			//member.setName("AAAA"); // 더티체킹 == 스냅샷 비교
			
			//entityManager.detach(member); // 엔티티를 영속성 컨텍스트에서 detach
			Member member2 = entityManager.find(Member.class, 200L); // 영속상태
			System.out.println(member == member2);
			tx.commit();
		}catch(Exception e) {
			tx.rollback();
		}finally {
			entityManager.close();
		}
		
		
		//EntityManager close
		entityManager.close();
		
		//EntityManagerFactory close
		emf.close();
	}
}

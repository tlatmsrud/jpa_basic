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
			Member member = new Member();
			member.setUsername("test8");
			entityManager.persist(member);
			
			tx.commit();
		}catch(Exception e) {
			tx.rollback();
		}finally {
			entityManager.close();
		}
		
		EntityManager entityManager2 = emf.createEntityManager();
		
		//EntityManager에서 Transaction 생성
		EntityTransaction t2 = entityManager2.getTransaction();
		try{
			Member member = new Member();
			member.setUsername("test9");
			entityManager2.persist(member);
			
			t2.commit();
		}catch(Exception e) {
			e.printStackTrace();
			t2.rollback();
		}finally {
			entityManager2.close();
		}
		
		//EntityManagerFactory close
		emf.close();
	}
}

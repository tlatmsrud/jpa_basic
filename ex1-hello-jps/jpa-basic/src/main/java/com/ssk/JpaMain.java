package com.ssk;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class JpaMain {

	public static void main(String[] args) {
		//Persistance ��ü�� persistance.xml ���� �ε� �� EntityManagerFactory ����
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		
		//EntityManagerFactory.���� EntityManager ����
		EntityManager entityManager = emf.createEntityManager();
		
		//EntityManager���� Transaction ����
		EntityTransaction tx = entityManager.getTransaction();
		
		//Transaction ����
		tx.begin();
		
		try{
			Member member = new Member();
			member.setUsername("test");
			entityManager.persist(member);
			
			tx.commit();
		}catch(Exception e) {
			tx.rollback();
		}finally {
			entityManager.close();
		}
		
		//EntityManagerFactory close
		emf.close();
	}
}

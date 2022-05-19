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
			// ���
			//Member member = new Member();
			//member.setId(1L);
			//member.setName("tester");
			//entityManager.persist(member);
			
			// ��ȸ
			Member findMember = entityManager.find(Member.class, 1L);
			
			List<Member> result = entityManager.createQuery("select m from Member as m", Member.class).getResultList();
			
			for(Member member : result) {
				System.out.println(member.getName());
			}
			// ����
			//entityManager.remove(findMember);
			
			// ����
			//findMember.setName("Updater");
			
		}catch(Exception e) {
			tx.rollback();
		}finally {
			entityManager.close();
		}
		
		
		//Transaction Ŀ��
		tx.commit();
		
		//EntityManager close
		entityManager.close();
		
		//EntityManagerFactory close
		emf.close();
	}
}

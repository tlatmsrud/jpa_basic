package com.ssk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class JpaMain {

	public static void main(String[] args) {
		//Persistance ��ü�� persistance.xml ���� �ε� �� EntityManagerFactory ����
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		
		//EntityManagerFactory.���� EntityManager ����
		EntityManager em = emf.createEntityManager();
		
		//EntityManager���� Transaction ����
		EntityTransaction tx = em.getTransaction();
		
		//Transaction ����
		tx.begin();
		
		try{
			Team team = new Team();
			team.setName("TeamA");
			
			System.out.println("1------------------");
			//team�� ���Ӽ� ���ؽ�Ʈ�� ����
			//team�� ���� id ���� ���Ӽ� ���ؽ�Ʈ�� ����� ������ ������ ���� ������.
			em.persist(team);
			System.out.println("2------------------");
			Member member = new Member();
			member.setUsername("member1");
			member.setTeam(team);
			System.out.println("3------------------");
			em.persist(member);
			
			System.out.println("4------------------");
			//member.getId�� ��ȸ�ϸ� ���Ӽ� ���ؽ�Ʈ�� �� �ִ� member�� ������.
			Member findMember = em.find(Member.class, member.getId());
			System.out.println("5------------------");
			Team findTeam = findMember.getTeam();
			System.out.println(findTeam.getName());
			
			//Team, Member Insert ���� ����
			tx.commit();
		}catch(Exception e) {
			tx.rollback();
		}finally {
			em.close();
		}
		
		//EntityManager���� Transaction ����

		//EntityManagerFactory close
		emf.close();
	}
}

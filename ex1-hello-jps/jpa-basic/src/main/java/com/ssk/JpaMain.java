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
		EntityManager em = emf.createEntityManager();
		
		//EntityManager���� Transaction ����
		EntityTransaction tx = em.getTransaction();
		
		//Transaction ����
		tx.begin();
		
		try{
			Team team = new Team();
			team.setName("TeamA");
			
			//team�� ���Ӽ� ���ؽ�Ʈ�� ����
			//team�� ���� id ���� ���Ӽ� ���ؽ�Ʈ�� ����� ������ ������ ���� ������.
			em.persist(team);
			Member member = new Member();
			member.setUsername("member1");
			member.setTeam(team);
			em.persist(member);
			
			em.flush();
			em.clear();
			//member.getId�� ��ȸ�ϸ� ���Ӽ� ���ؽ�Ʈ�� �� �ִ� member�� ������.
			Member findMember = em.find(Member.class, member.getId());

			List<Member> members = findMember.getTeam().getMembers();
			
			for(Member m : members) {
				System.out.println("m = "+ m.getUsername());
			}

			
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

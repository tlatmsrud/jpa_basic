package com.ssk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class JpaMain {

	public static void main(String[] args) {
		//Persistance 객체로 persistance.xml 파일 로드 후 EntityManagerFactory 생성
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		
		//EntityManagerFactory.에서 EntityManager 생성
		EntityManager em = emf.createEntityManager();
		
		//EntityManager에서 Transaction 생성
		EntityTransaction tx = em.getTransaction();
		
		//Transaction 시작
		tx.begin();
		
		try{
			Team team = new Team();
			team.setName("TeamA");
			
			System.out.println("1------------------");
			//team을 영속성 컨텍스트에 저장
			//team에 대한 id 값은 영속성 컨텍스트에 저장될 시점에 쿼리를 날려 가져옴.
			em.persist(team);
			System.out.println("2------------------");
			Member member = new Member();
			member.setUsername("member1");
			member.setTeam(team);
			System.out.println("3------------------");
			em.persist(member);
			
			System.out.println("4------------------");
			//member.getId로 조회하면 영속성 컨텍스트에 들어가 있는 member를 가져옴.
			Member findMember = em.find(Member.class, member.getId());
			System.out.println("5------------------");
			Team findTeam = findMember.getTeam();
			System.out.println(findTeam.getName());
			
			//Team, Member Insert 쿼리 실행
			tx.commit();
		}catch(Exception e) {
			tx.rollback();
		}finally {
			em.close();
		}
		
		//EntityManager에서 Transaction 생성

		//EntityManagerFactory close
		emf.close();
	}
}

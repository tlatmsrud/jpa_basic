package jpabook.jpashop;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;

public class JpaMain {
	public static void main(String[] args) {
		//Persistance 객체로 persistance.xml 파일 로드 후 EntityManagerFactory 생성
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpashop");
		
		//EntityManagerFactory.에서 EntityManager 생성
		EntityManager em = emf.createEntityManager();
		
		//EntityManager에서 Transaction 생성
		EntityTransaction tx = em.getTransaction();
		
		//Transaction 시작
		tx.begin();
		
		try{
			// 주문 조회
			Order order = em.find(Order.class, 1);
			
		
			
			tx.commit();
		}catch(Exception e) {
			tx.rollback();
		}finally {
			em.close();
		}
		
		//EntityManagerFactory close
		emf.close();
	}
}

package jpabook.jpashop;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import jpabook.jpashop.domain.Member;

public class JpqlMain {

	public static void main(String[] args) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpashop");
		
		EntityManager em = emf.createEntityManager();
		
		List<Member> list = em.createQuery("select m from Member m",Member.class).getResultList();
		
		for(Member member : list) {
			System.out.println(member.getName());
		}
	}
}

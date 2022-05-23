# 1. JPA 소개


1. Java 개발자의 현실 1

	- SQL 매퍼
	- SI 개발 > 모든 테이블의 CRUD 쿼리 개발 (기계적)
	- 특정 테이블에 컬럼 추가 > 모델 객체 수정 > 관련된 모든 쿼리에 추가된 컬럼 추가 (노가다)

   
2. Java 개발자의 현실 2

	- 로직은 객체 지향적으로. 테이블 설계와 데이터 처리는 안 객체지향적으로
	- 데이터 처리는 쿼리로, (SQL 매퍼)

3. 테이블 설계를 객체지향적으로 가능한가?

	- 테이블을 Java 객체에 맞게 생성
	- 쿼리 개발(SQL 매퍼)
	- 끝

4. 상속 관계에 대한 테이블 설계도 객체지향적으로 설계해보자.

	- 나는야 메이플 스토리같은 게임의 개발자인데, 팀장님이 임무 하달.
	- AS-IS : 모든 무기에는 이름, 공격력, 공격속도 옵션이 있음.
	- TO-BE : 히든무기, 유니크무기가 추가됨. 히든무기는 특수공격 옵션이, 유니크무기는 추가스텟 옵션이 추가됨.
	- 처리 내용 : 
	1) Java에서는 무기 클래스를 상속받는 히든무기, 유니크무기 클래스 생성. 
    2) 히든무기 클래스에는 특수공격 필드 추가, 유니크무기 클래스에는 추가스텟 필드 추가.
	3) 객체 지향적인 테이블 설계를 위해 특수무기 테이블 (무기 ID, 특수공격), 유니크무기 테이블(무기 ID, 추가 스텟) 생성. > 무기 테이블과 조인하여
   처리.
   
   완성된 설계
   
   무기테이블 - 무기 ID, 이름, 공격력, 공격속도.
   
   특수무기 테이블 - 무기 ID, 특수공격
   
   유니크무기 테이블 - 무기 ID, 추가 스텟
    
5. Java에서 간단한 조회, 등록 로직을 개발해보자.
    - 신규 특수무기 추가 : 특수무기 테이블과 무기 테이블에 데이터 INSERT
    - 신규 유니크무기 추가 : 유니크무기 테이블과 무기 테이블에 데이터 INSERT
    - 특수 무기 정보 조회 : 특수 무기 테이블, 무기 테이블 JOIN / 특수 무기 객체에 RETURN
    - 유니크 무기 정보 조회 : 유니크 무기 테이블과 무기 테이블 JOIN / 유니크 무기 객체에 RETURN
 	
    - 결론 : INSERT시 상속관계인 테이블 모두 작업 필요. 조회 시 테이블 JOIN. 쿼리 작업이 배로 많아짐. 그래도 할만함.


6. 연관 관계에 대한 테이블 설계도 객체지향적으로 설계해보자.

	- 모든 무기에 무기 제작 업체 명, 제작 일시, 제작 기간 속성를 추가하라는 요건.
	1) 무기 테이블에 각 필드를 추가하려했으나, 객체 지향적으로 설계하기 위해 제작 정보 객체를 생성.
    2) 무기 객체의 멤버 필드에 제정 정보 추가.
	3) GET 메서드를 만들어 제작 정보를 조회할 수 있도록 함.
  
    
7. jAVA에서 간단한 조회, 등록 로직을 개발해보자.
	- A 무기에 대한 제작 정보 추가
	1) 제작 정보 객체 생성
    2) 무기 객체에 SET.
    3) INSERT 쿼리 추가 > 제작 정보가 객체 이므로 Mybatis 구문에서는 ${무기.제작정보.제작업체명} 형태로 파라미터 설정.
    
    - A 무기에 대한 정보 조회
    1) 무기 정보 조회 쿼리 실행
    2) 조회된 무기에 대한 제작정보 조회 쿼리 실행. (한번에 조회 불가. 무기 객체 안에 제작 정보 객체가 있기 때문에 한번에 조회해도 바인딩 안됨)
    

8. 결론

	- RDB를 객체 지향적으로 설계하면 할일이 뒤지게 많다.
    
    
9. RDB 안쓰고 Java 컬렉션에 저장한다고 가정하면

	- 특수 무기 등록 : 무기객체.add(특수 무기객체) - 자동 바인딩
	- 유니크 무기 등록 : 무기객체.add(유니크 무기 객체)
	- 특수 무기 조회 : 특수무기 객체 = 무기객체.get(무기 ID)  >> 다형성 활용
	- 유니크 무기 조회 : 유니쿠 무기 객체 = 무기객체.get(무기 ID)  >> 다형성 활용


10. 쌉 최종결론

	- RDB를 객체 지향형로 설계 = 노예.
	- 자바 컬렉션처럼 RDB에 저장할 수는 없을까?

11. 그게바로 JPA.



# 2. JPA

1. ORM
	
    - Object reolational mapping(객체 관계 매핑)
    - 자바(객체)와 DB(관계형 DB)를 매핑


2. 동작방식

	- JPA를 통해 내부적으로 쿼리 생성 후 JDBC API 사용.
	- Persistance 객체가 persistence.xml 파일을 읽어 설정에 맞게 EntityManagerFactory를 생성한다. 그리고 필요할때마다 Factory에서 EntityManager를 생성하여 처리한다.
	- 더 나아가면 요청이 있을 때마다 EntityManagerFactory에서 EntityManager를 생성하고, EntityManager에서 트랜잭션을 생성한 후 begin, commit, rollback 과 같은 동작을 수행한다. 그 후 EntityManager가 close 된다.
	- 엔티티 매니저 팩토리는 하나만 생성해서 어플리케이션 전체에서 공유.
	- 엔티티 매니저는 요청올때마다 생성. 쓰레드간 공유하면 안됨.
  ![jpa_mechanism](./jpa_mechanism.PNG)


3. 사용해야하는 이유
	
    - 객체 중심 개발
    - 생산성 증대
    - 유지보수 증대 = 필드만 추가하면됨.
    - 패러다임의 불일치 해결
    - 신뢰할 수 있는 엔티티, 계층
	- 1차 캐싱 지원
	- 지연로딩 지원 (객체가 실제 사용될 때 로딩)
	- 즉시로딩 지원 (Join SQL로 한번에 연관 객체까지 미리 조회)


# 3. JPA 프로젝트 생성

1. h2 DB 설치
	
    - https://www.h2database.com/html/main.html

2. 메이븐 설정

	- groupId : jpa-basic
	- artifactId : ex1-hello-jpa
	- version : 1.0.0

3. persistance.xml 파일 생성

	- jpa 설정 관련 파일
	- 반드시 resource 경로에 META-INF 폴더 생성 후 그 안에 persistance.xml 파일로 생성

```html
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2"
			 xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    		 xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    
    <persistence-unit name="hello">
        <properties>
            <!-- 필수 속성 -->
            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="javax.persistence.jdbc.user" value="sa"/>
            <property name="javax.persistence.jdbc.password" value=""/>
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
            
            <!-- 옵션 -->
            <property name="hibernate.show_sql" value = "true"/>
            <property name="hibernate.format_sql" value = "true"/>
            <property name="hibernate.use_sql_comments" value = "true"/>
        </properties>
    </persistence-unit>
    
</persistence>

```

4. Test Class 생성

    - 아래 코드 실행 후 별다른 에러 로그가 없으면 성공

```java
package com.ssk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaMain {

	public static void main(String[] args) {
		//Persistance 객체로 persistance.xml 파일 로드 후 EntityManagerFactory 생성
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		
		//EntityManagerFactory.에서 EntityManager 생성
		EntityManager entityManager = emf.createEntityManager();
		
		//EntityManager close
		entityManager.close();
		
		//EntityManagerFactory close
		emf.close();
	}
}

```

5. 테스트 테이블 생성
```sql
create table Member(
    id bigint not null,
    name varchar(255),
    primary key (id)
);
```

6. 엔티디 클래스 생성
    - Member.java 클래스 생성
```java
package com.ssk;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Member {

	@Id
	private Long id;
	
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}

```

7. JPA 테스트
```java
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

			// 삭제
			//entityManager.remove(findMember);
			
			// 수정
			findMember.setName("Updater");
			
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


```

8. JPQL
    
    - JPA를 사용하면 엔티티 객체를 중심으로 개발
    - 문제는 검색쿼리 (join)
    - 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색
    - 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
    - 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색조건이 포함된 SQL이 필요
    - 이 솔루션이 바로 JPQL이며 JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어 제공.
    - SQL 문법과 유사
    - JPQL == 객체지향 SQL

```java
          // JPQL를 사용한 조회
			List<Member> result = entityManager.createQuery("select m from Member as m", Member.class).getResultList();	
```

# 4. 영속성 컨텍스트 1

1. 영속성 컨텍스트란
   
   - 엔티디를 영구 저장하는 환경이라는 뜻.
   - EntityManager.persist(entity); => entity를 영속성 컨텍스트에 저장한다.
   - 영속성 컨텍스트는 논리적인 개넘. 눈에 보이지 않음.
   - EntityManager를 통해 영속성 컨텍스트에 접근.
   - 영속성 컨텍스트에는 1차 캐시와 쓰기 지연 SQL 저장소가 존재한다.
   - 엔티티 매니저와 영속성 컨텍스트는 1:1 관계

2. 엔티티의 생명주기
  
   - 비영속 상태

      - 객체를 생성한 상태

	- 영속 상태

      - entityManager의 persist 메서드를 사용해 영속성 컨텍스트에 저장한 상태. 
      - 트랜잭션의 commit 호출 시 영속성 컨텍스트에 저장된 객체 쿼리가 호출됨

	- 준영속 상태
  
		- 영속성 컨텍스트에서 분리(detach)된 상태


3. 영속성 컨텍스트의 이점

	- 1차 캐시
		
		- DB와 어플리케이션 사이에 1차 캐시가 존재.


	- 동일성(identity) 보장

		- Member1 == Member2
  

	- 트랜잭션을 지원하는 쓰기 지연 (== 버퍼링)

		- 트랜잭션 commit을 해야 쓰기 지연 SQL 저장소에 쌓인 쿼리를 호출함.
		- JDBC 배치와 같은 효과를 얻을 수 있음


	- 변경 감지

		- Member member = em.find(Member.class, 1); ==> 영속성 엔티티 조회
		-  member.setName("ZZZZ") ==> 영속성 엔티티 수정
		-  commit() ==> 영속 컨텍스트(엔티티 매니저)의 1차 캐시 스냅샷을 통한 변경감지 후 변경된 내용이 있을 경우 Update쿼리 생성 후 commit
		-  결론적으로 값이 바뀌면 commit 시 update 쿼리 자동 호출한다.


	- 지연로딩

# 5. 플러시

1. 플러시란?

	- 영속성 컨텍스트의 변경 내용을 데이터베이스에 동기화하는 것.
	- 트랜잭션 커밋 시 플러시는 자동 호출된다.
	- em.flush 메서드를 통해 직접 호출도 가능하다.
	- 영속성 컨텍스트를 비우는 것이 아님


	```java
	Member member = new Member(200L, "member200");

	entityManager.persist(member); //영속성 컨텍스트에 저장

	entityManager.flush(); // 쓰기지연 저장소의 쿼리가 실행되며 Database 반영

	```

# 6. 준영속 상태

1. 준영속 상태란

	- 영속 상태에의 엔티티가 영속성 컨텍스트에서 분리(detached)된 상태
	- em.detach(entity) ==> 특정 엔티티만 영속성 컨텍스트에서 분리
	- em.clear() ==> 영속성 컨텍스트 자체를 초기화(1차 캐시, 쓰기지연 저장소도 초기화)
	- em.close() ==> 영속성 컨텍스트를 종료시켜 삭제.
  
  ```java
	Member member = entityManager.find(Member.class, 200L); // 영속상태
	member.setName("AAAA"); // 더티체킹 == 스냅샷 비교
			
	entityManager.detach(member); // 엔티티를 영속성 컨텍스트에서 detach
	tx.commit(); // update 쿼리가 나가지 않음.
  ```

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

# 8. 객체와 테이블 매핑

1. 엔티티 매핑 소개

	- 객체와 테이블 매핑 : @Entity, @Table
	- 필드와 컬럼매핑 : @Column
	- 기본키 매핑 : @Id
	- 연관관계 매핑 : @ManyToOne, @JoinColumn

2. @Entity

	- JPA가 관리하는 클래스. 엔티티라고 칭함.
	- JPA를 사용해서 테이블과 매핑할 클래스는 @Entity가 필수
	- 기본 생성자 필수

3. @Table
   
   - 엔티티와 매핑할 테이블 지정


# 9. 데이터베이스 스키마 자동 생성

- DDL을 어플리케이션 실행 시점에 자동 생성
- 개발 서버에서만 사용을 권장.
- 속성 : hibernate.hbm2ddl.auto
  ```java
  <property name="hibernate.hbm2ddl.auto" value="create"/>
  ```
- 데이터베이스 방언에 따라 생성이 가능.   
  
# 10. 필드와 컬럼 매핑


1. 매핑 어노테이션
	
	- @Column : 컬럼 매핑
	- @Temporal : 날짜 매핑
	- @Enumerated : enum 타입 매핑
	- @Lob : BLOB, CLOB 매핑
      - CLOB 
        > 사이즈가 큰 데이터를 외부 파일로 저장하기 위한 데이터 타입   
	    > 문자열 데이터를 DB 외부에 저장하기 위한 타입   
		> CLOB의 최대 길이는 외부 저장소에서 생성 가능한 파일 크기   
		> SQL문에서 문자열 타입으로 표현. CHAR, VARCHAR 등과 호환
		> 문자형 대용량 파일을 저장하는 타입.

	  - BLOB
		> 바이너리 데이터릴 DB 외부에 저장하기 위한 타입   
		> BLOB의 최대 길이는 외부 저장소에서 생성 가능한 파일 크기
		> SQL문에서 비트열 타입으로 표현. BIT, VIT VARYING 과 호환
		> 컴퓨터가 인식하는 모든 파일을 저장하는 타입.

	- @Transient 특정 필드를 컬럼에 매핑하지 않음.

2. @Column
	- 컬럼을 매핑할 때 사용
	- name : 필드와 매핑할 테이블의 컬럼 이름
	- insertable, updatable : 등록, 변경 가능여부
	- nullable(DDL) : null 값의 허용 여부를 설정. false시 not null 제약조건
	- unique(DDL) : 유니크 제약조건
	- columnDefinition(DDL) : 데이터베이스 컬럼 정보를 직접 줄 수 있음.
		> ex) varchar(100) default 'empty'
	- length : 문자 길이 제약조건

3. @Enumrated
    - Java enum 타입을 매핑할 때 사용.
		```java
		@Enumerated(EnumType.STRING)
		private RoleType roleType;
		```
    - ORDINAL은 사용하면 안됨. 추후 Enum 타입값이 추가될 경우 순서로 인해 버그가 발생
		> EnumType.ORDINAL : enum 순서를 DB에 저장   
		> EnumType.STRING : enum 이름을 데이터베이스에 저장.   
		> 기본 값이 EnumType.ORDINAL

4. @Temporal 속성
	- 날짜 타입(java.util.Date, java.util.Calendar)을 매핑할 때 사용
	- LocalDate, LocalDateTime을 사용할 때는 생략 가능(최신 하이버네이트 지원)
	- TemporalType.Date : 날짜, 데이터베이스 date 타입과 매핑
	- TemporalType.TIME : 시간, 데이터베이스 time 타입과 매핑
	- TemporalType.TIMESTAMP : 날짜와 시간, 데이터베이스 timestemp 타입과 매핑

5. @Lob
	- 매핑하는 필드 타입이 문자면 CLOB 매핑, 나머지는 BLOB 매핑.
	- LOB에는 속성이 없음.

# 11. 기본키 매핑
1. 직접 할당
	- @Id 어노테이션 사용.

2. 값 자동생성
	- @GeneratedValue 어노테이션 사용.
	- 자동생성 전략
		> IDENTITY : 데이터베이스에 위임.   
		> SEQUNECE : 데이터베이스 시퀀스 오브젝트 사용. @SequenceGenerator  
		> TABLE : 키 생성용 테이블 사용. @TableGenerator  
		> AUTO : 방언에 따라 자동 지정. 기본값.


3. IDENTITY 전략
	- 기본키 생성을 데이터베이스에 위임
	- JPA는 보통 트랜잭션 커밋 시점에 INSERT SQL 실행
    - em.persis() 시점에 즉시 INSERT SQL이 실행된다. 
  
4. SEQUENCE 전략
	- ID 타입은 Long을 권장. 공간은 2배 차이가 나지만 서버의 성능차이는 거의 없음.
	- 시퀀스 이름을 지정하지 않을 경우 자체 제공되는 시퀀스를 사용.
	- em.persist() 호출 시점에 SQL을 통해 시퀀스 값을 조회하고, flush 시점에 INSERT 쿼리가 실행됨.
		> Q. 그럼 INSERT 할때마다 시퀀스를 조회하는 네트웤 통신이 있다는 건데, 이거 성능이슈 아닌가요?  
		>> A. JPA에서는 이런 성능이슈를 최소화하기위해 @SequenceGenerator 옵션의 allocationSize를 제공합니다. 이는 시퀀스 조회 시 Size만큼의 값을 한번의 SQL로 조회 후 메모리에 할당합니다. 그리고 필요할때마다 DB 통신을 하는 것이 아닌 메모리에서 가져오게 됩니다. 기본값은 50입니다.  
		50이 넘게되면 다시 시퀀스 조회 DB 통신을 하게 됩니다.  
		
		> Q. 그럼 기본값을 최대한으로 늘리는 것이 좋지않나요? 왜 50인지 이해가 가지 않습니다.
		>> A. 맞습니다. 하나의 엔티티 매니저에서 insert하는 로직이 많고, allocationSize 값이 적을수록 시퀀스 조회를 위한 DB 통신 횟수는 증가할 것입니다. 하지만 중간에 서버가 종료된다면 그 값만큼 시퀀스 값이 메모리에서 사라지게 될것이고, 다음 트랜잭션 발생 시 해당 값만큼 건너뛰게되니 비어있는 값이 발생하게 될것입니다. 서버가 다중화 되어있어도 마찬가지입니다. 이때 비어있는 값이 크다면 아무래도 이상하게 느껴지겠죠? 


5. TABLE 전략
	- 키 생성 전용 테이블을 하나 만들어서 데이터베이스 시퀀스를 흉내내는 전략
	- 단점으로는 테이블에서 키를 조회하는 것이기때문에 성능 이슈 가능성이 있음

6. 권장하는 식별자 전략
	- 비지니스와 관련된 데이터를 키로 사용하는 것은 권장하지 않음.
	- Long형 + 대체키 + 키 생성전략 사용
	- 주민등록번호도 기본키로 적절하지 않다.

# 12. 요구사항 분석과 기본 매핑
1. 요구사항 분석
	- 회원은 상품을 주문할 수 있다.
	- 주문 시 여러 상품을 선택할 수 있다.

2. 도메인 모델 분석
	- 회원과 주문의 관계 : 회원은 여러 번 주문할 수 있다. (일대다)
	- 주문과 상품의 관계 : 주문할 때 여러 상품을 선택할 수 있다. 반대로 같은 상품도 여러 번 주문될 수 있다. 주문상품 이라는 모델을 만들어서 다대다 관계를 일대다, 다대일 관계로 풀어냄.

3. 엔티티 모델 설계
```java
@Entity
public class Member {

	@Id @GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;
	
	private String name;
	
	private String city;
	
	private String street;
	
	private String zipCode;

	// Getter Setter...
	
}


@Entity
@Table(name = "ORDERS")
public class Order {

	@Id @GeneratedValue
	@Column(name = "ORDER_ID")
	private Long id;
	
	@Column(name = "MEMBER_ID")
	private Long memberId;
	
	private LocalDateTime orderDate;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	// Getter Setter...

}

@Entity
public class OrderItem {

	@Id @GeneratedValue
	@Column(name = "ORDER_ITEM_ID")
	private Long id;
	
	@Column(name = "ORDER_ID")
	private Long orderId;
	
	@Column(name = "ITEM_ID")
	private Long itemId;
	
	private int orderPrice;
	private int count;	
	
}

```

4. 데이터 중심 설계의 문제점
	- 위 방식은 객체 설계를 테이블 설계에 맞춘 방식
	- 테이블의 외래키를 객체에 그대로 가져옴
	- 객체 그래프 탐색이 불가능
	- 참조가 없으므로 UML도 잘못됨.
	```java
		// 주문 조회
		Order order = em.find(Order.class, 1);
			
		// 멤버 정보 조회. 객체 그래프 탐색이 불가능하며 참조도 없음.
		Long memberId = order.getMemberId();
		Member member = em.find(Member.class, memberId);

		// Memeber memeber = order.getMemeber 형식이 객체지향적.
	```
	- __연관관계 매핑__ 을 적용해야함.


# 12. 연관관계 매핑
1. 목표
   - 객체와 테이블 연관관계의 차이를 이해
   - 객체의 참조와 테이블의 외래 키를 매핑
   - 용어이해
		> 방향 : 단방향 양방향  
		> 다중성 : 일대다, 다대일, 일대일, 다대다의 이해  
		> __연관관계의 주인__ : 객체 양방향 연관관계는 관리 주인이 필요

2. 예제 사나리오
	- 회원과 팀이 있다.
	- 회원은 하나의 팀에만 소속될 수 있다.
	- 회원과 팀은 다대일 관계이다.

3. 모델링
	![team_member_relation](./team_member_relation.PNG)

4. 객체를 테이블에 맞추어 생성
   - 테이블은 외래 키로 조인을 사용해서 연관된 테이블을 찾는다.
   - 객체는 참조를 사용해서 연관된 객체를 찾는다.
   - 테이블과 객체 사이에는 이런 큰 간격이 있다.

5. 객체 지향 모델링 
	![team_member_relation2](./team_member_relation2.PNG)

	- Member와 Team은 일대다 관계이며 FK를 가진 Member 테이블이 연관관계의 주인이 된다.
	- Member 엔티티에 Team 필드 생성 후 다대일을 나타내는 @ManyToOne 을 붙여준다. 즉 Member 엔티티와 Team 엔티티는 다대일 관계라는 의미이다.
	- 그리고 JoinColumn, 즉 FK Column 명을 넣어준다.

	```java
	@Entity
	public class Member {

		public Member() {
			
		}

		@Id @GeneratedValue
		@Column(name = "MEMBER_ID")
		private Long id;
		
		@Column(name = "USERNAME")
		private String username;

		//멤버입장에서는 다대일 관계
		@ManyToOne
		@JoinColumn(name = "TEAM_ID")
		private Team team;

		//getter, setter ...
	}


	@Entity
	public class Team {

		public Team() {
			
		}
		@Id @GeneratedValue
		@Column(name = "TEAM_ID")
		private long id;
		
		@Column(name = "NAME")
		private String name;

		/// getter, setter ...
		
		
	}
	```


# 13. 양방향 연관관계와 연관관계의 주인
1. 양방향 매핑
	- 테이블 연관관계에서는 PK와 FK로 조인을 통해 양방향 연관관계를 맺을 수 있음. 단 객체 연관관계에서는 키가 되는 컬럼으로 양방향 연관관계를 맺을 수 없음. 
	- mappedBy 구문을 사용하면 됨.

2. 객체의 양방향 연관관계
	- 양방향 X. 사실 2개의 단방향 연관관계를 갖음.
	- 때문에 관계를 갖는 두 객체 모두 서로에 대한 참조 값이 있어야함.

3. 테이블의 양방향 연관관계
	- 키값 하나로 양방향 연관관계를 갖음.
	- 관계를 갖는 두 테이블 중 하나만 참조값을 갖으면 됨.

4. 연관관계의 주인
	- 객체의 두 관계중 하나를 연관관계의 주인으로 지정
	- 연관관계의 주인만이 외래 키를 관리(등록, 수정)
	- 주인이 아닌쪽은 읽기만 가능.

5. 누구를 주인으로?
	- 외래 키가 있는 곳을 주인으로 정해라.
	- Member.team이 연관관계의 주인.
	- ManyToOne쪽이 연관관계의 주인.
	- DB 테이블 기준으로 '다' 쪽이 연관관계의 주인 (FK를 갖는 곳)
	- 멤버(주인) - 팀, 자동자 - 바퀴(주인)

6. 양방향 매핑 시 가장 많이 하는 실수
	- 연관관계의 주인에 값을 입력하지 않음.


# 14. 연관관계 매핑 시작
1. 요건
	> 초간단 쇼핑몰 테이블 구성  
	> 사용자는 여러건의 주문을 할 수 있다.

2. 테이블 구조
   ![jap_shop_erd](./jpa_shop_erd.PNG)
   - 유저는 여러 주문을 하거나,주문을 하지 않을 수 있다. (one 대 many or zero)
   - 하나의 주문에 여러 상품이 포함될 수 있고, 하나의 상품은 여러 주문에 포함되거나 없을 수 있다.(many 대 many >> one 대 many, many or zero 대 one)

3. 객체구조
   - Member와 Order : 양방향 연관관계 매핑 (Member 내 orders)
   - Order와 OrderItem : 양방향 연관관계 매핑 (Order 내 orderItems)

# 15. 연관관계
1. 연관관계 매핑 시 고려사항
	1) 다대일 : @ManyToOne
	2) 일대다 : @OneToMany
	3) 일대일 : @OneToOne

2. 단방향, 양방향
   1. 테이블 
      - 외래키 하나로 양쪽 조인 가능. 방향이라는 개념이 없음
   1. 객체 
      - 참조용 필드가 있는 쪽으로만 참조 가능
      - 한쪽만 참조하면 단방향
      - 양쪽이 서로 참조하면 양방향

3. 다대일 [N:1]
    - 멤버와 팀은 N : 1 관계
    - 멤버 객체가 연관관계의 주인

4. 다대일 양방향
   	- 팀 객체에 @OneToMany(mappedBy = "team")
	- 양쪽을 서로 참조하도록 개발

5. 일대일 [1:1]
   	- 일대일 관계는 그 반대도 일대일
   	- 주 테이블이나 대상 테이블 중 외래키 선택 가능
   	- 외래키에 유니크 제약주건 추가
  		> 유니크 제약조건 = 유일 키 제약조건  
		> 특정 컬럼에 대해 자료가 중복되지 않게 하는 것을 의미하며, 중ㅈ복은 허용하지 않으나, NULL은 중복될 수 있다.

6. 대다대 [N:N]
   	- 다대다는 사용하지 않고, 중간 엔티티를 생성 후 일대다, 다대일 연관관계 매핑을 해야함.


# 16. 상속관계 매핑
1. 정의
   - 객체의 상속 구조와 DB의 슈퍼타입 서브타입 관계를 매핑하는 것. RDB의 슈퍼타입 서브타입 관계라는 모델링 기법이 객체 상속과 유사함.
   - 상속관계 매핑 전략은 총 3가지로 조인전략, 단일테이블 전략, 구현클래스마다 테이블 전략이 있음.

2. 상속관계 매핑 전략
   1. 조인전략
      - 부모클래스와 자식클래스를 각각 테이블로 만든 뒤 식별관계로 설정
      - 자식 클래스 구분자인 DTYPE 생성
		![join_strategy](join_strategy.PNG)
   2. 단일 테이블 전략
      - 부모클래스와 자식클래스의 모든 컬럼을 가진 단일 테이블을 하나 생성 후 자식 클래스 구분자인 DTYPE 생성
		![one_table_strategy](one_table_strategy.PNG)
   3. 구현 클래스마다 테이블 전략
      - 자식클래스 테이블 생성 시 부모 클래스 필드를 추가
		![avatar_table_strategy](avatar_table_strategy.PNG)
		  
3. 장단점
   1. 조인전략
      - 장점 : 테이블 정규화, 외래 키 참조 무결성 제약조건 활용 가능, 저장공간 효율화
      - 단점 : 조회 시 조인을 많이 사용하여 성능 저하, 조회 쿼리가 복잡, 데이터 저장 시 INSERT SQL 2번 호출

   2. 단일 테이블 전략
      - 장점 : 조인이 필요 없으므로 (일반적으로) 조회 성능이 빠름, 조회 쿼리가 단순함
      - 단점 : 자식 엔티티가 매핑한 컬럼은 모두 NULL 허용해야하며, 단일 테이블에 모든 것을 저장하면 테이블이 커질 수 있기에 조회 성능이 느려질 수 있음.

   3. 구현 클래스마다 테이블 전략 (비추천)
      - 장점 : 서브 타입을 명확하게 구분해서 처리할 때 효과적이며, NOT NULL 제약조건 사용 가능
      - 단점 : 조회 시 모든 자식 테이블을 함께 조회할 경우 성능이 느림(UNION)

# 17.Mapped Superclass
1. 정의
   - 공통적으로 사용할 속성들을 지정하는 클래스이며, 자식 클래스에게만 매핑 정보를 제공하는 클래스. 
   - 상속관계 매핑, 엔티티, 테이블과 매핑하는 클래스가 아님.
   - 직접 생성해서 사용할 일이 없으므로 추상 클래스로 생성 권장.
   
2. 기능
   - 테이블과 관계 없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할
   - 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용
   - 실무에서 BaseEntity로 활용

3. 참고
   - @Entity 클래스는 엔티티나 @MappedSuperclass로 지정한 클래스만 상속이 가능하다. 

# 18. 프록시
1. 프록시 기초
	- em.find() vs em.getReference()
	- em.find() : 데이터베이스를 통해 실제 엔티티 객체 조회
	- em.getReference() : 데이터베이스 조회를 미루는 가짜 엔티티(=프록시) 객체 조회

2. 프록시 특징
	- 실제 클래스를 상속 받아서 만들어짐
	- 실제 클래스와 겉 모양이 같다.
	- 사용하는 입장에서 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 됨.
	- 프록시 객체는 처음 사용할 때 한번만 초기화
	- 초기화 시 프록시 객체가 실제 엔티티로 바뀌는게 아님. 프록시 객체를 통해 실제 엔티티에 접근이 가능한 구조임.
	- 프록시 객체는 원본 엔티티를 상속받음. 따라서 타입 체크시 주의( == 비교 실패, 대신 instance of 사용)
	- 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티 반환
	- 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제 발생

3. 프록시 확인
	- 프록시 인스턴스의 초기화 여부 확인
		- PersistenceUnitUtil.isLoaded(Object entity)
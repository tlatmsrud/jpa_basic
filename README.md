# 1. JPA에 들어가기 전에

## 1. Java 개발자의 현실

	- 자바 개발자의 주 업무는 SQL Mapper이다. 데이터를 비지니스 로직보다 SQL 쿼리로 처리하기 때문.
	- 로직은 객체 지향적으로 설계하나 테이블과 데이터처리는 관계지향적으로 처리한다. 객체지향언어로 관계지향적인 프로그래밍을 하는 상황이다.
	- 노가다성 업무가 필연적이다. 예를들어 특정 테이블에 컬럼이 추가된다면 모델 객체 및 쿼리에 해당 컬럼을 추가해야 한다.

<br>

## 2. 객체지향적인 설계
	- 테이블을 객체지향적으로 설계하면 객체지향적인 설계라고 할수있다. 다만, 객체지향적인 테이블설계는 불가능하다. 상속관계에 대한 테이블 설계가 불가능하기 때문이다.

<br>

## 3. 상속 관계에 대한 테이블 설계
	- 게임 유지보수 도중 아래와 같은 요건이 들어왔다고 가정한다.
    	- AS-IS : 모든 무기에는 이름, 공격력, 공격속도 옵션이 있음.
    	- TO-BE : 히든무기, 유니크무기가 추가됨. 히든무기는 특수공격 옵션이, 유니크무기는 추가스텟 옵션이 추가됨.
    
	- 기능구현을 위해 필요한 작업은 다음과 같다.
        1. 무기 클래스를 상속받는 히든무기, 유니크무기 클래스 생성. 
        2. 히든무기 클래스에는 특수공격 필드 추가, 유니크무기 클래스에는 추가스텟 필드 추가.
    	3. 히든무기 테이블 (무기 ID, 특수공격), 유니크무기 테이블(무기 ID, 추가 스텟) 생성.
    	4. 최종적으로 무기 테이블과 조인하여 데이터를 처리.

|테이블|컬럼 리스트|
|------|---|
|무기|무기 ID, 이름, 공격력, 공격속도|
|히든무기|무기 ID, 특수공격|
|유니크무기|무기 ID, 추가 스텟|
   
<br>

## 4. 설계된 테이블에 대한 테이블 CRUD 시나리오
    1. 신규 히든무기인 '슈퍼도끼' 추가 : 히든무기 테이블과 무기 테이블에 데이터 INSERT
   		- 왜? 기본정보는 무기테이블에, 특수공격은 히든무기 테이블에 넣어줘야하므로.
    2. 신규 유니크무기인 '유니콘도끼' 추가 : 유니크무기 테이블과 무기 테이블에 데이터 INSERT
   		- 왜? 마찬가지로 기본정보는 무기테이블에, 추가 스텟은 유니크 무기 테이블에 넣어줘야하므로.
    3. '슈퍼도끼' 정보 조회 : 히든 무기 테이블, 무기 테이블 JOIN 후 히든 무기 객체에 RETURN
        - Join 은 쿼리로, 리턴은 히든무기 객체로
    4. '유니콘도끼' 정보 조회 : 유니크 무기 테이블과 무기 테이블 JOIN 후 유니크 무기 객체에 RETURN
		- 위와 동일
	
	<br>

## 5. 설계된 테이블에 대한 테이블 CRUD 정리
	1. 추가 시 상속관계 테이블 모두 INSERT 작업 필요.
	2. 조회 시 상속관계 테이블 JOIN. 
	3. 관계가 복잡할수록 자연스럽게 쿼리 작업도 많아질 것 같으나 확 와닿진 않음.

<br>

## 6. 확! 와닿기 위한 요구사항 추가

	- 요구사항
    	- 모든 무기에 무기 제조 국가 , 제조 일시, 제조 비용 정보 추가

	- 기능 구현을 위한 작업은 다음과 같다.
    	1. 객체 지향적인 설계를 위해 제조 정보 객체 생성.
        2. 무기 객체의 멤버 필드에 제조 멤버필드 추가.

<br>

## 7. 자바 코드에서 무기 조회, 등록 로직 개발 시나리오
	- '슈퍼도끼'에 대한 제조 정보 추가
    	1) 제조 정보 객체 생성
        2) 무기 객체의 멤버필드에 Set(ex 생성자 혹은 Setter 사용)
        3) DB 데이터 처리를 위한 INSERT 쿼리 작성.
   			- 제작 정보가 객체 이므로 Mybatis 구문에서는 ${무기.제조정보.제조 국가} 형태로 전달
    
    - '슈퍼도끼'에 대한 정보 조회
        1) 무기 정보 조회 쿼리 실행
        2) 조회된 무기에 대한 제조정보 조회 쿼리 실행.
			- 무기 객체 안에 제작 정보 객체가 있어 한번의 쿼리로는 조회 불가능함.

<br>

## 8. 객체지향적인 DB 설계에 대한 결론

	- 관계형 데이터베이스 테이블을 객체 지향적으로 설계하면 할일이 아주 많아진다.
    

## 9. 그래서 어쩌라고
	- 일단 위 내용은 제쳐두고 RDB 대신 Java Collection 에 데이터를 저장한다면어떨까. 휘발성 고려하지 않고.

<br>

## 10. Collection에 대한 CRUD

1. 신규 히든무기인 '슈퍼도끼' 추가 : 무기리스트.add(특수 무기 객체)
	``` java
   		List<Weapon> 무기리스트 = new ArrayList<Weapon>();
		SpecialWeapon 슈퍼도끼 = new SpecialWeapon();
		
		무기리스트.add(0,슈퍼도끼); //자동바인딩
	```
<br>

2. 신규 유니크무기인 '유니콘도끼' 추가 :  무기리스트.add(유니크 무기 객체)
	``` java
		UniqueWeapon 유니콘도끼 = new UniqueWeapon();
		무기리스트.add(1,유니콘도끼);
	```
<br>

3. '슈퍼도끼' 정보 조회 : 특수무기 객체 = 무기리스트.get(무기 ID)
	``` java
		SpecialWeapon get슈퍼도끼 = (SpecialWeapon) 무기리스트.get(0); // 다형성 및 캐스팅 활용
	```
<br>

4. '유니콘도끼' 정보 조회 : 유니크 무기 객체 = 무기리스트.get(무기 ID)
	``` java
		UniqueWeapon get유니콘도끼 = (UniqueWeapon) 무기리스트.get(1); // 다형성 및 캐스팅 활용
	```

<br>

## 11. 결론
 ## Q. __컬렉션을 사용하니 훨 간단하네? 자바 컬렉션처럼 RDB에 저장할 수는 없을까?__
 ## A. __저장할수 있어. 그걸 가능하게 하는 기술이 바로 JPA야 !__

<br>

# 2. JPA

## 1. 정의
   - Java 진영에서의 ORM(Object Relational Mapping) 기술 표준 인터페이스
   - 쿼리가 아닌 Java 언어를 통해 RDB와 매핑해주는 기술을 정의한 인터페이스 
   - 인터페이스이기때문에 Hibernate, OpenJPA 등의 구현체를 채택하여 구현

## 2. ORM이란?
	
    - Object reolational mapping(객체 관계 매핑)
    - 자바(객체)와 DB(관계형 DB)를 매핑
  
## 3.  JPA 동작방식

	- JPA를 통해 내부적으로 쿼리 생성 후 JDBC API 사용.
	- Persistance 객체가 persistence.xml 파일을 읽어 설정에 맞게 EntityManagerFactory를 생성하고 필요할때마다 Factory에서 EntityManager를 생성하여 처리한다.
	- EntityManager에서 트랜잭션 생성 후 begin, commit, rollback 과 같은 동작을 수행한다.
	- 엔티티 매니저 팩토리는 하나만 생성해서 어플리케이션 전체에서 공유.
	- 엔티티 매니저는 요청올때마다 생성. 쓰레드간 공유하면 안됨.
  ![jpa_mechanism](./jpa_mechanism.PNG)


## 4. JPA를 사용해야하는 이유
	
    - 객체 중심 개발
    - 생산성 증대
    - 유지보수 증대 = 필드만 추가하면됨.
    - 패러다임의 불일치 해결
    - 신뢰할 수 있는 엔티티, 계층
	- 1차 캐싱 지원
	- 지연로딩 지원 (객체가 실제 사용될 때 로딩)
	- 즉시로딩 지원 (Join SQL로 한번에 연관 객체까지 미리 조회)

<br>

# 3. JPA 프로젝트 생성

## 1. h2 DB 설치
	
    - https://www.h2database.com/html/main.html

## 2. 메이븐 설정

	- groupId : jpa-basic
	- artifactId : ex1-hello-jpa
	- version : 1.0.0

## 3. persistance.xml 파일 생성

	- JPA 설정 관련 파일
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
			<property name="hibernate.hbm2ddl.auto" value="create"/> <!-- 시작 시 테이블 자동생성 -->
        </properties>
    </persistence-unit>
    
</persistence>

```

## 4. 메인 클래스 생성

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
		EntityManager em = emf.createEntityManager();
		
		//EntityManager close
		em.close();
		
		//EntityManagerFactory close
		emf.close();
	}
}

```

## 5. 테스트 테이블 생성
```sql
create table Member(
    id bigint not null,
    name varchar(255),
    primary key (id)
);
```

## 6. 엔티디 클래스 생성
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

## 7. JPA 테스트
```java
package com.ssk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

	public static void main(String[] args) {
		//Persistance 객체로 persistance.xml 파일 로드 후 EntityManagerFactory 생성
		//hello 값은 persistance.xml의 persistence-unit name 과 매핑되어야함.
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		
		//EntityManagerFactory.에서 EntityManager 생성
		EntityManager em = emf.createEntityManager();
		
		//EntityManager에서 Transaction 생성
		EntityTransaction tx = em.getTransaction();
		
		//Transaction 시작
		tx.begin();
		
		try{
			// 등록
			Member member = new Member();
			member.setId(1L);
			member.setName("tester");
			em.persist(member);
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


```

## 8. JPQL
    
JPA를 사용하면 엔티티 객체를 중심으로 개발하게 된다. 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색하나 모든 DB 데이터를 객체로 변환 및 검색하는 것은 한계가 있다. 애플리케이션이 필요한 데이터만 DB에서 불러오려면 검색조건이 포함된 SQL이 필요한 순간이 올 수 있다.

이에 대한 솔루션이 바로 JPQL이며 SQL을 추상화한 객체 지향 쿼리 언어이다. SQL 문법과 유사하다.

```java
    // JPQL를 사용한 조회
	List<Member> result = entityManager.createQuery(
		"select m from Member as m", Member.class).getResultList();	
```

<br>

# 4. 영속성 컨텍스트

## 1. 영속성 컨텍스트란? (JPA의 내부 동작 매커니즘과 관련)
   
   - JPA를 이해하는데 가장 중요한 용어.
   - 엔티디를 영구 저장하는 환경.
   - EntityManager.persist(entity); => entity를 영속성 컨텍스트에 저장한다.
   - 영속성 컨텍스트는 논리적인 개념이며. 눈에 보이지 않음.
   - EntityManager를 통해 영속성 컨텍스트에 접근한다.
   - 영속성 컨텍스트에는 1차 캐시와 쓰기 지연 SQL 저장소가 존재한다.
   - 엔티티 팩터리 매니저와 영속성 컨텍스트는 1:1 관계

<br>

## 2. 엔티티의 생명주기
  
1) 비영속 상태
   - 영속성 컨텍스트에 저장되지 않은 상태.
   - 객체를 생성한 상태.
	``` java
		// 1. 비영속상태 - 객체를 생성한 상태
		Member member = new Member();
		member.setId(2L);
		member.setName("Tester2");
	```

2) 영속 상태
   - 속성 컨텍스트에 저장한 상태.(persist 메서드 사용)
   - 트랜잭션의 commit 호출 시 영속성 컨텍스트에 저장된 객체 쿼리가 호출됨

	``` java
		// 1. 비영속상태 - 객체를 생성한 상태
		Member member = new Member();
		member.setId(3L);
		member.setName("Tester3");
		
		// 2. 영속상태 - 영속성 컨텍스트에 저장한 상태
		em.persist(member)
	```
3) 준영속 상태
   - 영속성 컨텍스트에서 분리(detach)된 상태
	``` java
		// 1. 비영속상태 - 객체를 생성한 상태
		Member member = new Member();
		member.setId(3L);
		member.setName("Tester3");
		
		// 2. 영속상태 - 영속성 컨텍스트에 저장한 상태
		em.persist(member);
		
		// 3. 준영속상태 - 회원 엔티티를 영속성 컨텍스트에서 분리한 상태
		em.detach(member);
	```

## 3. 영속성 컨텍스트의 이점
1) 1차 캐시의 존재
   - @Id와 Entity로 구성됨.
   - find() 시 1차 캐시 내에서 선 조회 후 없을 시 DB에서 조회하여 1차캐시에 저장 후 로드.
   - DB와 어플리케이션 사이에 1차 캐시가 존재하여 동일성이(identity) 보장됨
   - 단, 영속성 컨텍스트는 엔티티 매니저에 있고, 하나의 트랜잭션이 끝나면 영속성 컨텍스트는 close 시키는게 일반적이므로 활용도가 낮음.
	```java
		Member member = new Member();
		member.setId(1L);
		member.setName("Creater");
			
		// 영속성 컨텍스트의 1차 캐시에 저장 (영속상태)
		em.persist(member); 
	
		// 1차 캐시에 저장 된 Member 조회하기에 Select 쿼리가 나가지 않음.
		Member findMember = em.find(Member.class, 1L); 

		//nsert 쿼리 실행
		tx.commit();
		
		// 엔티티 매니저 삭제
		em.close();

		// 새로운 엔티티 매니저 생성
		EntityManager em2 = emf.createEntityManager();

		// 1L에 대한 Member가 새롭게 생성된 영속성 컨텍스트의 1차 캐시에 존재하지 않아 Select 쿼리 실행. DB 조회 후 1차 캐시에 저장
		Member findMember1 = em2.find(Member.class, 1L);

		// 1L에 대한 Member가 1차 캐시에 존재하므로 Select 쿼리가 실행되지 않음.
		Member findMember2 = em2.find(Member.class, 1L);

		// 동일성 보장 : true
		System.out.println("동일성 보장 : "+(findMember1 == findMember2));
	```

2) 트랜잭션을 지원하는 쓰기 지연
   - em.persist(member) 호출 시 1차 캐시에 저장 후 Insert 쿼리 생성 후 쓰기지연 SQL 저장소에 저장함.
   - 트랜잭션 commit을 해야 쓰기 지연 SQL 저장소에 쌓인 쿼리를 호출함. commit 호출 시 내부 동작은 다음과 같다.
      1) 쓰기지연 SQL 저장소 flush (생성된 쿼리를 DB로 보내어 실행)
      2) 트랜잭션 commit
   - 쿼리를 모아 한번에 flush 할 수 있음

<br>

3) 변경 감지
   - 엔티티 매니저 내 영속성 컨텍스트에는 1차 캐시에는 Id와 Entity말고도 스냅샷이라는 속성이 있음. 스냅샷은 1차 캐시로 들어온 최초 상태를 스냅샷으로 떠놓은 것.
   - commit 메서드 호출 시 엔티티와 스냅샷을 비교 한 후 다를 경우 Update 쿼리를 생성하여 쓰기지연 SQL 저장소에 저장함.

	```java
		Member member = em.find(Member.class, 1L); // 1차 캐시 조회
		member.setName("ZZZZ"); // 데이터 수정

		// em.persist() //persist 생략 가능!

		tx.commit(); // Entity와 스냅샷이 다르므로 Update 쿼리 생성 후 쓰기지연 SQL에 저장 및 flush, commit
	```

<br>


# 5. 플러시

## 1. 플러시란?

- 영속성 컨텍스트의 변경 내용을 데이터베이스에 동기화(반영)하는 것.
- tx.commit() 시 플러시가 자동 호출된다.
- em.flush()로 플러시를 직접 호출한다.
- JPQL 쿼리 실행 시 플러시가 자동 호출된다.
- 1차 캐시를 비우는게 아님.

## 2. 플러시 발생시 일어나는 작업
1) 변경감지(더티체킹)
2) 수정된 엔티티에 대한 쿼리 생성 후 쓰기지연 SQL 저장소에 등록
3) 쓰기지연 SQL 저장소의 쿼리를 DB로 전송


	```java
		Member member = new Member();
		member.setId(1L);
		member.setName("Creater");
			
		// 영속성 컨텍스트의 1차 캐시에 저장 (영속상태)
		em.persist(member); 
	
		em.flush();
		System.out.println("flush 호출 시점에 insert 쿼리가 나간다. 단, 트랜잭션 commit을 하지 않으면 DB에 반영되지 않는다.");
		
		tx.commit();
		emf.close();

	```

# 6. 준영속 상태

## 1. 준영속 상태란

- 영속 상태에의 엔티티가 영속성 컨텍스트에서 분리(detached)된 상태
- em.detach(entity) ==> 특정 엔티티만 영속성 컨텍스트에서 분리
- em.clear() ==> 영속성 컨텍스트 자체를 초기화(1차 캐시, 쓰기지연 SQL 저장소도 초기화)
- em.close() ==> 영속성 컨텍스트를 종료시켜 삭제.
  
```java
	Member insertMember = new Member();
	insertMember.setId(1L);
	insertMember.setName("Test");
		
	// 영속성 컨텍스트의 1차 캐시에 저장 (영속상태)
	em.persist(insertMember);

	// 영속성 컨텍스트 내 insertMember 엔티티를 준영속상태로 변경
	em.detach(insertMember);
	System.out.println("더이상 영속성 컨텍스트 내에서 관리되지 않기 때문에 insert 쿼리가 나가지 않음.");
```

# 7. 객체와 테이블 매핑

## 1. 엔티티 매핑 소개

- 객체와 테이블 매핑 : @Entity, @Table
- 필드와 컬럼매핑 : @Column
- 기본키 매핑 : @Id
- 연관관계 매핑 : @ManyToOne, @JoinColumn

<br>

## 2. 객체와 테이블 매핑

- @Entity가 붙은 클래스는 JPA가 관리하는 '엔티티'라 칭한다.
- JPA를 통해 관리하는 테이블 클래스는 @Entity 가 필수이다.
- 기본 생성자는 반드시 필요하다
- final, enum, interface, inner 클래스를 사용하면 안된다.
- 저장할 필드에 final을 사용하면 안된다.

<br>

## 3. @Entity

- JPA가 관리하는 클래스. 엔티티라고 칭함.
- JPA를 사용해서 테이블과 매핑할 클래스는 @Entity가 필수
- 기본 생성자 필수

<br>

## 4. DB 스키마 자동 생성

- DDL을 어플리케이션 실행 시점에 자동 생성
- 개발 서버에서만 사용을 권장.
- 속성 키 : hibernate.hbm2ddl.auto
- 속성 값
  - create : 테이블 drop 후 create
  - create-drop : create와 같으나 어플리케이션 종료 시점에 테이블 drop
  - update : 변경분만 반영
  - validate : 엔티티와 현재 테이블이 정상 매핑됐는지 확인
  ```java
  	<property name="hibernate.hbm2ddl.auto" value="create"/>
  ```
- 데이터베이스 방언에 따라 컬럼 생성이 가능.   

<br>

# 8. 필드와 컬럼 매핑

## 1. 매핑 어노테이션
	
- @Column : 컬럼 매핑
- @Temporal : 날짜 매핑
- @Enumerated : enum 타입 매핑
- @Lob : BLOB, CLOB 매핑
    - CLOB 
        - 사이즈가 큰 데이터를 외부 파일로 저장하기 위한 데이터 타입   
	    - 문자열 데이터를 DB 외부에 저장하기 위한 타입   
		- CLOB의 최대 길이는 외부 저장소에서 생성 가능한 파일 크기   
		- SQL문에서 문자열 타입으로 표현. CHAR, VARCHAR 등과 호환
		- 문자형 대용량 파일을 저장하는 타입.

	- BLOB
		- 바이너리 데이터릴 DB 외부에 저장하기 위한 타입   
		- BLOB의 최대 길이는 외부 저장소에서 생성 가능한 파일 크기
		- SQL문에서 비트열 타입으로 표현. BIT, VIT VARYING 과 호환
		- 컴퓨터가 인식하는 모든 파일을 저장하는 타입.

- @Transient : 특정 필드를 컬럼에 매핑하지 않음(= 테이블 생성 시 해당 컬럼은 생성하지 않음)

<br>

## 2. @Column
- 컬럼을 매핑할 때 사용하는 어노테이션
- name : 필드와 매핑할 테이블의 컬럼 이름
- insertable, updatable : 등록, 변경 가능여부
- nullable(DDL) : null 값의 허용 여부를 설정. false시 not null 제약조건
- unique(DDL) : 유니크 제약조건 설정. 단, 일반적으로 유니크 제약조건은 @Table 의 속성으로 설정함.
- columnDefinition(DDL) : 데이터베이스 컬럼 정보를 직접 줄 수 있음.
	> ex) varchar(100) default 'empty'
- length : 문자 길이 제약조건

<br>

## 3. @Enumrated
- Java enum 타입을 매핑할 때 사용.
- ORDINAL과 STRING 속성이 존재 (기본 값 ORDINAL)
	- EnumType.ORDINAL : enum 순서를 DB에 저장   
	- EnumType.STRING : enum 이름을 데이터베이스에 저장.   
- ORDINAL은 사용하면 안됨. 추후 Enum 타입값이 중간에 추가될 경우 데이터가 꼬이게 됨.
```java
	// Member.class
	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	...

	// Main.class
	Member member = new Member();
	member.setId(1L);
	member.setUsername("A");
	member.setRoleType(RoleType.USER); 
	//DB에 USER 로 저장됨. 만약 @Enumerated(EnumType.ORDINAL) 일 경우 1로 저장됨  
	
	em.persist(member);
```

<br>    

## 4. @Temporal 속성
- 날짜 타입(java.util.Date, java.util.Calendar)을 매핑할 때 사용
- LocalDate, LocalDateTime을 사용할 때는 생략 가능(최신 하이버네이트 지원)
- TemporalType.Date : 날짜, 데이터베이스 date 타입과 매핑
- TemporalType.TIME : 시간, 데이터베이스 time 타입과 매핑
- TemporalType.TIMESTAMP : 날짜와 시간, 데이터베이스 timestemp 타입과 매핑

<br>

## 5. @Lob
- 매핑하는 필드 타입이 문자면 CLOB 매핑, 나머지는 BLOB 매핑.
- LOB에는 속성이 없음.

<br>

## 6. @Transient
- 테이블 필드와 매핑시키지 않도록 할때 사용
- 주로 메모리상에서만 임시로 값을 저장하고 싶을 때 사용

<br>


# 9. 기본키 매핑
## 1. 직접 할당
	- @Id 어노테이션 사용.

<br>

## 2. 값 자동생성
	- @GeneratedValue 어노테이션 사용.
	- 자동생성 전략
		> IDENTITY : 데이터베이스에 위임.   
		> SEQUNECE : 데이터베이스 시퀀스 오브젝트 사용. @SequenceGenerator  
		> TABLE : 키 생성용 테이블 사용. @TableGenerator  
		> AUTO : 방언에 따라 위 셋중에 하나 자동 지정. 기본값.

<br>

## 3. IDENTITY 전략
	- 기본키 생성을 데이터베이스에 위임
	- JPA는 보통 트랜잭션 커밋 시점에 INSERT SQL 실행되나 IDENTITY는 em.persist() 시점에 INSERT 쿼리가 실행된다. persist는 곧 영속성 컨텍스트에서 엔티티를 관리한다는 뜻이며, 1차 캐시에 @ID 값을 필수적으로 넣어야한다. 기본키 생성을 DB에 위임했기 때문에 INSERT 쿼리를 날려봐야 ID 값을 알 수 있으며, 이로 인해 이 전략만 persist() 시점에 INSERT 쿼리가 실행된다.

<br>

## 4. SEQUENCE 전략
	- ID 타입은 Long을 권장. 공간은 2배 차이가 나지만 서버의 성능차이는 거의 없음.
	- 시퀀스 이름을 지정하지 않을 경우 자체 제공되는 시퀀스를 사용.
	- em.persist() 호출 시점에 SQL을 통해 시퀀스 값을 조회하고, flush 시점에 INSERT 쿼리가 실행됨.
		> Q. 그럼 INSERT 할때마다 시퀀스를 조회하는 네트웤 통신이 있다는 건데, 이거 성능이슈 아닌가요?  

		> A. JPA에서는 이런 성능이슈를 최소화하기위해 @SequenceGenerator 옵션의 allocationSize를 제공합니다. 이는 시퀀스 조회 시 Size만큼의 값을 한번의 SQL로 조회 후 메모리에 할당합니다. 그리고 필요할때마다 DB 통신을 하는 것이 아닌 메모리에서 가져오게 됩니다. 기본값은 50입니다.  
		50이 넘게되면 다시 시퀀스 조회 DB 통신을 하게 됩니다.  
		
		> Q. 그럼 기본값을 최대한으로 늘리는 것이 좋지않나요? 왜 50인지 이해가 가지 않습니다.

		> A. 맞습니다. 하나의 엔티티 매니저에서 insert하는 로직이 많고, allocationSize 값이 적을수록 시퀀스 조회를 위한 DB 통신 횟수는 증가할 것입니다. 하지만 중간에 서버가 종료된다면 그 값만큼 시퀀스 값이 메모리에서 사라지게 될것이고, 다음 트랜잭션 발생 시 해당 값만큼 건너뛰게되니 비어있는 값이 발생하게 될것입니다. 서버가 다중화 되어있어도 마찬가지입니다. 이때 비어있는 값이 크다면 아무래도 이상하게 느껴지겠죠? 

<br>

## 5. TABLE 전략
	- 키 생성 전용 테이블을 하나 만들어서 데이터베이스 시퀀스를 흉내내는 전략
	- 단점으로는 테이블에서 키를 조회하는 것이기때문에 성능 이슈 가능성이 있음

<br>

## 6. 권장하는 식별자 전략
	- 비지니스와 관련된 데이터를 키로 사용하는 것은 권장하지 않음.
	- Long형 + 대체키 + 키 생성전략 사용
	- 주민등록번호도 기본키로 적절하지 않다.

<br>

## 7. ID를 Long으로 사용하는 이유
	- long 은 int 보다 표현범위가 훨씬 크다
    	- int : -2,147,483,648 ~ 2,147,483,647
    	- long : -9,223,372,036,854,775,808 ~ 9,223,372,036,854,775,807
	- Long은 Wrapper Type으로 Null를 가질 수 있으나, int, long과 같은 primitive Type은 null을 가질 수 없으며 기본 값이 0이다. 이 말은 곧 id 값이 실제로 0인지, 값이 없는건지 구분할 수 없다.
	- Wrapper Type인 Long이나 Integer는 id가 없는 경우엔 확실히 null 값이고, 그 자체로 id가 없다는 걸 보장할 수 있다.

# 10. 요구사항 분석과 기본 매핑
## 1. 요구사항 분석
	- 회원은 상품을 주문할 수 있다.
	- 주문 시 여러 상품을 선택할 수 있다.

<br>

## 2. 도메인 모델 분석
	- 회원과 주문의 관계 : 회원은 여러 번 주문할 수 있다. (일대다)
	- 주문과 상품의 관계 : 주문할 때 여러 상품을 선택할 수 있다. 반대로 같은 상품도 여러 번 주문될 수 있다. 주문상품 이라는 모델을 만들어서 다대다 관계를 일대다, 다대일 관계로 풀어냄.

<br>

## 3. 엔티티 모델 설계
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

<br>

## 4. 데이터 중심 설계의 문제점
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

<br>


# 11. 연관관계 매핑
## 1. 목표
   - 객체와 테이블 연관관계의 차이를 이해
   - 객체의 참조와 테이블의 외래 키를 매핑
   - 용어이해
		> 방향 : 단방향 양방향  
		> 다중성 : 일대다, 다대일, 일대일, 다대다의 이해  
		> __연관관계의 주인__ : 객체 양방향 연관관계는 관리 주인이 필요

<br>

## 2. 예제 사나리오
	- 회원과 팀이 있다.
	- 회원은 하나의 팀에만 소속될 수 있다.
	- 회원과 팀은 다대일 관계이다.

<br>

## 3. 모델링
	![team_member_relation](./team_member_relation.PNG)

<br>

## 4. 객체를 테이블에 맞추어 생성
   - 테이블은 외래 키로 조인을 사용해서 연관된 테이블을 찾는다.
   - 객체는 참조를 사용해서 연관된 객체를 찾는다.
   - 테이블과 객체 사이에는 이런 큰 간격이 있다.

<br>

## 5. 객체 지향 모델링 
	![team_member_relation2](./team_member_relation2.PNG)

	- Member와 Team은 일대다 관계이며 FK를 가진 Member 테이블이 연관관계의 주인이 된다.
	- Member 엔티티에 Team 필드 생성 후 다대일을 나타내는 @ManyToOne 을 붙여준다. 즉 Member 엔티티와 Team 엔티티는 다대일 관계라는 의미이다.
	- JoinColumn, 즉 FK Column 명을 넣어준다.

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

<br>

# 13. 양방향 연관관계와 연관관계의 주인
## 1. 양방향 매핑
	- 테이블 연관관계에서는 PK와 FK간 조인을 통해 양방향 연관관계를 맺을 수 있음. - 객체 연관관계에서는 키가 되는 컬럼으로 양방향 연관관계를 맺을 수 없음. 앞선 멤버와 팀 예제에서 멤버에서 팀으로 접근 가능하나, 팀에서 멤버로는 접근이 불가능함. 즉, 팀 입장에서 팀에 속한 멤버 리스트를 조회할 수 없는 상태임. 이를 위해 양방향 매핑을 사용해야 함. 
	- mappedBy 구문을 사용하면 됨.

```java
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

	/// getter, setter ...

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
	
	// 팀입장에서는 일대다 관계이며 연관관계 대상과 매핑되는 컬럼은 team
	@OneToMany(mappedBy="team")
	private List<Member> members = new ArrayList<Member>();

	/// getter, setter ...
	
}

public static void main(String[] args) {
		
		...
			
			Team team = new Team();
			team.setName("TeamA");
			em.persist(team);
			
			Member member = new Member();
			member.setUsername("member1");
			member.setTeam(team);
			em.persist(member);
	
			em.flush();
			em.clear();
			
			Member findMember = em.find(Member.class, member.getId());

			// Team에 대한 getMembers를 통해 팀에 속한 멤버리스트를 조회함.
			List<Member> members= findMember.getTeam().getMembers();
			
			for(Member m : members) {
				System.out.println(m.getUsername());
			}
		...
	}
```

## 2. 객체와 테이블이 관계를 맺는 차이

### 2.1. 객체 연관관계
	- 객체간 양방향 연관관계는 사실 두개의 단방향 연관관계이며, 객체를 양방향으로 참조하려면 단방향 연관관계를 2개 만들어야 함.
    	- Member > Team (단방향 연관관계 #1), Team > Member (단방향 연관관계 #2)

### 2.2. 테이블 연관관계
	- 테이블간 양방향 연관관계는 외래 키 하나로 가능함.
    	- Member <-> Team (양방향 연관관계)

### 2.3. 딜레마 발생
    - 멤버의 팀을 CUD하는 방법은 Member.setTeam과 같이 Member에서 접근하는 방법과 Team.setMembers와 같이 Team에서 접근하는 방법이 있다. 즉, FK가 2개.
    - 이렇게 FK가 두개가 되는 상황이 만들어지기에 JPA에서는 FK가 하나만 될 수 있도록 규칙을 정했다. 이 규칙이 바로 연관관계의 주인을 정하는 것임.

## 3. 연관관계의 주인
	- 객체의 두 관계중 하나를 연관관계의 주인으로 지정
	- 연관관계의 주인만이 외래 키를 관리(CUD)
	- 주인이 아닌쪽은 읽기만 가능. (mappedBy)

## 4. 누구를 주인으로?
	- 외래 키가 있는 곳을 주인으로 정함. Member와 Team 은 다대 일이며 FK는 Member에 있으므로 Member.team이 연관관계의 주인이 됨.
	- DB 테이블 기준으로 '다' 쪽이 연관관계의 주인 (FK를 갖는 곳)

## 5. 양방향 매핑 시 가장 많이 하는 실수

### 5.1. 연관관계의 주인에 값을 입력하지 않음.
	- add(member) 시 Insert 쿼리가 나가지 않음.

``` java
	Team team = new Team();
	team.setName("TeamA");
	em.persist(team);
			
	Member member = new Member();
	member.setUsername("member1");
	em.persist(member);
	
	em.flush();
	em.clear();
			
	// 연관관계의 따까리에게 insert하고 있으며, 이는 반영되지 않음.
	team.getMembers().add(member);
```

### 5.2. 연관관계의 따까리에도 값을 입력하지 않음.
	- 1차 캐시에 있는 데이터를 flush/clear 하지 않았을 때 연관관계의 주인에 값을 입력하면 제대로된 조회가 되지 않음.
``` java
	Team team = new Team();
	team.setName("TeamA");
	em.persist(team); // 1차 캐시에 team add
			
	Member member = new Member();
	member.setUsername("member1");
	member.setTeam(team);
	em.persist(member); // 1차 캐시에 member add
	
	// 현재 1차 캐시에 들어있는 team의 member들을 조회하기에 조회 데이터가 없음. 
	List<Member> members = team.getMembers();

```

	- flush/clear를 한다면 team.getMembers() 조회 시 DB에서 데이터를 가져오기에 정상적으로 조회됨.
	- 이러한 특이 케이스로 인한 혼동이 생길 우려가 있으므로 일반적으로 연관관계의 주인과 따까리에 모두 데이터를 insert해줘야함.

``` java
	Team team = new Team();
	team.setName("TeamA");
	em.persist(team); // 1차 캐시에 team add
			
	Member member = new Member();
	member.setUsername("member1");
	member.setTeam(team);
	em.persist(member); // 1차 캐시에 member add
	
	team.getMembers().add(member);

	List<Member> members = team.getMembers();

```
	- 결론적으로 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정해야함.
	- 양쪽에 설정하는 과정을 단순화하기 위해 연관관계 편의 메소드를 생성한다.

```java
class Member{
	
	...

	// setTeam을 할 경우 team.getMembers().add(this)를 통해 team의 members 에 추가하려는 현재 Member 객체를 add해준다.
	public void setTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}
}
```

## 5.2. 양방향 매핑 정리
	- 단방향 매핑만으로도 이미 연관관계 매핑은 완료
	- 양방향 매핑은 반대 방향으로 조회 기능이 추가된 것 뿐이기에 양방향은 필요할 때 추가해도 됨.
	- JPQL에서 역방향으로 탐색할 일이 많음

# 14. 연관관계 매핑 시작
## 1. 요건
	> 초간단 쇼핑몰 테이블 구성  
	> 사용자는 여러건의 주문을 할 수 있다.

## 2. 테이블 구조
   ![jap_shop_erd](./jpa_shop_erd.PNG)
   - 유저는 여러 주문을 하거나,주문을 하지 않을 수 있다. (one 대 many or zero)
   - 하나의 주문에 여러 상품이 포함될 수 있고, 하나의 상품은 여러 주문에 포함되거나 없을 수 있다.(many 대 many >> one 대 many, many or zero 대 one)

## 3. 객체구조
   - Member와 Order : 양방향 연관관계 매핑 (Member 내 orders)
   - Order와 OrderItem : 양방향 연관관계 매핑 (Order 내 orderItems)

## # 15. 연관관계
# 1. 연관관계 매핑 시 고려사항
	1) 다대일 : @ManyToOne
	2) 일대다 : @OneToMany
	3) 일대일 : @OneToOne

## 2. 단방향, 양방향
   1. 테이블 
      - 외래키 하나로 양쪽 조인 가능. 방향이라는 개념이 없음
   1. 객체 
      - 참조용 필드가 있는 쪽으로만 참조 가능
      - 한쪽만 참조하면 단방향
      - 양쪽이 서로 참조하면 양방향

## 3. 다대일 [N:1]
    - 멤버와 팀은 N : 1 관계
    - 멤버 객체가 연관관계의 주인

## 4. 다대일 양방향
   	- 팀 객체에 @OneToMany(mappedBy = "team")
	- 양쪽을 서로 참조하도록 개발

## 5. 일대일 [1:1]
   	- 일대일 관계는 그 반대도 일대일
   	- 주 테이블이나 대상 테이블 중 외래키 선택 가능
   	- 외래키에 유니크 제약주건 추가
  		> 유니크 제약조건 = 유일 키 제약조건  
		> 특정 컬럼에 대해 자료가 중복되지 않게 하는 것을 의미하며, 중ㅈ복은 허용하지 않으나, NULL은 중복될 수 있다.

## 6. 대다대 [N:N]
   	- 다대다는 사용하지 않고, 중간 엔티티를 생성 후 일대다, 다대일 연관관계 매핑을 해야함.


# 16. 상속관계 매핑
## 1. 정의
   - 객체의 상속 구조와 DB의 슈퍼타입 서브타입 관계를 매핑하는 것. RDB의 슈퍼타입 서브타입 관계라는 모델링 기법이 객체 상속과 유사함.
   - 상속관계 매핑 전략은 총 3가지로 조인전략, 단일테이블 전략, 구현클래스마다 테이블 전략이 있음.

## 2. 상속관계 매핑 전략
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
		  
## 3. 장단점
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
## 1. 정의
   - 공통적으로 사용할 속성들을 지정하는 클래스이며, 자식 클래스에게만 매핑 정보를 제공하는 클래스. 
   - 상속관계 매핑, 엔티티, 테이블과 매핑하는 클래스가 아님.
   - 직접 생성해서 사용할 일이 없으므로 추상 클래스로 생성 권장.
   
## 2. 기능
   - 테이블과 관계 없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할
   - 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용
   - 실무에서 BaseEntity로 활용

## 3. 참고
   - @Entity 클래스는 엔티티나 @MappedSuperclass로 지정한 클래스만 상속이 가능하다. 

# 18. 프록시
## 1. 프록시 기초
	- em.find() vs em.getReference()
	- em.find() : 데이터베이스를 통해 실제 엔티티 객체 조회
	- em.getReference() : 데이터베이스 조회를 미루는 가짜 엔티티(=프록시) 객체 조회

## 2. 프록시 객체의 초기화
``` java
	Member member = em.getReference(Member.class, id);
	member.getName();
```

	1) getReference를 통해 Member 프록시 객체를 생성함. 이때 Proxy의 target은 null.
	2) member.getName() 과 같이 프록시 객체의 메서드 호출 시 영속성 target에 대한 초기화를 영속성 컨텍스트로 요청
	3) 영속성 컨텍스트에서 DB를 조회하여 Entity를 생성
	4) target을 해당 Entity로 초기화
	5) target에 대한 getName()을 호출하여 값을 조회함.


## 3. 프록시 특징
	- 실제 클래스를 상속 받아서 만들어짐
	- 실제 클래스와 겉 모양이 같다.
	- 사용하는 입장에서 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 됨.
	- 프록시 객체는 처음 사용할 때 한번만 초기화
	- 프록시 객체는 실제 객체의 참조를 보관
	- 초기화 시 프록시 객체가 실제 엔티티로 바뀌는게 아님. 프록시 객체를 통해 실제 엔티티에 접근이 가능한 구조임.
	- 프록시 객체는 원본 엔티티를 상속받음. 따라서 타입 체크시 주의( '==' 연산 사용할 시 비교 실패. instance of 사용해야함)
	- 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티 반환
	- 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제 발생



## 4. 프록시 확인
	- 프록시 인스턴스의 초기화 여부 확인
		- PersistenceUnitUtil.isLoaded(Object entity)

# 19. 즉시 로딩과 지연 로딩

## 1. 지연로딩
	- 지연로딩 LAZY를 사용할 경우 해당 객체를 프록시로 조회함
	- 프록시 메서드를 호출할때 초기화가 일어남
	- 비지니스 로직상 엔티티 내 특정 객체를 같이 조회하지 않은 경우가 많을 경우 지연로딩을 사용한다. 
		ex) Member를 조회할 때 Team을 같이 조회하지 않는 케이스가 많다.

``` java

public class Member {

	@Id @GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;
	
	@Column(name = "USERNAME")
	private String username;

	@ManyToOne(fetch = FetchType.LAZY) // 지연로딩 LAZY
	@JoinColumn(name = "TEAM_ID")
	private Team team;
    ...
}

...
	Member member1 = new Member();
	member1.setUsername("member1");
	em.persist(member1);
			
	Team team = new Team();
	team.setName("TEamA");
	em.persist(team);
			
	member1.setTeam(team);
			
	em.flush();
	em.clear();
			
	Member refMember = em.find(Member.class, member1.getId());
	System.out.println(refMember.getTeam().getClass()); // Team 은 프록시 타입
```
## 2. 즉시로딩
	- 즉시로딩 EAGER를 사용할 경우 프록시를 사용하지 않음
	- 비지니스 로직 상 엔티티 내 특정 객체와 함께 조회하는 경우가 많을 경우 즉시로딩을 사용한다.
		ex) Member를 조회할 때 Team을 거의 항상 같이 조회한다.

``` java
	public class Member {

		@Id @GeneratedValue
		@Column(name = "MEMBER_ID")
		private Long id;
		
		@Column(name = "USERNAME")
		private String username;

		@ManyToOne(fetch = FetchType.EAGER) // 지연로딩 LAZY
		@JoinColumn(name = "TEAM_ID")
		private Team team;
		...
	}
```

## 3. 프록시와 즉시로딩 주의
  - 가급적 지연 로딩만 사용
  - 즉시 로딩을 적용하면 예상치 못한 에러가 발생할 수 있음.
	> 테이블 조인으로 인한 성능저하

  - 즉시 로딩은 JPQL에서 N+1 문제를 일으킨다.
  - @ManyToOne, @OneToOne은 기본이 즉시 로딩 -> LAZY로 설정해야함
  - @OneToMany, @ManyToMany는 기본이 지연 로딩
  - 실무에서는 기본으로 LAZY로 하고 즉시로딩이 필요할 경우 JPQL에서 fetch 조인을 사용해야함.

## 4. 지연 로딩 활용 - 실무
	- 모든 연관관계에 지연 로딩을 사용해라
	- 실무에서 즉시 로딩을 사용하지 마라
	- 즉시로딩이 필요할 경우 JPQL fetch 조인이나 엔티티 그래프 기능을 사용해라.

# 20. 영속성 전이 CASCADE

## 1. 영속성 전이란
	- 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속상태로 만들고 싶을때 사용
	- ex) 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장

``` java

@Entity
public class Parent {

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@OneToMany(mappedBy = "parent")
	private List<Child> childList = new ArrayList<Child>();

	public void addChild(Child child) {
		childList.add(child);
		child.setParent(this);
	}
	public Long getId() {
		return id;
	}
	...
}


@Entity
public class Child {

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Parent parent;

	...
}


	Parent parent = new Parent();
		
	Child child1 = new Child();
	Child child2 = new Child();
		
	parent.addChild(child1);
	parent.addChild(child2);
			
	
	em.persist(parent);
	em.persist(child1);
	em.persist(child2);

```
    - 각각 persist를 해야 3개의 insert 쿼리가 나감.
	- 만약 em.persis(child1)을 주석처리한다면 child1에 대한 insert쿼리가 나가지 않음.
	- 이를 쉽게 하기 위해 부모 엔티티인 parent만 persist하면 관련된 자식 엔티티도 모두 persist 되게 하는 것이 영속성 전이이며, cascade = CascadeType.ALL 구문을 사용하여 처리 가능

``` java
@Entity
public class Parent {

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private List<Child> childList = new ArrayList<Child>();

	public void addChild(Child child) {
		childList.add(child);
		child.setParent(this);
	}
}
```

## 2. 영속성 전이 주의사항
	- 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음
	- 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공
	- 소유자가 하나일 때만 사용가능. 자식 엔티티가 다른 엔티티와 연관관계가 있을 경우 사용은 지양. 단일 엔티티에 종속정일때만 사용


## 3. CASCADE의 종류
	- 종류는 여러개가 있으나 거의 아래 두 개만 사용함
	- ALL : 모두적용
	- PERSIST : 영속

# 21. 고아객체
## 1. 고아객체란

	- 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
	- orphanRemoval = true

## 2. 고아객체 주의사항
	- 참조하는 곳이 하나일 때 사용해애함
	- 부모를 제거하면 자식도 제거됨.

## 3. 영속성 전이 + 고아 객체, 생명주기
	- CascadeType.ALL + orphanRemovel = true
	- 스스로 생명주기를 관리하는 엔티티는 em.persist()로 영속화, em.remove()로 제거
	- 두 옵션을 모두 활성화 하면 부모 엔티티를 통해서 자식의 생명 주기를 관리할 수 있음.

## 22. 글로벌 페치 전략 및 영속성 전이 설정
	- 모든 연관관계를 지연로딩으로 설정
	- @ManyToOne, @OneToOne은 기본이 즉시 로딩이므로 지연 로딩으로 변경
	- Order -> Delivery를 영속성 전이 ALL 설정
	- Order -> OrderItem을 영속성 전이 ALL 설정

# 23. 데이터 타입
	- JPA는 데이터 타입을 크게 2 가지로 분류함.
	1) 엔티티 타입
		- @Entity로 정의하는 객체
		- 데이터가 변해도 식별자로 지속해서 추적 가능
		- 예) 회원 엔티티의 키나 나이 값을 변경해도 식별자로 인식 가능


	2) 값 타입
		- int, Integer, String처럼 단순히 값으로 사용하는 자바 기본 타입이나 객체
		- 식별자가 없고 값만 있으므로 변경 시 추적 불가
		- 예) 숫자 100을 200으로 변경하면 완전히 다른 값으로 대체

## 1. 기본 값 타입
	- 생명주기를 엔티티에 의존
		ex) 회원을 삭제하면 이름, 나이 필드도 함께 삭제

	- 값 타입은 공유하면 안됨
		ex) 회원 이름 변경 시 다른 회원의 이름도 함께 변경되면 안됨
	
	- int, double 같은 피리미티브 타입은 절대 공유가 안됨
	- 기본 타입은 항상 값을 복사함
	- Integer같은 래퍼 클래스나 String 같은 특수한 클래스는 공유 가능한 객체이지만 변경할 수 있는 메서드가 없으므로 변경 자체가 안됨.
	- 즉, 기본 값을 사용하면 변경으로부터 안전함.

## 2. 임베디드 타입
	- 새로운 값 타입을 직접 정의할 수 있음
	- JPA는 임베디드 타입이라 함
	- 주로 기본 값 타입을 모아서 만들어서 복합 값 타입이라고도 함
	- int, String과 같은 '값 타입'
  
## 3. 임베디드 타입 사용법
	- @Embeddable : 값 타입을 정의하는 곳에 표시
	- @Embedded : 값 타입을 사용하는 곳에 표시
	- 기본 생성자 필수


## 4. 임베디드 타입의 장점
	- 재사용 가능
	- 높은 응집도
	- 해당 값 타입만 사용하는 의미있는 메서드를 만들 수 있음
	- 엔티티에 생명주기를 의존함 (값 타입이기 때문)

## 5. 임베디드 타입과 테이블 매핑
	- 임베디드 타입은 엔티티의 값일 뿐임
	- 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같음
	- 객체와 테이블을 세밀하게(fin-grained) 매핑하는 것이 가능
	- 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많음

# 24. 값 타입과 불변 객체
## 1. 값 타입 공유 참조
	- 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험함.
	- 사이드 이펙트 발생 확률이 올라감.
	- 이로 인해 값 타입 복사를 사용해야함

## 2. 값 타입 복사
    - 값 타입의 실제 인스턴스 값을 공유하는 것은 위험
    - 대신 값(인스턴스)를 복사해서 사용

## 3. 객체 타입의 한계
	- 항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 사이드 이펙트를 피할 수 있음.
	- 문제는 임베디드 타입처럼 직접 정의한 값 타입은 자바의 기본 타입이 아니라 객체 타입임.
	- 자바 기본 타입에 값을 대입하면 복사되나, 객체 타입은 참조 값을 대입하는 것이고 이를 문법적으로 막을 수 없음.
	- 즉, 객체의 공유 참조는 100% 피할 수 없음

## 4. 불변객체
	- 객체 타입을 수정할 수 없게 만들면 사이드 이벡트를 원천 차단 가능
	- 값 타입은 불변 객체로 설계해야함
	- 불변객체란 생성 시점 이후 절대 값을 변경할 수 없는 객체
	- 생성자로만 값을 설정하고 수정자(Setter)를 만들지 않으면 됨.
	- 참고로 Interger, String은 자바에서 제공하는 대표적인 불변객체임
	- 불변 객체 내 값을 수정하려면 객체를 새로 만들어서 Set해줘야함

## 5. 결론
	- 값 타입은 불변으로 만들어라.

# 25. 값 타입 비교
	- 값 타입은 인스턴스가 달라도 그 안에 값이 같으면 같은 것으로 봐야 함.
    - 동일성 비교는 인스턴스의 참조 값을 비교('==' 연산)
    - 동등성 비교는 인스턴스의 값을 비교 (equals 메서드)
    - 값 타입은 동일성 비교가 아닌 동등성 비교를 사용해야 하며, 객체의 경우 equals 메서드를 Java에서 제공하는 재정의 메서드로 재정의 해야 함. 
    - 

``` java
	int a = 10;
	int b = 10;
	// a == b : true

	Address adrs1 = new Address("서울시");
	Address adrs2 = new Address("서울시");
	// adrs1 == adrs2 : false (동일성 비교)
	// adrs1.equals(adrs2) : true (단, equals 메서드를 재정의했다고 가정)

```

# 26. 값 타입 컬렉션
## 1. 값 타입 컬렉션이란
	- 값 타입을 저장하는 컬렉션 객체
	- 값 타입을 하나 이상 저장하려면 컬렉션에 보관하고 @ElementCollection, @CollectionTable 어노테이션을 사용하면 됨.
	- 값 타입 컬렉션은 영속성 전이(Cascade) + 고아객체 제거(Oprhan remove) 기능을 필수적으로 가진다고 볼 수 있다.
	- 페치 전략은 LAZY가 기본 적용된다.


``` java
	@Entity
	public class Member{

		...

		@ElementCollection
		@CollectionTable(name = "FAVORITE_FOODS"
			,joinColumns = @JoinColumn(name = "MEMBER_ID"))
		@Column(name = "FOOD_NAME")
		private Set<String> favoriteFoods = new HashSet<String>();

		...
	}
```

## 2. 값 타입 컬렉션의 제약사항
	- 값 타입은 식별자 개념이 없어 원본 데이터를 쉽게 찾아서 변경할 수 없음
	- 이로 인해 값 타입 컬렉션에 변경 사항이 발생하면, 값 타입 컬렉션이 매핑된 테이블의 연관된 모든 데이터를 삭제하고, 현재 값 타입 컬렉션 객체에 있는 모든 값을 테이블에 다시 저장한다.
	- 실무에서는 값 타입 컬렉션이 매핑된 테이블에 데이터가 많다면 값 타입 컬렉션 대신 일대다 관계를 고려해야 한다.

# 27. JPQL

## 1. 특징
	- JPQL은 객체지향 쿼리 언어이다. 따라서 테이블을 대상으로 쿼리하는 게 아니라 엔티티를 대상으로 쿼리한다.
	- JPQL은 SQL을 추상화해서 특정 데이터베이스 SQL에 의존하지 않는다.
	- JPQL은 결국 SQL로 변환된다.

## 2. JPQL 문법
	- Select 문법은 일반 쿼리와 동일하다.
	- Update, Delete는 벌크연산으로 처리된다.
	- 엔티티와 속성은 대소문자를 구문하므로 객체와 동일한 이름으로 사용해야한다.
	- SELECT, FROM, WHERE 과 같은 JPQL 키워드는 대소문자를 구분하지 않는다.
	- JPQL에는 테이블의 이름이 아닌 엔티티 이름을 사용한다.
	- 별칭이 필수이다(m)
		ex) select m from Member as m 

## 3. TypeQuery와 Query
	- TypeQuerty는 반환 값이 명확할 때, Query는 불명확할때 사용한다.
``` java
	// 반환 타입이 Member 클래스로 명확
	TypedQuery<Member> typedQuery = 
		em.createQuery("select m from Member m", Member.class);
    
	// 반환 타입이 username, id로 불명확
	Query query = 
		em.createQuery("select m.username, m.id from Member m");
		
```

## 4. 결과 조회 API
	- query.getResultList() : 결과가 하나 이상일 때 리스트를 반환하고, 결과가 없을 시 빈 리스트를 반환한다.

	- query.getSingleResult() : 결과가 정확히 하나일 때 단일 객체를 반환하고, 결과가 없으면 NoResultException, 결과가 둘 이상이면 NonUniqueResultException을 예외를 발생시킨다.

	- getSingleResult를 사용하면 예외가 발생할 수 있기에 try, catch를 통해 예외 핸들링을 해줘야한다. Spring Data JPA에서는 값이 없을 경우 예외를 발생시키는 부분을 개선하였으며 null 혹은 Optional 객체를 리턴하도록 구현되어 있다.

``` java
	TypedQuery<Member> list = 
		em.createQuery("select m from Member m", Member.class);

	// 결과가 하나 이상일 것을 예상하여 getResultList를 통해 반환받음
	List<Member> memberList = list.getResultList();
    

	TypedQuery<Member> single = 
		em.createQuery("select m from Member m where m.id = 1L", Member.class);

	// 결과가 하나일 것을 예상하여 getSingleResult를 통해 반환받음.
	// 만약 결과가 2개 이상이거나 없을 경우 예외가 발생함.
    Member singleMember = single.getSingleResult();
```

## 5. 파라미터 바인딩
	- 이름 기준과 위치 기준으로 바인딩하는 방법이 있다.
	- 이름 기준으로 사용 시 쿼리에 ":" 구문을 사용하며, 위치 기준으로 사용 시 "?번호"를 사용한다.
	- 위치 기반은 중간에 조건이 하나 더 추가되면 번호가 밀려 에러를 유발수 있으므로 이름 기준으로 사용하는 것이 권장된다.
	- 메서드 체이닝을 사용하면 한번에 처리 가능하며, 일반적으로 메서드 체이닝을 통해 사용함.

``` java
	// 이름 기준 파라미터 바인딩
	TypedQuery<Member> query 
		= em.createQuery("select m from Member m where m.id = :id and m.username = :username", Member.class);
    query.setParameter("username", "심승경");
    query.setParameter("id", 1L);

    Member singleMember = query.getSingleResult();

	// 메서드 체이닝 방식
	Member singleResult 
		= em.createQuery("select m from Member m where m.id = :id and m.username = :username", Member.class)
            .setParameter("username", "심승경")
            .setParameter("id", 1L)
            .getSingleResult();
	
	// 위치 기준 파라미터 바인딩
	singleResult = 
		em.createQuery("select m from Member m where m.id = ?1 and m.username = ?2", Member.class)
            .setParameter(1, 1L)
            .setParameter(2, "심승경")
            .getSingleResult();
```
<br>

# 28. 프로젝션
## 1. 정의
	- SELECT 절에 조회할 대상을 지정하는 것.
	- 조회된 대상은 모두 영속성 컨텍스트에서 관리된다.

## 2. 조회할 대상
	- 조회할 대상( = 프로젝션 대상)은 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자 등 기본 데이터 타입)이 있음.

``` java

	SELECT m FROM Member m // Member 엔티티를 조회하는 엔티티 프로젝션

	SELEECT m.team FROM Member m // Member 엔티티와 관계를 맺고 있는 Team 엔티티를 조회하는 엔티티 프로젝션

	SELECT m.address FROM Member m // Member 엔티티에 임베디드 타입인 address를 조회하는 임베디드 타입 프로젝션

	SELECT m.username, m.age FROM Member m // 기본 데이터 타입들을 조회하는 스칼라 타입 프로젝션
```

## 3. 여러 값 조회
	- 한 로우에 여러 값을 조회하면 반환 값이 명확하지 않다는 뜻이다. 즉 기본적으로 Query 타입으로 조회될 것이며, 이때 여러 로우를 조회하는 getResultList 사용 시에는 결과를 ArrayList<Object> 형태로 리턴받고, getSingleResult 사용 시에는 결과를 Object 형태로 리턴받는다. 
![Query 타입의 결과형태](https://user-images.githubusercontent.com/9374562/231656420-81069621-df62-4668-b0ad-45f38455fad3.jpg)
	
	- 이때 조회한 로우의 속성들을 각각 조회하고 싶다면 Object를 Object[] 로 캐스팅해야 한다.
  
``` java
	// Query 타입 조회 방법
	Object result2 = 
		em.createQuery("select m.username, m.id from Member m where m.id = 1L").getSingleResult();

	Object[] objects = (Object[])result2;
	System.out.println(objects[0]); // username
    System.out.println(objects[1]); // id
```
	- 이러한 메커니즘때문에 여러 값을 조회하는 방법은 
	1) 앞서 언급한 Query 타입으로 조회하는 방법
	2) 반환 값을 Object[]로 받는 TypedQuery 타입으로 조회하는 방법
	3) new 명령어로 조회하는 방법이 있다.

``` java
	// TypedQuery 타입 조회 방법 == Object[] 타입 조회
	Object[] result3 = 
		em.createQuery("select m.username, m.id from Member m where m.id = 1L",Object[].class).getSingleResult();
        System.out.println(result3[0]); // username
        System.out.println(result3[1]); // id
```
	- TypedQuery를 통해 Object[].class로 조회 시 따로 캐스팅하는 코드를 작성하지 않아도 된다는 장점이 있다.

``` java
	// new 생성자를 통한 조회
	MemberDto result3 = 
		em.createQuery("select new jqpl.MemberDto(m.id, m.username) from Member m where m.id = 1L",MemberDto.class).getSingleResult();
        System.out.println(result3.getId()); // username
        System.out.println(result3.getUsername()); // id

	
	...
	// MemberDto.java
	public class MemberDto {

    	private Long id;

    	private String username;

    	public MemberDto(Long id, String username) {
        	this.id = id;
        	this.username = username;
    	}
		...
		// getter, setter
	}

```
	- new 생성자를 사용할 경우 다음과 같이 MemberDto 클래스를 생성하고 JPQL에서 조회할 형태에 맞는 생성자를 생성해줘야한다.
	- 세 가지 방법 중 가장 깔끔하나 JPQL에서 사용 시 풀 패키지 경로를 넣어야한다는 단점이 있다. 하지만 쿼리 DSL에서 극복되었으므로 이 방식을 사용하는 것이 권장된다. 

# 29. 페이징 API

## 1. 개요
	- JPA는 페이징을 다음 두 API로 추상화한다.
	- setFirstResult(int startPosition) : 조회 시작 위치
	- setMaxResults(int maxResult) : 조회할 데이터 수
	- 실제 사용되는 쿼리는 설정한 Database 방언에 맞게 실행된다.

## 2. 예제

``` java
	for(int i =0 ; i< 100 ; i++){
        Member member = new Member();
        member.setUsername("멤버"+i);
        member.setAge(i);
        em.persist(member);
    }

	// 0번째부터 시작해서 10개의 데이터를 조회한다.
    List<Member> list = 
		em.createQuery("select m from Member m order by m.age asc",Member.class)
            .setFirstResult(0)
            .setMaxResults(10)
            .getResultList();

    for(Member member : list){
        System.out.println(member.toString());
    }

	//실행결과
	Member{id=1, username='멤버0', age=0}
	Member{id=2, username='멤버1', age=1}
	Member{id=3, username='멤버2', age=2}
	Member{id=4, username='멤버3', age=3}
	Member{id=5, username='멤버4', age=4}
	Member{id=6, username='멤버5', age=5}
	Member{id=7, username='멤버6', age=6}
	Member{id=8, username='멤버7', age=7}
	Member{id=9, username='멤버8', age=8}
	Member{id=10, username='멤버9', age=9}
```

# 30. 조인

## 1. 개요
	- JPQL에서 사용하는 조인은 내부조인, 외부조인, 세타 조인이 있다.
  
## 2. 내부조인
	- 연관 관계가 맺어진 엔티티들에 대한 Inner 조인을 말하며 INNER JOIN의 INNER는 생략 가능하다.
``` java
	SELECT m FROM Member m [INNER] JOIN m.team t
```

## 3. 외부조인
	- 연관 관계가 맺어진 엔티티들에 대한 Left Outer 조인을 말하며 LEFT OUTER JOIN의 OUTER는 생략 가능하다.
``` java
	SELECT m FROM Member m LEFT [OUTER] JOIN m.team t
```

## 4. 세타 조인
	- 연관 관계와 상관없이 모든 엔티티들에 대한 조인을 말한다.
``` java
	SELECT count(m) FROM Member m, Team t WHERE m.username = t.name
```

## 5. 조인 예제
	- 테스트를 위해 teamA와 teamB를 생성하고 멤버 0부터 9까지는 teamA, 멤버 10부터 19까지는 teamB에 속하도록 하고, 멤버 20부터 29까지는 팀이 없도록 셋팅했다. 코드는 아래와 같다.
``` java

	Team teamA = new Team();
    teamA.setName("teamA");
    em.persist(teamA);

    Team teamB = new Team();
    teamB.setName("teamB");
    em.persist(teamB);

    for(int i =0 ; i< 10 ; i++){
        Member member = new Member();
        member.setUsername("멤버"+i);
        member.setAge(i);
        member.changeTeam(teamA);
        em.persist(member);
    }

    for(int i =10 ; i< 20 ; i++){
        Member member = new Member();
        member.setUsername("멤버"+i);
        member.setAge(i);
        member.changeTeam(teamB);
        em.persist(member);
    }

    for(int i =20 ; i< 30 ; i++){
        Member member = new Member();
        member.setUsername("멤버"+i);
        member.setAge(i);
        em.persist(member);
    }
```

	- Inner join JPQL 실행 시 team이 존재하는 Member 정보만을 리턴한다.
``` java
	String innerJoinQuery = "select m from Member m inner join m.team t";
    List<Member> list = em.createQuery(innerJoinQuery,Member.class)
    	.getResultList();

    for(Member member : list){
        System.out.println(member.toString());
        System.out.println(member.getTeam());
    }

	// 출력 결과
	Member{id=3, username='멤버0', age=0}
	Team{id=1, name='teamA'}
	Member{id=4, username='멤버1', age=1}
	Team{id=1, name='teamA'}
	...
	Member{id=21, username='멤버18', age=18}
	Team{id=2, name='teamB'}
	Member{id=22, username='멤버19', age=19}
	Team{id=2, name='teamB'}
```

	- Left Join JPQL 실행 시 team이 존재하지 않는 Member 정보도 함께 리턴하며 team은 null로 리턴한다.

``` java
 	String leftJoinQuery = "select m from Member m left join m.team t";
    List<Member> list2 = em.createQuery(leftJoinQuery,Member.class)
        .getResultList();

    for(Member member : list2){
        System.out.println(member.toString());
        System.out.println(member.getTeam());
    }

	// 출력 결과
	Member{id=3, username='멤버0', age=0}
	Team{id=1, name='teamA'}
	Member{id=4, username='멤버1', age=1}
	Team{id=1, name='teamA'}
	...
	Member{id=31, username='멤버28', age=28}
	null
	Member{id=32, username='멤버29', age=29}
	null
```

	- 세타 조인을 위해 member 이름을 TeamA로 하나 생성한 후 테스트를 진행하였다. 쿼리 확인 결과 두 테이블을 크로스 조인 후 조건에 해당하는 값만 조회한다.
``` java
	Member thetaMember = new Member();
    thetaMember.setUsername("teamA");
    thetaMember.changeTeam(teamA);
    em.persist(thetaMember);

    String thetaJoinQuery = "select m from Member m, Team t where m.username = t.name";
    List<Member> list3 = em.createQuery(thetaJoinQuery,Member.class)
        .getResultList();

    for(Member member : list3){
        System.out.println(member.toString());
        System.out.println(member.getTeam());
    }

	// 출력 결과
	Member{id=33, username='teamA', age=0}
	Team{id=1, name='teamA'}

```

## 6. 조인 대상 필터링
	- SQL에서 사용하던 on과 동일하게 사용하면 된다.
	- 내부조인 및 외부조인도 동일한 방식으로 사용 가능하다.
``` java
	// 내부 조인에 대한 필터링 - 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인
	JPQL : SELECT m, t FROM Member m LEFT JOIN m.team t ON t.name = 'A'
	SQL : SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.team_id = t.id and t.name = 'A'

	// 외부 조인에 대한 필터링 - 회원의 이름과 팀 이름이 같은 대상 외부조인
	JPQL : SELECT m, t FROM Member m LEFT JOIN Team t ON m.username = t.name
	SQL : SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.username = t.name
```

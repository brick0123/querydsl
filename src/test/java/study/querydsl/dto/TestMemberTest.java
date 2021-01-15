package study.querydsl.dto;

import static study.querydsl.dto.QTestMember.testMember;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;
import study.querydsl.entity.TestDto;

@SpringBootTest
@Transactional
class TestMemberTest {

  @Autowired
  EntityManager em;

  JPAQueryFactory queryFactory;

  @Disabled
  @BeforeEach
  void setup() {
    queryFactory = new JPAQueryFactory(em);
    em.persist(new TestMember("member1", "team1", 10));
    em.persist(new TestMember("member2", "team2", 10));
    em.persist(new TestMember("member3", "team3", 10));
    em.persist(new TestMember("member4", "team4", 10));
  }

  @Test
  void map() {
    List<TestMember> result = queryFactory
        .selectFrom(testMember)
        .fetch();

    result.stream().map(TestDto::new).collect(Collectors.toList()).forEach(System.out::println);

  }

  @Test
  void setterTest() {
    List<TestDto> result = queryFactory
        .select(
            Projections.bean(TestDto.class,
                testMember.name,
                testMember.teamName,
                testMember.age
            ))
        .from(testMember)
        .fetch();

    for (TestDto testDto : result) {
      System.out.println("testDto = " + testDto);
    }
  }

  @Test
  void fieldTest() {
    List<TestDto> result = queryFactory
        .select(
            Projections.fields(TestDto.class,
                testMember.teamName,
                testMember.name,
                testMember.age
            ))
        .from(testMember)
        .fetch();

    for (TestDto testDto : result) {
      System.out.println("testDto = " + testDto);
    }
  }

  @Test
  void test() {
    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");
    em.persist(teamA);
    em.persist(teamB);

    Member member1 = new Member("member1", 10, teamA);
    Member member2 = new Member("member2", 20, teamA);
    Member member3 = new Member("member3", 30, teamB);
    Member member4 = new Member("member4", 40, teamB);

    em.persist(member1);
    em.persist(member2);
    em.persist(member3);
    em.persist(member4);

    List<Tuple> fetch = queryFactory.select(member, team.name)
        .from(member)
        .join(member.team, team)
//        .where(team.name.eq("teamA").or(team.name.eq("teamB")))
        .where(team.name.eq("teamA"))
        .fetch();

    for (Tuple tuple : fetch) {
      System.out.println("### tuple = " + tuple);
    }
  }


}
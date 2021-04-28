package study.querydsl;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

@Slf4j
@SpringBootTest
@Transactional
@Rollback(value = false)
public class QueryTest {

  @Autowired
  EntityManager em;

  JPAQueryFactory queryFactory;

  @BeforeEach
  public void setup() {
    queryFactory = new JPAQueryFactory(em);
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
    em.flush();
    em.clear();
  }

  @Test
  @DisplayName("일대다 N+1 발생시키기")
  void oneToMany() {
    List<Team> teams = queryFactory
        .selectFrom(team)
        .fetch();

    for (Team team : teams) {
      List<Member> members = team.getMembers();

      members.forEach(mem -> log.info(">>> age ={}", mem.getAge()));
    }
  }

  @Test
  @DisplayName("일대다 페치 조인 데이터 뻥튀기 발생시키기")
  void oneToMany2() {
    List<Team> result = queryFactory
        .selectFrom(team)
        .join(team.members, member).fetchJoin()
        .where(team.name.eq("teamA"))
        .fetch();

    for (Team team : result) {
      log.info("team ={}, member ={}", team.getName(), team.getMembers().size());
    }

    assertThat(result.size()).isEqualTo(2);
  }

}

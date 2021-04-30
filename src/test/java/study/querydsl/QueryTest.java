package study.querydsl;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberResponseDto;
import study.querydsl.dto.TeamResponseDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

@Slf4j
@SpringBootTest
@Transactional
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

  @Test
  @DisplayName("일대다 관계에서 entity를 dto로 파싱해서 응답한다")
  void entityToDto() {
    List<Team> teams = queryFactory
        .selectFrom(team)
        .fetch();

    List<TeamResponseDto> teamResponseDtoList = teams.stream()
        .map(TeamResponseDto::new)
        .collect(Collectors.toList());

    for (TeamResponseDto teamResponseDto : teamResponseDtoList) {
      log.info(">>> team = {}", teamResponseDto);
    }

    assertThat(teamResponseDtoList.size()).isEqualTo(2);
    assertThat(teamResponseDtoList.get(0).getMembers().size()).isEqualTo(2);
  }

  @Test
  @DisplayName("일대다 관계에서 entity를 dto로 직접 조회해서 응답한다")
  void entityToDtoV2() {
    List<TeamResponseDto> teams = queryFactory
        .select(Projections.fields(TeamResponseDto.class,
            QTeam.team.id,
            QTeam.team.name
        ))
        .from(team)
        .fetch();

    List<Long> teamIds = toTeamIds(teams);

    log.info(">>> teamsId ={}", teamIds);

    List<MemberResponseDto> members = queryFactory
        .select(Projections.fields(MemberResponseDto.class,
            member.id,
            member.team.id.as("teamId"),
            member.username
        ))
        .from(member)
        .where(member.team.id.in(teamIds))
        .fetch();

    Map<Long, List<MemberResponseDto>> memberMap = members.stream()
        .collect(Collectors.groupingBy(MemberResponseDto::getTeamId));


    teams.forEach(t -> t.setMembers(memberMap.get(t.getId())));

    assertThat(teams.size()).isEqualTo(2);
    assertThat(teams.get(0).getMembers().size()).isEqualTo(2);
    assertThat(teams.get(1).getMembers().size()).isEqualTo(2);
  }

  private List<Long> toTeamIds(List<TeamResponseDto> teamResponseDtoList) {
    return teamResponseDtoList.stream()
        .map(TeamResponseDto::getId)
        .collect(Collectors.toList());
  }
}
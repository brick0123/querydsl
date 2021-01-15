package study.querydsl.controller;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {

  private final InitMemberService initMemberService;

//  @Transactional // 두개를 함께 못 쓰기 때문에 아래와 같은 방식으로
 @PostConstruct
  public void init() {
    initMemberService.init();
  }

  @Component
  static class InitMemberService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void init() {
      Team teamA = new Team("teamA");
      Team teamB = new Team("teamB");
      em.persist(teamA);
      em.persist(teamB);

      for (int i = 0; i < 100; i++) {
        Team selectTeam = i % 2 == 0 ? teamA : teamB;
        em.persist(new Member("member"+i, i, selectTeam));
      }
    }
  }

}

package study.querydsl.dto;

import static org.junit.jupiter.api.Assertions.*;
import static study.querydsl.dto.QTestMember.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.QMember;
import study.querydsl.entity.TestDto;

@SpringBootTest
@Transactional
class TestMemberTest {

  @Autowired
  EntityManager em;

  JPAQueryFactory queryFactory;


  @BeforeEach
  void setup() {
    queryFactory = new JPAQueryFactory(em);
    em.persist(new TestMember("member1", "team1", 10));
    em.persist(new TestMember("member2", "team2", 10));
    em.persist(new TestMember("member3", "team3", 10));
    em.persist(new TestMember("member4", "team4", 10));
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


}
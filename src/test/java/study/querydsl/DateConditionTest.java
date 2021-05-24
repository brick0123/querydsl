package study.querydsl;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMovie.movie;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Movie;

@Transactional
@SpringBootTest
public class DateConditionTest {

  @Autowired
  EntityManager em;

  JPAQueryFactory queryFactory;

  @BeforeEach
  public void setup() {
    queryFactory = new JPAQueryFactory(em);
    Movie movie1 = new Movie(LocalDate.of(2021, 5, 10));
    Movie movie2 = new Movie(LocalDate.of(2021, 6, 10));
    em.persist(movie1);
    em.persist(movie2);
  }

  @Test
  @DisplayName("시작일이 있고 종료일이 null이면 시작일 >= 값을 찾아온다")
  void t1() {
    LocalDate start = LocalDate.of(2021, 5, 10);
    LocalDate end = null;

    List<Movie> fetch = queryFactory
        .selectFrom(movie)
        .where(dateCondition(start, end))
        .fetch();

    assertThat(fetch.size()).isEqualTo(2);
  }

  @Test
  @DisplayName("시작일이 null이고 종료일이 있으면 종료일 <= 값을 찾아온다")
  void t2() {
    LocalDate start = null;
    LocalDate end = LocalDate.of(2021, 5, 25);

    List<Movie> fetch = queryFactory
        .selectFrom(movie)
        .where(dateCondition(start, end))
        .fetch();

    assertThat(fetch.size()).isEqualTo(1);
  }

  @Test
  @DisplayName("시작일과 종료일이 모두 Null이면 조건 절이 없어진다")
  void t3() {
    LocalDate start = null;
    LocalDate end = null;

    List<Movie> fetch = queryFactory
        .selectFrom(movie)
        .where(dateCondition(start, end))
        .fetch();

    assertThat(fetch.size()).isEqualTo(2);
  }

  private Predicate dateCondition(LocalDate start, LocalDate end) {
    if (start != null || end != null) {
      return movie.startDate.between(start, end);
    }
    return null;
  }
}

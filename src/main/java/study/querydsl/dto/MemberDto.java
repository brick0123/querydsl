package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@NoArgsConstructor
public class MemberDto {

  private String username;
  private int age;

  @QueryProjection
  public MemberDto(String username, int age) {
    this.username = username;
    this.age = age;
  }

}

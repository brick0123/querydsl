package study.querydsl.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import study.querydsl.entity.Member;

@ToString
@Getter
@NoArgsConstructor
public class MemberResponseDto {

  private Long id;
  private Long teamId;
  private String username;

  public MemberResponseDto(Member member) {
    this.id = member.getId();
    this.username = member.getUsername();
  }
}

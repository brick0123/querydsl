package study.querydsl.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.querydsl.entity.Team;

@Getter
@Setter
@NoArgsConstructor
public class TeamResponseDto {

  private Long id;
  private String name;
  private List<MemberResponseDto> members;

  public TeamResponseDto(Team team) {
    this.id = team.getId();
    this.name = team.getName();
    this.members = team.getMembers().stream()
        .map(MemberResponseDto::new)
        .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return "team name = " + this.name + " \n >>> member = " + this.members;
  }

}

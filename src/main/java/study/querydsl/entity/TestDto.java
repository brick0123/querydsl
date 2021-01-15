package study.querydsl.entity;

import lombok.ToString;
import study.querydsl.dto.TestMember;

//@Setter
@ToString
public class TestDto {
  private String name;
  private String teamName;
  private int age;

  public TestDto(TestMember testMember) {
    this.name = testMember.getName();
    this.teamName = testMember.getTeamName();
    this.age = testMember.getAge();
  }
}

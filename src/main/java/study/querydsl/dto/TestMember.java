package study.querydsl.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class TestMember {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String teamName;

  private int age;

  public TestMember(String name, String teamName, int age) {
    this.name = name;
    this.teamName = teamName;
    this.age = age;
  }
}

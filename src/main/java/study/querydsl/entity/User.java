package study.querydsl.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class User {
  private int age;
  private String name;

  @Builder
  public User(int age, String name) {
    this.age = age;
    this.name = name;
  }
}

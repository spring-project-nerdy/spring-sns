package com.fastcampus.sns.model;


import com.fastcampus.sns.model.entity.UserEntity;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {

  private Integer id;
  private String userName;
  private String password;
  private UserRole role;
  private Timestamp registeredAt;
  private Timestamp updatedAt;
  private Timestamp deletedAt;

  public static User fromEntity(UserEntity entity) {
    return new User(
        entity.getId(),
        entity.getUserName(),
        entity.getPassword(),
        entity.getRole(),
        entity.getRegisteredAt(),
        entity.getUpdatedAt(),
        entity.getDeletedAt()
    );
  }
}

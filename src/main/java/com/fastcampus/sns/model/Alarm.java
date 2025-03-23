package com.fastcampus.sns.model;

import com.fastcampus.sns.model.entity.AlarmEntity;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {
  private Integer id;
  private User user;
  private AlarmType alarmType;
  private AlarmArgs args;
  private Timestamp registeredAt;
  private Timestamp updatedAt;
  private Timestamp deletedAt;
  
  public static Alarm fromEntity(AlarmEntity entity) {
    return new Alarm(
        entity.getId(),
        User.fromEntity(entity.getUser()),
        entity.getAlarmType(),
        entity.getArgs(),
        entity.getRegisteredAt(),
        entity.getUpdatedAt(),
        entity.getDeletedAt()
    );
  }
}

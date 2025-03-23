package com.fastcampus.sns.controller.response;

import com.fastcampus.sns.model.Alarm;
import com.fastcampus.sns.model.AlarmArgs;
import com.fastcampus.sns.model.AlarmType;
import com.fastcampus.sns.model.User;
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
public class AlarmResponse {
  private Integer id;
  private AlarmType alarmType;
  private AlarmArgs alarmArgs;
  private String text;
  private Timestamp registeredAt;
  private Timestamp updatedAt;
  private Timestamp deletedAt;
  
  public static AlarmResponse fromAlarm(Alarm alarm) {
    return new AlarmResponse(
        alarm.getId(),
        alarm.getAlarmType(),
        alarm.getArgs(),
        alarm.getAlarmType().getAlarmText(),
        alarm.getRegisteredAt(),
        alarm.getUpdatedAt(),
        alarm.getDeletedAt()
    );
  }
}

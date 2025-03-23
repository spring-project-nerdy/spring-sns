package com.fastcampus.sns.model.entity;

import com.fastcampus.sns.model.AlarmArgs;
import com.fastcampus.sns.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "\"alarm\"", indexes = {
    @Index(name = "user_id_idx", columnList = "user_id")
})
@Getter
@Setter
@SQLDelete(sql = "UPDATE alarm SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
public class AlarmEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;
  
  @Enumerated(EnumType.STRING)
  private AlarmType alarmType;
  
  @Type(JsonType.class)  // Hibernate JSON 타입 적용
  @Column(columnDefinition = "json")  // MariaDB는 json 사용
  private AlarmArgs args;
  
  @Column(name = "registered_at")
  private Timestamp registeredAt;
  
  @Column(name = "updated_at")
  private Timestamp updatedAt;
  
  @Column(name = "deleted_at")
  private Timestamp deletedAt;
  
  @PrePersist
  void registeredAt() {
    this.registeredAt = Timestamp.from(Instant.now());
  }
  
  @PreUpdate
  void updatedAt() {
    this.updatedAt = Timestamp.from(Instant.now());
  }

  public static AlarmEntity of(UserEntity userEntity, AlarmType alarmType, AlarmArgs args) {
    AlarmEntity entity = new AlarmEntity();
    entity.setUser(userEntity);
    entity.setAlarmType(alarmType);
    entity.setArgs(args);
    return entity;
  }
}

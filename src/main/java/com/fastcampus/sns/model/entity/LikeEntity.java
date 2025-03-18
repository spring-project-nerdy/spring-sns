package com.fastcampus.sns.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

@Entity
@Table(name = "\"like\"")
@Getter
@Setter
@SQLDelete(sql = "UPDATE like SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
public class LikeEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;
  
  @ManyToOne
  @JoinColumn(name = "post_id")
  private PostEntity post;
  
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

  public static LikeEntity of(UserEntity userEntity, PostEntity postEntity) {
    LikeEntity entity = new LikeEntity();
    entity.setUser(userEntity);
    entity.setPost(postEntity);
    return entity;
  }
}

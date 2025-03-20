package com.fastcampus.sns.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "\"comment\"", indexes = {
    @Index(name = "post_id_idx", columnList = "post_id")
})
@Getter
@Setter
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
public class CommentEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;
  
  @ManyToOne
  @JoinColumn(name = "post_id")
  private PostEntity post;
  
  @Column(name = "comment")
  private String comment;
  
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

  public static CommentEntity of(UserEntity userEntity, PostEntity postEntity, String comment) {
    CommentEntity entity = new CommentEntity();
    entity.setUser(userEntity);
    entity.setPost(postEntity);
    entity.setComment(comment);
    return entity;
  }
}

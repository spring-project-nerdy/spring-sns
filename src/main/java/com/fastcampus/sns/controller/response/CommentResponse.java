package com.fastcampus.sns.controller.response;

import com.fastcampus.sns.model.Comment;
import com.fastcampus.sns.model.Post;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponse {
  private Integer id;
  private String comment;
  private String userName;
  private Integer postId;
  private Timestamp registeredAt;
  private Timestamp updatedAt;
  private Timestamp deletedAt;

  public static CommentResponse fromPost(Comment comment) {
    return new CommentResponse(
        comment.getId(),
        comment.getComment(),
        comment.getUserName(),
        comment.getPostId(),
        comment.getRegisteredAt(),
        comment.getUpdatedAt(),
        comment.getDeletedAt()
    );
  }
}

package com.fastcampus.sns.service;

import com.fastcampus.sns.exception.ErrorCode;
import com.fastcampus.sns.exception.SnsApplicationException;
import com.fastcampus.sns.model.Comment;
import com.fastcampus.sns.model.Post;
import com.fastcampus.sns.model.entity.CommentEntity;
import com.fastcampus.sns.model.entity.LikeEntity;
import com.fastcampus.sns.model.entity.PostEntity;
import com.fastcampus.sns.model.entity.UserEntity;
import com.fastcampus.sns.repository.CommentEntityRepository;
import com.fastcampus.sns.repository.LikeEntityRepository;
import com.fastcampus.sns.repository.PostEntityRepository;
import com.fastcampus.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
  
  private final PostEntityRepository postEntityRepository;
  private final UserEntityRepository userEntityRepository;
  private final LikeEntityRepository likeEntityRepository;
  private final CommentEntityRepository commentEntityRepository;

  @Transactional
  public void create(String title, String body, String userName) {
    UserEntity userEntity = getUserOrException(userName);
    PostEntity saved = postEntityRepository.save(PostEntity.of(title, body, userEntity));
  }

  @Transactional
  public Post modify(String title, String body, String userName, Integer postId) {
    UserEntity userEntity = getUserOrException(userName);
    PostEntity postEntity = getPostEntityOrException(postId);

    if(postEntity.getUser() != userEntity) {
      throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,
          String.format("%s has no permission with %s", userName, postId));
    }

    postEntity.setTitle(title);
    postEntity.setBody(body);

    return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
  }

  @Transactional
  public void delete(String userName, Integer postId) {
    UserEntity userEntity = getUserOrException(userName);
    PostEntity postEntity = getPostEntityOrException(postId);

    if(postEntity.getUser() != userEntity) {
      throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,
          String.format("%s has no permission with %s", userName, postId));
    }

    postEntityRepository.delete(postEntity);
  }

  public Page<Post> list(Pageable pageable) {
    return postEntityRepository.findAll(pageable).map(Post::fromEntity);
  }

  public Page<Post> myList(String userName, Pageable pageable) {
    UserEntity userEntity = getUserOrException(userName);

    return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
  }
  
  @Transactional
  public void like(Integer postId, String userName) {
    PostEntity postEntity = getPostEntityOrException(postId);
    UserEntity userEntity = getUserOrException(userName);
    
    likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
      throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("userName %s already like post %d", userName, postId));
    });
  
    likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));
  }
  
  public int likeCount(Integer postId) {
    PostEntity postEntity = getPostEntityOrException(postId);
    
    return likeEntityRepository.countByPost(postEntity);
  }
  
  @Transactional
  public void comment(Integer postId, String userName, String comments) {
    PostEntity postEntity = getPostEntityOrException(postId);
    UserEntity userEntity = getUserOrException(userName);
  
    commentEntityRepository.save(CommentEntity.of(userEntity, postEntity, comments));
  }
  
  public Page<Comment> getComment(Integer postId, Pageable pageable) {
    PostEntity postEntity = getPostEntityOrException(postId);
    return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
  }
  
  private UserEntity getUserOrException(String userName) {
    return userEntityRepository.findByUserName(userName)
        .orElseThrow(() -> new SnsApplicationException(
            ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
  }
  
  private PostEntity getPostEntityOrException(Integer postId) {
    return postEntityRepository.findById(postId).orElseThrow(
        () -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND,
            String.format("%s not founded", postId)));
  }
  
  
}

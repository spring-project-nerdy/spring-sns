package com.fastcampus.sns.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fastcampus.sns.exception.ErrorCode;
import com.fastcampus.sns.exception.SnsApplicationException;
import com.fastcampus.sns.fixture.PostEntityFixture;
import com.fastcampus.sns.fixture.UserEntityFixture;
import com.fastcampus.sns.model.entity.PostEntity;
import com.fastcampus.sns.model.entity.UserEntity;
import com.fastcampus.sns.repository.PostEntityRepository;
import com.fastcampus.sns.repository.UserEntityRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class PostServiceTest {
  
  @Autowired
  private PostService postService;
  
  @MockitoBean
  private PostEntityRepository postEntityRepository;

  @MockitoBean
  private UserEntityRepository userEntityRepository;

  @Test
  public void 포스트작성이_성공한경우() {
    String title = "title";
    String body = "body";
    String userName = "userName";

    //Mocking
    when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
    when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
    
    Assertions.assertDoesNotThrow(() -> postService.create(title, body, userName));
  }
  
  @Test
  public void 포스트작성시_요청한유저가_존재하지않는경우_에러발생() {
    String title = "title";
    String body = "body";
    String userName = "userName";
  
    //Mocking
    when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
    when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
  
    SnsApplicationException e = Assertions.assertThrows(
        SnsApplicationException.class, () -> postService.create(title, body, userName));
    Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  public void 포스트수정이_성공한경우() {
    String title = "title";
    String body = "body";
    String userName = "userName";
    Integer postId = 1;

    PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
    UserEntity userEntity = postEntity.getUser();

    when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
    when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
    when(postEntityRepository.saveAndFlush(any())).thenReturn(postEntity);

    Assertions.assertDoesNotThrow(() -> postService.modify(title, body, userName, postId));
  }

  @Test
  public void 포스트수정시_포스트가_존재하지않는_경우_에러발생() {
    String title = "title";
    String body = "body";
    String userName = "userName";
    Integer postId = 1;

    PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
    UserEntity userEntity = postEntity.getUser();

    when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
    when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

    SnsApplicationException e = Assertions.assertThrows(
        SnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
    Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
  }

  @Test
  public void 포스트수정시_권한이_없는경우_에러발생() {
    String title = "title";
    String body = "body";
    String userName = "userName";
    Integer postId = 1;

    PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
    UserEntity writer = UserEntityFixture.get("userName1", "qwer");

    when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
    when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

    SnsApplicationException e = Assertions.assertThrows(
        SnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
    Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
  }

  @Test
  public void 포스트삭제가_성공한경우() {
    String userName = "userName";
    Integer postId = 1;

    PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
    UserEntity userEntity = postEntity.getUser();

    when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
    when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

    Assertions.assertDoesNotThrow(() -> postService.delete(userName, postId));
  }

  @Test
  public void 포스트삭제시_포스트가_존재하지않는_경우_에러발생() {
    String userName = "userName";
    Integer postId = 1;

    PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
    UserEntity userEntity = postEntity.getUser();

    when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
    when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

    SnsApplicationException e = Assertions.assertThrows(
        SnsApplicationException.class, () -> postService.delete(userName, postId));
    Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
  }

  @Test
  public void 포스트삭제시_권한이_없는경우_에러발생() {
    String userName = "userName";
    Integer postId = 1;

    PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
    UserEntity writer = UserEntityFixture.get("userName1", "qwer");

    when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
    when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

    SnsApplicationException e = Assertions.assertThrows(
        SnsApplicationException.class, () -> postService.delete(userName, postId));
    Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
  }

  @Test
  void 피드목록요청이_성공한경우() {
    Pageable pageable = mock(Pageable.class);
    when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());
    Assertions.assertDoesNotThrow(() -> postService.list(pageable));
  }

  @Test
  void 내피드목록요청이_성공한경우() {
    Pageable pageable = mock(Pageable.class);
    when(postEntityRepository.findAllByUser(any(), pageable)).thenReturn(Page.empty());
    Assertions.assertDoesNotThrow(() -> postService.myList("", pageable));
  }
}

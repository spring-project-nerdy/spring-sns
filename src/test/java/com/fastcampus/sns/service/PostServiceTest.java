package com.fastcampus.sns.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fastcampus.sns.exception.ErrorCode;
import com.fastcampus.sns.exception.SnsApplicationException;
import com.fastcampus.sns.model.entity.PostEntity;
import com.fastcampus.sns.model.entity.UserEntity;
import com.fastcampus.sns.repository.PostEntityRepository;
import com.fastcampus.sns.repository.UserEntityRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
  public void 포스트작성시_요청한유저가_존재하지않는경우() {
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
}

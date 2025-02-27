package com.fastcampus.sns.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fastcampus.sns.controller.request.PostCreateRequest;
import com.fastcampus.sns.controller.request.UserJoinRequest;
import com.fastcampus.sns.model.User;
import com.fastcampus.sns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest  // 스프링 컨텍스트를 로드하도록 추가
@AutoConfigureMockMvc
public class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @MockitoBean(name = "userService")
  private UserService userService;
  
  @Test
  @WithMockUser
  public void 포스트작성() throws Exception {
    
    String title = "title";
    String body = "body";
  
    mockMvc.perform(post("/api/v1/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
        ).andDo(print())
        .andExpect(status().isOk());
  }
  
  @Test
  @WithAnonymousUser
  public void 포스트작성시_로그인하지않경우() throws Exception {
    
    String title = "title";
    String body = "body";
    
    //로그인하지않은경우
    
    mockMvc.perform(post("/api/v1/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
        ).andDo(print())
        .andExpect(status().isUnauthorized());
  }
}

package com.fastcampus.sns.service;

import com.fastcampus.sns.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  public User join() {
    return new User();
  }
  
  public String login() {
    return "";
  }
}
